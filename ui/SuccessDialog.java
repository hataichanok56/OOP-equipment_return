package ui;

import javax.swing.*;
import java.awt.*;

public class SuccessDialog extends JDialog {
    public SuccessDialog(JFrame owner) {
        super(owner, "สำเร็จ", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        var panel = new JPanel();
        panel.setBackground(new Color(0xF2EDF8));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        var box = new JPanel();
        box.setBackground(new Color(0xDCDCDC));
        box.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

        var lbl = new JLabel("ทำการยืมเสร็จสิ้น", SwingConstants.CENTER);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 28f));

        var icon = new JLabel("\u2713", SwingConstants.CENTER); // ✓
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        icon.setFont(icon.getFont().deriveFont(Font.BOLD, 48f));

        box.add(lbl);
        box.add(Box.createVerticalStrut(16));
        box.add(icon);

        panel.add(box);
        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(owner);
    }
}
