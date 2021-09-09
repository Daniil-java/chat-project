package ru.geekbrains.june.chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private String username;
    private DataInputStream in;
    private DataOutputStream out;

    public String getUsername() {
        return username;
    }

    public ClientHandler(Server server, Socket socket) {
        try {
            //Запоминаем сервер
            this.server = server;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            //Поток общения с клиентом
            new Thread(() -> logic()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) { //Отправка сообщения
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logic() { //Отключение пользователя от сервера
        try {
            while (!consumeAuthorizeMessage(in.readUTF()));
            while (consumeRegularMessage(in.readUTF()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Клиент " + username + " отключился");
            server.unsubscribe(this);
            closeConnection();
        }
    }

    private boolean consumeRegularMessage(String inputMessage) {
        if (inputMessage.startsWith("/")) {
            if (inputMessage.equals("/exit")) {
                sendMessage("/exit");
                return false;
            }
            if (inputMessage.startsWith("/w ")) {
            }
            return true;
        }
        server.broadcastMessage(username + ": " + inputMessage);
        return true;
    }

    private boolean consumeAuthorizeMessage(String message) throws IOException { //Узнаём имя пользователя
        while (true) {                                                      //Сюда отправляется запрос с данными из tryToAuth
            String str = in.readUTF();
            if (str.startsWith("/auth")) {
                String[] parts = str.split("\\s");
                String nick = server.getAuthService().getNickByLoginAndPass(parts[1], parts[2]);
                if (nick != null) {                                         //Проверяет наличие пользователя в базе данных
                    if (!server.isNickBusy(nick)) {
                        sendMessage("/authok " + nick);
                        username = nick;
                        server.broadcastMessage(username + " зашел в чат");
                        server.subscribe(this);
                        return true;
                    } else {
                        sendMessage("Учетная запись уже используется");
                    }
                } else {
                    sendMessage("Неверные логин/пароль");
                }
            }
        }


    }


    private void closeConnection() { //Закрываем соединение с сервером
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
