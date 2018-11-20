package com.rj.springCloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @version 1.0.0
 * @descripton
 * @auth rj
 * @date 2018/11/11
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.rj.springcloud")
@ComponentScan(basePackages = "com.rj.springcloud")
public class ConsumerFeign8004Application {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerFeign8004Application.class,args);
    }
}
