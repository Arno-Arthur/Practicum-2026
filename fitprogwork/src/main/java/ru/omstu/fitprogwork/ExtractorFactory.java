package ru.omstu.fitprogwork;

public class ExtractorFactory {
    public static DataExtractor getExtractor(String filePath) {
        if (filePath.toLowerCase().endsWith(".json")) {
            return new JsonDataExtractor();
        } else if (filePath.toLowerCase().endsWith(".xml")) {
            return new XmlDataExtractor();
        } else {
            throw new IllegalArgumentException("Поддерживаются только .json и .xml файлы");
        }
    }
}