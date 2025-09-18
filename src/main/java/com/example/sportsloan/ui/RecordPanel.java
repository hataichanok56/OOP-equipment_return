package com.example.sportsloan.ui;

import com.example.sportsloan.domain.BorrowRecord;
import com.example.sportsloan.service.EquipmentService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;   // ✅ import HBox
import javafx.scene.layout.VBox;  // ถ้ามีใช้ในอนาคต

public class RecordPanel {
    private final EquipmentService service = new EquipmentService();
    private final ObservableList<BorrowRecord> data =
            FXCollections.observableArrayList();

    public BorderPane getView() {
        BorderPane root = new BorderPane();

        TextField studentField = new TextField();
        studentField.setPromptText("ค้นหารหัสนิสิต");

        Button searchBtn = new Button("ค้นหา");
        searchBtn.setOnAction(e -> {
            data.setAll(service.byStudent(studentField.getText().trim()));
        });

        TableView<BorrowRecord> table = new TableView<>(data);

        TableColumn<BorrowRecord, String> colId = new TableColumn<>("รหัสนิสิต");
        colId.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getStudentId().value()));

        TableColumn<BorrowRecord, String> colEq = new TableColumn<>("รายการ");
        colEq.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getEquipmentName()));

        TableColumn<BorrowRecord, Integer> colQty = new TableColumn<>("จำนวน");
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));

        TableColumn<BorrowRecord, String> colBorrowed = new TableColumn<>("วันที่ยืม");
        colBorrowed.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getBorrowedAt().toString()));

        TableColumn<BorrowRecord, String> colReturned = new TableColumn<>("วันที่คืน");
        colReturned.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getReturnedAt() == null
                                ? "-" : c.getValue().getReturnedAt().toString()));

        table.getColumns().addAll(colId, colEq, colQty, colBorrowed, colReturned);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        root.setTop(new HBox(10, studentField, searchBtn)); // ✅ HBox แก้แล้ว
        root.setCenter(table);

        return root;
    }
}