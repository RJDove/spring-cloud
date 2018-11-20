package com.rj.springCloud.controller;

import com.rj.springCloud.service.ProviderService;
import com.rj.springcloud.dto.DeptDTO;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version 1.0.0
 * @descripton
 * @auth rj
 * @date 2018/11/10
 */
@RestController
@Log
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping(value = "/dept/test",method = RequestMethod.POST)
    public String test(@RequestBody DeptDTO deptDTO){
        log.info("[controller收到请求][开始]");
        return providerService.getDept(deptDTO);
    }

    @RequestMapping(value = "/dept/test02/{name}",method = RequestMethod.GET)
//    @HystrixCommand(fallbackMethod = "testHystrix")
    public String test02(@PathVariable String name){
        log.info("[controller --> test02 收到请求][开始]");
        if ("rj".equals(name)) {
            throw new RuntimeException();
        }
        return this.providerService.sayHello(name);
    }

//    public String testHystrix(@PathVariable String name){
//        return "testHystrix";
//    }

    @RequestMapping(value = "/dept/discovery",method = RequestMethod.GET)
    public void testDiscovery(){
        List<String> list = discoveryClient.getServices();
        log.info("list -->" + list);

        List<ServiceInstance> serviceInstances = discoveryClient.getInstances("SPRINGCLOUD-DEPT-8001");

        for(ServiceInstance element : serviceInstances){
            log.info(element.getServiceId() + "\t" + element.getHost() + "\t" + element.getUri() + "\t" + element.getPort());
        }
    }
}
