package org.kevin.admin.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.kevin.admin.dto.UserDto;
import org.kevin.admin.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;


public class DbRealm extends AuthorizingRealm {

    private static final Logger log = LoggerFactory.getLogger(DbRealm.class);

    private UserService userService;
    // 用户密码加密所用的salt
    private String pwdSalt;

    public DbRealm(UserService userService, String pwdSalt){
        this.userService = userService;
        this.pwdSalt = pwdSalt;
        this.setCredentialsMatcher(new HashedCredentialsMatcher(Sha256Hash.ALGORITHM_NAME));
    }

    /**
     * 当有多个Realm，且需要根据token区分时，需要重载此方法
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    /**
     * 登录认证，subject.login(token)最终会调用此方法
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken userPasswordToken = (UsernamePasswordToken)token;
        String username = userPasswordToken.getUsername();
        UserDto user = userService.getUserInfo(username);
        if(user == null)
            throw new AuthenticationException("用户名或者密码错误");

        return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(pwdSalt), "dbRealm");
    }


    /**
     * 角色、权限认证，subject.checkRole() 、subject.checkPermission()等最终会调用此方法
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        UserDto user = (UserDto) principals.getPrimaryPrincipal();
        List<String> roles = user.getRoles();
        if(roles == null) {
            roles = userService.getUserRoles(user.getId());
            user.setRoles(roles);
        }
        if (roles != null)
            simpleAuthorizationInfo.addRoles(roles);

        Set<String> permissions = user.getPermissions();
        if(permissions == null && (roles != null && !roles.isEmpty())){
            for(String role : roles){
                permissions = userService.getUserPermissions(1L);
            }
        }
        if(permissions != null){
            user.setPermissions(permissions);
            simpleAuthorizationInfo.setStringPermissions(permissions);
        }

        return simpleAuthorizationInfo;
    }
}
