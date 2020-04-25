package com.example.queueme;

public class person {
    public person(Integer s, String n, String id){
        position = s;
        name = n;
        this.id = id;
    }
    public person(Integer s, String n){
        position = s;
        name = n;
    }
    private String id;
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

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
