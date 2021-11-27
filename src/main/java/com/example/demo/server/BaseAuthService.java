package com.example.demo.server;

import java.sql.SQLException;
import java.util.ArrayList;

public class BaseAuthService implements Authentication {
private BaseSQLConnect DB;
    public BaseAuthService(){
        try {
            DB = new BaseSQLConnect();
            DB.setConnection();
            DB.createTableOfUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DB.disconnect();
        }
    }
    @Override
    public String getNickByLoginPass(String login, String password) {
        try {
            DB.setConnection();
            return DB.getNicknameByUserdata(login,password);
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DB.disconnect();
        }
       return null;
    }
    @Override
    public boolean registration(String nickname, String login, String password) {
        try {
            DB.setConnection();
            ArrayList<String> logins = DB.getLogins();
            for (String a: logins) {
                if(a.equals(login)){
                    return false;
                }
                else {
                    DB.userAdd(nickname,login,password);
                    return true;
                }
            }
            if (logins.size()==0){
                DB.userAdd(nickname,login,password);
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DB.disconnect();
        }
        return false;
    }

    public void nickChange(String oldNick,String newNick){
        try {
            DB.setConnection();
            DB.nicknameChange(oldNick,newNick);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DB.disconnect();
        }

    }
}
