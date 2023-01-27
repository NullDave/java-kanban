package tools.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;

public class CSVWriter extends FileWriter {
    private final String delimiter;
    private boolean isNewTable;

    public CSVWriter(File file, Charset charset, String delimiter) throws IOException {
        super(file, charset, false);
        this.delimiter = delimiter;
        this.isNewTable = true;
    }
    public void addRow(String[] rows) throws IOException {
        write(String.join(delimiter,rows));
        next();
    }

    public void addTable(String[] columnNames) throws IOException {
        if(isNewTable)
            isNewTable = false;
        else
            next();
        addRow(columnNames);
    }

    private void next() throws IOException {
        write("\n");
    }
}
