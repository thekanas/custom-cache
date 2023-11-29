package by.stolybko.printer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Printer {

    private final String path = "output/";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyDDDA");
    private final PrinterExecutorFactory printerExecutorFactory = new PrinterExecutorFactory();


    public void print(Object obj, PrinterType type) {
        PrinterExecutor printerExecutor = printerExecutorFactory.getPrinter(type);
        String pathName = path + LocalDateTime.now().format(formatter);

        if(obj instanceof List<?> objects) {
            printerExecutor.printTable(objects, pathName);
        } else {
            printerExecutor.printEntity(obj, pathName);
        }
    }
}
