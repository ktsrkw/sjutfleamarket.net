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
                        @RequestParam(defaultValue = "3", value = "pageSize") Integer pageSize) {
        //给首页拿到所有商品的数据
//        List<Goods> goods = goodsService.getAllGoods();
//        model.addAttribute("goods",goods);

        //如果登录了，在index页显示当前登录用户的用户名
        //获取session
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        //通过session判断用户是否登录
        //如果用户登陆了，把当前用户名带给前端
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
            pageSize = 3;
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

        return "index";
    }

    @GetMapping("/goods/{goodsid}")
    public String goods(Model model, @PathVariable int goodsid) {
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
                           @PathParam("birthdayDateString") String birthdayDateString,
                           Model model) throws ParseException {

        //注册时三个不能为空的值的检查
        if(user.getEmail().equals("")){
            model.addAttribute("msg02","请输入邮箱");
            return "/register";
        }
        if(user.getUsername().equals("")){
            model.addAttribute("msg03","请输入用户名");
            return "/register";
        }
        if(user.getPassword().equals("")){
            model.addAttribute("msg04","请输入密码");
            return "/register";
        }

        //检查两次输入的密码是否一致
        if(!user.getPassword().equals(passwordConfirm)){
            model.addAttribute("msg05","两次输入的密码不一致");
            return "/register";
        }

        //检查改用户名或邮箱是否已被占用
        if(userService.getUserByUsername(user.getEmail())!=null){
            model.addAttribute("msg07","该邮箱已被注册");
            return "/register";
        }
        if(userService.getUserByUsername(user.getUsername())!=null){
            model.addAttribute("msg06","该用户名已被占用");
            return "/register";
        }


        //日期的处理
        //如果输入了日期（生日），走插入语句1
        if(!birthdayDateString.equals("")){
            //处理日期的格式，装入对象中
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date birthday = format.parse(birthdayDateString);
            user.setBirthday(birthday);

            userService.insertAnUserWithBirthday(user);
        }else{
            //如果没有输入生日，走插入语句2
            userService.insertAnUserWithoutBirthday(user);
        }

        return "registersuccess";
    }

    @GetMapping("/user/{userid}")
    public String userAdmin(@PathVariable Integer userid,Model model){
        //拿到用户的所有信息，送到前台
        User user = userService.getUserById(userid);
        model.addAttribute("user",user);

        return "admin";
    }

    //处理用户修改用户信息
    @PostMapping("/user/update")
    public String updateUser(User user,
                             @PathParam("passwordConfirm") String passwordConfirm,
                             @PathParam("birthdayDate") String birthdayDate,
                             Model model) throws ParseException {
        //修改信息时三个不能为空的检查
        if(user.getEmail().equals("")){
            model.addAttribute("msg08","邮箱不能为空");
            return "admin";
        }
        if(user.getUsername().equals("")){
            model.addAttribute("msg09","用户名不能为空");
            return "admin";
        }
        if(user.getPassword().equals("")){
            model.addAttribute("msg10","密码不能为空");
            return "admin";
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
        //调service层，根据userid删除用户表的记录
        userService.deleteUserById(userid);

        //由于缓存的存在，所以效果不能马上看到，主动帮用户退出登录
        Subject subject = SecurityUtils.getSubject();
        subject.logout();

        return "redirect:/welcome";
    }

    @GetMapping("/releasegoods")
    public String toReleasePage(Model model){
        //用户名拿到前台
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();

        model.addAttribute("username",session.getAttribute("username"));

        return "releasegoods";
    }

    //处理发布商品表单发的请求，处理上传的图片
    @PostMapping("/releasegoods")
    public String releaseGoods(Goods goods,
                               MultipartFile[] uploadFiles,
                               HttpServletRequest request){
        //获取当前时间
        goods.setDeliveryTime(new Date());

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
        //根据商品名查库以拿到商品的id
        Goods goods1 = goodsService.getGoodsByTitle(goods.getTitle());
        int goodsid = goods1.getGoodsid();

        for(MultipartFile uploadFile : uploadFiles){
            //处理上传的图片
            try{
                //处理上传文件的步骤

                //1、创建文件在服务器端的存放路径
                String imgPath = "D:\\codes\\resources\\sjut-flea-market-user-upload";
                File imgDir = new File(imgPath);

                //2、生成文件在服务器端存放的名字（避免重名）
                //得到上传文件的后缀名（.jpg，.txt，.mp4 ...）
                String fileSuffix= Objects.requireNonNull(uploadFile.getOriginalFilename())
                        .substring(uploadFile.getOriginalFilename().lastIndexOf("."));
                //给文件准备好一个新的名字imgNewName：随机生成的UUID再加上前面取得的文件后缀名
                String imgNewName= UUID.randomUUID().toString()+fileSuffix;
                File uploadedFile = new File(imgDir+"/"+imgNewName);

                //3、上传
                uploadFile.transferTo(uploadedFile);

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
                return "imagesuploadfailed";
            }
        }

        return "redirect:/index";
    }

}
