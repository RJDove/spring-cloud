package com.rj.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import zipkin.server.EnableZipkinServer;

/**
 * @version 1.0.0
 * @descripton
 * @auth rj
 * @date 2018/11/13
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZipkinServer
public class SleuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SleuthApplication.class,args);
    }
}
