package com.example.backend0.util;

import jakarta.persistence.criteria.CriteriaBuilder;

/**
 * @ClassName VariableDefine
 * @Description
 **/

public class VariableDefine {
    public static String tokenValid(){return "validToken";}
    public static String signature(){return "XXWANDCPX";}
    public static long illegalTime(){return 3*60*60;}// 三小时
    public static Integer getTypeUser(){
        return 1;
    }
    public static Integer getTypeShopper(){
        return 2;
    }
    public static Integer getTypeAdmin(){
        return 0;
    }
    public static String getAdminAccountName(){return "admin";}
    public static String getAdminAccountPassword(){return "123123";}
    public static Integer getUserNumber(){return 100;}
    public static Integer getShopNumber(){return 30;}
    public static Integer getProductNumber(){return 10;}
    public static Integer getConcreteProductNumber(){return getProductNumber()*getShopNumber();}
}
