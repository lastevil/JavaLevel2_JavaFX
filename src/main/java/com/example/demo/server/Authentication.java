package com.example.demo.server;

public interface Authentication {
    String getNickByLoginPass(String login, String password);
    boolean registration(String nickname, String login, String password);
}
