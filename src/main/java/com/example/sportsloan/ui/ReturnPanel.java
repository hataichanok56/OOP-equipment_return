package com.example.sportsloan.ui;

import com.example.sportsloan.domain.StudentId;
import com.example.sportsloan.domain.Equipment;
import com.example.sportsloan.service.EquipmentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ReturnPanel {
    private final EquipmentService service = new EquipmentService();
    private final ObservableList<Equipment> data =
            FXCollections.observableArrayList(service.listAll());

    public BorderPane getView() {
        BorderPane root = new BorderPane();

        ListView<Equipment> list = new ListView<>(data);
        list.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Equipment eq, boolean empty) {
                super.updateItem(eq, empty);
                if (empty || eq == null) setText(null);
                else setText(eq.getName() + " (คงเหลือ " + eq.getStock() + ")");
            }
        });

        TextField studentField = new TextField();
        studentField.setPromptText("รหัสนิสิต");

        Spinner<Integer> qtySpinner = new Spinner<>(1, 10, 1);

        Button returnBtn = new Button("ทำการคืน");
        returnBtn.setOnAction(e -> {
            Equipment selected = list.getSelectionModel().getSelectedItem();
            if (selected == null) { showError("เลือกอุปกรณ์ก่อน"); return; }
            try {
                StudentId sid = new StudentId(studentField.getText().trim());
                service.giveBack(sid, selected.getName(), qtySpinner.getValue());
                data.setAll(service.listAll());
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        VBox right = new VBox(10, new Label("รหัสนิสิต:"), studentField,
                new Label("จำนวนที่จะคืน:"), qtySpinner, returnBtn);

        root.setLeft(list);
        root.setCenter(right);
        return root;
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }
}