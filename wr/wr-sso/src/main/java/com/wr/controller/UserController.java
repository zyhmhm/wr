package com.wr.controller;

import com.wr.pojo.User;
import com.wr.service.UserService;
import com.wr.util.CookieUtil;
import com.wr.util.IPUtil;
import com.wr.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    /**
     * 实现单点登录
     * 1.动态获取ip
     * 2.将ticket信息发送到Cookie中
     *
     * cookie:
     *      ticketCookie.setMaxAge(大于0);存活时间
     *      ticketCookie.setMaxAge(0); 表示立即删除
     *      ticketCookie.setMaxAge(-1); 关闭回话时，cookie删除
     *      关于path的说明
     *      url:www.jd.com/login.html
     *      ticketCookie.setPath("/");url可以访问该cookie
     *      ticketCookie.setPath("/abc"); url无法访问该cookie
     * @param user
     * @return
     */

    @PostMapping("/doLogin")
    @ResponseBody
    public JsonResult login(User user, HttpServletRequest request, HttpServletResponse response){
        //1.动态获取用户ip
        String ip = IPUtil.getIpAddr(request);
        return userService.findUserByUP(user,ip,request,response);
        //2.获取校验结果
//        JsonResult result = userService.findUserByUP(user,ip,request,response);

//        if(StringUtils.isEmpty(ticket)){
//            //表示用户名和密码错误
//            return JsonResult.err("用户名或密码错误");
//        }
//
//        //3.数据保存到cookie中
//        Cookie ticketCookie = new Cookie("JT_TICKET",ticket);
//        ticketCookie.setMaxAge(7*24*3600);
////        ticketCookie.setHttpOnly(false);
//        ticketCookie.setPath("/");      //表示根目录有效
//        //由于单点登录的业务需求，我们需要将cookie设置为共享数据
//        ticketCookie.setDomain("huisen.top");
//        response.addCookie(ticketCookie);
//        JurisdictionVo jurisdictionVo = new JurisdictionVo();
//        if("admin".equals(user.getUserName()))
//            jurisdictionVo.setToken("admin-token");
//        else
//            jurisdictionVo.setToken("editor-token");
//        return JsonResult.ok(jurisdictionVo);
    }

    /**
     * 实现用户登出操作
     * 业务：
     *      1.删除redis中的数据操作    ticket
     *      2.删除cookie "JT_TICKET"
     *
     * 实现思路：
     *  先获取"JT_TICKET"中的值
     *  删除redis数据
     *  删除cookie记录
     * @param request
     * @param response
     * @return
     */

    @RequestMapping("/logout")
    @ResponseBody
    public JsonResult logout(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        String ticket = "";
        Cookie cookie = CookieUtil.getCookie(request, "JT_TICKET");

        if(cookie==null){
            //重定向到系统首页
            return JsonResult.ok();
        }

        //2.删除redis中的数据
        ticket = cookie.getValue();
        userService.logout(ticket);

        //3.删除浏览器cookie  规则：定义一个与原来cookie名称配置一指的cookie之后进行删除
        //value不能为null
        CookieUtil.removeCookie(response, "JT_TICKET",
                "huisen.top", "/");

        //重定向到系统首页
        return JsonResult.ok();
    }
    /**
     * 实现用户注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    @ResponseBody
    public JsonResult doRegister(User user){

        return userService.insertUser(user);
    }
    /**
     * 获取用户权限（角色信息）
     */
    @GetMapping("info")
    @ResponseBody
    public JsonResult info(Long token){
        return userService.getInfo(token);
    }

    /**
     * 校验用户名是否用过
     */
    @GetMapping("/check")
    @ResponseBody
    public JsonResult checkUser(String userName){
        return userService.checkUser(userName);
    }
    /**
     * 更新用户信息
     */
    @PostMapping("updateUser")
    public JsonResult updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }
}
