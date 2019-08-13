package com.itheima.Community.mapper;


import com.itheima.Community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface UserMapper {
    //User user是一个实体类的对象，自动把实体类对应的属性放进#{}
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified)values(#{name},#{accountID},#{token},#{gmt_create},#{gmt_modified})")
    void insert(User user);

    //这里token不是一个类，必须加上注解才可以将形参传进#{token}  key = param里name value = token
     @Select("select* from user where token = #{good}")
    User findByToken(@Param("good") String token);
}
