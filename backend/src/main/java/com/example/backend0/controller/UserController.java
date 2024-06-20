package com.example.backend0.controller;

import com.example.backend0.entity.*;
import com.example.backend0.jwt.JwtBodyData;
import com.example.backend0.jwt.JwtCreate;
import com.example.backend0.jwt.JwtParse;
import com.example.backend0.repository.CollectRepository;
import com.example.backend0.repository.ConcreteProductRepository;
import com.example.backend0.result.Result;
import com.example.backend0.result.ResultFactory;
import com.example.backend0.service.*;
import com.example.backend0.util.VariableDefine;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;

/**
 * @ClassName UserController
 * @Description 用户的相关操作
 **/
@RestController
public class UserController {
    @Autowired
    AccountService accountService;
    @Autowired
    UserService userService;
    @Autowired
    ShopService shopService;
    @Autowired
    CollectService collectService;
    @Autowired
    ProductService productService;
    @Autowired
    MessageService messageService;
    @Autowired
    CollectRepository collectRepository;
    @Autowired
    ConcreteProductRepository concreteProductRepository;
    @PostMapping("/user/register")
    public Result register_com(@RequestParam("accountName") String accountName, @RequestParam("type")Integer type, @RequestParam("password")String password,
                               @RequestParam(value = "userName",required = false) String userName, @RequestParam(value = "age",required = false)Integer age, @RequestParam(value = "sex",required = false)String sex, @RequestParam(value = "phone",required = false)String phone,
                               @RequestParam(value = "shopName",required = false)String shopName, @RequestParam(value = "shopAddress",required = false)String shopAddress) {
        // 先判断type，只允许user和shopper两种类型进行注册
        if(type==null || ( !type.equals(VariableDefine.getTypeUser()) && !type.equals(VariableDefine.getTypeShopper()) )){
            return ResultFactory.buildFailedResult("用户类型错误");
        }
        // 判断用户名是否重复
        if(accountService.accountNameIsPresent(accountName)){
            return ResultFactory.buildFailedResult("该账号名已被注册");
        }
        // 准备account类
        Account account=new Account();
        account.setAccountName(accountName);
        account.setType(type);
        account.setPassword(password);
        Account account_saved=null;
        // 先进行user注册
        if(type.equals(VariableDefine.getTypeUser())){
            // 进行参数检验
//            if(userName==null || age==null || sex==null ||  phone==null){
//                return ResultFactory.buildFailedResult("user注册参数缺失");
//            }
//            if(!(sex.equals("male") || sex.equals("female"))){
//                return ResultFactory.buildFailedResult("user的sex属性错误");
//            }
//            if(!validPhone(phone)){
//                return ResultFactory.buildFailedResult("电话号码无效");
//            }

            // 存入表格
            User user=new User();
            user.setUserName(userName);
            user.setAge(age);
            user.setPhone(phone);
            user.setSex(sex);
            User user_saved=userService.save(user);
            if(user_saved==null){
                return ResultFactory.buildFailedResult("数据库内部错误-register-user-save");
            }

            account.setInfoID(user_saved.getID());
            account_saved=accountService.save(account);
            if(account_saved==null){
                return ResultFactory.buildFailedResult("数据库内部错误-register-user-account-save");
            }

        }
        else {// 进行商店注册
            // 进行参数检验
            if(shopName==null || shopAddress==null){
                return ResultFactory.buildFailedResult("shop注册参数缺失");
            }
            Shop shop=new Shop();
            shop.setShopName(shopName);
            shop.setShopAddress(shopAddress);
            Shop shop_saved=shopService.save(shop);
            if(shop_saved==null){
                return ResultFactory.buildFailedResult("数据库内部错误-register-shop-save");
            }
            account.setInfoID(shop_saved.getID());
            account_saved=accountService.save(account);
            if(account_saved==null){
                return ResultFactory.buildFailedResult("数据库内部错误-register-shop-account-save");
            }
        }
        return ResultFactory.buildSuccessResult(account_saved);
    }
    @PostMapping("/user/login")
    public Result login(@RequestParam("accountName")String accountName,@RequestParam("password")String password){
        if(accountName==null || password==null){
            return ResultFactory.buildFailedResult("参数缺失");
        }
        if(accountService.rightAccount(accountName,password)){
            Account account=accountService.getAccountByAccountName(accountName);
            if(account==null){
                ResultFactory.buildFailedResult("后台错误");
            }
            String token= JwtCreate.create(account.getAccountName(),account.getType(),account.getInfoID(),VariableDefine.illegalTime(),VariableDefine.signature());
            return ResultFactory.buildSuccessResult(new LogInReturn(account.getAccountName(),account.getType(),account.getInfoID(),token));
        }
        else{
            return ResultFactory.buildFailedResult("密码错误");
        }
    }
    @PostMapping("/createAdmin")
    public Result createAdmin(){
        Account admin=new Account();
        admin.setInfoID(-1);
        admin.setAccountName(VariableDefine.getAdminAccountName());
        admin.setPassword(VariableDefine.getAdminAccountPassword());
        admin.setType(VariableDefine.getTypeAdmin());
        return ResultFactory.buildSuccessResult(accountService.save(admin));
    }
    @PostMapping("/user/favorite")
    public Result createFavorite(@RequestParam("concreteProductID")Integer concreteProductID,@RequestParam("minimumPrice")Float minimumPrice,@RequestParam("token")String token){
        if(concreteProductID==null||minimumPrice==null||token==null){
            return ResultFactory.buildFailedResult("参数缺失");
        }
        JwtBodyData jwtBodyData= JwtParse.parse(token,VariableDefine.signature());
        if(!jwtBodyData.getValidity().equals(VariableDefine.tokenValid())){
            return ResultFactory.buildFailedResult("令牌失效");
        }
        if(!jwtBodyData.getType().equals(VariableDefine.getTypeUser())){
            return ResultFactory.buildFailedResult("非用户操作");
        }
        // 判断是否有过收藏
        if(collectRepository.getCollectByUserIDAndConcreteProductID(jwtBodyData.getId(),concreteProductID)!=null){
            return ResultFactory.buildFailedResult("已经收藏");
        }
        Collect collect=new Collect();
        collect.setUserID(jwtBodyData.getId());
        collect.setConcreteProductID(concreteProductID);
        Date date=new Date(System.currentTimeMillis());
        collect.setDate(date);
        if(minimumPrice==-1||minimumPrice>productService.getPartialProductByConcreteProductId(concreteProductID).getCurrentPrice()){
            minimumPrice=productService.getPartialProductByConcreteProductId(concreteProductID).getCurrentPrice()-1;
        }
        collect.setMinimumPrice(minimumPrice);
        // 增加收藏量
        ConcreteProduct concreteProduct=concreteProductRepository.findById(concreteProductID).orElse(null);
        if(concreteProduct==null){
            return ResultFactory.buildFailedResult("商品不存在");
        }
        concreteProduct.setCollectNum(concreteProduct.getCollectNum()+1);
        return ResultFactory.buildSuccessResult(collectService.save(collect));
    }
    @PostMapping("/user/favoritecheck")
    public Result getFavoritesByUserID(@RequestParam("token")String token){
        if(token==null){
            return ResultFactory.buildFailedResult("参数缺失");
        }
        JwtBodyData jwtBodyData=JwtParse.parse(token,VariableDefine.signature());
        if(!jwtBodyData.getValidity().equals(VariableDefine.tokenValid())){
            return ResultFactory.buildFailedResult("令牌失效");
        }
        if(!jwtBodyData.getType().equals(VariableDefine.getTypeUser())){
            return ResultFactory.buildFailedResult("非用户操作");
        }
        return ResultFactory.buildSuccessResult(collectService.getFavoritesByUserID(jwtBodyData.getId()));
    }
    @PostMapping("/usr/message")
    public Result getMessage(@RequestParam("token")String token){
        if(token==null){
            return ResultFactory.buildFailedResult("参数缺失");
        }
        JwtBodyData jwtBodyData=JwtParse.parse(token,VariableDefine.signature());
        if(!jwtBodyData.getValidity().equals(VariableDefine.tokenValid())){
            return ResultFactory.buildFailedResult("令牌失效");
        }
        if(!jwtBodyData.getType().equals(VariableDefine.getTypeUser())){
            return ResultFactory.buildFailedResult("非用户操作");
        }
        return ResultFactory.buildSuccessResult(messageService.getMessagesByUserID(jwtBodyData.getId()));
    }

