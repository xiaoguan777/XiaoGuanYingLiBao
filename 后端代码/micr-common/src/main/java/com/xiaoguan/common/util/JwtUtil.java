package com.xiaoguan.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.time.DateUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class JwtUtil {
    public JwtUtil(String key) {
        this.key = key;
    }

    private String key;
    //创建jwt
    public String createJwt(Map<String,Object> data,Integer minute) throws Exception{
        SecretKey secretKey= Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        Date nowTime=new Date();
        String jwt = Jwts.builder().signWith(secretKey, SignatureAlgorithm.HS256)
                    .setExpiration(DateUtils.addMinutes(nowTime, minute))
                    .setIssuedAt(nowTime)
                    .setId(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase())
                    .addClaims(data)
                    .compact();
        return jwt;
    }
    public Claims readJwt(String jwt) throws Exception{
        SecretKey secretKey= Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        Claims body = Jwts.parserBuilder().setSigningKey(secretKey)
                .build().parseClaimsJws(jwt).getBody();
        return body;
    }
}
