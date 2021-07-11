package com.example.smartpad.objects;

import java.util.LinkedHashMap;

public class User {

    private String name;
    private int age;
    private int theirPeriodLength, padNumber;
    private int theirLengthBetween;
    private String uid;
    private LinkedHashMap<String, Period> periods;

    public User(String name, String uid, int age, int theirPeriodLength, int theirLengthBetween, LinkedHashMap<String, Period> periods, int padNumber){
        this.name=name;
        this.uid=uid;
        this.age=age;
        this.theirPeriodLength=theirPeriodLength;
        this.theirLengthBetween=theirLengthBetween;
        this.periods=periods;
        this.padNumber=padNumber;
    }

    public int getPadNumber() {
        return padNumber;
    }

    public void setPadNumber(int padNumber) {
        this.padNumber = padNumber;
    }

    public LinkedHashMap<String, Period> getPeriods() {
        return periods;
    }

    public void setPeriods(LinkedHashMap<String, Period> periods) {
        this.periods = periods;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getTheirPeriodLength() {
        return theirPeriodLength;
    }

    public int getTheirLengthBetween() {
        return theirLengthBetween;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setTheirPeriodLength(int theirPeriodLength) {
        this.theirPeriodLength = theirPeriodLength;
    }

    public void setTheirLengthBetween(int theirLengthBetween) {
        this.theirLengthBetween = theirLengthBetween;
    }
}
