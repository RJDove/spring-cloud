package com.rj.springCloud.controller;

import com.rj.springcloud.service.DeptClientService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0.0
 * @descripton
 * @auth rj
 * @date 2018/11/11
 */
@RestController
@Log
public class FeignController {

    @Autowired
    private DeptClientService deptClientService;

    @RequestMapping(value = "/feign/{name}",method = RequestMethod.GET)
    public String test(@PathVariable String name){
        log.info("feign收到请求，入参：" + name);
        return deptClientService.testFeign(name);
    }

}
