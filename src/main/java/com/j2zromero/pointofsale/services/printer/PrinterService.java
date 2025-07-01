package com.j2zromero.pointofsale.services.printer;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.output.PrinterOutputStream;
import com.j2zromero.pointofsale.models.printer.LocalPrinter;
import com.j2zromero.pointofsale.repositories.printer.PrinterRepository;

import javax.print.PrintService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PrinterService {
    private PrinterRepository repo = new PrinterRepository();

    /** Devuelve todas las impresoras disponibles */
    public List<LocalPrinter> getAllPrinters() throws SQLException {
        return repo.getAll();
    }

    /** Busca una impresora por su id */
    public LocalPrinter getPrinterById(String id) throws SQLException {
        return repo.findById(id);
    }


    public void add(LocalPrinter printer) throws SQLException {
         repo.add(printer);
    }

    public LocalPrinter getLocalPrinter() throws SQLException {
        return repo.getLocalPrinter();
    }
    public void openCashDrawer() throws SQLException, IOException {
        // 1) Recupera la impresora de la BD
        LocalPrinter local = getLocalPrinter();
        if (local == null) {
            throw new IllegalStateException("No hay impresora configurada en la BD.");
        }

        // 2) Busca el PrintService por nombre (helper de escpos-coffee)
        PrintService ps = PrinterOutputStream
                .getPrintServiceByName(local.getName());
        System.out.println(ps);
        // 3) Envío del pulso ESC p 0 t1 t2
        try (EscPos escpos = new EscPos(new PrinterOutputStream(ps))) {
            escpos.write(27)   // ESC
                    .write(112)  // 'p'
                    .write(0)    // drawer #1
                    .write(55)   // t1 = 55 ms
                    .write(121)  // t2 = 121 ms
            ;

        }
    }
}
