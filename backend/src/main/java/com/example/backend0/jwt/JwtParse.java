package com.example.backend0.jwt;

import com.example.backend0.util.VariableDefine;
import io.jsonwebtoken.*;

/**
 * @ClassName JwtParse
 * @Description
 **/
public class JwtParse {
    public static JwtBodyData parse(String token,String signature){
        JwtBodyData jwtBodyData=new JwtBodyData();
        try {
            JwtParser jwtParser = Jwts.parser();
            Jws<Claims> claimsJws = jwtParser.setSigningKey(signature).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            jwtBodyData.setType((Integer) claims.get("type"));
            jwtBodyData.setName(claims.get("name").toString());
            //System.out.println(claims.get("userid"));
            jwtBodyData.setId((Integer) claims.get("id"));
            //System.out.println(claims.getExpiration());
        } catch(ExpiredJwtException e){
            jwtBodyData.setValidity("ExpiredJwtException");
            return jwtBodyData;
        }catch (SignatureException f){
            jwtBodyData.setValidity("SignatureException");
            return jwtBodyData;
        }catch(Exception g){
            jwtBodyData.setValidity("Exception");
            return jwtBodyData;
        }
        jwtBodyData.setValidity(VariableDefine.tokenValid());
        return jwtBodyData;
    }
}

