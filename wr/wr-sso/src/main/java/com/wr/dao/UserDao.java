package com.wr.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wr.pojo.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseMapper<User> {
}
