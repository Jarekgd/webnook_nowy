package com.jaro.webnookbook.models;

/**
 * Category class for representing categories
 */
public class Category {
    private int id;
    private String name;

    // Constructor
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
