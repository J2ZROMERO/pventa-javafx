package com.j2zromero.pointofsale.services.sale;

import com.j2zromero.pointofsale.models.sale.Sale;
import com.j2zromero.pointofsale.models.sale.SaleDetail;
import com.j2zromero.pointofsale.repositories.sale.SaleRepository;

import java.sql.SQLException;
import java.util.List;

public class SaleService {
    private final SaleRepository saleRepository = new SaleRepository();

    public boolean add(Sale sale, List<SaleDetail>  saleDetail) throws SQLException {
        return saleRepository.add(sale,saleDetail);
    }

 /*   public List<Sale> getAll() throws SQLException {
        return saleRepository.getAll();
    }

    public void update(Sale sale) throws SQLException {
        saleRepository.update(sale);
    }

    public void delete(Long id) throws SQLException {
        saleRepository.delete(id);
    }*/
 public SaleDetail getProductFromInventory(String productCode) throws SQLException {
     return saleRepository.getProductFromInventory(productCode);
 }

    public Sale getSalesSummary() throws SQLException {
        return saleRepository.getSalesSummary();
    }

    /*public void saveAllSales(List<Sale> sales) throws SQLException {
        if (sales == null || sales.isEmpty()) {
            throw new IllegalArgumentException("No hay ventas para guardar.");
        }
        saleRepository.addAll(sales); // Call the batch insert method
    }*/
}
