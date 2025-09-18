// src/main/java/com/example/sportsloan/ui/ReturnPanel.java
package com.example.sportsloan.ui;

import com.example.sportsloan.domain.StudentId;
import com.example.sportsloan.service.EquipmentService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ReturnPanel {
    private final EquipmentService service;
    public ReturnPanel(EquipmentService service){ this.service = service; }

    public BorderPane getView(){
        BorderPane root = new BorderPane(); root.setPadding(new Insets(16));

        ListView<String> list = new ListView<>();
        list.setItems(FXCollections.observableArrayList(
                service.listAll().stream().map(e -> e.getName()+" ("+e.getStock()+")").toList()
        ));

        ComboBox<String> cb = new ComboBox<>();
        cb.setPromptText("อุปกรณ์"); cb.getItems().setAll(service.listAll().stream().map(e->e.getName()).toList());

        Spinner<Integer> qty = new Spinner<>(1, 999, 1);
        TextField sid = new TextField(); sid.setPromptText("รหัสนิสิต");
        Button ret = new Button("ทำการคืน");
        Label msg = new Label();

        ret.setOnAction(e -> {
            try {
                int left = service.giveBack(new StudentId(sid.getText().trim()), cb.getValue(), qty.getValue());
                msg.setText("คืนสำเร็จ คงเหลือ: " + left);
                list.setItems(FXCollections.observableArrayList(
                        service.listAll().stream().map(x -> x.getName()+" ("+x.getStock()+")").toList()
                ));
            } catch (Exception ex){ msg.setText("ผิดพลาด: "+ex.getMessage()); }
        });

        VBox right = new VBox(10, new Label("คืนอุปกรณ์"), cb, qty, sid, ret, msg);

        root.setLeft(list);
        root.setCenter(right);
        return root;
    }
}