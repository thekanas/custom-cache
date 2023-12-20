package by.stolybko.printer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Printer {

    //private final String path = "output/";
    private final String path = "H:\\java+\\custom-cache\\output\\";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyDDDA");
    private final PrinterExecutorFactory printerExecutorFactory = PrinterExecutorFactory.getInstance();
    private static Printer instance;

    public static Printer getInstance() {
        if (instance == null) {
            instance = new Printer();
        }
        return instance;
    }

    public void print(Object obj, PrinterType type) {
        PrinterExecutor printerExecutor = printerExecutorFactory.getPrinter(type);
        String pathName = path + LocalDateTime.now().format(formatter);

        if (obj instanceof List<?> objects) {
            printerExecutor.printTable(objects, pathName);
        } else {
            printerExecutor.printEntity(obj, pathName);
        }
    }
}
