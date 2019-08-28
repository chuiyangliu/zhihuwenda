package com.willow.advancednc.model;

import java.util.HashMap;
import java.util.Map;

public class ViewObject {
    private Map<String,Object> map=new HashMap<String,Object>();
    public void set(String s,Object o){
        map.put(s,o);
    }
    public Object get(String s){
        return map.get(s);
    }
}
