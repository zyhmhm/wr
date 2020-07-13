package com.wr.configure;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.wr.pojo.User;
import com.wr.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestFilter extends ZuulFilter {

    @Autowired
    private JedisCluster jedisCluster;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;//在请求之前
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER+1;//filter执行顺序
    }

    @Override
    public boolean shouldFilter() {
        return true;//是否执行该过滤器，此处应为true,说明需要过滤
    }

    @Override
    public Object run() {
        // 获取request对象
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();


        // 这些是对请求头的匹配，网上有很多解释
        // 设置哪个源可以访问我
//        response.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Origin","http://www.huisen.top");
        //允许携带cookie
        response.setHeader("Access-Control-Allow-Credentials","true");
        // 允许哪个方法(也就是哪种类型的请求)访问我
        response.setHeader("Access-Control-Allow-Methods","GET, HEAD, POST, PUT, DELETE, OPTIONS, PATCH");
        // 允许携带哪个头访问我
        response.setHeader("Access-Control-Allow-Headers","authorization, X-Requested-With, Content-Type, X-File-Name,X-Token,token");
        response.setHeader("Access-Control-Expose-Headers","X-forwared-port, X-forwarded-host");
        response.setHeader("Vary","Origin,Access-Control-Request-Method,Access-Control-Request-Headers");

        // 跨域请求一共会进行两次请求 先发送options 是否可以请求
        // 调试可发现一共拦截两次 第一次请求为options，第二次为正常的请求 eg：get请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())){
            ctx.setSendZuulResponse(false); //验证请求不进行路由
            ctx.setResponseStatusCode(HttpStatus.OK.value());//返回验证成功的状态码
            ctx.set("isSuccess", true);
            return null;
        }
        // 第二次请求先判断是不是请求sso方法
        String serviceId = (String) ctx.get(FilterConstants.SERVICE_ID_KEY);
        if("sso-service".equals(serviceId)){
            ctx.setSendZuulResponse(true); //对请求进行路由
            ctx.setResponseStatusCode(HttpStatus.OK.value());
            ctx.set("isSuccess", true);
            return null;
        }
        // 第二次请求（非验证，eg：get请求不会进到上面的if） 会走到这里往下进行
//        //获取Cookie数据
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
                        ctx.setSendZuulResponse(true); //对请求进行路由
                        ctx.setResponseStatusCode(HttpStatus.OK.value());
                        ctx.set("isSuccess", true);
                        return null;
                        // return true;//表示放行
                    }

                }
            }
        }
        JsonResult jsonResult = JsonResult.build(409);// 未登录的状态码
        ctx.setSendZuulResponse(false); //对请求进行路由
        ctx.setResponseStatusCode(200);
        ctx.setResponseBody(JsonUtil.to(jsonResult));
        ctx.set("isSuccess", true);
        return null;


//        // 不需要token认证
//        ctx.setSendZuulResponse(true); //对请求进行路由
//        ctx.setResponseStatusCode(HttpStatus.OK.value());
//        ctx.set("isSuccess", true);
//        return null;
    }
}
