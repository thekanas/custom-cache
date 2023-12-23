package by.stolybko.printer;

import by.stolybko.printer.impl.PDFPrinter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PrinterExecutorFactory {

    private static PrinterExecutorFactory instance;

    public static PrinterExecutorFactory getInstance() {
        if (instance == null) {
            instance = new PrinterExecutorFactory();
        }
        return instance;
    }


    public PrinterExecutor getPrinter(PrinterType type) {
        switch (type) {
            case PDF -> {
                return new PDFPrinter();
            }
            case TXT -> {
                // ToDo
                return null;
            }
            default -> {
                return null;
            }
        }
    }
}
