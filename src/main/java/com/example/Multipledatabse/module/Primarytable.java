package com.example.Multipledatabse.module;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Primarytable {

    @Id
    int id;

    public Primarytable(int id, String name) {
        this.id = id;
        Name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    String Name;
}
