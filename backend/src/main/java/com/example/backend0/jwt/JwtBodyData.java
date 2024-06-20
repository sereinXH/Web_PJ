package com.example.backend0.jwt;

import lombok.Data;

/**
 * @ClassName JwtBodyData
 * @Description 定义token中body的数据
 **/
@Data
public class JwtBodyData {
    String validity;
    String name;
    Integer type;
    Integer id;
    public String toString(){
        return "validity :"+validity+" userName:"+name+" userType:"+type+" userId:"+id;
    }
}