    public static boolean validPhone(String phone){
        return true;// todo
    }

    public Result createFavoriteTest(@RequestParam("concreteProductID")Integer concreteProductID, @RequestParam("minimumPrice")Float minimumPrice,Date date,Integer userId){
        if(collectRepository.getCollectByUserIDAndConcreteProductID(userId,concreteProductID)!=null){
            return ResultFactory.buildFailedResult("已经收藏");
        }
        Collect collect=new Collect();
        collect.setUserID(userId);
        collect.setConcreteProductID(concreteProductID);
        collect.setDate(date);

        if(minimumPrice==-1||minimumPrice>productService.getPartialProductByConcreteProductId(concreteProductID).getCurrentPrice()){
            minimumPrice=productService.getPartialProductByConcreteProductId(concreteProductID).getCurrentPrice()-1;
        }
        collect.setMinimumPrice(minimumPrice);
        // 增加收藏量
        ConcreteProduct concreteProduct=concreteProductRepository.findById(concreteProductID).orElse(null);
        if(concreteProduct==null){
            return ResultFactory.buildFailedResult("商品不存在");
        }
        concreteProduct.setCollectNum(concreteProduct.getCollectNum()+1);
        return ResultFactory.buildSuccessResult(collectService.save(collect));
    }
}
@Data
class LogInReturn{
    String accountName;
    Integer type; // 0 管理员，1用户，2商户
    Integer infoID;
    String token;

    public LogInReturn(String accountName, Integer type, Integer infoID, String token) {
        this.accountName = accountName;
        this.type = type;
        this.infoID = infoID;
        this.token = token;
    }
}