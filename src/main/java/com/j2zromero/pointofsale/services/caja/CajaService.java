package com.j2zromero.pointofsale.services.caja;

import com.j2zromero.pointofsale.models.caja.Caja;
import com.j2zromero.pointofsale.repositories.caja.CajaRepository;

import java.sql.SQLException;
import java.util.List;

public class CajaService {
    private CajaRepository repo = new CajaRepository();

    public void openCaja(Caja c) throws SQLException {
        repo.open(c);
    }

/*

    public void closeCaja(Caja c) throws SQLException {
        repo.update(c);
    }

    public List<Caja> getAll() throws SQLException {
        return repo.getAll();
    }*/
}
