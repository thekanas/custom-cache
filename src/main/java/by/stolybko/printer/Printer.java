package by.stolybko.printer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Printer {

    private final String path = "output/";
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
        String pathName = LocalDateTime.now().format(formatter);

        if (obj instanceof List<?> objects) {
            pathName = path + pathName + "-" + objects.get(0).getClass().getSimpleName() + "-table" + type.getExtension();
            printerExecutor.printTable(objects, pathName);
        } else {
            pathName = path + pathName + "-" + obj.getClass().getSimpleName() + type.getExtension();
            printerExecutor.printEntity(obj, pathName);
        }
    }

    public byte[] getDocAsBytes(Object obj, PrinterType type) {
        PrinterExecutor printerExecutor = printerExecutorFactory.getPrinter(type);

        if (obj instanceof List<?> objects) {
            return printerExecutor.getTableAsBytes(objects);
        } else {
            return printerExecutor.getEntityAsBytes(obj);
        }
    }
}
