package com.example.demo.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;

public class BaseAuthService implements Authentication {
    private static final Logger LOGGER = LogManager.getLogger(BaseAuthService.class);
private BaseSQLConnect DB;
    public BaseAuthService(){
        try {
            DB = new BaseSQLConnect();
            DB.setConnection();
            DB.createTableOfUsers();
        } catch (SQLException e) {
            LOGGER.error("SQL ERROR");
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
            LOGGER.error("SQL ERROR");
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
            if (logins.size()==0){
                DB.userAdd(nickname,login,password);
                LOGGER.info(login + " registered");
                return true;
            }
            else {
                for (String a : logins) {
                    if (a.equals(login)) {
                        return false;
                    }
                    else {
                        DB.userAdd(nickname, login, password);
                        LOGGER.info(login + " registered");
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            LOGGER.error("SQL ERROR");
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
            LOGGER.info(oldNick + " change nickname to "+newNick);
        } catch (SQLException throwables) {
            LOGGER.error("SQL ERROR");
            throwables.printStackTrace();
        }finally {
            DB.disconnect();
        }

    }
}
