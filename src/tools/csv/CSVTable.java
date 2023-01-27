package tools.csv;

import java.util.List;

public class CSVTable {
    private final List<CSVRow> rows;
    private final String[] columnNames;
    public CSVTable(List<CSVRow> rows, String[] columnNames) {
        this.rows = rows;
        this.columnNames = columnNames;
    }

    public CSVRow getRow(int index){
        return  rows.get(index);
    }

    public List<CSVRow> getRows() {
        return rows;
    }

    public String[] columnNames(){
        return columnNames;
    }

    public int size(){
        return rows.size();
    }


}
