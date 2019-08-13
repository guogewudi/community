package com.itheima.Community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublishController {
    @GetMapping("/publish")
    public String pub(){
        return "publish";
    }
}
