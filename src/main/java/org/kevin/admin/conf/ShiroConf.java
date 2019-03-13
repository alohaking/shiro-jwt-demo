package org.kevin.admin.conf;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.kevin.admin.service.UserService;
import org.kevin.admin.shiro.DbRealm;
import org.kevin.admin.shiro.JWTShiroRealm;
import org.kevin.admin.shiro.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Map;

@Configuration
public class ShiroConf {
    /**
     * dbShiroRealm 作为首次登录的认证和获取角色、权限的授权认证
     * @return
     */
    @Bean("dbRealm")
    public Realm dbRealm(UserService userService, @Value("${pwdSalt}") String pwdSalt){
        return new DbRealm(userService, pwdSalt);
    }
    @Bean("jwtRealm")
    public Realm jwtRealm(UserService userService){
        return new JWTShiroRealm(userService);
    }

    /**
     * 禁用session
     * @return
     */
    @Bean
    protected SessionStorageEvaluator sessionStorageEvaluator(){
        DefaultWebSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }

    /**
     * 因为要做token的登录验证，所以需要在filter链中加入自定义的token filter
     */
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager,
                                              UserService userService,
                                              @Value("${tokenRefreshInterval:300}") int tokenRefreshInterval) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filterMap = factoryBean.getFilters();
        filterMap.put("authcToken", new JwtAuthFilter(userService, tokenRefreshInterval));
        factoryBean.setFilters(filterMap);
        factoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition().getFilterChainMap());
        return factoryBean;
    }

    /**
     * path和登录、权限验证的对应关系
     * @return
     */
    @Bean("shiroFilterChainDefinition")
    protected ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/login", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/logout", "noSessionCreation,authcToken[permissive]");
        chainDefinition.addPathDefinition("/**", "noSessionCreation,authcToken");
        return chainDefinition;
    }


}
