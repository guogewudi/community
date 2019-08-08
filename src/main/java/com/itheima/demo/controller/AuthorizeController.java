package com.itheima.demo.controller;

import com.itheima.demo.controller.data_transform_object.AccessTokenDTO;
import com.itheima.demo.controller.githubprovider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
//API 文档 https://developer.github.com/apps/building-oauth-apps/authorizing-oauth-apps/
@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @GetMapping("/callback")
    //IndexController 通过访问https://github.com/login/oauth/authorize 附带code，state，等5个参数
    //github返回了code和state给重定向地址callback
    //我们需要再次发送post请求向GitHub，此时必须携带发过来的code state以及必须的三个参数
    //然后github就返回了这个用户的access_token，比如姓名


    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state) throws IOException {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri("https://localhost:8081/callback");//重定向地址
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_secret("cac6df006323f3839287294c3adb4fe0b1dd8542");
        accessTokenDTO.setClient_id("dc93d13d5409f449ec98");

        githubProvider.getAccessToken(accessTokenDTO);//将accesstokenDTO对象以post方式给了github，github会返回access_token，
        //access_token包含了用户数据
        //问题：code和state虽然被传回callback页面，但是怎么真正获得这两个值？？还是说这两个值就已经存在了callback中
        return "index";
    }
}
