package ru.geekbrains.june.chat.server;

import java.sql.*;

public class AuthService { //База данных

    private static Connection connection;
    private static Statement stmt;

    static void connection() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:chat.db");
            stmt = connection.createStatement();
            createTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setNewUser(String login, String password, String nick) throws SQLException { //Добавляет в БД нового пользователя
        connection();
//        String sql = String.format("INSERT INTO users (login, password, nick) VALUES ('%s', '%s', '%s')", login, password, nick);
//        if ()
        PreparedStatement ps = connection.prepareStatement("INSERT INTO users (login, password, nick) VALUES (?, ?, ?)");
        ps.setString(1, login);
        ps.setString(2, password);
        ps.setString(3, nick);
        ps.execute();

//        try {
//            boolean rs = stmt.execute(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }


    static String getNickByLoginAndPass(String login, String password)  {    //Данный метод, нужен для того, чтобы в чате отображался никнейм пользователя, а не его логин
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("SELECT nick FROM users where login = ? and password = ?");
            ps.setString(1, login);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

//        String sql = String.format("SELECT nick FROM users where login = '%s' and password = '%s'", login, password);
//        try {
//            ResultSet rs = stmt.executeQuery(sql);
//
//            if (rs.next()) {
//                String str = rs.getString(1);
//                return rs.getString(1);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return null;
    }

        private static void createTable() throws SQLException {
        stmt.executeUpdate("create table if not exists users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "login TEXT UNIQUE," +
                "password TEXT," +
                "nick TEXT UNIQUE" +
                ")");
    }

}


