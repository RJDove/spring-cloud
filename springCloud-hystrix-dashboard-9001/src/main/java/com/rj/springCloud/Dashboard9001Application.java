package com.rj.springCloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @version 1.0.0
 * @descripton
 * @auth rj
 * @date 2018/11/11
 */
@SpringBootApplication
@EnableHystrixDashboard
public class Dashboard9001Application {
    public static void main(String[] args) {
        SpringApplication.run(Dashboard9001Application.class,args);
    }
}
