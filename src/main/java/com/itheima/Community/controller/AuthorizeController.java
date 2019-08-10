package com.itheima.Community.controller;

import com.itheima.Community.data_transform_object.data_transform_object.AccessTokenDTO;
import com.itheima.Community.githubprovider.githubprovider.GithubProvider;
import com.itheima.Community.data_transform_object.data_transform_object.GithubUser;
import com.itheima.Community.mapper.UserMapper;
import com.itheima.Community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

//API 文档 https://developer.github.com/apps/building-oauth-apps/authorizing-oauth-apps/
@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client.id}")//读取配置文件
    private String clientID;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;


    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    //过程详情请看API文档
    //IndexController 通过访问https://github.com/login/oauth/authorize 附带code，state，等5个参数
    //github返回了code和state给重定向地址callback
    //我们需要再次发送post请求向GitHub，此时必须携带发过来的code state以及必须的三个参数
    //然后github就返回了这个用户的access_token，比如姓名


    public String callback(@RequestParam(name="code")String coder,
                           @RequestParam(name="state")String stater,
                                   HttpServletRequest request) throws IOException {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(coder);
        accessTokenDTO.setRedirect_uri(redirectUri);//重定向地址

        accessTokenDTO.setState(stater);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setClient_id(clientID);

        String accessToken = githubProvider.getAccessToken(accessTokenDTO);//将accesstokenDTO对象以post方式给了github，github会返回access_token，
//access_token包含了用户数据
        GithubUser githubUser = githubProvider.getUser(accessToken);

        if(githubUser!=null){

            request.getSession().setAttribute("githubUser",githubUser);
            if(githubUser.getName()!=null){
                User user = new User();
                user.setToken(UUID.randomUUID().toString());
                user.setName(githubUser.getName());
                user.setAccountID(String.valueOf(githubUser.getId()));
                user.setGmt_create(System.currentTimeMillis());
                user.setGmt_modified(user.getGmt_create());

                userMapper.insert(user);
                System.out.println(githubUser.getName());
            }else{
                System.out.println("您没昵称");
            }
            return "redirect:/";
        }else {
            System.out.println("没有此用户");
            //没获取session怎么办？
            return "redirect:/";
        }



        //问题：code和state虽然被传回callback页面，但是怎么真正获得这两个值？？还是说这两个值就已经存在了callback中
        //解决：因为github第一次返回callback网址时候，因为是GET方法请求的所以返回的参数是默认在地址中的，此时我们的AuthorizeController
        //访问callback时候，地址里的参数绑定在了String coder，stater参数当中
        //return "index";
    }
}
