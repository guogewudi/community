package com.itheima.demo.controller;

import com.itheima.demo.controller.data_transform_object.AccessTokenDTO;
import com.itheima.demo.controller.data_transform_object.GithubUser;
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


    public String callback(@RequestParam(name="code")String coder,
                           @RequestParam(name="state")String stater) throws IOException {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(coder);
        accessTokenDTO.setRedirect_uri("http://localhost:8081/callback");//重定向地址

        accessTokenDTO.setState(stater);
        accessTokenDTO.setClient_secret("cac6df006323f3839287294c3adb4fe0b1dd8542");
        accessTokenDTO.setClient_id("dc93d13d5409f449ec98");

        String accessToken = githubProvider.getAccessToken(accessTokenDTO);//将accesstokenDTO对象以post方式给了github，github会返回access_token，
//access_token包含了用户数据
        GithubUser user = githubProvider.getUser(accessToken);
        if(user!=null){
            if(user.getName()!=null){
                System.out.println(user.getName());
            }else{
                System.out.println("您没昵称");
            }
        }else {
            System.out.println("没有此用户");
        }



        //问题：code和state虽然被传回callback页面，但是怎么真正获得这两个值？？还是说这两个值就已经存在了callback中
        //解决：因为github第一次返回callback网址时候，因为是GET方法请求的所以返回的参数是默认在地址中的，此时我们的AuthorizeController
        //访问callback时候，地址里的参数绑定在了String coder，stater参数当中
        return "index";
    }
}
