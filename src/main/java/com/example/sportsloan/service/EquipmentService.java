package com.example.sportsloan.service;

import com.example.sportsloan.domain.*;
import com.example.sportsloan.repository.*;

import java.util.*;
import java.util.stream.Collectors;

public class EquipmentService {
    private final EquipmentRepository repo;
    private final List<BorrowRecord> ledger = Collections.synchronizedList(new ArrayList<>());

    public EquipmentService() {
        this.repo = new InMemoryEquipmentRepository();
        seed();
    }

    private void seed() {
        addItem("ลูกบาสเก็ตบอล", 12);
        addItem("ลูกฟุตบอล", 18);
        addItem("แร็คเก็ตแบดมินตัน", 24);
        addItem("เชือกกระโดด", 10);
    }

    // Admin
    public Equipment addItem(String name, int total) {
        long id = repo.nextId();
        return repo.save(Equipment.create(id, name, total));
    }
    public boolean deleteItem(long id) { return repo.deleteById(id); }
    public Collection<Equipment> search(String keyword) { return repo.search(keyword); }
    public Equipment update(long id, String name, Integer total, Integer stock){
        Equipment e = repo.findById(id).orElseThrow();
        if (name!=null && !name.isBlank()) e.setName(name);
        if (total!=null) e.setTotal(total);
        if (stock!=null) e.setStock(stock);
        return repo.save(e);
    }
    public Collection<Equipment> listAll(){ return repo.findAll(); }

    // User
    public int borrow(StudentId sid, String equipmentName, int qty) {
        Equipment e = repo.findByName(equipmentName).orElseThrow();
        if (!e.borrow(qty)) throw new IllegalStateException("สต็อกคงเหลือไม่พอ");
        ledger.add(new BorrowRecord(sid, e.getName(), qty));
        repo.save(e);
        return e.getStock();
    }
    public int giveBack(StudentId sid, String equipmentName, int qty) {
        Equipment e = repo.findByName(equipmentName).orElseThrow();
        e.giveBack(qty);
        ledger.stream()
              .filter(r -> r.getStudentId().equals(sid)
                        && r.getEquipmentName().equalsIgnoreCase(equipmentName)
                        && r.getReturnedAt()==null)
              .findFirst().ifPresent(BorrowRecord::markReturned);
        repo.save(e);
        return e.getStock();
    }

    // Ledger
    public List<BorrowRecord> byStudent(String studentId) {
        return ledger.stream()
                .filter(r -> r.getStudentId().value().equalsIgnoreCase(studentId))
                .collect(Collectors.toList());
    }
}
