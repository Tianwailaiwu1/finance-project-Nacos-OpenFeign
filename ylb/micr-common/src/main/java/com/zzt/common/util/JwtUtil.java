package com.zzt.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.time.DateUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class JwtUtil {


    private String selfKey;

    public JwtUtil(String selfKey) {
        this.selfKey = selfKey;
    }

    //创建jwt
    public String createJwt(Map<String, Object> data, Integer minute) throws Exception {
        Date curDate = new Date();
        //创建SecretKey
        SecretKey secretKey = Keys.hmacShaKeyFor(selfKey.getBytes(StandardCharsets.UTF_8));
        String jwt = Jwts.builder().signWith(secretKey, SignatureAlgorithm.HS256)
                .setExpiration(DateUtils.addMinutes(curDate, minute))
                .setIssuedAt(curDate)
                .setId(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase())
                .addClaims(data).compact();
        return jwt;

    }


    //读jwt
    public Claims readJwt(String jwt) throws Exception {
        //创建SecretKey
        SecretKey secretKey = Keys.hmacShaKeyFor(selfKey.getBytes(StandardCharsets.UTF_8));
        //解析jwt
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwt);
        //读数据
        Claims body = claims.getBody();
        return body;
    }
}
