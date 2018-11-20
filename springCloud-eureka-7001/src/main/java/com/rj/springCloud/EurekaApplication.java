package com.rj.springCloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @version 1.0.0
 * @descripton 演示eureka注册中心
 * @auth rj
 * @date 2018/11/10
 */
@SpringBootApplication
@EnableEurekaServer //EurekaServer服务端启动类，接受其他微服务注册进来
public class EurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class,args);
    }
}
