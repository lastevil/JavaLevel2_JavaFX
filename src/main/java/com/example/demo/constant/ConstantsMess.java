package com.example.demo.constant;

public enum ConstantsMess {
    ALL ("/all"), TO("/w"),END("/end"),LOGOUT("/logout"),
    AUTH("/auth"), AUTHOK("/authok "), AUTH_TIMEOUT("/timeout"),
    CLIENTS("/clients"), REG("/reg"),CHN_NICK("/chn_nick");

    private String attribute;

    public String getAttribute() {
        return attribute;
    }

    ConstantsMess(String attribute) {
        this.attribute = attribute;
    }
}
