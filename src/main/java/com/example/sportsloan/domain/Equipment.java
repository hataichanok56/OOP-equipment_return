package com.example.sportsloan.domain;

import java.util.Objects;

public class Equipment {
    private final long id;
    private String name;
    private int total;
    private int stock;

    private Equipment(long id, String name, int total) {
        this.id = id;
        this.name = name;
        this.total = Math.max(0, total);
        this.stock = this.total;
    }

    public static Equipment create(long id, String name, int total) {
        return new Equipment(id, name, total);
    }

    public boolean borrow(int qty) {
        if (qty <= 0) return false;
        if (stock < qty) return false;
        stock -= qty;
        return true;
    }

    public void giveBack(int qty) {
        if (qty <= 0) return;
        stock = Math.min(total, stock + qty);
    }

    // getters & setters
    public long getId() { return id; }
    public String getName() { return name; }
    public int getTotal() { return total; }
    public int getStock() { return stock; }
    public void setName(String name) { this.name = name; }
    public void setTotal(int total) {
        this.total = Math.max(0, total);
        if (stock > this.total) stock = this.total;
    }
    public void setStock(int stock) {
        this.stock = Math.max(0, Math.min(stock, total));
    }

    @Override public boolean equals(Object o) { return (o instanceof Equipment e) && e.id==id; }
    @Override public int hashCode() { return Objects.hash(id); }
}