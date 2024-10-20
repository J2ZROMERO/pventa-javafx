package com.j2zromero.pointofsale.utils;

public class GenericValue<T> {

    public  GenericValue(T data){
        this.data=data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    private T data;
}
