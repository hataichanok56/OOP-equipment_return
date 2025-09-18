package ui;

import model.Equipment;
import model.Inventory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Optional;

public class MainFrame extends JFrame {
    private final Inventory inventory;
    private final InventoryTableModel tableModel;

    private final JComboBox<Equipment> cboEquipment;
    private final JSpinner spQty;

    public MainFrame(Inventory inventory) {
        super("กองกิจการนิสิต - ยืมอุปกรณ์กีฬา");
        this.inventory = inventory;
        this.tableModel = new InventoryTableModel(inventory);

        // ---------- Frame ----------
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        var root = new JPanel(new BorderLayout());
        root.setBackground(new Color(0xEDE1FA));
        setContentPane(root);

        // ---------- Menu Bar ----------
        setJMenuBar(buildMenuBar());

        // ---------- Center Content ----------
        var main = new JPanel(new GridLayout(1, 2, 24, 0));
        main.setBorder(new EmptyBorder(24, 24, 24, 24));
        main.setOpaque(false);

        // Left panel (เลือกอุปกรณ์/จำนวน)
        var leftCard = new JPanel();
        leftCard.setBackground(new Color(0xDCDCDC));
        leftCard.setBorder(new EmptyBorder(32, 32, 32, 32));
        leftCard.setLayout(new BoxLayout(leftCard, BoxLayout.Y_AXIS));

        var lblEq = new JLabel("อุปกรณ์");
        lblEq.setFont(lblEq.getFont().deriveFont(Font.BOLD, 20f));
        lblEq.setAlignmentX(Component.LEFT_ALIGNMENT);

        cboEquipment = new JComboBox<>(inventory.listAll().toArray(new Equipment[0]));
        cboEquipment.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        cboEquipment.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftCard.add(lblEq);
        leftCard.add(Box.createVerticalStrut(8));
        leftCard.add(cboEquipment);
        leftCard.add(Box.createVerticalStrut(24));

        var lblQty = new JLabel("จำนวน");
        lblQty.setFont(lblQty.getFont().deriveFont(Font.BOLD, 20f));
        lblQty.setAlignmentX(Component.LEFT_ALIGNMENT);

        spQty = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        spQty.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        spQty.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftCard.add(lblQty);
        leftCard.add(Box.createVerticalStrut(8));
        leftCard.add(spQty);

        main.add(leftCard);

        // Right panel (ตารางคงเหลือ)
        var rightCard = new JPanel();
        rightCard.setLayout(new BorderLayout());
        rightCard.setBackground(new Color(0xF4ECFF));
        rightCard.setBorder(new EmptyBorder(16, 16, 16, 16));

        var header = new JPanel(new GridLayout(1, 2));
        header.add(makeHeaderLabel("อุปกรณ์กีฬา"));
        header.add(makeHeaderLabel("คงเหลือ"));
        rightCard.add(header, BorderLayout.NORTH);

        var table = new JTable(tableModel);
        table.setRowHeight(28);
        rightCard.add(new JScrollPane(table), BorderLayout.CENTER);

        main.add(rightCard);

        root.add(main, BorderLayout.CENTER);

        // Bottom (ปุ่มทำการยืม)
        var bottom = new JPanel();
        bottom.setOpaque(false);
        var btnBorrow = new JButton("ทำการยืม");
        btnBorrow.setPreferredSize(new Dimension(160, 40));
        btnBorrow.addActionListener(e -> onBorrow());
        bottom.add(btnBorrow);
        root.add(bottom, BorderLayout.SOUTH);
    }

