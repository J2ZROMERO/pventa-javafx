package com.j2zromero.pointofsale.utils;

import java.sql.SQLException;

public interface CrudDB {

    public abstract void CreateDB() throws SQLException;
    void createDB(String id, String rol, String name, String secondName, String pass, String contact) throws SQLException;

    public abstract void ReadDB();
    public abstract void UpdateDB();
    public abstract void DeleteDB();

}
