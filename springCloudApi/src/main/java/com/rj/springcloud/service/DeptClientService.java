package com.rj.springcloud.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @version 1.0.0
 * @descripton  演示Feign 接口调用
 * @auth rj
 * @date 2018/11/11
 */
//@FeignClient(value = "SPRINGCLOUD-DEPT") //微服务名
@FeignClient(value = "SPRINGCLOUD-DEPT",fallbackFactory = MyHystrix.class)
public interface DeptClientService {

    @RequestMapping(value = "/dept/test02/{name}",method = RequestMethod.GET)
    public String testFeign(@PathVariable("name") String name);
}
