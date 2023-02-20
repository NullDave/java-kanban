package tools.csv;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CSVElement {
    private final String data;

    public CSVElement(String data) {
        this.data = data;
    }

    public String getString() {
        return data;
    }

    public int getInt(){
        return Integer.parseInt(data);
    }

    public long getLong(){
        return Long.parseLong(data);
    }

    public LocalDateTime getDataTime(DateTimeFormatter formatter){
        return LocalDateTime.parse(data,formatter);
    }

    public boolean isNull(){
        return data == null || data.isBlank();
    }

    public boolean isNotNull(){
        if (data != null) return !data.isBlank();
        return false;
    }

}
