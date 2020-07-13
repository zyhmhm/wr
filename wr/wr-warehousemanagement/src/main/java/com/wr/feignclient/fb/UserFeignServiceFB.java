package com.wr.feignclient.fb;

import com.wr.feignclient.UserFeignService;
import com.wr.pojo.User;
import com.wr.util.JsonResult;
import org.springframework.stereotype.Component;

@Component
public class UserFeignServiceFB implements UserFeignService {
    @Override
    public JsonResult updateUser(User user) {
        return null;
    }
}
