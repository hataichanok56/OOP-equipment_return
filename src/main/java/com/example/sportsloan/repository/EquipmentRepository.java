package com.example.sportsloan.repository;

import com.example.sportsloan.domain.Equipment;
import java.util.Collection;
import java.util.Optional;

public interface EquipmentRepository {
    Equipment save(Equipment e);
    Optional<Equipment> findById(long id);
    Optional<Equipment> findByName(String name);
    Collection<Equipment> findAll();
    Collection<Equipment> search(String keyword);
    boolean deleteById(long id);
    long nextId();
}
