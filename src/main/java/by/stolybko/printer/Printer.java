package by.stolybko.printer;

import by.stolybko.util.PropertiesManager;

import java.io.IOException;
import java.util.List;

public abstract class Printer {

    private final String path = PropertiesManager.get("pathToPrint");



    public abstract void printEntity(Object obj) throws IOException, IllegalAccessException;
    public abstract void printTable(List<?> objectList);



}
