package com.itheima.Community.mapper;


import com.itheima.Community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper {
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified)values(#{name},#{accountID},#{token},#{gmt_create},#{gmt_modified})")
    void insert(User user);

}
