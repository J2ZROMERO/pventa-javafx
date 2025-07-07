package com.j2zromero.pointofsale.services.caja;

import com.j2zromero.pointofsale.models.caja.*;
import com.j2zromero.pointofsale.models.sale.Sale;
import com.j2zromero.pointofsale.repositories.caja.CajaRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CajaService {
    private CajaRepository repo = new CajaRepository();

    public Long openCaja(Caja c) throws SQLException {
       return  repo.open(c);
    }

    public void closeCaja(Caja c) throws SQLException {
          repo.closeCaja(c);
    }

    public List<SummaryCaja> getSummaryCaja(LocalDate date) throws SQLException {
        return repo.getSummaryCaja(date);
    }

    public List<SummaryDetailsCaja> getSummaryDetailsCajas() {
        return repo.getSummaryDetailsCajas();
    }

    public Double getTotalCaja() throws SQLException {
        return repo.getTotalCaja();
    }

    public void addWithdrawal(double amount, String motive) throws SQLException {
        repo.addWithdrawal(amount,motive);
    }

    public List<Withdrawal> getWithdrawalsByCajaId() throws SQLException {
        return repo.getWithdrawalsByCajaId();
    }

    public CloseCaja getCajaSummary() throws SQLException {
        return repo.getCajaSummary();
    }

/*

    public void closeCaja(Caja c) throws SQLException {
        repo.update(c);
    }

    public List<Caja> getAll() throws SQLException {
        return repo.getAll();
    }*/
}
