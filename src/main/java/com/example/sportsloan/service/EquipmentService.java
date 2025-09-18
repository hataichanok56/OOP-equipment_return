// src/main/java/com/example/sportsloan/service/EquipmentService.java
package com.example.sportsloan.service;

import com.example.sportsloan.domain.*;
import com.example.sportsloan.repository.*;

import java.util.*;
import java.util.stream.Collectors;

public class EquipmentService {
    private final EquipmentRepository repo;
    private final List<BorrowRecord> ledger = Collections.synchronizedList(new ArrayList<>());

    // DIP: allow inject other repo impl in tests
    public EquipmentService() { this(new InMemoryEquipmentRepository()); }
    public EquipmentService(EquipmentRepository repo) {
        this.repo = repo;
        seed();
    }

    private void seed() {
        addItem("บอล", 10);
        addItem("บาส", 10);
        addItem("ลูกขนไก่", 10);
        addItem("วอลเลย์บอล", 10);
        addItem("เชปะตะกร้อ", 10);
        addItem("ไม้แบด", 10);
    }

    // ===== Menu: Add / Delete / Search =====
    public Equipment addItem(String name, int total){
        long id = repo.nextId();
        return repo.save(Equipment.create(id, name, total));
    }
    public boolean deleteItem(long id){ return repo.deleteById(id); }
    public Collection<Equipment> searchItem(String keyword){ return repo.search(keyword); }
    public Collection<Equipment> listAll(){ return repo.findAll(); }
    public Equipment updateItem(long id, String newName, Integer newTotal, Integer newStock){
        Equipment e = repo.findById(id).orElseThrow();
        if (newName!=null) e.setName(newName);
        if (newTotal!=null) e.setTotal(newTotal);
        if (newStock!=null) e.setStock(newStock);
        return repo.save(e);
    }

    // ===== Borrow / Return =====
    public int borrow(StudentId sid, String equipmentName, int qty){
        Equipment e = repo.findByName(equipmentName).orElseThrow();
        if (!e.borrow(qty)) throw new IllegalStateException("สต็อกคงเหลือไม่พอ");
        ledger.add(new BorrowRecord(sid, e.getName(), qty));
        repo.save(e);
        return e.getStock();
    }
    public int giveBack(StudentId sid, String equipmentName, int qty){
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

    // ===== Records =====
    public List<BorrowRecord> byStudent(String studentId){
        String q = studentId==null? "" : studentId.trim();
        return ledger.stream()
                .filter(r -> r.getStudentId().value().equalsIgnoreCase(q))
                .collect(Collectors.toList());
    }
}