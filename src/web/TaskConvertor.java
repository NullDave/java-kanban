package web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import task.EpicTask;
import task.SubTask;
import task.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class TaskConvertor {
   private final Gson gson;

    public TaskConvertor() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    public String toJson(Object obj){
        return gson.toJson(obj);
    }

    public Task getTask(String json){
        return gson.fromJson(json,Task.class);
    }

    public SubTask getSubTask(String json){
        return gson.fromJson(json,SubTask.class);
    }

    public EpicTask getEpicTask(String json){
        return gson.fromJson(json,EpicTask.class);
    }

    private static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm");

        @Override
        public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
            if(localDateTime != null)
                jsonWriter.value(localDateTime.format(formatter));
            else
                jsonWriter.value("");
        }

        @Override
        public LocalDateTime read(JsonReader jsonReader) throws IOException {
            if(jsonReader.nextString().isBlank())
                return null;
            else
                return LocalDateTime.parse(jsonReader.nextString(),formatter);
        }
    }
}
