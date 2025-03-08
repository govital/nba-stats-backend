package com.talgokhberg.nbastatsapplication.model;

import java.io.Serializable;

public class Team implements Serializable { // ✅ Implements Serializable
    private static final long serialVersionUID = 1L; // Recommended for Serializable classes

    private int id;
    private String name;

    public Team() {}

    public Team(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
