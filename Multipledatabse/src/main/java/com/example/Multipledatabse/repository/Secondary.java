package com.example.Multipledatabse.repository;

import com.example.Multipledatabse.module.Secondarytable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Secondary extends JpaRepository<Secondarytable,Integer> {
}
