package com.example.Multipledatabse.repository;

import com.example.Multipledatabse.module.Primarytable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Primaryrepo extends JpaRepository<Primarytable,Integer> {
}
