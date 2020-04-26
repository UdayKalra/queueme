package com.bet.queueme;

public class person {
    public person(Integer s, String n, String ss){
        position = s;
        name = n;
        store = ss;
    }
    public person(Integer s, String n){
        position = s;
        name = n;
    }
    private String store;
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
    public void setStore(String s) {
        this.store = s;
    }
    public String getStore() {
        return store;
    }
}
