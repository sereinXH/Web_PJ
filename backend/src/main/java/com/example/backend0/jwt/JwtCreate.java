package com.example.backend0.jwt;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.UUID;

/**
 * @ClassName JwtCreate
 * @Description
 **/
public class JwtCreate {
    public static String create(String username, Integer userType, Integer userId, long illegalTime, String signature) {// 根据传入的信息创建jwt字符串，illegalTIme为秒数
        JwtBuilder jwtBuilder = Jwts.builder();
        long time = 1000 * illegalTime;
        String token = jwtBuilder.
                // head
                        setHeaderParam("typ", "JWT").
                setHeaderParam("alg", "HS256").
                // body
                        claim("name", username).
                claim("type", userType).
                claim("id", userId).
                setExpiration(new Date(System.currentTimeMillis() + time)).
                setId(UUID.randomUUID().toString()).
                // signature
                        signWith(SignatureAlgorithm.HS256, signature).
                compact();
        return token;
    }
}