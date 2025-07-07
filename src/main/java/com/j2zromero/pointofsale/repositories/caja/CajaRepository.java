package com.j2zromero.pointofsale.repositories.caja;
import com.j2zromero.pointofsale.models.caja.*;
import com.j2zromero.pointofsale.models.sale.Sale;
import com.j2zromero.pointofsale.models.user.User;
import com.j2zromero.pointofsale.services.user.UserService;
import com.j2zromero.pointofsale.utils.MariaDB;
import com.j2zromero.pointofsale.utils.SQLUtils;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CajaRepository {
    private static final String SCHEMA = "p_venta";

    public Long open(Caja c) throws SQLException {
        String sql = "{ CALL OpenCaja(?, ?, ?, ?, ?) }";
        try (Connection con = DriverManager.getConnection(
                MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setLong(1, c.getTerminalId());
            stmt.setLong(2, c.getCashierId());
            stmt.setDouble(3, c.getOpeningAmount());
            stmt.setString(4, c.getNotes());

            // Register OUT parameter for the new caja ID
            stmt.registerOutParameter(5, java.sql.Types.BIGINT);

            stmt.executeUpdate();

            // Return the generated ID
            return stmt.getLong(5);
        }
    }

    public List<Withdrawal> getWithdrawalsByCajaId() throws SQLException {
        List<Withdrawal> withdraws = new ArrayList<>();
        String sql = "{ CALL GetWithdrawalsById(?) }";

        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setLong(1, UserService.getCajaId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Withdrawal w = new Withdrawal();
                    w.setId( rs.getLong("id") );
                    w.setCajaId( rs.getLong("fk_caja_id") );
                    w.setAmount( rs.getDouble("amount") );
                    w.setReason(rs.getString("reason"));
                    w.setCreatedAt(rs.getDate("created_at"));
                    withdraws.add(w);
                }
            }
        }

        return withdraws;
    }

    /**
     * Cierra la sesión de caja con totales finales.
     */
    public void closeCaja(Caja caja)
            throws SQLException
    {

        String sql = "{ CALL CloseCaja(?, ?, ?, ?, ?, ?, ?) }";
        try (Connection con = DriverManager.getConnection(
                MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setLong(1, UserService.getCajaId());
            stmt.setDouble(2, caja.getClosingAmount());
            stmt.setDouble(3, caja.getTotalSales());
            SQLUtils.setNullable(stmt, 4,caja.getTotalDiscounts(), Types.DOUBLE);
            SQLUtils.setNullable(stmt, 5,caja.getTotalCash(), Types.DOUBLE);
            SQLUtils.setNullable(stmt, 6,caja.getTotalCard(), Types.DOUBLE);
            SQLUtils.setNullable(stmt, 7, caja.getTotalTransfer(), Types.DOUBLE);
            stmt.execute();
        }
    }

    public List<SummaryCaja> getSummaryCaja(LocalDate date) throws SQLException {
        List<SummaryCaja> resumenes = new ArrayList<>();
        String sql = "{ CALL GetSummaryCaja(?) }";

        try (Connection con = DriverManager.getConnection(
                MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setDate(1, Date.valueOf(date));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SummaryCaja r = new SummaryCaja();
                    r.setCajaId(rs.getLong("caja_id"));
                    r.setUserName(rs.getString("user_name"));
                    r.setOpeningAmount(rs.getDouble("opening_amount"));
                    r.setClosingAmount(rs.getDouble("closing_amount"));
                    r.setSalesCount(rs.getDouble("sales_count"));
                    r.setSubTotal(rs.getDouble("sub_total"));
                    r.setTotalDiscounts(rs.getDouble("total_discounts"));
                    r.setTotalSold(rs.getDouble("total_sold"));
                    r.setTotalWithdrawals(rs.getDouble("total_withdrawals"));
                    r.setOpenedAt(
                            Optional.ofNullable(rs.getTimestamp("opened_at"))
                                    .map(Timestamp::toLocalDateTime)
                                    .orElse(null)
                    );
                    r.setClosedAt(
                            Optional.ofNullable(rs.getTimestamp("closed_at"))
                                    .map(Timestamp::toLocalDateTime)
                                    .orElse(null)
                    );
                    resumenes.add(r);
                }
            }
        }
        return resumenes;
    }
    public List<SummaryDetailsCaja> getSummaryDetailsCajas() {

        List<SummaryDetailsCaja> resumenes = new ArrayList<>();
        // si tu procedimiento está en el schema p_venta, inclúyelo:
        String sql = "{ CALL GetSummaryDetailsCaja() }";

        try (Connection con = DriverManager.getConnection(
                MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                SummaryDetailsCaja r = new SummaryDetailsCaja();
                r.setCajaId(rs.getLong("caja_id"));
                r.setCajeroName(rs.getString("cajero"));
                r.setProductCode(rs.getString("product_code"));
                r.setProductName(rs.getString("producto"));
                r.setSoldUnits(rs.getDouble("unidades_vendidas"));
                r.setUnitPrice(rs.getDouble("precio_unitario"));
                r.setImporteTotal(rs.getDouble("importe_linea"));
                resumenes.add(r);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resumenes;

    }

    public Double getTotalCaja() throws SQLException {
        String sql = "{ CALL GetTotalCaja() }";

        try (Connection con = DriverManager.getConnection(
                MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (!rs.next()) {
                // no row returned
                return null;
                // or: throw new SQLException("No total returned");
            }
            // use getBigDecimal for money
            return rs.getDouble("total_general");
        }
    }

    public void addWithdrawal(double amount, String motive)
            throws SQLException
    {
        String sql = "{ CALL AddWithdrawal(?, ?, ?) }";
        try (Connection con = DriverManager.getConnection(
                MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setLong(1, UserService.getCajaId());
            stmt.setDouble(2, amount);
            stmt.setString(3, motive);
            stmt.execute();
        }
    }

    /**
     * Obtiene todas las ventas (cabeceras) desde BD.
     */
    public CloseCaja getCajaSummary() throws SQLException {
        String sql = "{ CALL GetSalesSummary(?) }";
        CloseCaja caja = new CloseCaja();
        double openingAmount = 0;
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setLong(1, UserService.getCajaId());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                caja.setTotalSales(rs.getDouble(1));
                caja.setTotalDiscount(rs.getDouble(2));
                caja.setOpeningAmount(rs.getDouble(3));
                caja.setTotalWithdrawl(rs.getDouble(4));
            }
            return caja;
        }
    }


}
