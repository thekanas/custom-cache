package by.stolybko.printer;

import java.util.List;

public interface PrinterExecutor {

    void printEntity(Object obj, String path);
    void printTable(List<?> objList, String path);
}
