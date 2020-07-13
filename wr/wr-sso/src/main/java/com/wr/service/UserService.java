package com.wr.service;

import com.wr.pojo.User;
import com.wr.util.JsonResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {
    JsonResult findUserByUP(User user, String ip, HttpServletRequest request, HttpServletResponse response);

    void logout(String ticket);

    JsonResult insertUser(User user);

    JsonResult checkUser(String userName);

    JsonResult getInfo(Long token);

    JsonResult updateUser(User user);
}
