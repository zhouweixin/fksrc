package com.hnu.fk.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 14:17 2018/8/1
 * @Modified By:
 */
@RestController
public class HelloController {
    @RequestMapping(value = "/hello")
    public String hello(){
        return "Hello";
    }

    @RequestMapping(value = "/hello1")
    public String hello1(){
        return "Hello1";
    }
}
