package com.icbcintern.prepaycard.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.icbcintern.prepaycard.pojo.Entity;
import com.icbcintern.prepaycard.pojo.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTools {
    private static final Logger logger = LoggerFactory.getLogger(JwtTools.class);
    /**
     * 密钥
     */
    private static final String SECRET = "my_secret";

    /**
     * 过期时间
     **/
    private static final long EXPIRATION = 36000L;//单位为秒

    /**
     * 生成用户token,设置token超时时间
     */
    public static String createToken(Entity entity) {
        // 过期时间
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRATION * 1000);
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        String token = JWT.create()
                .withHeader(map)// 添加头部
                //可以将基本信息放到claims中
                .withClaim("userId", entity.getId()) //Id
                .withClaim("userName", entity.getName())
//                .withClaim("password", entity.getLoginPasswd()) //password
                .withExpiresAt(expireDate) //超时设置,设置过期的日期
                .withIssuedAt(new Date()) //签发时间
                .sign(Algorithm.HMAC256(SECRET)); //SECRET加密
        return token;
    }


    /**
     * 校验 token 并解析 token
     */
    public static DecodedJWT verifyToken(String token) {
        DecodedJWT jwt;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();  // 使用秘钥 SECRET 解密
            jwt = verifier.verify(token); // 对给定的令牌执行验证
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error("token解码异常");
            // 解码异常则抛出异常,返回 null
            return null;
        }
        return jwt;
    }

}
