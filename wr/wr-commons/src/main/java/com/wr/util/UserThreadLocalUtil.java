package com.wr.util;

import com.wr.pojo.User;

public class UserThreadLocalUtil {
    private static final ThreadLocal<User> userThreadLocal= new ThreadLocal<User>();

    public static void setUser(User user){
        userThreadLocal.set(user);
    }

    public static User getUser(){
//        User user = new User();
//        user.setId(1l);
//        user.setUserName("test").setRepoId(1l);
        return userThreadLocal.get();
//        return user;
    }

    /**
     * 为了防止内存溢出，必须要添加移除方法
     */
    public static void remove(){
        userThreadLocal.remove();
    }
}
