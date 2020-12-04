package com.amadana.dao;

import com.amadana.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.io.IOException;
import java.util.List;

@Mapper
public interface UserMapper {
    User login(User user) throws IOException;
    List<User> findUserByAccount(String account);

}
