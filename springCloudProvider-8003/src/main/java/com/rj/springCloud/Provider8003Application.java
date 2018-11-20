package com.rj.springCloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @version 1.0.0
 * @descripton
 * @auth rj
 * @date 2018/11/10
 */
@SpringBootApplication
@EnableEurekaClient //将服务注册到eurake注册中心
@EnableDiscoveryClient //服务发现
public class Provider8003Application {

    public static void main(String[] args) {
        SpringApplication.run(Provider8003Application.class,args);
    }
}
