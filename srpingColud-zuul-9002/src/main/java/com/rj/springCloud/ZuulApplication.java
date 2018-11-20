package com.rj.springCloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @version 1.0.0
 * @descripton
 * @auth rj
 * @date 2018/11/11
 */
@SpringBootApplication
@EnableZuulProxy //开启网关
public class ZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class,args);
    }
}
