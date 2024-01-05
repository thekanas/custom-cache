package by.stolybko.printer;

import lombok.Getter;

@Getter
public enum PrinterType {
    PDF(".pdf"), TXT(".txt");

    private final String extension;

    PrinterType(String extension) {
        this.extension = extension;
    }

}
