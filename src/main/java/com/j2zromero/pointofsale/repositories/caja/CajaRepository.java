package com.j2zromero.pointofsale.repositories.caja;
import com.j2zromero.pointofsale.models.caja.Caja;
import com.j2zromero.pointofsale.services.user.UserService;
import com.j2zromero.pointofsale.utils.MariaDB;
import com.j2zromero.pointofsale.utils.SQLUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

  /*  public Long add(Caja c) throws SQLException {
        String sql = "INSERT INTO " + SCHEMA + ".caja"
                + "(terminal_id, cashier_id, opening_time, opening_amount, notes)"
                + " VALUES(?, ?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, c.getTerminalId());
            stmt.setLong(2, c.getCashierId());
            stmt.setTimestamp(3, c.getOpeningTime());
            stmt.setBigDecimal(4, c.getOpeningAmount());
            stmt.setString(5, c.getNotes());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    c.setId(id);
                    return id;
                }
            }
        }
        return null;
    }
*/
    /*public void update(Caja c) throws SQLException {
        String sql = "UPDATE " + SCHEMA + ".caja SET "
                + "closing_time=?, closing_amount=?, total_sales=?, total_discounts=?, total_taxes=?,"
                + " total_cash=?, total_card=?, total_transfer=?, is_closed=?, notes=?, updated_at=CURRENT_TIMESTAMP "
                + "WHERE id=?";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setTimestamp(1, c.getClosingTime());
            stmt.setBigDecimal(2, c.getClosingAmount());
            stmt.setBigDecimal(3, c.getTotalSales());
            stmt.setBigDecimal(4, c.getTotalDiscounts());
            stmt.setBigDecimal(5, c.getTotalTaxes());
            stmt.setBigDecimal(6, c.getTotalCash());
            stmt.setBigDecimal(7, c.getTotalCard());
            stmt.setBigDecimal(8, c.getTotalTransfer());
            stmt.setBoolean(9, c.getIsClosed());
            stmt.setString(10, c.getNotes());
            stmt.setLong(11, c.getId());
            stmt.executeUpdate();
        }
    }

    public List<Caja> getAll() throws SQLException {
        List<Caja> list = new ArrayList<>();
        String sql = "SELECT * FROM " + SCHEMA + ".caja";
        try (Connection con = DriverManager.getConnection(MariaDB.URL, MariaDB.user, MariaDB.password);
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Caja c = new Caja();
                c.setId(rs.getLong("id"));
                c.setTerminalId(rs.getString("terminal_id"));
                c.setCashierId(rs.getLong("cashier_id"));
                c.setOpeningTime(rs.getTimestamp("opening_time"));
                c.setClosingTime(rs.getTimestamp("closing_time"));
                c.setOpeningAmount(rs.getBigDecimal("opening_amount"));
                c.setClosingAmount(rs.getBigDecimal("closing_amount"));
                c.setTotalSales(rs.getBigDecimal("total_sales"));
                c.setTotalDiscounts(rs.getBigDecimal("total_discounts"));
                c.setTotalTaxes(rs.getBigDecimal("total_taxes"));
                c.setTotalCash(rs.getBigDecimal("total_cash"));
                c.setTotalCard(rs.getBigDecimal("total_card"));
                c.setTotalTransfer(rs.getBigDecimal("total_transfer"));
                c.setIsClosed(rs.getBoolean("is_closed"));
                c.setNotes(rs.getString("notes"));
                c.setCreatedAt(rs.getTimestamp("created_at"));
                c.setUpdatedAt(rs.getTimestamp("updated_at"));
                list.add(c);
            }
        }
        return list;
    }*/
}
