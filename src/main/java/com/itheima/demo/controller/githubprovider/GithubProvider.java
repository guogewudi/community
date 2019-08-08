package com.itheima.demo.controller.githubprovider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itheima.demo.controller.data_transform_object.AccessTokenDTO;
import com.itheima.demo.controller.data_transform_object.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;

@Component
//用okhttp模拟post请求,github返回token
//https://developer.github.com/apps/building-oauth-apps/authorizing-oauth-apps/
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO) throws IOException {
         MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();


            RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
            Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String string = response.body().string();
                String[] split = string.split("&");
                //access_token=b2b0acf65e7511fc9da3daa9b9c45c970bb812cc&scope=user&token_type=bearer
                String token_str = split[0];
                //access_token=b2b0acf65e7511fc9da3daa9b9c45c970bb812cc
                String[] split1 = token_str.split("=");
                //b2b0acf65e7511fc9da3daa9b9c45c970bb812cc
                String token  = split1[1];



                System.out.println(token);
                return token;
            }catch (Exception e){
                 e.printStackTrace();
            }
            return null;

    }




    public GithubUser getUser(String accessToken){
        /*
        OkHttpClient client = new OkHttpClient();

String run(String url) throws IOException {
  Request request = new Request.Builder()
      .url(url)
      .build();

  try (Response response = client.newCall(request).execute()) {
    return response.body().string();
  }
}
         */


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
             GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
             return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

}
