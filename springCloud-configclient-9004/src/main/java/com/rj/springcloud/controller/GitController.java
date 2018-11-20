package com.rj.springcloud.controller;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0.0
 * @descripton 读取git上的配置文件
 * @auth rj
 * @date 2018/11/12
 */
@RestController
@Log
public class GitController {

    @Value("${spring.profiles}")
    private String name;

    @Value("${spring.application.name}")
    private String age;

    @RequestMapping(value = "/test")
    public String test(){
        log.info("收到请求");
        return name + "\t" + age;
    }

}
