//package com.generator;
//
//import com.google.common.base.CaseFormat;
//import com.squareup.javapoet.*;
//import org.jfaster.mango.annotation.DB;
//import org.jfaster.mango.annotation.SQL;
//
//import javax.lang.model.element.Modifier;
//import java.io.File;
//import java.io.IOException;
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @program: mp-conference-manage
// * @description:
// * @author: zjw
// * @create: 2020-01-16 15:47
// **/
//public class GeneratorMain {
//
//    public static void main(String[] args) throws IOException {
//        Generator4BeanAndDao generator = new Generator4BeanAndDao();
//        try {
//            generator.generateCode(
//                    "mysql-m-wr-car-all-dev-new-db.01zhuanche.com",
//                    "3307",
//                    "MDB_CAR_MANAGE",
//                    "car_adm_log",
//                    "dev_sqhc_rentcar",
//                    "dev_BzkmQM%O^U7Dy8X3",
//                    "com.sq.mp.manage",
//                    "D:\\workspace\\mp-manage-boot\\mp-manage-dao\\src\\main\\java");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("------------done----------");
//    }
//
//}
