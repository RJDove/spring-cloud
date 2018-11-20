package com.rj.springCloud.service.impl;

import com.rj.springCloud.service.ProviderService;
import com.rj.springcloud.dto.DeptDTO;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

/**
 * @version 1.0.0
 * @descripton
 * @auth rj
 * @date 2018/11/10
 */
@Service
@Log
public class ProviderServiceImpl implements ProviderService {

    @Override
    public String getDept(DeptDTO deptDTO) {
        log.info("[入参][" + deptDTO + "]");
        return "hello: " + deptDTO.getName();
    }

    @Override
    public String sayHello(String name) {
        return "hello2: " + name + " 8002";
    }
}
