package com.wt.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wt.pojo.Comment;
import com.wt.pojo.Goods;
import com.wt.pojo.Images;
import com.wt.pojo.User;
import com.wt.service.CommentService;
import com.wt.service.GoodsService;
import com.wt.service.ImagesService;
import com.wt.service.UserService;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class RouterController {
    @Autowired
    @Qualifier("userServiceImpl")
    UserService userService;

    @Autowired
    @Qualifier("goodsServiceImpl")
    GoodsService goodsService;

    @Autowired
    @Qualifier("imagesServiceImpl")
    ImagesService imagesService;

    @Autowired
    @Qualifier("commentServiceImpl")
    CommentService commentService;

    @RequestMapping({"/", "/welcome"})
    public String toWelcomePage() {
        return "welcome";
    }


    @RequestMapping("/index")
    public String index(Model model,
                        @RequestParam(required = false, defaultValue = "1", value = "pageNum") Integer pageNum,
                        @RequestParam(defaultValue = "9", value = "pageSize") Integer pageSize) {
        //给首页拿到所有商品的数据
//        List<Goods> goods = goodsService.getAllGoods();
//        model.addAttribute("goods",goods);

        //如果登录了，在index页显示当前登录用户的用户名
        //获取session
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        //通过session判断用户是否登录
        //如果用户登陆了，把当前用户名，userid带给前端
        if(session.getAttribute("username") != null){
            model.addAttribute("username",session.getAttribute("username").toString());
            model.addAttribute("userid",(Integer)session.getAttribute("userid"));
        }

        //分页的一些操作
        if (pageNum == null) {
            pageNum = 1;
        }  //设置默认当前页
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 9;
        }    //设置默认每页显示的数据数

        //使用分页插件
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> goods = goodsService.getAllGoods();
        PageInfo pageInfo = new PageInfo(goods);

        //到当前页的请求
        String reqPath = "/index?";
        model.addAttribute("reqPath", reqPath);
        model.addAttribute("goods", goods);
        model.addAttribute("pageInfo", pageInfo);

        //从数据库中拿到图片的连接以作为首页商品略缩图展示
        //搞个map来放url
        Map<Integer,String> imagesMap = new HashMap<>();
        for(Goods singleGoods : goods){
            //根据goodsid得到其包含的图片列表
            List<Images> images = imagesService.getImagesByGoodsId(singleGoods.getGoodsid());
            //如果此商品包含图片，将第0张图片的url和商品id存入map
            if(!images.isEmpty()){
                imagesMap.put(singleGoods.getGoodsid(),images.get(0).getImgurl());
            }
        }
        //存有图片url的map送到前台
        model.addAttribute("imagesMap",imagesMap);



        return "index";
    }

    @GetMapping("/goods/{goodsid}")
    public String goods(Model model, @PathVariable int goodsid) {
        //如果用户登录，session中存的username与userid送到前台
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        if (session.getAttribute("username") != null){
            model.addAttribute("username",(String)session.getAttribute("username"));
            model.addAttribute("userid",(int)session.getAttribute("userid"));
        }

        //根据goodsid拿到goods的信息
        Goods goods = goodsService.getGoodsById(goodsid);
        model.addAttribute("goods", goods);

        //根据goodsid拿到图片的信息
        List<Images> images = imagesService.getImagesByGoodsId(goodsid);
        model.addAttribute("images",images);

        //根据商品id得到发布者信息
        User user = userService.getUserByGoodsid(goodsid);
        model.addAttribute("user", user);

        //根据goodsid拿到所有评论
        List<Comment> comments = commentService.getCommentsByGoodsId(goodsid);
        model.addAttribute("comments",comments);

        //根据评论对象中的userid得到用户名并联系commentid存在map中
        Map<Integer,String> usernameByCommentid = new HashMap<Integer,String>();
        for(Comment comment : comments){
            //根据userid得到user
            User user1 = userService.getUserById(comment.getUserid());
            //以<commentid,username>的形式存在map中
            usernameByCommentid.put(comment.getCommentid(),user1.getUsername());
        }
        model.addAttribute("usernameByCommentid",usernameByCommentid);



        return "goods";
    }

    @GetMapping("/login")
    public String toLoginPage(){
        return "login";
    }

    @PostMapping("/login")
    public String login(@PathParam("username") String username,
                        @PathParam("password") String password,
                        Model model){
        //用户在前台输入用户名或用户邮箱和密码实现登录
        //获取当前的用户
        Subject subject = SecurityUtils.getSubject();
        //封装用户的登录数据成token对象
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        //检查用户登录数据
        try{
            subject.login(token);
            //如果成功登录，将用户名存入session
            //拿到登录的用户对象，再从此对象拿到用户名，这样用户通过邮箱登陆时也能拿到username
            User user = userService.getUserByUsername(username);

            //向session中存入当前用户名与id
            subject.getSession().setAttribute("username",user.getUsername());
            subject.getSession().setAttribute("userid",user.getUserid());

            return "redirect:/index";
        }catch(UnknownAccountException uae){
            model.addAttribute("msg01","用户名或邮箱不存在");
            return "login";
        }catch (IncorrectCredentialsException ice){
            model.addAttribute("msg01","密码错误");
            return "login";
        }

    }

    @GetMapping("/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();//先获取当前用户对象
        subject.logout();//执行登出

        return "redirect:/index";
    }

    @GetMapping("/register")
    public String toRegisterPage(){
        return "register";
    }

    //处理注册请求
    @PostMapping("/register")
    public String register(User user,
                           @PathParam("passwordConfirm") String passwordConfirm,
                           Model model) throws ParseException {

        //注册时三个不能为空的值的检查
//        if(user.getEmail().equals("")){
//            model.addAttribute("msg02","请输入邮箱");
//            return "/register";
//        }
//        if(user.getUsername().equals("")){
//            model.addAttribute("msg03","请输入用户名");
//            return "/register";
//        }
//        if(user.getPassword().equals("")){
//            model.addAttribute("msg04","请输入密码");
//            return "/register";
//        }

        //检查两次输入的密码是否一致
        if(!user.getPassword().equals(passwordConfirm)){
            model.addAttribute("msg05","两次输入的密码不一致");
            return "register";
        }

        //检查改用户名或邮箱是否已被占用
        if(userService.getUserByEmail(user.getEmail())!=null){
            model.addAttribute("msg05","该邮箱已被注册");
            return "register";
        }
        if(userService.getUserByUsername(user.getUsername())!=null){
            model.addAttribute("msg05","该用户名已被占用");
            return "register";
        }


        //日期的处理
        //如果输入了日期（生日），走插入语句1
//        if(!birthdayDateString.equals("")){
//            //处理日期的格式，装入对象中
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//            Date birthday = format.parse(birthdayDateString);
//            user.setBirthday(birthday);
//
//            userService.insertAnUserWithBirthday(user);
//        }else{
//            //如果没有输入生日，走插入语句2
//            userService.insertAnUserWithoutBirthday(user);
//        }

        //为用户设置默认信息
        user.setGender("保密");
        user.setUniversity("上海工程技术大学");

        //利用前台传来的信息注册
        userService.insertAnUser(user);

        return "registersuccess";
    }

    @GetMapping("/user/{userid}")
    public String userAdmin(@PathVariable Integer userid,Model model){
        //判断当前用户是否有权限访问此页面
        //获取当前session中存的userid
        int useridInSession = (Integer) SecurityUtils.getSubject().getSession().getAttribute("userid");
        //比对传进来userid与session中的userid，判断是否有权限登录
        if(userid==useridInSession){
            //拿到用户的所有信息，送到前台
            User user = userService.getUserById(userid);
            model.addAttribute("user",user);
            return "admin";
        }else {
            //跳转到被拦截页面
            return "error/intercept";
        }

    }

    //处理用户修改用户信息
    @PostMapping("/user/update")
    public String updateUser(User user,
                             @PathParam("passwordConfirm") String passwordConfirm,
                             @PathParam("birthdayDate") String birthdayDate,
                             Model model) throws ParseException {
        //修改信息时三个不能为空的检查
//        if(user.getEmail().equals("")){
//            model.addAttribute("msg08","邮箱不能为空");
//            return "admin";
//        }
//        if(user.getUsername().equals("")){
//            model.addAttribute("msg09","用户名不能为空");
//            return "admin";
//        }
//        if(user.getPassword().equals("")){
//            model.addAttribute("msg10","密码不能为空");
//            return "admin";
//        }
        //判断当前用户是否有权限更新此用户信息
        //拿到session，再取出session中存的的userid与传过来的参数做对比
        Subject subject = SecurityUtils.getSubject();
        if ((int)subject.getSession().getAttribute("userid") != user.getUserid()){
            return "error/intercept";
        }

        //检查两次输入的密码是否一致
        if(!user.getPassword().equals(passwordConfirm)){
            model.addAttribute("msg11","两次输入的密码不一致");
            return "admin";
        }

        //判断是否输入了日期
        if(!birthdayDate.equals("")){
            //输入了日期，处理日期的格式，装入对象中
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date birthday = format.parse(birthdayDate);
            user.setBirthday(birthday);
            userService.updateUserWithBirthday(user);
        }else {
            //没有输入日期，走这里
            userService.updateUserWithoutBirthday(user);
        }

        return "redirect:/index";
    }

    //实现注销账户
    @GetMapping("/cancelaccount/{userid}")
    public String cancelAccount(@PathVariable int userid){
        //判断当前用户是否有权限注销此账户
        //拿到session，再取出session中存的的userid与传过来的参数做对比
        Subject subject = SecurityUtils.getSubject();
        if ((int)subject.getSession().getAttribute("userid") != userid){
            return "error/intercept";
        }
        //user发布有goods，删除user同时也删除goods
        //userid作为goods与comment的外键
        //goodsid作为images与comment的外键
        //所以先删除商品再删除用户
        //根据userid得到其发布的商品
        List<Goods> goodsList = goodsService.getGoodsByUserid(userid);
        //循环删除所有的商品记录
        for (Goods goods : goodsList){
            //根据goodsid删除商品
            //goodsid作为comment与images的外键
            //先删除此goods的images与comment，再删除goods
            imagesService.deleteImagesByGoodsid(goods.getGoodsid());
            commentService.deleteCommentsByGoodsId(goods.getGoodsid());
            goodsService.deleteGoodsByGoodsId(goods.getGoodsid());
        }
        //删除此用户发布的评论
        commentService.deleteCommentsByUserId(userid);

        //商品、评论删除完了后再删除此用户
        //调service层，根据userid删除用户表的记录
        userService.deleteUserById(userid);

        //由于缓存的存在，所以效果不能马上看到，主动帮用户退出登录
        subject.logout();

        return "redirect:/welcome";
    }

    @GetMapping("/releasegoods")
    public String toReleasePage(Model model){
        //用户名,用户id拿到前台
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        model.addAttribute("username",session.getAttribute("username"));
        model.addAttribute("userid",(Integer)session.getAttribute("userid"));

        return "releasegoods";
    }

    //处理发布商品表单发的请求，处理上传的图片
    @PostMapping("/releasegoods")
    public String releaseGoods(Goods goods,
                               MultipartFile[] uploadFiles,
                               HttpServletRequest request){
        //获取当前时间
        Date date = new Date();
        goods.setDeliveryTime(date);

        //userid的添加
        //之前执行登录代码的时候向session中添加了用户名与id
        //这里拿到userid以填充goods表的userid字段
        //先拿到subject，再通过subject拿到session
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        //向goods对象中添加userid数据
        goods.setUserid((int)session.getAttribute("userid"));

        //增加一条商品信息
        goodsService.addAGoods(goods);

        //插入成功后，db会自动生成goodsid，需要拿到这个goodsid，以作为下面图片的外键
        //根据商品名和插入时间查库拿到商品的id
        //java日期的格式化
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = format.format(date);
        System.out.println(formatDate);
        int goodsid = goodsService.getGoodsIdByDateAndTitle(formatDate,goods.getTitle());

        for(MultipartFile uploadFile : uploadFiles){
            //处理上传的图片
            try{
//                //处理上传文件的步骤

                //1、创建文件在服务器端的存放路径
                String imgPath = "D:\\codes\\resources\\sjut-flea-market-user-upload";
                File imgDir = new File(imgPath);

                //2、生成文件在服务器端存放的名字（避免重名）
                //得到上传文件的后缀名（.jpg，.txt，.mp4 ...）
                String fileSuffix= Objects.requireNonNull(uploadFile.getOriginalFilename())
                        .substring(uploadFile.getOriginalFilename().lastIndexOf(".") + 1);
                //给文件准备好一个新的名字imgNewName：随机生成的UUID再加上前面取得的文件后缀名
                String imgNewName= UUID.randomUUID().toString() + "." + fileSuffix;
                File uploadedFile = new File(imgDir+"/"+imgNewName);

                //压缩上传
                Thumbnails.of(uploadFile.getInputStream()).scale(0.2f)
                        .outputFormat(fileSuffix).toFile(uploadedFile);


                //3、上传
//                uploadFile.transferTo(uploadedFile);


                //4、获取上传文件的访问路径
                //这里的images是映射路径，实际不是存储在这的
                String filePath = request.getScheme() + "://" + request.getServerName() + ":"
                        + request.getServerPort() + "/images/" + imgNewName;

                //创建一个对象，添加到数据库中
                Images images = new Images(filePath,goodsid);
                imagesService.addAnImage(images);


            }catch(Exception e) {
                //图片上传失败，跳转到失败页面
                e.printStackTrace();
                return "error/imagesuploadfailed";
            }
        }

        return "redirect:/index";
    }

    //处理到管理发布商品页面的请求
    @GetMapping("/managergoods")
    public String toManagerGoodsPage(Model model){
        //用户名,用户id拿到前台
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        int userid = (Integer)session.getAttribute("userid");
        model.addAttribute("username",session.getAttribute("username"));
        model.addAttribute("userid",userid);

        //根据userid拿到此用户发布的商品的对象集合
        List<Goods> goods = goodsService.getGoodsByUserid(userid);
        model.addAttribute("goods",goods);

        return "managergoods";
    }

    //处理添加评论的请求
    @PostMapping("/addcomment")
    public String addComment(Comment comment){
        //得到当前用户的id
        //拿到subject再拿到session再从session中拿到userid，set给当前comment对象
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        comment.setUserid((Integer)session.getAttribute("userid"));

        //得到当前日期对象，set给comment
        comment.setCommentDeliveryTime(new Date());

        //添加入数据库
        commentService.addAComment(comment);

        //重定向
        return "redirect:/goods/" + comment.getGoodsid();
    }

    //处理到修改商品信息页的请求
    @GetMapping("/updategoodsinfo/{goodsid}")
    public String toUpdateGoodsInfoPage(@PathVariable int goodsid,
                                        Model model){
        //判断用户是否为此商品的发布者
        //拿到session，再取出session中存的的userid与goodsid对应的userid做对比
        Subject subject = SecurityUtils.getSubject();
        if ((int)subject.getSession().getAttribute("userid") != goodsService.getUserIdByGoodsId(goodsid)){
            return "error/intercept";
        }

        //获取当前session中存的username，送到前台
        String username = (String) SecurityUtils.getSubject().getSession().getAttribute("username");
        model.addAttribute("username",username);

        //根据goodsid拿到goods信息，送到前台
        Goods goods = goodsService.getGoodsById(goodsid);
        model.addAttribute("goods",goods);

        return "updategoodsinfo";
    }

    //处理修改商品信息的请求
    @PostMapping("/updategoodsinfo")
    public String updateGoodsInfo(Goods goods,
                                  MultipartFile[] uploadFiles,
                                  HttpServletRequest request){
        //判断用户是否为此商品的发布者
        //拿到session，再取出session中存的的userid与goodsid对应的userid做对比
        Subject subject = SecurityUtils.getSubject();
        if ((int)subject.getSession().getAttribute("userid") !=
                goodsService.getUserIdByGoodsId(goods.getGoodsid())){
            return "error/intercept";
        }

        //修改数据库中商品的信息
        goodsService.updateGoods(goods);

        //处理用户上传的图片
        //如果用户上传了图片则做以下处理
        for(MultipartFile uploadFile : uploadFiles){
                //如果用户有选择图片
                if (!uploadFile.isEmpty()){
                    //处理上传的图片
                    try{
                        //处理上传文件的步骤

                        //1、创建文件在服务器端的存放路径
                        String imgPath = "D:\\codes\\resources\\sjut-flea-market-user-upload";
                        File imgDir = new File(imgPath);

                        //2、生成文件在服务器端存放的名字（避免重名）
                        //得到上传文件的后缀名（.jpg，.txt，.mp4 ...）
                        String fileSuffix= Objects.requireNonNull(uploadFile.getOriginalFilename())
                                .substring(uploadFile.getOriginalFilename().lastIndexOf(".") + 1);
                        //给文件准备好一个新的名字imgNewName：随机生成的UUID再加上前面取得的文件后缀名
                        String imgNewName= UUID.randomUUID().toString() + "." + fileSuffix;
                        File uploadedFile = new File(imgDir+"/"+imgNewName);

                        //压缩上传
                        Thumbnails.of(uploadFile.getInputStream()).scale(0.2f)
                                .outputFormat(fileSuffix).toFile(uploadedFile);

                        //3、上传
//                        uploadFile.transferTo(uploadedFile);

                        //4、获取上传文件的访问路径
                        //这里的images是映射路径，实际不是存储在这的
                        String filePath = request.getScheme() + "://" + request.getServerName() + ":"
                                + request.getServerPort() + "/images/" + imgNewName;

                        //创建一个对象，添加到数据库中
                        Images images = new Images(filePath,goods.getGoodsid());
                        imagesService.addAnImage(images);

                    }catch(Exception e) {
                        //图片上传失败，跳转到失败页面
                        e.printStackTrace();
                        return "error/imagesuploadfailed";
                    }
                }
        }

        return "redirect:/goods/" + goods.getGoodsid();
    }

    //处理下架商品的请求
    @GetMapping("/offshelf/{goodsid}")
    public String offShelfGoods(@PathVariable int goodsid){
        //判断用户是否为此商品的发布者
        //拿到session，再取出session中存的的userid与goodsid对应的userid做对比
        Subject subject = SecurityUtils.getSubject();
        if ((int)subject.getSession().getAttribute("userid") != goodsService.getUserIdByGoodsId(goodsid)){
            return "error/intercept";
        }

        //根据goodsid下架商品
        goodsService.offShelfGoodsByGoodsid(goodsid);

        return "redirect:/managergoods";
    }

    //处理上架商品的请求
    @GetMapping("/onshelf/{goodsid}")
    public String onShelfGoods(@PathVariable int goodsid){
        //判断用户是否为此商品的发布者
        //拿到session，再取出session中存的的userid与goodsid对应的userid做对比
        Subject subject = SecurityUtils.getSubject();
        if ((int)subject.getSession().getAttribute("userid") != goodsService.getUserIdByGoodsId(goodsid)){
            return "error/intercept";
        }

        //根据goodsid上架商品
        goodsService.onShelfGoodsByGoodsid(goodsid);

        return "redirect:/managergoods";
    }

    //处理删除商品的请求
    @GetMapping("/delete/{goodsid}")
    public String deleteGoodsByGoodsId(@PathVariable int goodsid){
        //判断用户是否为此商品的发布者
        //拿到session，再取出session中存的的userid与goodsid对应的userid做对比
        Subject subject = SecurityUtils.getSubject();
        if ((int)subject.getSession().getAttribute("userid") != goodsService.getUserIdByGoodsId(goodsid)){
            return "error/intercept";
        }

        //根据goodsid删除商品
        //goodsid作为comment与images的外键
        //先删除此goods的images与comment，再删除goods
        imagesService.deleteImagesByGoodsid(goodsid);
        commentService.deleteCommentsByGoodsId(goodsid);
        goodsService.deleteGoodsByGoodsId(goodsid);

        return "redirect:/managergoods";
    }

    @GetMapping("/introduction")
    public String toIntroductionPage(){
        return "introduction";
    }


}
