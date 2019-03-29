package com.ldz.util.commonUtil;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * Created by chenwei on 2017/9/18
 */
public class JwtUtil {
   

    private static Algorithm algorithm;
    private static JWTVerifier verifier;

    static{
        try {
            algorithm = Algorithm.HMAC256("secret");
            verifier = JWT.require(algorithm)
                    .withIssuer("wcpms")
                    .withSubject("wcpms")
                    .withAudience("wcpms")
                    .build();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public static String create(){
        try {
            String token = JWT.create()
                    .withIssuer("wcpms")
                    .withSubject("wcpms")
                    .withAudience("wcpms")
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            //UTF-8 encoding not supported
        }
        return null;
    }
    public static String createToken(String userId,String loginName){
        try {
            String token = JWT.create()
                    .withIssuer("wcpms")
                    .withSubject("wcpms")
                    .withAudience("wcpms")
                    .withClaim("userId",userId)
                    .withClaim("loginName",loginName)
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            //UTF-8 encoding not supported
        }
        return null;
    }
    public static String createWechatToken(String type,String userInfo){
        try {
            String token = JWT.create()
                    .withIssuer("wcpms")
                    .withSubject("wcpms")
                    .withAudience("wcpms")
                    .withClaim("type",type)
                    .withClaim("userInfo",userInfo)
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            //UTF-8 encoding not supported
        }
        return null;
    }

    public static DecodedJWT decode(String s){
        try {
            DecodedJWT jwt = JWT.decode(s);
            return jwt;
        } catch (JWTDecodeException exception){
            exception.printStackTrace();
        }
        return null;
    }

    public static DecodedJWT verify(String s){
        try {
            DecodedJWT jwt = verifier.verify(s);
            return jwt;
        } catch (JWTVerificationException exception){
            //UTF-8 encoding not supported
        }
        return null;
    }

    public static Map<String,Claim> verifyAndGetClaims(String s){
        DecodedJWT jwt = verify(s);
        if (jwt == null)return null;
        return jwt.getClaims();
    }

    public static String getClaimAsString(String token,String key){
        DecodedJWT jwt = verify(token);
        if (jwt == null)return null;
        return jwt.getClaim(key).asString();
    }

    public static void main(String[] args) {
        String t = create();
        System.out.println(t);
        t+="aa";
        DecodedJWT jwt = verify(t);
        for (Map.Entry<String, Claim> entry : jwt.getClaims().entrySet()) {
            System.out.println("key="+entry.getKey());
            System.out.println("val="+entry.getValue().asInt());
        }
        System.out.println(jwt.getClaim("age").asInt());
    }
}
