package com.j2zromero.pointofsale.repositories.sale;

import com.j2zromero.pointofsale.models.inventories.Inventory;
import com.j2zromero.pointofsale.models.sale.Sale;
import com.j2zromero.pointofsale.models.sale.SaleDetail;
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
    // , List<SaleDetail> details
    public boolean add(Sale sale) throws SQLException {
        String sqlHeader = "{ CALL AddSale(?, ?, ?, ?, ?, ?, ?, ?, ?) }";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sqlHeader)) {
            // Cabecera de venta
            stmt.setString(1, sale.getCashierId());
            stmt.setString(2, sale.getTerminalId());
            SQLUtils.setNullable(stmt, 3, null , Types.VARCHAR);
            stmt.setDouble(4, sale.getSubtotal());
            stmt.setDouble(5, sale.getDiscount());
            stmt.setDouble(6, sale.getTaxes());
            stmt.setDouble(7, sale.getTotal());
            stmt.setString(8, sale.getPaymentMethod());
            // Parámetro OUT para obtener el id generado
            stmt.registerOutParameter(9, Types.BIGINT);
            stmt.execute();
            long saleId = stmt.getLong(9);
            System.out.println(saleId);
            // Detalles de venta en batch
         /*   String sqlDetail = "{ CALL AddSaleDetail(?, ?, ?, ?, ?, ?, ?, ?, ?) }";
            try (CallableStatement stmtD = con.prepareCall(sqlDetail)) {
                for (SaleDetail d : details) {
                    stmtD.setLong(1, saleId);
                    stmtD.setLong(2, d.getProductId());
                    stmtD.setDouble(3, d.getAmount());
                    stmtD.setDouble(4, d.getUnitPrice());
                    stmtD.setDouble(5, d.getDiscountLine());
                    stmtD.setDouble(6, d.getTaxesLine());
                    stmtD.setDouble(7, d.getTotalLine());
                    SQLUtils.setNullable(stmtD, 8, d.getCreatedAt(), Types.TIMESTAMP);
                    SQLUtils.setNullable(stmtD, 9, d.getUpdatedAt(), Types.TIMESTAMP);
                    stmtD.addBatch();
                }
                stmtD.executeBatch();
            }
*/
            return true;
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
                s.setTerminalId(rs.getString("terminal_id"));
                s.setCashierId(rs.getString("cashier_id"));
                s.setClientId(rs.getString("client_id"));
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

