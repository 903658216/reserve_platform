package com.jh.util;

import com.jh.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 *
 */
public class JWTUtil {

    /**
     * 密钥
     */
    private static  final  String secret ="fiyhvasdk987jfnkjasdnfawD5321FALJMKSM0";
    /**
     * 有效期
     */
    private  static  final  Integer timeout = 1000*360*24*7;


    /**
     * 生成JWT令牌
     * @param user
     * @param devId
     * @return
     */
    public  static  String createJwtToken(User user,String devId){

        JwtBuilder builder = Jwts.builder()
                .setId(user.getId()+"")
                .setSubject(user.getNickname())
                .setIssuedAt(new Date())
                //添加自定义属性
                .claim("uid",user.getId())
                .claim("username",user.getUsername())
                //设置jwt的过期时间
                .setExpiration(new Date(System.currentTimeMillis()+timeout))
                .signWith(SignatureAlgorithm.HS256,secret+devId);
        String jwtToken = builder.compact();
        return jwtToken;
    }


    /**
     * 验证token的方法
     * @param jwtToken
     * @param devId
     * @return
     */
    public  static User isToken(String jwtToken,String devId){

        try {
            Claims claims = Jwts.parser().setSigningKey(secret+devId)
                    .parseClaimsJws(jwtToken).getBody();
            System.out.println("验签通过");
            //解析出用户的自定义信息
            Integer uid = (Integer) claims.get("uid");
            String username = (String) claims.get("username");
            User user = new User()
                    .setId(uid)
                    .setUsername(username);
            return user;

        } catch (Exception e) {
            System.out.println("验签为通过");
            e.printStackTrace();
        }

        return null;
    }
}
