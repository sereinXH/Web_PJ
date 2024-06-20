package com.example.backend0.controller;

import com.example.backend0.jwt.JwtBodyData;
import com.example.backend0.jwt.JwtParse;
import com.example.backend0.result.Result;
import com.example.backend0.result.ResultFactory;
import com.example.backend0.service.AccountService;
import com.example.backend0.service.ShopService;
import com.example.backend0.service.UserService;
import com.example.backend0.util.VariableDefine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TokenController
 * @Description
 **/
@RestController
public class TokenController {
    @Autowired
    AccountService accountService;
    @Autowired
    UserService userService;
    @Autowired
    ShopService shopService;
    @PostMapping("/token")
    public Result getInfoByToken(@RequestParam("token")String token){
        JwtBodyData accountData=JwtParse.parse(token, VariableDefine.signature());
        if(!accountData.getValidity().equals(VariableDefine.tokenValid())){
            return ResultFactory.buildFailedResult("无效token");
        }
        if(accountData.getId()<0){
            return ResultFactory.buildFailedResult("无效账号");
        }
        if(accountData.getType().equals(VariableDefine.getTypeUser())){
            return ResultFactory.buildSuccessResult(userService.getUserByID(accountData.getId()));
        }
        else if(accountData.getType().equals(VariableDefine.getTypeShopper())){
            return ResultFactory.buildSuccessResult(shopService.getShopById(accountData.getId()));
        }
        else{
            return ResultFactory.buildSuccessResult();
        }
    }
}
