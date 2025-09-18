package ui;

import model.Equipment;
import model.Inventory;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class InventoryTableModel extends AbstractTableModel {
    private final Inventory inventory;
    private List<Equipment> snapshot;
    private final String[] cols = {"อุปกรณ์กีฬา", "คงเหลือ"};

    public InventoryTableModel(Inventory inventory) {
        this.inventory = inventory;
        refresh();
    }

    public void refresh() {
        snapshot = inventory.listAll();
        fireTableDataChanged();
    }

    @Override public int getRowCount() { return snapshot.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int c) { return cols[c]; }

    @Override public Object getValueAt(int r, int c) {
        Equipment e = snapshot.get(r);
        return (c == 0) ? e.getName() : e.getStock();
    }
}
