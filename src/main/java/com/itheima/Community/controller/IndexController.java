package com.itheima.Community.controller;

import com.itheima.Community.mapper.UserMapper;
import com.itheima.Community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/")
    public String index(HttpServletRequest request){
        //response设置cookie
        //request请求cookie
        Cookie[] cookies = request.getCookies();
        //防止cookies空
        if(cookies!=null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("githubUser", user);
                    }
                    break;

                }
            }
            return "index";
        }
            //System.out.println("");
            return "index";

    }

}
