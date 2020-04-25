package com.example.queueme;

public class person {
    public person(Integer s, String n){
        position = s;
        name = n;
    }
    Integer position;
    String name;
    public String getName(){
        return name;
    }
    public Integer getPosition(){
        return position;
    }
}
