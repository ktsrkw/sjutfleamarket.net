package com.wt.config;

import com.wt.pojo.User;
import com.wt.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken userToken = (UsernamePasswordToken) authenticationToken;
        //通过传过来的用户名在数据库中匹配用户名或匹配邮箱
        if (userService.getUserByUsername(userToken.getUsername()) == null){//数据库中查不到用户
            return null;
        }
        //密码认证，shiro来做
        return new SimpleAuthenticationInfo("",
                userService.getUserByUsername(userToken.getUsername()).getPassword(),
                "");
    }
}
