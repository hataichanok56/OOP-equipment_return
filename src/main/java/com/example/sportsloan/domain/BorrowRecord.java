// src/main/java/com/example/sportsloan/domain/BorrowRecord.java
package com.example.sportsloan.domain;

import java.time.LocalDateTime;

public class BorrowRecord {
    private final StudentId studentId;
    private final String equipmentName;
    private final int qty;
    private final LocalDateTime borrowedAt;
    private LocalDateTime returnedAt;

    public BorrowRecord(StudentId sid, String equipmentName, int qty) {
        this.studentId = sid;
        this.equipmentName = equipmentName;
        this.qty = qty;
        this.borrowedAt = LocalDateTime.now();
    }
    public void markReturned() { this.returnedAt = LocalDateTime.now(); }

    public StudentId getStudentId() { return studentId; }
    public String getEquipmentName() { return equipmentName; }
    public int getQty() { return qty; }
    public LocalDateTime getBorrowedAt() { return borrowedAt; }
    public LocalDateTime getReturnedAt() { return returnedAt; }
}