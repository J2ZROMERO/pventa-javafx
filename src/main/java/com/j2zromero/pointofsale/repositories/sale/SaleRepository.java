package com.j2zromero.pointofsale.repositories.sale;

import com.j2zromero.pointofsale.models.sale.Sale;
import com.j2zromero.pointofsale.utils.MariaDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleRepository {

    public void add(Sale sale) throws SQLException {
        String sql = "{ CALL AddSale(?, ?, ?, ?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setLong(1, sale.getIdProduct());
            stmt.setDouble(2, sale.getSoldAmount());
            stmt.setDouble(3, sale.getUnitPrice());
            stmt.setDouble(4, sale.getTotalSale());
            stmt.setString(5, sale.getSaleType());
            stmt.execute();
        }
    }

    public void addAll(List<Sale> sales) throws SQLException {
        String sql = "{ CALL AddSale(?, ?, ?, ?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {

            for (Sale sale : sales) {
                stmt.setLong(1, sale.getIdProduct());
                stmt.setDouble(2, sale.getSoldAmount());
                stmt.setDouble(3, sale.getUnitPrice());
                stmt.setDouble(4, sale.getTotalSale());
                stmt.setString(5, "pieza");
                stmt.addBatch();
            }

            stmt.executeBatch(); // Execute the batch of inserts
        }
    }
/*
    public List<Sale> getAll() throws SQLException {
        List<Sale> sales = new ArrayList<>();
        String sql = "{ CALL GetSells() }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Sale sale = new Sale();
                sale.setId(rs.getLong("id"));
                sale.setIdProduct(rs.getLong("id_product"));
                sale.setSoldAmount(rs.getDouble("sold_amount"));
                sale.setUnitPrice(rs.getDouble("unit_price"));
                sale.setTotalSold(rs.getDouble("total_sold"));
                sale.setDate(rs.getTimestamp("date"));
                sale.setSellType(rs.getString("sell_type"));
                sales.add(sale);
            }
        }
        return sales;
    }

    public void update(Sale sale) throws SQLException {
        String sql = "{ CALL UpdateSell(?, ?, ?, ?, ?, ?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setLong(1, sale.getId());
            stmt.setLong(2, sale.getIdProduct());
            stmt.setDouble(3, sale.getSoldAmount());
            stmt.setDouble(4, sale.getUnitPrice());
            stmt.setDouble(5, sale.getTotalSold());
            stmt.setTimestamp(6, new Timestamp(sale.getDate().getTime()));
            stmt.setString(7, sale.getSellType());
            stmt.execute();
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "{ CALL DeleteSell(?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setLong(1, id);
            stmt.execute();
        }
    }*/
}
