// src/main/java/com/example/sportsloan/ui/MainApp.java
package com.example.sportsloan.ui;

import com.example.sportsloan.service.EquipmentService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainApp extends Application {
    private final EquipmentService service = new EquipmentService();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Sports Loan App");

        TabPane tabs = new TabPane();
        tabs.getTabs().addAll(
            new Tab("จัดการอุปกรณ์กีฬา", new AdminPanel(service).getView()),
            new Tab("บันทึกการยืม-คืน", new RecordPanel(service).getView()),
            new Tab("ยืมอุปกรณ์", new BorrowPanel(service).getView()),
            new Tab("คืนอุปกรณ์", new ReturnPanel(service).getView())
        );
        tabs.getTabs().forEach(t -> t.setClosable(false));

        stage.setScene(new Scene(tabs, 1200, 700));
        stage.show();
    }

    public static void main(String[] args) { launch(); }
}