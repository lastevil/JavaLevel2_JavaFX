package com.example.demo.server;
import java.sql.*;
import java.util.ArrayList;


public class BaseSQLConnect {
    private static Connection connection;
    private static Statement statement;

    public BaseSQLConnect(){
    }

    public static void setConnection() throws SQLException{
         connection = DriverManager.getConnection("jdbc:sqlite:javadb.db");
         statement = connection.createStatement();
    }
    public static void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void createTableOfUsers() throws SQLException{
        statement.executeUpdate("" +
                "CREATE TABLE IF not exists USERS"+
                "(ID integer not null primary key autoincrement,"+
                "nickname text not null,"+
                "login text not null unique,"+
                "password text not null,"+
                "auth boolean not null default false"+
                ");"
        );
    }
    public static void userAdd(String nickname, String login, String password){
        try(  final PreparedStatement ps = connection
                .prepareStatement("INSERT INTO USERS "+
                        "(login,nickname,password) VALUES (?,?,?);"
                )){
        ps.setString(1,login);
        ps.setString(2,nickname);
        ps.setString(3,password);
        ps.executeUpdate();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static void nicknameChange(String oldNick, String newNick){
        try(  final PreparedStatement ps = connection
                .prepareStatement("UPDATE USERS SET nickname = ? where nickname = ?;"
                )){
            ps.setString(1,oldNick);
            ps.setString(2,newNick);
            ps.executeUpdate();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static ArrayList<String> getLogins() throws SQLException{
        final ArrayList<String> logins = new ArrayList<>();
        ResultSet rs = statement.executeQuery("SELECT login FROM USERS;");
        while (rs.next()){
            logins.add(rs.getString(1));
        }
        return logins;
    }
    public static String getNicknameByUserdata(String login, String password) throws SQLException{
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT nickname FROM USERS"+
                        " WHERE login = ? and password = ?;"
        )){
            ps.setString(1,login);
            ps.setString(2,password);
            ResultSet rs = ps.executeQuery();
            String nickname = null;
            while (rs.next()){
                nickname = rs.getString(1);
            }
            return nickname;
        }
    }

}
