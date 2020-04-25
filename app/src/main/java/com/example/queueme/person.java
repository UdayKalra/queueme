package com.example.queueme;

public class person {
    public person(Integer s, String n){
        position = s;
        name = n;
    }
    private Integer position;
    private String name;
    public String getName(){
        return name;
    }
    public Integer getPosition(){
        return position;
    }
    public void setPosition(Integer p){
        position = p;
    }
    public void setName(String s){
        name = s;
    }
}
