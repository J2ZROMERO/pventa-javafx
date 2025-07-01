package com.j2zromero.pointofsale.repositories.sale;

import com.j2zromero.pointofsale.models.inventories.Inventory;
import com.j2zromero.pointofsale.models.sale.Sale;
import com.j2zromero.pointofsale.models.sale.SaleDetail;
import com.j2zromero.pointofsale.services.user.UserService;
import com.j2zromero.pointofsale.utils.MariaDB;
import com.j2zromero.pointofsale.utils.SQLUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleRepository {

    /**
     * Agrega una nueva venta (cabecera y sus detalles) usando procedimientos almacenados.
     * @param sale Objeto Sale con datos de cabecera (sin id)
     * @return true si la operación fue exitosa
     * @throws SQLException en caso de error de BD
     */
    public boolean add(Sale sale, List<SaleDetail> saleDetail) throws SQLException {
        String sqlHeader = "{ CALL AddSale(?, ?, ?, ?, ?, ?, ?, ?, ?,?) }";
        String sqlDetail = "{ CALL AddSaleDetails(?, ?, ?, ?, ?, ?, ?) }";

        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password)) {
            con.setAutoCommit(false);  // START TRANSACTION

            try (CallableStatement stmt = con.prepareCall(sqlHeader)) {
                SQLUtils.setNullable(stmt, 1, sale.getTerminalId(), Types.BIGINT);
                SQLUtils.setNullable(stmt, 2, sale.getCashierId(), Types.BIGINT);
                SQLUtils.setNullable(stmt, 3, null, Types.BIGINT);
                stmt.setDouble(4, sale.getSubtotal());
                SQLUtils.setNullable(stmt, 5, sale.getDiscount(), Types.DOUBLE);
                stmt.setDouble(6, sale.getTotal());
                stmt.setString(7, sale.getPaymentMethod());
                SQLUtils.setNullable(stmt, 8, sale.getTaxes(), Types.DOUBLE);
                SQLUtils.setNullable(stmt, 9, UserService.getCajaId(), Types.BIGINT);

                stmt.registerOutParameter(10, Types.BIGINT);

                stmt.execute();
                long saleId = stmt.getLong(10);
                if (saleId <= 0) {
                    System.out.println("entro en el error");
                    throw new SQLException("Sale ID not generated.");
                }

                System.out.println(saleId);
                try (CallableStatement stmtD = con.prepareCall(sqlDetail)) {
                    for (SaleDetail d : saleDetail) {
                        stmtD.setLong(1, saleId);
                        stmtD.setString(2, d.getProductCode());
                        stmtD.setDouble(3, d.getQuantity());
                        stmtD.setDouble(4, d.getUnitPrice());
                        SQLUtils.setNullable(stmtD, 5, d.getDiscountLine(), Types.DOUBLE);
                        SQLUtils.setNullable(stmtD, 6, d.getTaxesLine(), Types.DOUBLE);
                        stmtD.setDouble(7, d.getTotalLine());
                        stmtD.addBatch();
                    }
                    stmtD.executeBatch();
                }


                con.commit(); // COMMIT TRANSACTION
                return true;

            } catch (Exception ex) {
                con.rollback(); // ROLLBACK ON FAILURE
                throw ex;
            }
        }
    }

    /**
     * Obtiene todas las ventas (cabeceras) desde BD.
     */
    public Sale getSalesSummary() throws SQLException {
        String sql = "{ CALL GetSalesSummary(?) }";
        Sale sale = new Sale();
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setLong(1, UserService.getCajaId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                 sale.setTotal(rs.getDouble(1)); // assuming the SP returns a single column
                sale.setDiscount(rs.getDouble(2));
            }
            return sale;
        }
    }


    /**
     * Obtiene todas las ventas (cabeceras) desde BD.
     */
    public List<Sale> getAll() throws SQLException {
        List<Sale> sales = new ArrayList<>();
        String sql = "{ CALL GetSales() }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getLong("id"));
                s.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                s.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                s.setTerminalId(rs.getLong("terminal_id"));
                s.setCashierId(rs.getLong("cashier_id"));
                s.setClientId(rs.getLong("client_id"));
                s.setSubtotal(rs.getDouble("subtotal"));
                s.setDiscount(rs.getDouble("discount"));
                s.setTaxes(rs.getDouble("taxes"));
                s.setTotal(rs.getDouble("total"));
                s.setPaymentMethod(rs.getString("payment_method"));
                sales.add(s);
            }
        }
        return sales;
    }

    /**
     * Elimina una venta por su ID.
     */
    public void delete(long id) throws SQLException {
        String sql = "{ CALL DeleteSale(?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setLong(1, id);
            stmt.execute();
        }
    }

    public SaleDetail getProductFromInventory(String productCode) throws SQLException{
        String sql = "{ CALL GetInventoryToSell(?) }";
        SaleDetail saleDetail = new SaleDetail();

        try (
                Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
                CallableStatement stmt = con.prepareCall(sql)
        ) {
            stmt.setString(1, productCode);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    saleDetail.setId(rs.getLong("id"));
                    saleDetail.setProductCode(rs.getString("code"));
                    saleDetail.setUnitMeasurement(rs.getString("unit_measurement"));
                    saleDetail.setAmountEntered(rs.getDouble("amount_entered"));
                    saleDetail.setUnitPrice(rs.getDouble("unit_price"));
                    saleDetail.setCreatedAt(rs.getDate("created_at"));
                    saleDetail.setUpdatedAt(rs.getDate("updated_at"));

                }
            }
        }
        return saleDetail;

    }
    // Puedes agregar update(), findById(), etc. siguiendo este mismo estilo.
}

