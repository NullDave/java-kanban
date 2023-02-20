package tools.csv;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// класс для работы с CSV. Сделал отдельно, вдруг пригодится в другом проекте.
public class CSVTool {
    private String delimiter;
    private final File file;
    private final List<CSVTable> current;

    public CSVTool(File file) {
        this.file = file;
        this.current = new ArrayList<>();
        this.delimiter = ",";
    }

    public CSVTool(File file, String delimiter) {
        this.file = file;
        this.current = new ArrayList<>();
        this.delimiter = delimiter;
    }

    // читаем первую таблицу CSV
    public CSVTool read(){
        read(false);
        return this;
    }

    // все таблицы CSV разделенные \n
    public CSVTool readAll(){
        read(true);
        return this;
    }

    // перезаписать файл CSV
    public CSVWriter rewriteCSV() throws IOException {
        return new CSVWriter(file,StandardCharsets.UTF_8,delimiter);
    }


    //  парсим таблицу или несколько таблиц зависимости от флага
    private void read(boolean multiTable){
        try(BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            current.clear();
            do {
                List<CSVRow> rows = new ArrayList<>();
                line = reader.readLine();
                if(line == null) return;
                String[] names = line.split(delimiter);
                while ((line = reader.readLine()) != null && !line.isBlank()) {
                    Map<String, CSVElement> row = new HashMap<>(names.length);
                    String[] elements = line.split(delimiter);
                    for (int i = 0; i < names.length; i++) {
                        if(elements.length > i) {
                            row.put(names[i],new CSVElement(elements[i]));
                        } else {
                            row.put(names[i],new CSVElement(null));
                        }
                    }
                    rows.add(new CSVRow(row));
                }
                current.add(new CSVTable(rows,names));
            } while (line != null && line.isBlank() && multiTable);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CSVTable get(int index){
        return current.get(index);
    }
    public List<CSVTable> getAll(){
        return current;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
