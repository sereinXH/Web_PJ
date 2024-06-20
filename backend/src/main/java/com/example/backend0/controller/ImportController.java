package com.example.backend0.controller;

import com.example.backend0.result.Result;
import com.example.backend0.result.ResultFactory;
import com.example.backend0.util.RandomUtil;
import com.example.backend0.util.VariableDefine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ImportController
 * @Description
 **/
@RestController
public class ImportController {
    @Autowired
    AdminController adminController;
    @Autowired
    ProductController productController;
    @Autowired
    UserController userController;
    @Autowired
    ShopController shopController;
    @Autowired
    CollectController controller;

    @PostMapping("/test/importPlatform")
    public void importPlatforms() {
        adminController.upperPlatform("京东");
        adminController.upperPlatform("淘宝");
        adminController.upperPlatform("得物");
        adminController.upperPlatform("拼多多");
    }

    @PostMapping("/test/productImport")
    public void importProducts() {
        // 创建产品1
        productController.upperAProduct("笔记本电脑", "电子产品", "上海市XXX路123号", "2023-01-01", "专业人士的强大笔记本电脑");

        // 创建产品2
        productController.upperAProduct("智能手机", "电子产品", "北京市YYY路456号", "2023-02-15", "具备先进功能的最新款智能手机");

        // 创建产品3
        productController.upperAProduct("咖啡机", "家用电器", "广州市ZZZ路789号", "2023-03-10", "自动咖啡机，让你的早晨更美好");

        // 创建产品4
        productController.upperAProduct("空气净化器", "家用电器", "深圳市AAA路101号", "2023-04-05", "有效清除室内空气污染物");

        // 创建产品5
        productController.upperAProduct("运动鞋", "服装", "南京市BBB路202号", "2023-05-20", "舒适耐穿的运动鞋");
        //
        productController.upperAProduct("平板电脑", "电子产品", "广州市ZZZ路789号", "2023-03-10", "轻薄便携平板电脑，助力高效办公");

        // 创建产品4
        productController.upperAProduct("空调", "家用电器", "深圳市AAA路101号", "2023-04-05", "智能空调，全方位舒适体验");

        // 创建产品5
        productController.upperAProduct("时尚手提包", "时尚配饰", "南京市BBB路202号", "2023-05-20", "潮流设计，展现个性时尚");

        // 创建产品6
        productController.upperAProduct("健康按摩椅", "家居健康", "成都市CCC路303号", "2023-06-15", "舒适按摩椅，缓解疲劳，促进健康");

        // 创建产品7
        productController.upperAProduct("高级化妆品套装", "美妆护肤", "重庆市DDD路404号", "2023-07-10", "精选化妆品，呵护肌肤");

        // 创建产品8
        productController.upperAProduct("电动摩托车", "交通工具", "武汉市EEE路505号", "2023-08-25", "环保电动交通，畅享出行乐趣");

        // 创建产品9
        productController.upperAProduct("书桌椅组合", "家具", "西安市FFF路606号", "2023-09-10", "实用书桌椅，打造舒适学习空间");

        // 创建产品10
        productController.upperAProduct("新鲜水果礼盒", "食品饮料", "天津市GGG路707号", "2023-10-15", "新鲜水果，健康营养，送礼佳品");
    }

    @PostMapping("/test/shopperImport")
    public void importShoppers() {
        for (int i = 0; i < VariableDefine.getShopNumber(); i++) {
            userController.register_com("shop" + i, VariableDefine.getTypeShopper(), "111111", "张三", 19, "male", null, i + "店", "邯郸路558");
        }
    }

    @PostMapping("/test/userImport")
    public void importUsers() {
        for (int i = 0; i < VariableDefine.getUserNumber(); i++) {
            userController.register_com("user" + i, VariableDefine.getTypeUser(), "111111", "大虎1", 23, "male", "13655879586", null, null);
        }
    }

    @PostMapping("/test/concreteProductImport")
    public void importConcreteProduct() {
        LocalDate specificDate = LocalDate.of(2022, 1, 1);

        // 将 LocalDate 转换为 java.sql.Date
        Date date = Date.valueOf(specificDate);
        for (int i = 1; i <= VariableDefine.getProductNumber(); i++) {
            for (int j = 1; j <= VariableDefine.getShopNumber(); j++) {
                productController.upperAConcreteProductTest(j, RandomUtil.generateRandomInt(1, 4), i, RandomUtil.generateRandomFloat(1, 80), date);
            }

        }
//        productController.upperAConcreteProductTest(1, 1, 1, 9.36F, date);
//        productController.upperAConcreteProductTest(2, 2, 2, 90.26F, date);
//        productController.upperAConcreteProductTest(1, 3, 4, 36F, date);
//        productController.upperAConcreteProductTest(2, 4, 3, 98.6F, date);
//        productController.upperAConcreteProductTest(3, 4, 5, 97.6F, date);
//        productController.upperAConcreteProductTest(3, 4, 6, 6.6F, date);
//        productController.upperAConcreteProductTest(4, 2, 7, 9.3F, date);
//        productController.upperAConcreteProductTest(4, 2, 8, 9.53F, date);
//        productController.upperAConcreteProductTest(4, 2, 9, 9.83F, date);
//        productController.upperAConcreteProductTest(1, 2, 10, 955.229F, date);
//        productController.upperAConcreteProductTest(1, 2, 1, 9.36F, date);
//        productController.upperAConcreteProductTest(1, 2, 2, 90.26F, date);
//        productController.upperAConcreteProductTest(2, 3, 4, 35F, date);
//        productController.upperAConcreteProductTest(2, 4, 4, 98.9F, date);
//        productController.upperAConcreteProductTest(3, 4, 5, 97.6F, date);
//        productController.upperAConcreteProductTest(3, 2, 6, 5.6F, date);
//        productController.upperAConcreteProductTest(4, 1, 7, 9.3F, date);
//        productController.upperAConcreteProductTest(4, 1, 8, 9.53F, date);
//        productController.upperAConcreteProductTest(4, 1, 9, 8.83F, date);
//        productController.upperAConcreteProductTest(3, 2, 10, 555.229F, date);
//        productController.upperAConcreteProductTest(2, 2, 1, 9.36F, date);
//        productController.upperAConcreteProductTest(3, 2, 1, 58.36F, date);
//        productController.upperAConcreteProductTest(4, 2, 1, 9.3F, date);
//        productController.upperAConcreteProductTest(4, 1, 1, 8.36F, date);
//        productController.upperAConcreteProductTest(3, 4, 1, 9.36F, date);
    }

