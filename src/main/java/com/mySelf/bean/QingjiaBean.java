package com.mySelf.bean;

import java.io.Serializable;

public class QingjiaBean implements Serializable {
    private double money;
    private double day;
    private String name;
    private String reason;
    private String uuid;

    @Override
    public String toString() {
        return "QingjiaBean{" +
                "money=" + money +
                ", day=" + day +
                ", name='" + name + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }


    public QingjiaBean(double money, double day, String name, String reason) {
        this.money = money;
        this.day = day;
        this.name = name;
        this.reason = reason;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getDay() {
        return day;
    }

    public void setDay(double day) {
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
