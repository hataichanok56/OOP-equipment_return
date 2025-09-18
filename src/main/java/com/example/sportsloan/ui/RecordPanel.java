// src/main/java/com/example/sportsloan/ui/RecordPanel.java
package com.example.sportsloan.ui;

import com.example.sportsloan.domain.BorrowRecord;
import com.example.sportsloan.service.EquipmentService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBox; 

public class RecordPanel {
    private final EquipmentService service;
    private final ObservableList<BorrowRecord> data = FXCollections.observableArrayList();

    public RecordPanel(EquipmentService service){ this.service = service; }

    public BorderPane getView() {
        BorderPane root = new BorderPane();

        TextField studentField = new TextField();
        studentField.setPromptText("ค้นหารหัสนิสิต");

        Button searchBtn = new Button("ค้นหา");
        searchBtn.setOnAction(e -> data.setAll(service.byStudent(studentField.getText().trim())));

        TableView<BorrowRecord> table = new TableView<>(data);

        TableColumn<BorrowRecord, String> colId = new TableColumn<>("student ID");
        colId.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStudentId().value()));

        TableColumn<BorrowRecord, String> colEq = new TableColumn<>("Item");
        colEq.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEquipmentName()));

        TableColumn<BorrowRecord, Integer> colQty = new TableColumn<>("Quantity");
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));

        TableColumn<BorrowRecord, String> colBorrowed = new TableColumn<>("Borrow Date");
        colBorrowed.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getBorrowedAt().toString()));

        TableColumn<BorrowRecord, String> colReturned = new TableColumn<>("Return Date");
        colReturned.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getReturnedAt()==null ? "-" : c.getValue().getReturnedAt().toString()));

        table.getColumns().addAll(colId, colEq, colQty, colBorrowed, colReturned);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        root.setTop(new HBox(10, studentField, searchBtn));
        root.setCenter(table);
        return root;
    }
}