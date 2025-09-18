package model;

import java.util.*;

public class Inventory {
    private final Map<String, Equipment> byCode = new LinkedHashMap<>();

    // ---------- CRUD ----------
    public boolean add(Equipment eq) {
        if (byCode.containsKey(eq.getCode())) return false;
        byCode.put(eq.getCode(), eq);
        return true;
    }

    public boolean delete(String code) {
        return byCode.remove(code) != null;
    }

    public Equipment searchByCode(String code) {
        return byCode.get(code);
    }

    public List<Equipment> listAll() {
        return new ArrayList<>(byCode.values());
    }

    // ---------- Borrow ----------
    public void borrow(String code, int qty) throws IllegalArgumentException {
        if (qty <= 0) throw new IllegalArgumentException("จำนวนต้องมากกว่า 0");
        Equipment eq = byCode.get(code);
        if (eq == null) throw new IllegalArgumentException("ไม่พบอุปกรณ์");
        if (eq.getStock() < qty) throw new IllegalArgumentException("คงเหลือไม่พอ");
        eq.remove(qty);
    }

    // ---------- Seed sample data ----------
    public static Inventory sample() {
        Inventory inv = new Inventory();
        inv.add(new Equipment("BASK", "บาส", 10));
        inv.add(new Equipment("VOLL", "วอลเลย์บอล", 10));
        inv.add(new Equipment("FOOT", "ฟุตบอล", 10));
        inv.add(new Equipment("BDMN", "แบดมินตัน", 10));
        inv.add(new Equipment("SEPK", "เซปักตะกร้อ", 10));
        return inv;
    }
}
