package ru.omstu.fitprogwork;

public class Main {
    public static void main(String[] args) {
        String jsonFile = "sample.json";
        String xmlFile = "sample.xml";

        DataExtractor jsonExtractor = ExtractorFactory.getExtractor(jsonFile);
        DataExtractor xmlExtractor = ExtractorFactory.getExtractor(xmlFile);

        System.out.println("JSON /name: " + jsonExtractor.extractValue(jsonFile, "/name"));
        System.out.println("JSON /relation/[1]/name: " + jsonExtractor.extractValue(jsonFile, "/relation/[1]/name"));

        System.out.println("XML /name: " + xmlExtractor.extractValue(xmlFile, "/name"));
        System.out.println("XML /relation/[1]/name: " + xmlExtractor.extractValue(xmlFile, "/relation/[1]/name"));
    }
}