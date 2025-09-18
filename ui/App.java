package ui;
import javax.swing.SwingUtilities;
import model.Inventory;
import ui.MainFrame;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Inventory inv = Inventory.sample(); // เติมรายการเริ่มต้น
            new MainFrame(inv).setVisible(true);
        });
    }
}