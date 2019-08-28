package com.willow.advancednc.model;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


@Component
public class HostHolder {
    private static ThreadLocal<User> users=new ThreadLocal<User>();
    public User get(){
        return users.get();
    }
    public void put(User user){
        users.set(user);
    }
    public void clear(){
        users.remove();
    }
}
