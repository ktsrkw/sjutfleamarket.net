package com.wt.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wt.pojo.Goods;
import com.wt.pojo.User;
import com.wt.service.GoodsService;
import com.wt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
