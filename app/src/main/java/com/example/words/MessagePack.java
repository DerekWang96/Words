package com.example.words;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.util.Vector;

import db.*;

public class MessagePack implements Serializable {
    private static final long serialVersionUID = 1L;
    public String orderType;
    public User user;
    public Boolean isChanged = false;
    public Vector<String> strPack  = new Vector<String>();
    public Vector<Boolean> booleanResult = new Vector<Boolean>();
    public List<Wordbook> wordbookList = new ArrayList<Wordbook>();
    public String taskType;
    public MessagePack(){}

    public MessagePack(String ot) {
        this.orderType = ot;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Boolean getChanged() {
        return isChanged;
    }
    public void setChanged(Boolean changed) {
        isChanged = changed;
    }
    public String getOrderType() {
        return orderType;
    }
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
    public void booleanAdd(Boolean b){
    	this.booleanResult.add(b);
    }
    public void setListwordbook(List<Wordbook> wb){
    	this.wordbookList = wb; 
    }
    public List<Wordbook> getListwordbook(){
    	return wordbookList;
    }
    public Vector<String> getStrPack() {
        return strPack;
    }
    public void setStrPack(Vector<String> strPack) {
        this.strPack = strPack;
    }
    public void addString(String str){
        this.strPack.add(str);
    }
    public Vector<Boolean> getBooleanResult() {
        return booleanResult;
    }
    public void setBooleanResult(Vector<Boolean> booleanResult) {
        this.booleanResult = booleanResult;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public List<Wordbook> getWordbookList() {
        return wordbookList;
    }

    public void setWordbookList(List<Wordbook> wordbookList) {
        this.wordbookList = wordbookList;
    }
}