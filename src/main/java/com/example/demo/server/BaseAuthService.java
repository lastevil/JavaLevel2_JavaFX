package com.example.demo.server;

import java.util.ArrayList;
import java.util.List;

public class BaseAuthService implements Authentication {
    private final List<Userdata> users;

    public BaseAuthService(){
        users = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            users.add(new Userdata("login"+i,"pass"+i,"nick"+i));
        }
    }

    @Override
    public String getNickByLoginPass(String login, String password) {
        for (Userdata user: users) {
            if(user.login.equals(login) && user.password.equals(password)){
                return user.nickname;
            }
        }
        return null;
    }

    private static class Userdata {
        private final String login;
        private final String password;
        private final String nickname;

        private Userdata(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }
    }

}
