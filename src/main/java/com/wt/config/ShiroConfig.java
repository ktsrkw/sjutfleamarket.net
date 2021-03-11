package com.wt.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    //ShiroFilterFactoryBean:3
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(defaultWebSecurityManager);//设置安全管理器

        //添加shiro的内置过滤器
        /*
         * shiro的内置过滤器有下面5个功能
         * anon:无需认证（登录）就可以访问
         * authc:必须认证（登录）了才能访问
         * perms:拥有对某个资源的权限才能访问
         * role:拥有某个角色权限才能访问
         * user:必须拥有 记住我 功能才能用
         * */
        //开始配置过滤规则
        Map<String,String> filterMap = new LinkedHashMap<>();//用个map来存规则
        //filterMap.put("/index","authc");//测试用，规定首页必须登陆了才能访问
        filterMap.put("/user/*","authc");//user页面下的所有页面只有登录了才能访问，方便controller里的过滤逻辑的编写
        filterMap.put("/logout","authc");
        filterMap.put("/cancelaccount/*","authc");
        filterMap.put("/releasegoods","authc");
        filterMap.put("/managergoods","authc");
        filterMap.put("/addcomment","authc");
        filterMap.put("/updategoodsinfo/*","authc");
        filterMap.put("/offshelf/*","authc");
        filterMap.put("/onshelf/*","authc");
        filterMap.put("/delete/*","authc");

        bean.setFilterChainDefinitionMap(filterMap);//添加自己配置的拦截规则
        bean.setLoginUrl("/login");//如果没有登录访问页面被拦截，则跳到登录页，设置能跳到登录页的请求路径

        return bean;
    }

    //DefaultWebSecurityManager:2
    @Bean(name="securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("userRealm")UserRealm userRealm){
        //使用@Qualifier指定bean的名字进行自动装配
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);//关联manager与userRealm
        return securityManager;
    }

    //创建realm对象，需要自定义类:1
    @Bean(name="userRealm")
    public UserRealm getUserRealm(){
        return new UserRealm();
    }

    //配置shiroDialect：用来整合shiro与thymeleaf
    @Bean
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }

}
