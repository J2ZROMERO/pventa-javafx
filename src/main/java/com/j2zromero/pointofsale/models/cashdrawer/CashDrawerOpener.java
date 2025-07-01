package com.j2zromero.pointofsale.models.cashdrawer;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;

/**
 * Utilidad para abrir el cajón de efectivo enviando un comando ESC/POS
 * a la impresora conectada.
 */
public class CashDrawerOpener {

    /**
     * Abre el cajón usando la impresora por defecto del sistema.
     *
     * @throws PrintException si no hay impresora por defecto o falla la impresión.
     */
    public static void openDrawer() throws PrintException {
        // 1) Buscamos la impresora por defecto
        PrintService defaultPrinter = getDefaultPrinter();
        if (defaultPrinter == null) {
            throw new PrintException("No se encontró impresora por defecto");
        }
        // 2) Delegamos al método que envía el comando
        openDrawer(defaultPrinter);
    }

    /**
     * Abre el cajón en la impresora cuyo nombre coincida (case-insensitive).
     *
     * @param printerName nombre exacto de la impresora (tal como aparece en el SO).
     * @throws PrintException si no se encuentra la impresora o falla la impresión.
     */
    public static void openDrawer(String printerName) throws PrintException {
        PrintService ps = findPrintService(printerName);
        if (ps == null) {
            throw new PrintException("Impresora no encontrada: " + printerName);
        }
        openDrawer(ps);
    }

    /**
     * Abre el cajón enviando el pulso ESC/POS directamente al PrintService dado.
     *
     * @param service el servicio de impresión al que enviar el pulso.
     * @throws PrintException si falla el envío.
     */
    public static void openDrawer(PrintService service) throws PrintException {
        // 3) Comando ESC p m t1 t2:
        //    0x1B 0x70 0x00 0x3C 0xFF es el pulso estándar para abrir
        byte[] pulseCommand = new byte[]{ 0x1B, 0x70, 0x00, 0x3C, (byte)0xFF };

        DocPrintJob job = service.createPrintJob();
        Doc doc = new SimpleDoc(pulseCommand, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
        // Enviamos, no necesitamos atributos especiales
        job.print(doc, new HashPrintRequestAttributeSet());
    }

    // ——————————————————————————————————————————————
    // Métodos auxiliares para localizar impresoras
    // ——————————————————————————————————————————————

    /**
     * @return la impresora por defecto del sistema, o null si no existe.
     */
    private static PrintService getDefaultPrinter() {
        return PrintServiceLookup.lookupDefaultPrintService();
    }

    /**
     * Busca entre todas las impresoras instaladas una cuyo nombre
     * coincida (ignorando mayúsculas/minúsculas).
     *
     * @param name el nombre buscado
     * @return el PrintService encontrado, o null si no hay coincidencias
     */
    private static PrintService findPrintService(String name) {
        for (PrintService ps : PrintServiceLookup.lookupPrintServices(null, null)) {
            if (ps.getName().equalsIgnoreCase(name)) {
                return ps;
            }
        }
        return null;
    }
}
