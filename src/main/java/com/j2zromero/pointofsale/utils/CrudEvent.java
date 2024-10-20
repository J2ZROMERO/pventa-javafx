package com.j2zromero.pointofsale.utils;

import java.sql.SQLException;

public interface CrudEvent {
    public abstract void CreateEvent() throws SQLException;
    public abstract void ReadEvent();
    public abstract void UpdateEvent();
    public abstract void DeleteEvent();

}
