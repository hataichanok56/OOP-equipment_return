package com.example.sportsloan.ui;

import com.example.sportsloan.domain.Equipment;
import com.example.sportsloan.service.EquipmentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

public class AdminPanel {
    private final EquipmentService service = new EquipmentService();
    private final ObservableList<Equipment> data =
            FXCollections.observableArrayList(service.listAll());

    public BorderPane getView() {
        BorderPane root = new BorderPane();

        TextField nameField = new TextField();
        nameField.setPromptText("ชื่ออุปกรณ์");

        TextField totalField = new TextField();
        totalField.setPromptText("จำนวนทั้งหมด");

        Button addBtn = new Button("+ เพิ่ม");
        addBtn.setOnAction(e -> {
            try {
                String name = nameField.getText().trim();
                int total = Integer.parseInt(totalField.getText().trim());
                if (!name.isBlank() && total > 0) {
                    service.addItem(name, total);
                    data.setAll(service.listAll());
                    nameField.clear();
                    totalField.clear();
                }
            } catch (Exception ex) {
                showError("ใส่ข้อมูลไม่ถูกต้อง");
            }
        });

        HBox topBar = new HBox(10, nameField, totalField, addBtn);

        TableView<Equipment> table = new TableView<>(data);

        TableColumn<Equipment, Long> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Equipment, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Equipment, Integer> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        TableColumn<Equipment, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn<Equipment, Void> colActions = new TableColumn<>("Manage");
        colActions.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("แก้ไข");
            private final Button delBtn = new Button("ลบ");
            {
                editBtn.setOnAction(e -> {
                    Equipment eq = getTableView().getItems().get(getIndex());
                    TextInputDialog dialog = new TextInputDialog(eq.getTotal() + "");
                    dialog.setHeaderText("แก้ไขจำนวนทั้งหมด");
                    dialog.showAndWait().ifPresent(val -> {
                        try {
                            int newTotal = Integer.parseInt(val);
                            service.update(eq.getId(), null, newTotal, null);
                            data.setAll(service.listAll());
                        } catch (Exception ex) {
                            showError("ใส่จำนวนไม่ถูกต้อง");
                        }
                    });
                });
                delBtn.setOnAction(e -> {
                    Equipment eq = getTableView().getItems().get(getIndex());
                    service.deleteItem(eq.getId());
                    data.setAll(service.listAll());
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(new HBox(5, editBtn, delBtn));
            }
        });

        table.getColumns().addAll(colId, colName, colTotal, colStock, colActions);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        root.setTop(topBar);
        root.setCenter(table);
        return root;
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }
}