package com.j2zromero.pointofsale.repositories.sale;

import com.j2zromero.pointofsale.models.sale.Sale;
import com.j2zromero.pointofsale.models.sale.SaleDetail;
import com.j2zromero.pointofsale.services.user.UserService;
import com.j2zromero.pointofsale.utils.MariaDB;
import com.j2zromero.pointofsale.utils.SQLUtils;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SaleRepository {

    /**
     * Agrega una nueva venta (cabecera y sus detalles) usando procedimientos almacenados.
     * @param sale Objeto Sale con datos de cabecera (sin id)
     * @return true si la operación fue exitosa
     * @throws SQLException en caso de error de BD
     */
    public Long add(Sale sale, List<SaleDetail> saleDetail) throws SQLException {
        System.out.println(saleDetail);
        String sqlHeader = "{ CALL AddSale(?, ?, ?, ?, ?, ?, ?, ?, ?,?) }";
        String sqlDetail = "{ CALL AddSaleDetails( ?, ?, ?, ?, ?, ?, ?, ?, ?,?) }";
        long saleId;
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
                saleId = stmt.getLong(10);
                if (saleId <= 0) {
                    System.out.println("entro en el error");
                    throw new SQLException("Sale ID not generated.");
                }

                try (CallableStatement stmtD = con.prepareCall(sqlDetail)) {
                    for (SaleDetail d : saleDetail) {
                        stmtD.setLong(1, saleId);
                        stmtD.setString(2, d.getProductCode());
                        SQLUtils.setNullable(stmtD, 3, d.getQuantity(), Types.DOUBLE);
                        stmtD.setDouble(4, d.getUnitPrice());
                        SQLUtils.setNullable(stmtD, 5, d.getDiscountLine(), Types.DOUBLE);
                        SQLUtils.setNullable(stmtD, 6, d.getTaxesLine(), Types.DOUBLE);
                        stmtD.setDouble(7, d.getTotalLine());
                        SQLUtils.setNullable(stmtD, 8, d.getCodePrice(), Types.VARCHAR);
                        SQLUtils.setNullable(stmtD, 9, d.getTotalInPackage(), Types.DOUBLE);
                        SQLUtils.setNullable(stmtD, 10, d.getExtraLine(), Types.DOUBLE);


                        stmtD.addBatch();
                    }
                    stmtD.executeBatch();
                }


                con.commit(); // COMMIT TRANSACTION
                return saleId;

            } catch (Exception ex) {
                con.rollback(); // ROLLBACK ON FAILURE
                throw ex;
            }
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
                    saleDetail.setProductName(rs.getString("name"));
                    saleDetail.setStock(rs.getDouble("stock"));
                    saleDetail.setUnitMeasurement(rs.getString("unit_measurement"));
                    saleDetail.setUnitPrice(rs.getDouble("unit_price"));
                    saleDetail.setCreatedAt(rs.getDate("created_at"));
                    saleDetail.setUpdatedAt(rs.getDate("updated_at"));
                    saleDetail.setPackagePrice(rs.getDouble("package_price"));
                    saleDetail.setHasPackageLogic(rs.getBoolean("has_package_logic"));
                    saleDetail.setTotalInPackage(rs.getDouble("total_in_package"));
                    saleDetail.setDescription(rs.getString("description"));

                }
            }
        }
        return saleDetail;

    }
    // Puedes agregar update(), findById(), etc. siguiendo este mismo estilo.

    /**
     * Recupera las ventas de la fecha indicada usando el SP GetSalesByDate.
     */
    public List<Sale> getSalesByDate(LocalDate date) throws SQLException {
        List<Sale> reports = new ArrayList<>();
        String sql = "{ CALL GetSalesByDate(?) }";

        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {

            // 1) Fijamos el parámetro de fecha
            stmt.setDate(1, Date.valueOf(date));

            // 2) Ejecutamos y mapeamos cada fila al DTO
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Sale r = new Sale();
                    r.setId(rs.getLong("id"));
                    r.setCashierName(rs.getString("cashier"));
                    r.setTerminalName(rs.getString("terminal"));
                    r.setSubtotal(rs.getDouble("subtotal"));
                    r.setDiscount(rs.getDouble("discount"));
                    r.setTotal(rs.getDouble("total"));
                    r.setPaymentMethod(rs.getString("payment_method"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) {
                        r.setCreatedAt(ts.toLocalDateTime());
                    }
                    r.setCajaId(rs.getLong("caja_id"));
                    reports.add(r);
                }
            }
        }

        return reports;
    }

    public List<SaleDetail> getDetailsBySaleId(long saleId) throws SQLException {
        List<SaleDetail> detalles = new ArrayList<>();
        String sql = "{ CALL GetSaleDetailsBySellId(?) }";

        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setLong(1, saleId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SaleDetail d = new SaleDetail();
                    d.setId(rs.getLong(1));
                    d.setSellId(rs.getLong(2));
                    d.setProductCode(rs.getString(3));
                    d.setQuantity(rs.getDouble(4));
                    d.setUnitPrice(rs.getDouble(5));
                    d.setDiscountLine(rs.getDouble(6));
                    d.setTaxesLine(rs.getDouble(7));
                    d.setTotalLine(rs.getDouble(8));
                    d.setUpdatedAt(rs.getDate(9));
                    d.setCreatedAt(rs.getDate(10));
                    d.setUnitMeasurement(rs.getString(11));
                    detalles.add(d);

                }
            }
        }
        System.out.println(detalles);
        return detalles;
    }
}

