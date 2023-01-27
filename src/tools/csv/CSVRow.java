package tools.csv;

import java.util.Map;

public class CSVRow {
    private final Map<String, CSVElement> elements;

    public CSVRow(Map<String, CSVElement> elements) {
        this.elements = elements;
    }

    public CSVElement get(String key){
        return  elements.get(key);
    }

}
