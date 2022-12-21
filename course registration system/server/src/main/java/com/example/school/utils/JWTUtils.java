package com.example.school.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

public class JWTUtils {
    private static final String KEY = "kd1ih431ihk31ak1";
    /*
     * 生成token
     * 输入一个Map，将Map中的String:String添加为token的内容并生成token
     */
    public static String getToken(Map<String,String> map){
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE,100);

        JWTCreator.Builder builder = JWT.create();

        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });
        String token = builder.withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(KEY));
        return token;
    }
    /*
     * 验证token，这句话在这个token有任何不合法时都会抛异常
     */
    public static void verify(String token){
        JWT.require(Algorithm.HMAC256(KEY)).build().verify(token);
    }
    /*
     * 获取token信息的方法
     */
    public static DecodedJWT getTokenInfo(String token){
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(KEY)).build().verify(token);
        return verify;
    }
    //DecodedJWT对象可以通过.getClaim(String name)方法，来获得键为name的键值
    //想要使用此方法，需要在参数controller的参数中包含HttpServletRequest request这一参数，使用request.getHeader
    //来获取请求头
}
