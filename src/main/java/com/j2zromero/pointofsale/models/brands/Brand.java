package com.j2zromero.pointofsale.models.brands;

public class Brand {
    private String name;
    private int id;

    public Brand(){
    }
    public Brand(String name, int id){
        this.name =name;
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}