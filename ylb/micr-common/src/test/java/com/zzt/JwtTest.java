package com.zzt;

import com.sun.org.apache.bcel.internal.generic.DUP;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class JwtTest {
    //ef982fa6e4a941df90ecfe7e879c8180
    @Test
    public void testCreateJwt() {
        String key = "ef982fa6e4a941df90ecfe7e879c8180";

        //创建SecretKey
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

        Date curDate = new Date();
        Map<String, Object> data = new HashMap<>();
        //隐私数据
        data.put("userId", 1001);

        //创建jwt
        String jwt = Jwts.builder().signWith(secretKey, SignatureAlgorithm.HS256)
                .setExpiration(DateUtils.addMinutes(curDate, 10))
                .setIssuedAt(curDate)
                .setId(UUID.randomUUID().toString())
                .addClaims(data).compact();

        System.out.println(jwt);
    }

    @Test
    public void testReadJwt() {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MDkyOTg3MDMsImlhdCI6MTcwOTI5ODEwMywianRpIjoiOGExYjk2NjktMDZlYy00YmJlLWFhMmEtMzdjMzFlOTUyNmIxIiwidXNlcklkIjoxMDAxfQ.NBUgHuF4A5wAgkx8lfCfT-984TCe8qLYPk1T3qBENz8";
        String key = "ef982fa6e4a941df90ecfe7e879c8180";

        //创建SecretKey
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

        //解析jwt
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwt);

        //读数据
        Claims body = claims.getBody();
        Integer userId = body.get("userId", Integer.class);
        System.out.println(userId);
    }
}
