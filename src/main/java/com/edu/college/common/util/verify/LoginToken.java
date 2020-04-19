package com.edu.college.common.util.verify;

import com.edu.college.common.util.redis.RedisUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

import static com.edu.college.common.constant.Constants.LOGIN_SENCONDS;

@Component
public class LoginToken {
    @Resource
    private RedisUtil redisUtil;

    public <U> String saveAndGetToken(U user) {
        //若用户登录验证成功后将对应的登陆信息和登陆凭证一起存入redis中
        //生成登陆凭证token UUID
        String uuidToken = UUID.randomUUID().toString() + new Date().getTime();
        uuidToken = uuidToken.replace("-", "");
        //将token和用户登录态之间建立联系
        //续时 3分钟。
        redisUtil.putAndExpire(uuidToken, user, LOGIN_SENCONDS);
        //登陆成功。
        return uuidToken;
    }

    /**
     * 获取登录的用户
     */
    @SuppressWarnings("unchecked")
    public <U> U getUser(String token) {
        if (token == null) {
            return null;
        }
        return (U) redisUtil.getAndExpire(token, LOGIN_SENCONDS);
    }

    public void removeUser(String token) {
        redisUtil.delete(token);
    }
}
