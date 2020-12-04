package com.amadana.service;

import com.amadana.entity.User;
import com.amadana.result.Expection;
import org.apache.ibatis.annotations.Mapper;

import java.io.IOException;
import java.util.List;

@Mapper
/**
 * @author han
 * @date 2020/11/9
 * @desc 用户接口类
 */
public interface UserService {

    Expection login(User user) throws IOException;
    List<User> findUserByAccount(String account) throws IOException;
}
