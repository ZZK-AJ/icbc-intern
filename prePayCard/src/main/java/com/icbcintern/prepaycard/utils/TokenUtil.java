package com.icbcintern.prepaycard.utils;

import com.icbcintern.prepaycard.pojo.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TokenUtil {
    /**
     * 生成 token
     *
     * @param user
     * @return
     */

    public static String genToken(User user) {
        String secretKey = "aLA1NHpD6ADodEUAMWWTZTWuIZfBPxMAUoDQTAe1B5atlXM6xD8j0ACZ4HdukbT6JnItu0x6pHivlXiOG38YioImYZoRP8SvTg8R";
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId(user.getId() + "")
                .setSubject(user.getName())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, secretKey).setExpiration(new Date(new Date().getTime() + 86400000));

        System.out.println(jwtBuilder.compact());
        return jwtBuilder.compact();
    }

    /**
     * 解析 token
     */
    public static Claims ParserToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey("aLA1NHpD6ADodEUAMWWTZTWuIZfBPxMAUoDQTAe1B5atlXM6xD8j0ACZ4HdukbT6JnItu0x6pHivlXiOG38YioImYZoRP8SvTg8R")
                .parseClaimsJws(token)
                .getBody();
        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(claims.getIssuedAt()));
        System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(claims.getExpiration()));
        System.out.println(claims.get("role"));
        return claims;
    }
}
