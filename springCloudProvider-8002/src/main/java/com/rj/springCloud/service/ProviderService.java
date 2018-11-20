package com.rj.springCloud.service;

import com.rj.springcloud.dto.DeptDTO;

/**
 * @version 1.0.0
 * @descripton
 * @auth rj
 * @date 2018/11/10
 */
public interface ProviderService {

    public String sayHello(String name);

    public String getDept(DeptDTO deptDTO);
}
