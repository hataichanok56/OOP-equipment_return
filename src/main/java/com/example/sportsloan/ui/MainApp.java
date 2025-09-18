package com.example.sportsloan.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Sports Loan App");

        TabPane tabs = new TabPane();
        tabs.getTabs().addAll(
            new Tab("จัดการอุปกรณ์กีฬา", new AdminPanel().getView()),
            new Tab("บันทึกการยืม-คืน", new RecordPanel().getView()),
            new Tab("ยืมอุปกรณ์", new BorrowPanel().getView()),
            new Tab("คืนอุปกรณ์", new ReturnPanel().getView())
        );

        tabs.getTabs().forEach(t -> t.setClosable(false));

        Scene scene = new Scene(tabs, 1200, 700);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}