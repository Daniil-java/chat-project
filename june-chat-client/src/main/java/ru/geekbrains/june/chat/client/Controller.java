package ru.geekbrains.june.chat.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import ru.geekbrains.june.chat.server.AuthService;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller {
    @FXML
    TextArea chatArea;

    @FXML
    TextField messageField, loginField, nicknameField, regNick, regLogin;

    @FXML
    PasswordField regPassword, passwordField;

    @FXML
    HBox authPanel, msgPanel, nicknamePanel, registrationPanel;

    @FXML
    ListView<String> clientsListView;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;


    private String name;
//    private String directory;
    ChatHistory chatHistory = new ChatHistory(name);


    public void setAuthorized(boolean authorized) { //При значении "true" пропадает окно авторизации, появляется чат
        msgPanel.setVisible(authorized);
        msgPanel.setManaged(authorized);
        authPanel.setVisible(!authorized);
        authPanel.setManaged(!authorized);
        registrationPanel.setVisible(!authorized);
        registrationPanel.setManaged(!authorized);
        nicknamePanel.setVisible(authorized);
        nicknamePanel.setManaged(authorized);
        clientsListView.setVisible(authorized);
        clientsListView.setManaged(authorized);
    }

    public void sendMessage() {
        try {
            out.writeUTF(messageField.getText());
            messageField.clear();
            messageField.setPromptText("Вы используете имя: " + name);
            messageField.requestFocus();
        } catch (IOException e) {
            showError("Невозможно отправить сообщение на сервер");
        }
    }

    public void sendCloseRequest() {
        try {
            if (out != null) {
                out.writeUTF("/exit");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tryToAuth() throws InterruptedException { //Попытка авторизации. Отправляет запрос на ClientHandler. Тот сравнивает с БД.
        String login = loginField.getText();
        String password = passwordField.getText();
        try {
            out.writeUTF("/auth " + login + " " + password);
            if (login != null) {
                this.name = loginField.getText();
            }
//            directory = String.format("history_%s.txt", name);
            chatHistory.setDirectory(login);
            System.out.println(name);
            loginField.clear();
            passwordField.clear();
        } catch (IOException e) {
            showError("Невозможно отправить запрос авторизации на сервер");
        }

    }

    public void connect() {
        if (socket != null && !socket.isClosed()) {
            return;
        }
        try {
            socket = new Socket("localhost", 8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
//            ExecutorService service = Executors.newFixedThreadPool(10);
//            service.execute(() -> logic());
//            service.shutdown();
            new Thread(() -> logic()).start();
        } catch (IOException e) {
            showError("Невозможно подключиться к серверу");
        }
    }

    public Controller() {
        connect();
    }

    private void logic() {      //Отвечает за то, чтобы передать в метод setAuthorized значении true, дав доступ к чату
        try {
            while (true) {
                String inputMessage = in.readUTF();
                if (inputMessage.equals("/exit")) { //Команда для выхода из чата
                    closeConnection();
                }
                if (inputMessage.startsWith("/authok ")) {  //Сервер вернёт сообщение, которе начинается с "/authok ", если пользователь существует в БД
                    setAuthorized(true);
//                    loadHistory();
                    chatHistory.loadHistory(chatArea);

                    break;
                }
                chatArea.appendText(inputMessage + "\n");
            }
            while (true) {
                String inputMessage = in.readUTF();
                if (inputMessage.startsWith("/")) {
                    if (inputMessage.equals("/exit")) {
                        break;
                    }
                    // /clients_list bob john
                    if (inputMessage.startsWith("/clients_list ")) {
                        Platform.runLater(() -> {
                            String[] tokens = inputMessage.split("\\s+");
                            clientsListView.getItems().clear();
                            for (int i = 1; i < tokens.length; i++) {
                                clientsListView.getItems().add(tokens[i]);
                            }
                        });
                    }
                    continue;
                }
                chatArea.appendText(inputMessage + "\n");
//                saveHistory();
                chatHistory.saveHistory(chatArea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void closeConnection() {
        setAuthorized(false);
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

    public void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message, ButtonType.OK).showAndWait();
    }

    public void registration() throws SQLException {    //Отвечает за регистрацию. Отправляет запрос напрямую в БД
        if (socket == null || socket.isClosed()) {
            connect();
        }
        AuthService.setNewUser(regLogin.getText(), regPassword.getText(), regNick.getText());
        System.out.println("Вы зарегистрировались.");
        regLogin.clear();
        regPassword.clear();
        regNick.clear();
    }


    public void clientsListDoubleClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            String selectedUser = clientsListView.getSelectionModel().getSelectedItem();
            messageField.setText("/w " + selectedUser + " ");
            messageField.requestFocus();
            messageField.selectEnd();
        }
    }


//    private void saveHistory() throws IOException {
//        try {
//            File history = new File(directory);
//            if (!history.exists()) {
//                System.out.println("File is not exist. Creating.");
//                history.createNewFile();
//            }
//            PrintWriter fileWriter = new PrintWriter(new FileWriter(history, false));
//
//            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
//            bufferedWriter.write(chatArea.getText());
//            bufferedWriter.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void loadHistory() throws IOException {
//        int historysrtings = 100;
//        File history = new File(directory);
//        if (!history.exists()) {
//            System.out.println("File is not exist. Creating.");
//            history.createNewFile();
//        }
//        List<String> historyList = new ArrayList<>();
//        FileInputStream in = new FileInputStream(history);
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
//
//        String temp;
//        while ((temp = bufferedReader.readLine()) != null) {
//            historyList.add(temp);
//        }
//
//        if (historyList.size() > historysrtings) {
//            for (int i = historyList.size() - historysrtings; i <= (historyList.size() - 1); i++) {
//                chatArea.appendText(historyList.get(i) + "\n");
//            }
//        } else {
//            for (int i = 0; i < historyList.size(); i++) {
//                chatArea.appendText(historyList.get(i) + "\n");
//            }
//        }
//    }
}


