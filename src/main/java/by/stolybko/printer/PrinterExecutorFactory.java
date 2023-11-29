package by.stolybko.printer;

import by.stolybko.printer.impl.PDFPrinter;

public class PrinterExecutorFactory {

    public PrinterExecutor getPrinter(PrinterType type) {
        switch (type) {
            case PDF -> {
                return new PDFPrinter();
            }
            case TXT -> {
                return null;
            }
            default -> {
                return null;
            }
        }
    }
}