    @PostMapping("/import/changePrice")
    public void changePrice() {
        List<Date> generatedSqlDates = ProductController.generateSqlDates(300);
        for (Date date : generatedSqlDates) {
            System.out.println("价格" + date.toString());
            for (int i = 1; i <= VariableDefine.getConcreteProductNumber(); i++) {
                if (RandomUtil.generateRandomInt(0, 80) == 5)
                    shopController.changeProductOnlyPrice((float) RandomUtil.generateRandomFloat(1, 10), i, date);
            }
        }

    }

    @PostMapping("/import/favorite")
    public void favorite(@RequestParam("days") Integer days, @RequestParam("random1") Integer random1) {
        List<Date> generatedSqlDates = ProductController.generateSqlDates(days);
        for (Date date : generatedSqlDates) {
            System.out.println("收藏" + date.toString());
            for (int i = 1; i <= VariableDefine.getConcreteProductNumber(); i++) {
                for (int j = 1; j <= VariableDefine.getUserNumber(); j++) {
                    if (RandomUtil.generateRandomInt(0, random1) == 5) {
                        userController.createFavoriteTest(i, (float) RandomUtil.generateRandomFloat(1, 60), date, j);
                    }
                }
            }
        }
    }

    @PostMapping("/import/fullOpe")
    public void full() {
        List<Date> generatedSqlDates = ProductController.generateSqlDates(365);
        for (Date date : generatedSqlDates) {
            System.out.println("*******" + date.toString());
            //随机收藏1件商品
            userController.createFavoriteTest(RandomUtil.generateRandomInt(1,VariableDefine.getConcreteProductNumber()), (float) RandomUtil.generateRandomFloat(1, 6), date, RandomUtil.generateRandomInt(1, VariableDefine.getUserNumber()));
            // 随机更改1件商品的价格
            shopController.changeProductOnlyPrice((float) RandomUtil.generateRandomFloat(1, 10), RandomUtil.generateRandomInt(1, VariableDefine.getConcreteProductNumber()), date);
            // 随机更改四件商品的价格，增加收藏
            List<Integer> list = new ArrayList<>();
            while (list.size() <= 4) {
                Integer integer = RandomUtil.generateRandomInt(1, VariableDefine.getConcreteProductNumber());
                if (!list.contains(integer)) {
                    list.add(integer);
                }
            }
            for (Integer concreteId : list) {
                // 随机改变价格
                shopController.changeProductOnlyPrice((float) RandomUtil.generateRandomFloat(5, 10), concreteId, date);
                // 增加收藏
                for (int i = 1; i < 5; i++)
                    userController.createFavoriteTest(concreteId, (float) RandomUtil.generateRandomFloat(1, 10), date, RandomUtil.generateRandomInt(1, VariableDefine.getUserNumber()));
            }
        }
    }


    @PostMapping("/test/importFull")
    public Result fullImport() {
        userController.createAdmin();
        importPlatforms();
        importShoppers();
        importProducts();
        importUsers();
        importConcreteProduct();
//        favorite(365, 20);
//        changePrice();
        full();

        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/test/trigger")
    public Result testTrigger() {
        productController.upperAProduct("testTrigger", "testTrigger", "上海市XXX路123号", "2023-01-01", "testTrigger");
        userController.register_com("shop-testTrigger", VariableDefine.getTypeShopper(), "111111", "testTrigger", 19, "male", null, "testTrigger店", "邯郸路558");
        userController.register_com("testTrigger", VariableDefine.getTypeUser(), "111111", "大虎testTrigger", 23, "male", "13655879586", null, null);
        LocalDate specificDate = LocalDate.of(2022, 1, 1);
        Date date = Date.valueOf(specificDate);
        productController.upperAConcreteProductTest(VariableDefine.getShopNumber() + 1, RandomUtil.generateRandomInt(1, 4), 11, 8.8F, date);
        userController.createFavoriteTest(VariableDefine.getConcreteProductNumber() + 1, 8f, date, 101);
        shopController.changeProductOnlyPrice(6f, VariableDefine.getConcreteProductNumber() + 1, date);
        return null;
    }
}
