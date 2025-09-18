import java.util.ArrayList;
import java.util.Scanner;

// -------------------------
// 1. Abstract Class (Abstraction)
// -------------------------
abstract class BaseItem {
    public abstract void display();
}

// -------------------------
// 2. Item Class (Encapsulation)
// -------------------------
class Item extends BaseItem {
    private static int nextId = 1000;
    private int id;
    private String name;
    private int quantity;

    public Item(String name, int quantity) {
        this.id = nextId++;
        this.name = name;
        this.quantity = quantity;
    }

    // Getter & Setter
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int qty) {
        this.quantity = qty;
    }

    // Polymorphism (override)
    @Override
    public void display() {
        System.out.println("รหัส: " + id + " | อุปกรณ์: " + name + " | จำนวน: " + quantity);
    }
}

// -------------------------
// 3. Inventory Class (Composition)
// -------------------------
class Inventory {
    private ArrayList<Item> items = new ArrayList<>();

    public void addItem(String name, int qty) {
        items.add(new Item(name, qty));
        System.out.println("✅ เพิ่มรายการสำเร็จ");
    }

    public void deleteItem(int id) {
        for (Item i : items) {
            if (i.getId() == id) {
                items.remove(i);
                System.out.println("🗑️ ลบเรียบร้อย");
                return;
            }
        }
        System.out.println("❌ ไม่พบรายการ");
    }

    public void searchItem(String keyword) {
        boolean found = false;
        for (Item i : items) {
            if (i.getName().contains(keyword)) {
                i.display();
                found = true;
            }
        }
        if (!found) {
            System.out.println("❌ ไม่พบข้อมูล");
        }
    }

    public void showAll() {
        if (items.isEmpty()) {
            System.out.println("⚠️ ไม่มีข้อมูล");
        } else {
            for (Item i : items) {
                i.display();
            }
        }
    }
}

// -------------------------
// 4. User Interface (Menu)
// -------------------------
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Inventory inventory = new Inventory();

        while (true) {
            System.out.println("\n===== เมนูหลัก =====");
            System.out.println("1. เพิ่มอุปกรณ์กีฬา");
            System.out.println("2. ลบอุปกรณ์กีฬา");
            System.out.println("3. ค้นหาอุปกรณ์กีฬา");
            System.out.println("4. แสดงอุปกรณ์ทั้งหมด");
            System.out.println("5. ออกจากโปรแกรม");
            System.out.print("เลือกเมนู: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("ชื่ออุปกรณ์: ");
                    String name = sc.nextLine();
                    System.out.print("จำนวน: ");
                    int qty = Integer.parseInt(sc.nextLine());
                    inventory.addItem(name, qty);
                    break;

                case "2":
                    System.out.print("ใส่รหัสอุปกรณ์: ");
                    int id = Integer.parseInt(sc.nextLine());
                    inventory.deleteItem(id);
                    break;

                case "3":
                    System.out.print("ใส่คำค้นหา: ");
                    String keyword = sc.nextLine();
                    inventory.searchItem(keyword);
                    break;

                case "4":
                    inventory.showAll();
                    break;

                case "5":
                    System.out.println("👋 ออกจากโปรแกรมแล้ว");
                    sc.close();
                    return;

                default:
                    System.out.println("❌ เลือกเมนูไม่ถูกต้อง");
            }
        }
    }
}