    private JLabel makeHeaderLabel(String text) {
        var lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setOpaque(true);
        lbl.setBackground(new Color(0xC9A0FF));
        lbl.setForeground(Color.WHITE);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 16f));
        lbl.setBorder(new EmptyBorder(10, 8, 10, 8));
        return lbl;
    }

    private JMenuBar buildMenuBar() {
        var bar = new JMenuBar();
        var menu = new JMenu("หน้า\u200Bหลัก"); // ใส่ Zero-width space กันตัด
        var mAdd = new JMenuItem("Add item");
        var mDelete = new JMenuItem("Delete item");
        var mSearch = new JMenuItem("Search item");

        mAdd.addActionListener(e -> onAdd());
        mDelete.addActionListener(e -> onDelete());
        mSearch.addActionListener(e -> onSearch());

        menu.add(mAdd);
        menu.add(mDelete);
        menu.add(mSearch);
        bar.add(menu);
        return bar;
    }

    // -------- Actions --------
    private void onBorrow() {
        Equipment eq = (Equipment) cboEquipment.getSelectedItem();
        int qty = (int) spQty.getValue();
        if (eq == null) {
            JOptionPane.showMessageDialog(this, "กรุณาเลือกอุปกรณ์", "แจ้งเตือน",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            inventory.borrow(eq.getCode(), qty);
            tableModel.refresh();

            // แสดงหน้า “ทำการยืมเสร็จสิ้น”
            new SuccessDialog(this).setVisible(true);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "ไม่สามารถยืมได้",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onAdd() {
        JTextField code = new JTextField();
        JTextField name = new JTextField();
        JSpinner stock = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));

        var panel = new JPanel(new GridLayout(0, 1, 6, 6));
        panel.add(new JLabel("รหัสอุปกรณ์:"));
        panel.add(code);
        panel.add(new JLabel("ชื่ออุปกรณ์:"));
        panel.add(name);
        panel.add(new JLabel("จำนวนเริ่มต้น:"));
        panel.add(stock);

        int ok = JOptionPane.showConfirmDialog(this, panel, "Add item", JOptionPane.OK_CANCEL_OPTION);
        if (ok == JOptionPane.OK_OPTION) {
            boolean success = inventory.add(new model.Equipment(code.getText().trim(), name.getText().trim(),
                    (int) stock.getValue()));
            if (!success) {
                JOptionPane.showMessageDialog(this, "มีรหัสนี้อยู่แล้ว", "เพิ่มไม่สำเร็จ", JOptionPane.ERROR_MESSAGE);
            } else {
                refreshCombo();
            }
        }
    }

    private void onDelete() {
        String code = JOptionPane.showInputDialog(this, "ใส่รหัสอุปกรณ์ที่จะลบ:", "Delete item", JOptionPane.PLAIN_MESSAGE);
        if (code == null || code.isBlank()) return;
        boolean ok = inventory.delete(code.trim());
        if (!ok) {
            JOptionPane.showMessageDialog(this, "ไม่พบรหัสนี้", "ลบไม่สำเร็จ", JOptionPane.ERROR_MESSAGE);
        } else {
            refreshCombo();
        }
    }

    private void onSearch() {
        String code = JOptionPane.showInputDialog(this, "ใส่รหัสอุปกรณ์ที่ต้องการค้นหา:", "Search item",
                JOptionPane.PLAIN_MESSAGE);
        if (code == null || code.isBlank()) return;
        Equipment e = inventory.searchByCode(code.trim());
        if (e == null) {
            JOptionPane.showMessageDialog(this, "ไม่พบอุปกรณ์", "ผลการค้นหา", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "พบ: " + e.getName() + " | คงเหลือ: " + e.getStock(),
                    "ผลการค้นหา", JOptionPane.INFORMATION_MESSAGE);
            // เลือกใน Combo ให้เลย
            selectEquipmentByCode(e.getCode());
        }
    }

    private void refreshCombo() {
        var selected = (Equipment) cboEquipment.getSelectedItem();
        cboEquipment.removeAllItems();
        inventory.listAll().forEach(cboEquipment::addItem);
        tableModel.refresh();
        if (selected != null) selectEquipmentByCode(selected.getCode());
    }

    private void selectEquipmentByCode(String code) {
        for (int i = 0; i < cboEquipment.getItemCount(); i++) {
            Equipment cur = cboEquipment.getItemAt(i);
            if (cur.getCode().equals(code)) {
                cboEquipment.setSelectedIndex(i);
                break;
            }
        }
    }
}
