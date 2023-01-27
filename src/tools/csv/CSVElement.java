package tools.csv;

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

    public boolean isNull(){
        return data.isEmpty() || data.isBlank();
    }


}
