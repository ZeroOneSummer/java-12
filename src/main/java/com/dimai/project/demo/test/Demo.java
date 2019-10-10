package com.dimai.project.demo.test;

import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.function.Function;

/**
 * Created by pijiang on 2019/8/28.
 * 适用于java12
 */
public class Demo {

    private static void switchDemo(Color color){
//        var a = "yes";
//        var b = 400;
//        var c = new Date();
//        switch (color) {
//            case RED,YELLOW,GLAY -> System.out.println(a);
//            case GREEN -> System.out.println(b);
//            case PINK -> System.out.println(c);
//        }
    }

    private static void strTransDemo(String str){
//        System.out.println(str.transform(new Function<String, String>() {
//            @Override
//            public String apply(String s) {
//                return StringUtils.capitalize(s);
//            }
//        }));
    }

    public static void main(String[] args) {
        Demo.switchDemo(Color.GREEN);
//        Demo.strTransDemo("name");
    }
}