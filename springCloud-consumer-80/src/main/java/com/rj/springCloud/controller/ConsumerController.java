package com.rj.springCloud.controller;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @version 1.0.0
 * @descripton
 * @auth rj
 * @date 2018/11/10
 */
@RestController
@Log
public class ConsumerController {

    //public static final String URL = "http://localhost:8001";

    //通过微服务名调用服务
    public static final String URL = "http://SPRINGCLOUD-DEPT";

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/consumer/{name}",method = RequestMethod.GET)
    public String testRest(@PathVariable String name){
        log.info("入参：" + name);
        return restTemplate.getForObject(URL+"/dept/test02/" + name,String.class);
    }
}
