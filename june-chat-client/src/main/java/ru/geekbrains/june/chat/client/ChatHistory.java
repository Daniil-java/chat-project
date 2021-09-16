package ru.geekbrains.june.chat.client;

import javafx.fxml.FXML;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ChatHistory {

    private String directory;

    @FXML
    TextArea chatArea;



    public ChatHistory(String name) {       //Использьует имя клиента, для наименования директории истории
        directory = String.format("history_%s.txt", name);
    }

    public void setDirectory(String directory) {
        this.directory = String.format("history_%s.txt", directory);
    }

    public void saveHistory(javafx.scene.control.TextArea chatArea) throws IOException {
        try {
            File history = new File(directory);
            System.out.println(directory);
            if (!history.exists()) {
                System.out.println("File is not exist. Creating.");
                history.createNewFile();
            }
            PrintWriter fileWriter = new PrintWriter(new FileWriter(history, false));

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(chatArea.getText());
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadHistory(javafx.scene.control.TextArea chatArea) throws IOException {
        int historysrtings = 100;
        File history = new File(directory);
        System.out.println(directory);
        if (!history.exists()) {
            System.out.println("File is not exist. Creating.");
            history.createNewFile();
        }
        List<String> historyList = new ArrayList<>();
        FileInputStream in = new FileInputStream(history);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        String temp;
        while ((temp = bufferedReader.readLine()) != null) {
            historyList.add(temp);
        }
        if (historyList.size() > historysrtings) {
            for (int i = historyList.size() - historysrtings; i <= (historyList.size() - 1); i++) {
                chatArea.appendText(historyList.get(i) + "\n");
            }
        } else {
            for (int i = 0; i < historyList.size(); i++) {
                chatArea.appendText(historyList.get(i) + "\n");
            }
        }
    }
}
