package com.rj.springCloud.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @version 1.0.0
 * @descripton
 * @auth rj
 * @date 2018/11/10
 */
@Configuration
public class Config {

    @Bean
    @LoadBalanced  //负载均衡工具
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    /**
     * 默认负载均衡策略为轮训
     *  RandomRule为随机
     * @return
     */
    @Bean
    public IRule getIRule(){
        return new RandomRule();
    }
}
