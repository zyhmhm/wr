package com.wr.feignclient;

import com.wr.feignclient.fb.UserFeignServiceFB;
import com.wr.pojo.User;
import com.wr.util.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "sso-service",fallback = UserFeignServiceFB.class)
public interface UserFeignService {
    @PostMapping("/user/updateUser")
    public JsonResult updateUser(@RequestBody User user);
}
