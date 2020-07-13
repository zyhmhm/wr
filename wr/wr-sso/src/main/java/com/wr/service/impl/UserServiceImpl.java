package com.wr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wr.dao.RoleDao;
import com.wr.dao.UserDao;
import com.wr.exe.ServiceException;
import com.wr.pojo.Role;
import com.wr.pojo.User;
import com.wr.service.UserService;
import com.wr.util.JsonResult;
import com.wr.util.JsonUtil;
import com.wr.util.UserThreadLocalUtil;
import com.wr.vo.JurisdictionVo;
import com.wr.vo.UserRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private JedisCluster jedisCluster;
    @Autowired
    private RoleDao roleDao;
    /**
     * 1.返回值数据  返回加密后的秘钥
     * 2.
     * 3.
     * @param user
     * @param ip
     * @return
     */
    @Override
    public JsonResult findUserByUP(User user, String ip, HttpServletRequest request, HttpServletResponse response) {

        //1.将密码进行加密
        String mdsPass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(mdsPass);

        //2.根据用户信息查询数据库
        //根据对象中不为null的属性当做where条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>(user);
        User userDb = userDao.selectOne(queryWrapper);
        if(userDb == null){

            return null;
        }

        //如果到这里说明用户名和密码正确
        //3.动态生成ticket
        String ticketUUID = UUID.randomUUID().toString();
        String ticket = DigestUtils.md5DigestAsHex(ticketUUID.getBytes());
        //4.脱敏处理，并将用户信息存到redis中去
        userDb.setPassword("123456");
        //6.防止用户重复登录。需要将之前的登录信息先删除
        if(jedisCluster.exists("JT_USERNAME_" + userDb.getUserName())){
            String oldTicket = jedisCluster.get("JT_USERNAME_" + userDb.getUserName());
            jedisCluster.del(oldTicket);
        }

        //5.将数据保存到redis中去
        jedisCluster.hset(ticket, "JT_USER", JsonUtil.to(userDb));
        jedisCluster.hset(ticket, "JT_USER_IP", ip);
        jedisCluster.expire(ticket, 7*24*3600);
        //实现用户名与ticket绑定
        jedisCluster.setex("JT_USERNAME_" + userDb.getUserName(),7*24*3600,ticket);

        if(StringUtils.isEmpty(ticket)){
            //表示用户名和密码错误
            return JsonResult.err("用户名或密码错误");
        }

        //数据保存到cookie中
        Cookie ticketCookie = new Cookie("JT_TICKET",ticket);
        ticketCookie.setMaxAge(7*24*3600);
//        ticketCookie.setHttpOnly(false);
        ticketCookie.setPath("/");      //表示根目录有效
        //由于单点登录的业务需求，我们需要将cookie设置为共享数据
        ticketCookie.setDomain("huisen.top");
        response.addCookie(ticketCookie);

        // 返回用户角色Id，便于查询权限
        JurisdictionVo jurisdictionVo = new JurisdictionVo();
        jurisdictionVo.setToken(userDb.getRoleId().toString());
        return JsonResult.ok(jurisdictionVo);
    }

    @Override
    public void logout(String ticket) {
        jedisCluster.del(ticket);

    }

    /**
     * 1.为了避免后台数据库报错，暂时使用手机代替Email
     * 2.使用MD5加密算法,32位16进制字符串=2^128
     * 3.添加设定操作时间
     * 注意：
     *      1.注册时采用加密算法
     *      2.用户登录时，加密算法必须相同
     * @param user
     */
    @Override
    public JsonResult insertUser(User user) {
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        try {
            userDao.insert(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("s数据库异常插入失败");
        }
        return JsonResult.ok();
    }

    @Override
    public JsonResult checkUser(String userName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("user_name", userName);
        User userDB = null;
        try {
            userDB = userDao.selectOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("数据库异常");
        }
        if(userDB != null)
            return JsonResult.build(203, "该用户名已经被使用");
        return JsonResult.ok();
    }

    /**
     * 用户权限等信息
     * @param token
     * @return
     */
    @Override
    public JsonResult getInfo(Long token) {
        Long userId = UserThreadLocalUtil.getUser().getId();
        Role role = roleDao.selectById(token);
        List<String> roles = new ArrayList<>();
        roles.add(role.getRoleName());
        UserRoleVo userRoleVo = new UserRoleVo().setUserId(userId).setRoles(roles).setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return JsonResult.ok(userRoleVo);
    }

    @Override
    public JsonResult updateUser(User user) {
        userDao.updateById(user);
        return JsonResult.ok();
    }
}
