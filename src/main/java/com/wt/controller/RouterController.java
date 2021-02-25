package com.wt.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wt.pojo.Goods;
import com.wt.pojo.User;
import com.wt.service.GoodsService;
import com.wt.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class RouterController {
    @Autowired
    @Qualifier("userServiceImpl")
    UserService userService;

    @Autowired
    @Qualifier("goodsServiceImpl")
    GoodsService goodsService;

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

        //根据商品id得到发布者信息
        User user = userService.getUserByGoodsid(goodsid);
        model.addAttribute("user", user);

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
        subject.logout();//再执行登出

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
            System.out.println(user.getPassword().getClass().getName());
            System.out.println(user.getPassword());
            System.out.println(passwordConfirm.getClass().getName());
            System.out.println(passwordConfirm);
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

}
