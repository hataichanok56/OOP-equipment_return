// src/main/java/com/example/sportsloan/ui/AdminPanel.java
package com.example.sportsloan.ui;

import com.example.sportsloan.domain.Equipment;
import com.example.sportsloan.service.EquipmentService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.util.converter.IntegerStringConverter;

public class AdminPanel {
    private final EquipmentService service;
    private final ObservableList<Equipment> data = FXCollections.observableArrayList();

    public AdminPanel(EquipmentService service) {
        this.service = service;
        refresh();
    }

    private void refresh() { data.setAll(service.listAll()); }

    public BorderPane getView() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(16));

        TextField searchField = new TextField(); searchField.setPromptText("ค้นหาอุปกรณ์");
        Button searchBtn = new Button("ค้นหา");
        searchBtn.setOnAction(e -> data.setAll(service.searchItem(searchField.getText())));

        TextField nameField = new TextField(); nameField.setPromptText("เพิ่มรายการ");
        Spinner<Integer> totalSp = new Spinner<>(1, 9999, 10);
        Button addBtn = new Button("+ เพิ่ม");
        addBtn.setOnAction(e -> {
            if (!nameField.getText().isBlank()) {
                service.addItem(nameField.getText().trim(), totalSp.getValue());
                nameField.clear();
                totalSp.getValueFactory().setValue(10);
                refresh();
            }
        });

        HBox top = new HBox(10, searchField, searchBtn, new Separator(),
                nameField, totalSp, addBtn);
        top.setPadding(new Insets(0,0,10,0));

        TableView<Equipment> table = new TableView<>(data);
        table.setEditable(true);

        // ใช้ Long/Integer + asObject()
        TableColumn<Equipment, Long> colId = new TableColumn<>("Asset Code");
        colId.setCellValueFactory(c -> new SimpleLongProperty(c.getValue().getId()).asObject());

        TableColumn<Equipment, String> colName = new TableColumn<>("Equipment List");
        colName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setOnEditCommit(evt -> {
            Equipment row = evt.getRowValue();
            service.updateItem(row.getId(), evt.getNewValue(), null, null);
            refresh();
        });

        TableColumn<Equipment, Integer> colTotal = new TableColumn<>("Quantity");
        colTotal.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getTotal()).asObject());
        colTotal.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colTotal.setOnEditCommit(evt -> {
            Equipment row = evt.getRowValue();
            service.updateItem(row.getId(), null, evt.getNewValue(), null);
            refresh();
        });

        TableColumn<Equipment, Integer> colStock = new TableColumn<>("Available Quantity");
        colStock.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getStock()).asObject());
        colStock.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colStock.setOnEditCommit(evt -> {
            Equipment row = evt.getRowValue();
            service.updateItem(row.getId(), null, null, evt.getNewValue());
            refresh();
        });

        TableColumn<Equipment, Void> colDel = new TableColumn<>(" Delete Item");
        colDel.setCellFactory(col -> new TableCell<>() {
            final Button btn = new Button("🗑");
            { btn.setOnAction(e -> {
                Equipment row = getTableView().getItems().get(getIndex());
                service.deleteItem(row.getId());
                refresh();
            });}
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        table.getColumns().addAll(colId, colName, colTotal, colStock, colDel);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        root.setTop(top);
        root.setCenter(table);
        return root;
    }
}