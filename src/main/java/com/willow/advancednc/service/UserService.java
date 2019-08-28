package com.willow.advancednc.service;

import com.willow.advancednc.dao.LoginTicketDAO;
import com.willow.advancednc.dao.UserDAO;
import com.willow.advancednc.model.LoginTicket;
import com.willow.advancednc.model.User;
import com.willow.advancednc.utils.ZhiHuUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private static final Logger logger=LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserDAO userDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    public User selectByName(String name){
        return userDAO.selectByName(name);
    }

    public User getUser(int id){
        return userDAO.selectById(id);
    }

    public Map<String,Object> register(String name,String password){
        Map<String,Object> map=new HashMap<String,Object>();
        if(StringUtils.isBlank(name)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(name);
        if(user!=null){
            map.put("msg","用户已被注册");
            return map;
        }

        //添加user
        user=new User();
        user.setName(name);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setPassword(ZhiHuUtil.MD5(password+user.getSalt()));
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);

        userDAO.addUser(user);

        //登陆
        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;

    }

    public Map<String,Object> login(String name,String password){
        Map<String,Object> map=new HashMap<String,Object>();
        if(StringUtils.isBlank(name)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        User user=userDAO.selectByName(name);

        if(user==null){
            map.put("msg","用户未被注册");
            return map;
        }

        if(!ZhiHuUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msg","密码错误");
            return map;
        }


        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);
        map.put("userId",user.getId());
        return map;

    }

    private String addLoginTicket(int userId){
        LoginTicket loginTicket=new LoginTicket();
        loginTicket.setUserId(userId);
        Date date=new Date();
        date.setTime(date.getTime()+1000*3600*24);
        loginTicket.setExpired(date);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));//why
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }
}
