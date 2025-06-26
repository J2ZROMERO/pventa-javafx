package com.j2zromero.pointofsale.services.terminal;

import com.j2zromero.pointofsale.models.terminal.Terminal;
import com.j2zromero.pointofsale.repositories.terminal.TerminalRepository;

import java.sql.SQLException;
import java.util.List;

public class TerminalService {
    private TerminalRepository repo = new TerminalRepository();

    public List<Terminal> getAll() throws SQLException {
        return repo.getAll();
    }

    public Long add(Terminal terminal) throws SQLException {
        return repo.add(terminal);
    }

    public void update(Terminal terminal) throws SQLException {
        repo.update(terminal);
    }

    public void delete(Long id) throws SQLException {
        repo.delete(id);
    }
}
