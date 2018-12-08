package com.greentea.model;

public enum MemoryName {
    TEMP_DATA("temp_data");

    MemoryName(String name) {
        this.name = name;
    }

    private String name;

    @Override
    public String toString() {
        return name;
    }
}
