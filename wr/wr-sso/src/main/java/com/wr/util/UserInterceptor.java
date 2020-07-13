package com.wr.util;

import com.wr.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component //表示交给spring管理
public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    private JedisCluster jedisCluster;

    /**
     * boolean: true放行
     *          false拦截  必须配合重定向使用
     * 业务思路：
     *      如何判断用户是否登录
     * 步骤：
     *      1.动态获取Cookie中的JT_TICKET中的值
     *      2.获取用户ip地址，校验数据
     *      3.查询redis服务器是否有数据
     *
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        //获取Cookie数据
        Cookie cookie = CookieUtil.getCookie(request, "JT_TICKET");
        if(cookie != null){
            String ticket = cookie.getValue();
            if(!StringUtils.isEmpty(ticket)){
                if(jedisCluster.exists(ticket)){
                    //校验ip
                    String nowIp = jedisCluster.hget(ticket, "JT_USER_IP");
                    String realIp = IPUtil.getIpAddr(request);
                    if(nowIp.equals(realIp)){
                        String jt_user = jedisCluster.hget(ticket, "JT_USER");
                        User user = JsonUtil.from(jt_user, User.class);
                        //request.setAttribute("JT_USER", user);
                        UserThreadLocalUtil.setUser(user);
                        return true;//表示放行
                    }

                }
            }
        }

//        response.sendRedirect("/user/login.html");
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocalUtil.remove();
    }
}
