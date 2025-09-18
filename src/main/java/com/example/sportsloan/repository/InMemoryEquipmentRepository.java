// src/main/java/com/example/sportsloan/repository/InMemoryEquipmentRepository.java
package com.example.sportsloan.repository;

import com.example.sportsloan.domain.Equipment;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryEquipmentRepository implements EquipmentRepository {
    private final Map<Long, Equipment> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(1000);

    @Override public Equipment save(Equipment e){ store.put(e.getId(), e); return e; }
    @Override public Optional<Equipment> findById(long id){ return Optional.ofNullable(store.get(id)); }
    @Override public Optional<Equipment> findByName(String name){
        return store.values().stream().filter(x->x.getName().equalsIgnoreCase(name)).findFirst();
    }
    @Override public Collection<Equipment> findAll(){
        return store.values().stream().sorted(Comparator.comparing(Equipment::getName)).toList();
    }
    @Override public Collection<Equipment> search(String keyword){
        if (keyword==null || keyword.isBlank()) return findAll();
        String q = keyword.toLowerCase();
        return store.values().stream()
                .filter(x->x.getName().toLowerCase().contains(q))
                .sorted(Comparator.comparing(Equipment::getName)).toList();
    }
    @Override public boolean deleteById(long id){ return store.remove(id) != null; }
    @Override public long nextId(){ return seq.incrementAndGet(); }
}