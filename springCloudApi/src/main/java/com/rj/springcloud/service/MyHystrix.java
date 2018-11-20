package com.rj.springcloud.service;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @version 1.0.0
 * @descripton 服务降级 不采用注解的方式 降级逻辑业务逻辑解耦
 * @auth rj
 * @date 2018/11/11
 */
@Component
public class MyHystrix implements FallbackFactory<DeptClientService>{

    @Override
    public DeptClientService create(Throwable throwable) {
        return new DeptClientService() {

            @Override
            public String testFeign(String name) {
                return "自定义服务降级hystrix";
            }
        };
    }
}
