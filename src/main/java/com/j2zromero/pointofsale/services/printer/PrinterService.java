package com.j2zromero.pointofsale.services.printer;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.output.PrinterOutputStream;
import com.j2zromero.pointofsale.models.printer.LocalPrinter;
import com.j2zromero.pointofsale.models.sale.Sale;
import com.j2zromero.pointofsale.models.sale.SaleDetail;
import com.j2zromero.pointofsale.repositories.printer.PrinterRepository;
import com.j2zromero.pointofsale.services.user.UserService;
import com.j2zromero.pointofsale.utils.DialogUtils;

import javax.print.PrintService;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    /**
     * Imprime el ticket de venta. Si la impresora no está disponible,
     * muestra alerta y no envía nada a la cola.
     */
    public void printReceipt(Sale sale, List<SaleDetail> items) throws SQLException {
        LocalPrinter local = getLocalPrinter();
        if (local == null) {
            throw new IllegalStateException("No hay impresora configurada en la BD.");
        }

        PrintService ps = PrinterOutputStream.getPrintServiceByName(local.getName());
        if (ps == null) {
            DialogUtils.showWarningAlert(
                    "Venta registrada",
                    "La venta se completó, pero la impresora \"" + local.getName() + "\" no está disponible.",
                    null
            );
            return;
        }

        PrinterIsAcceptingJobs accepting = ps.getAttribute(PrinterIsAcceptingJobs.class);
        if (accepting == PrinterIsAcceptingJobs.NOT_ACCEPTING_JOBS) {
            DialogUtils.showWarningAlert(
                    "Venta registrada",
                    "La venta se completó, pero la impresora está fuera de línea o no acepta trabajos.",
                    null
            );
            return;
        }

        try (
                PrinterOutputStream pos = new PrinterOutputStream(ps);
                EscPos escpos           = new EscPos(pos)
        ) {
            Style header = new Style()
                    .setFontSize(Style.FontSize._2, Style.FontSize._2)
                    .setJustification(EscPosConst.Justification.Center);
            Style normal = new Style()
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setJustification(EscPosConst.Justification.Left_Default);
            Style right  = new Style()
                    .setJustification(EscPosConst.Justification.Right);
            Style center = new Style().setJustification(EscPosConst.Justification.Center);

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            escpos.writeLF(header, local.getEnterpriseName())
                    .writeLF(normal, local.getAddress())
                    .writeLF(center, df.format(new Date()))
                    .feed(1);

            escpos.writeLF(normal, "Venta ID: "   + sale.getId())
                    .writeLF(normal, "Cajero: "     + UserService.getUser().getName())
                    .writeLF(normal, "Cliente ID: " + sale.getClientId())
                    .writeLF(normal, "Método: "     + sale.getPaymentMethod())
                    .feed(1);

            escpos.writeLF(right, String.format("%-10s %5s x %6s = %6s",
                    "Código","Cant.","P.U.","Tot."));
            for (SaleDetail d : items) {
                escpos.writeLF(right, String.format(
                        "%-10s %5.2f x %6.2f = %6.2f",
                        d.getProductCode(),
                        d.getQuantity(),
                        d.getUnitPrice(),
                        d.getTotalLine()
                ));
            }
            escpos.feed(1);

            escpos.writeLF(right, String.format("SubTotal: %6.2f", sale.getSubtotal()))
                    .writeLF(right, String.format("Descuento: %6.2f", sale.getDiscount()))
                    .writeLF(right, String.format("Impuestos: %6.2f", sale.getTaxes()))
                    .writeLF(right, String.format("TOTAL:    %6.2f", sale.getTotal()))
                    .feed(2);

            // Descomenta si necesitas cortar:
            // escpos.writeLF(header, "¡Gracias por su compra!")
            //       .feed(3)
            //       .cut(EscPos.CutMode.FULL);

        } catch (IOException e) {
            DialogUtils.showWarningAlert(
                    "Venta registrada",
                    "La venta se completó, pero ocurrió un error al conectar con la impresora.",
                    null
            );
        }
    }

    /**
     * Envía el pulso para abrir el cajón. Si la impresora no está disponible,
     * muestra alerta y no envía nada.
     */
    public void openCashDrawer() throws SQLException {
        LocalPrinter local = getLocalPrinter();
        if (local == null) {
            throw new IllegalStateException("No hay impresora configurada en la BD.");
        }

        PrintService ps = PrinterOutputStream.getPrintServiceByName(local.getName());
        if (ps == null) {
            DialogUtils.showWarningAlert(
                    "Acción de cajón",
                    "No se encontró la impresora para abrir el cajón.",
                    null
            );
            return;
        }

        PrinterIsAcceptingJobs accepting = ps.getAttribute(PrinterIsAcceptingJobs.class);
        if (accepting == PrinterIsAcceptingJobs.NOT_ACCEPTING_JOBS) {
            DialogUtils.showWarningAlert(
                    "Acción de cajón",
                    "La impresora está fuera de línea o no acepta trabajos.",
                    null
            );
            return;
        }

        try (EscPos escpos = new EscPos(new PrinterOutputStream(ps))) {
            escpos.write(27).write(112).write(0).write(55).write(121);
        } catch (IOException e) {
            DialogUtils.showWarningAlert(
                    "Acción de cajón",
                    "Ocurrió un error al intentar abrir el cajón de efectivo.",
                    null
            );
        }
    }
}
