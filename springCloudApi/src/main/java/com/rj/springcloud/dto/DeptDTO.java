package com.rj.springcloud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @version 1.0.0
 * @descripton
 * @auth rj
 * @date 2018/11/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DeptDTO implements Serializable{

    private String name;

    private Integer age;

    private String address;
}
