package com.mock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * description: SmsApplication <br>
 * date: 2021/3/18 9:28 <br>
 * author: cn_yaojin <br>
 * version: 1.0 <br>
 */
@SpringBootApplication
public class SmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmsApplication.class, args);
        System.out.println("服务已启动");
    }

}
