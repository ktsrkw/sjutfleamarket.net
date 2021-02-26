package com.wt.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wt.pojo.Goods;
import com.wt.service.GoodsService;
import com.wt.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.websocket.server.PathParam;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    @Qualifier("goodsServiceImpl")
    GoodsService goodsService;

    @Autowired
    @Qualifier("userServiceImpl")
    UserService userService;

    @GetMapping("/goods/search")
    public String searchGoods(@PathParam("searchContent") String searchContent,
                              Model model,
                              @RequestParam(required = false, defaultValue = "1", value = "pageNum") Integer pageNum,
                              @RequestParam(defaultValue = "3", value = "pageSize") Integer pageSize) {
        //如果登录了，在页面显示当前登录用户的用户名
        //获取session
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        //通过session判断用户是否登录
        //如果用户登陆了，把当前用户名带给前端
        if(session.getAttribute("username") != null){
            model.addAttribute("username",session.getAttribute("username").toString());
        }

        if (pageNum == null) {
            pageNum = 1;
        } //设置默认当前页
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 3;
        }   //设置默认每页显示的数据数

        //根据输入模糊查询名称得到商品
        //使用分页插件
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> goods = goodsService.getGoodsByInputName(searchContent);
        PageInfo pageInfo = new PageInfo(goods);

        //到当前页的请求
        String reqPath = "/goods/search?searchContent=" + searchContent + "&";
        model.addAttribute("reqPath", reqPath);

        model.addAttribute("goods", goods);
        model.addAttribute("pageInfo", pageInfo);

        return "index";
    }

    @GetMapping("/goods/studysupplies")
    public String getStudySupplies(Model model,
                                   @RequestParam(required = false, defaultValue = "1", value = "pageNum") Integer pageNum,
                                   @RequestParam(defaultValue = "3", value = "pageSize") Integer pageSize) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        if(session.getAttribute("username") != null){
            model.addAttribute("username",session.getAttribute("username").toString());
        }

        if (pageNum == null) {
            pageNum = 1;
        } //设置默认当前页
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 3;
        }   //设置默认每页显示的数据数

        //根据分类的类型得到商品
        //使用分页插件
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> goods = goodsService.getGoodsByCategory("学习用品");
        PageInfo pageInfo = new PageInfo(goods);

        //到当前页的请求
        String reqPath = "/goods/studysupplies?";
        model.addAttribute("reqPath", reqPath);

        model.addAttribute("goods", goods);
        model.addAttribute("pageInfo", pageInfo);

        return "index";
    }

    @GetMapping("/goods/transportation")
    public String getTransportation(Model model,
                                    @RequestParam(required = false, defaultValue = "1", value = "pageNum") Integer pageNum,
                                    @RequestParam(defaultValue = "3", value = "pageSize") Integer pageSize) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        if(session.getAttribute("username") != null){
            model.addAttribute("username",session.getAttribute("username").toString());
        }

        if (pageNum == null) {
            pageNum = 1;
        } //设置默认当前页
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 3;
        }   //设置默认每页显示的数据数

        //根据分类的类型得到商品
        //使用分页插件
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> goods = goodsService.getGoodsByCategory("交通工具");
        PageInfo pageInfo = new PageInfo(goods);

        //到当前页的请求
        String reqPath = "/goods/transportation?";
        model.addAttribute("reqPath", reqPath);

        model.addAttribute("goods", goods);
        model.addAttribute("pageInfo", pageInfo);

        return "index";
    }

    @GetMapping("/goods/dailynecessities")
    public String getDailyNecessities(Model model,
                                      @RequestParam(required = false, defaultValue = "1", value = "pageNum") Integer pageNum,
                                      @RequestParam(defaultValue = "3", value = "pageSize") Integer pageSize) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        if(session.getAttribute("username") != null){
            model.addAttribute("username",session.getAttribute("username").toString());
        }

        if (pageNum == null) {
            pageNum = 1;
        } //设置默认当前页
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 3;
        }   //设置默认每页显示的数据数

        //根据分类的类型得到商品
        //使用分页插件
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> goods = goodsService.getGoodsByCategory("生活日用品");
        PageInfo pageInfo = new PageInfo(goods);

        //到当前页的请求
        String reqPath = "/goods/dailynecessities?";
        model.addAttribute("reqPath", reqPath);

        model.addAttribute("goods", goods);
        model.addAttribute("pageInfo", pageInfo);

        return "index";
    }

    @GetMapping("/goods/clothing")
    public String getClothing(Model model,
                              @RequestParam(required = false, defaultValue = "1", value = "pageNum") Integer pageNum,
                              @RequestParam(defaultValue = "3", value = "pageSize") Integer pageSize) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        if(session.getAttribute("username") != null){
            model.addAttribute("username",session.getAttribute("username").toString());
        }

        if (pageNum == null) {
            pageNum = 1;
        } //设置默认当前页
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 3;
        }   //设置默认每页显示的数据数

        //根据分类的类型得到商品
        //使用分页插件
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> goods = goodsService.getGoodsByCategory("衣物");
        PageInfo pageInfo = new PageInfo(goods);

        //到当前页的请求
        String reqPath = "/goods/clothing?";
        model.addAttribute("reqPath", reqPath);

        model.addAttribute("goods", goods);
        model.addAttribute("pageInfo", pageInfo);

        return "index";
    }

    @GetMapping("/goods/electronicproduct")
    public String getElectronicProduct(Model model,
                                       @RequestParam(required = false, defaultValue = "1", value = "pageNum") Integer pageNum,
                                       @RequestParam(defaultValue = "3", value = "pageSize") Integer pageSize) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        if(session.getAttribute("username") != null){
            model.addAttribute("username",session.getAttribute("username").toString());
        }

        if (pageNum == null) {
            pageNum = 1;
        } //设置默认当前页
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 3;
        }   //设置默认每页显示的数据数

        //根据分类的类型得到商品
        //使用分页插件
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> goods = goodsService.getGoodsByCategory("电子产品");
        PageInfo pageInfo = new PageInfo(goods);

        //到当前页的请求
        String reqPath = "/goods/electronicproduct?";
        model.addAttribute("reqPath", reqPath);

        model.addAttribute("goods", goods);
        model.addAttribute("pageInfo", pageInfo);

        return "index";
    }

    @GetMapping("/goods/books")
    public String getBooks(Model model,
                           @RequestParam(required = false, defaultValue = "1", value = "pageNum") Integer pageNum,
                           @RequestParam(defaultValue = "3", value = "pageSize") Integer pageSize) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        if(session.getAttribute("username") != null){
            model.addAttribute("username",session.getAttribute("username").toString());
        }

        if (pageNum == null) {
            pageNum = 1;
        } //设置默认当前页
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 3;
        }   //设置默认每页显示的数据数

        //根据分类的类型得到商品
        //使用分页插件
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> goods = goodsService.getGoodsByCategory("书籍");
        PageInfo pageInfo = new PageInfo(goods);

        //到当前页的请求
        String reqPath = "/goods/books?";
        model.addAttribute("reqPath", reqPath);

        model.addAttribute("goods", goods);
        model.addAttribute("pageInfo", pageInfo);

        return "index";
    }

    @GetMapping("/goods/othergoods")
    public String getOtherGoods(Model model,
                                @RequestParam(required = false, defaultValue = "1", value = "pageNum") Integer pageNum,
                                @RequestParam(defaultValue = "3", value = "pageSize") Integer pageSize) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        if(session.getAttribute("username") != null){
            model.addAttribute("username",session.getAttribute("username").toString());
        }

        if (pageNum == null) {
            pageNum = 1;
        } //设置默认当前页
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 3;
        }   //设置默认每页显示的数据数

        //根据分类的类型得到商品
        //使用分页插件
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> goods = goodsService.getGoodsByCategory("其他");
        PageInfo pageInfo = new PageInfo(goods);

        //到当前页的请求
        String reqPath = "/goods/othergoods?";
        model.addAttribute("reqPath", reqPath);

        model.addAttribute("goods", goods);
        model.addAttribute("pageInfo", pageInfo);

        return "index";
    }

}
