package com.amadana.serviceImpl;

import com.amadana.dao.UserMapper;
import com.amadana.entity.User;
import com.amadana.enums.StateCode;
import com.amadana.result.Expection;
import com.amadana.service.UserService;
import com.amadana.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserMapper userMapper;
    /**
     * 登录接口
     * @param user
     * @return
     * @throws IOException
     */
    @Override
    public Expection login(User user) throws IOException {
        List<User> userList = userMapper.findUserByAccount(user.getAccount());
        if (userList.size() == 0) {
            return new Expection(StateCode.INVALID_USER.getCode(),StateCode.INVALID_USER.getMessage());
        }else {
            //加密处理
            user.setPassword(MD5Utils.getEncodeChar(user.getPassword()));
            User user1 = userMapper.login(user);
            if (user1 == null) {
                return new Expection(StateCode.LOGIN_FAILED.getCode(),StateCode.LOGIN_FAILED.getMessage());
            }
            return new Expection(StateCode.LOGIN_SUCCESS.getCode(),StateCode.LOGIN_SUCCESS.getMessage());
        }
    }

    @Override
    public List<User> findUserByAccount(String account) throws IOException {
        return userMapper.findUserByAccount(account);
    }
}
