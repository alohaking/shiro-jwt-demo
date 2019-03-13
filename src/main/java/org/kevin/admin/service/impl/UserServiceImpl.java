package org.kevin.admin.service.impl;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.kevin.admin.dto.UserDto;
import org.kevin.admin.service.UserService;
import org.kevin.admin.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private StringRedisTemplate redisTemplate;
    private String pwdSalt;
    private long jwtTokenExpire;

    @Autowired
    public UserServiceImpl(StringRedisTemplate redisTemplate,
                           @Value("${pwdSalt}") String pwdSalt,
                           @Value("${jwtTokenExpire:3600}") long jwtTokenExpire){
        this.redisTemplate = redisTemplate;
        this.pwdSalt = pwdSalt;
        this.jwtTokenExpire = jwtTokenExpire;
    }

    /**
     * 保存user登录信息，返回token
     * @param userName
     */
    @Override
    public String generateJwtToken(String userName) {
        String salt = "12345";//JwtUtils.generateSalt();
        /**
         * @todo 将salt保存到数据库或者缓存中
         * redisTemplate.opsForValue().set("token:"+username, salt, 3600, TimeUnit.SECONDS);
         */
        return JwtUtils.sign(userName, salt, jwtTokenExpire); //生成jwt token，设置过期时间为1小时
    }

    /**
     * 获取上次token生成时的salt值和登录用户信息
     * @param username
     * @return
     */
    @Override
    public UserDto getJwtTokenInfo(String username) {
        String salt = "12345";
        /**
         * @todo 从数据库或者缓存中取出jwt token生成时用的salt
         * salt = redisTemplate.opsForValue().get("token:"+username);
         */
        UserDto user = getUserInfo(username);
        user.setSalt(salt);
        return user;
    }

    /**
     * 清除token信息
     * @param userName 登录用户名
     */
    @Override
    public void deleteLoginInfo(String userName) {
        /**
         * @todo 删除数据库或者缓存中保存的salt
         * redisTemplate.delete("token:"+username);
         */

    }

    /**
     * 获取数据库中保存的用户信息，主要是加密后的密码
     * @param userName
     * @return
     */
    @Override
    public UserDto getUserInfo(String userName) {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setUserName("admin");
        user.setPassword(new Sha256Hash("123456", pwdSalt).toHex());
        return user;
    }

    /**
     * 获取用户角色列表，强烈建议从缓存中获取
     * @param userId
     * @return
     */
    @Override
    public List<String> getUserRoles(Long userId){
        return Arrays.asList("admin");
    }

    /**
     * 获取角色所对应的权限,需要根据角色id获取，这里为了便于演示直接返回
     * @param roleId
     * @return
     */
    @Override
    public Set<String> getUserPermissions(Long roleId){
        return new HashSet<>(Arrays.asList("article:view"));
    }
}
