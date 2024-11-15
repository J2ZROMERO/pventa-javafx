package com.j2zromero.pointofsale.utils;

public class UnitType {
    private int id;
    private String name;

    public UnitType(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name; // This ensures only the name is displayed in ChoiceBox/ComboBox
    }
}
