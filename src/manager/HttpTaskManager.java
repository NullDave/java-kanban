package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import task.EpicTask;
import task.SubTask;
import task.Task;
import task.TaskType;
import web.KVTaskClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpTaskManager extends FileBackedTasksManager {
    private KVTaskClient client;
    private Gson gson;
    private final static String KEY = "key";

    public HttpTaskManager(URL url) {
        super();
        try {
            client = new KVTaskClient(url);
            gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .serializeNulls()
                    .create();
            loud();
        } catch (IOException | URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        DataWrapperTask wrapperTask = new DataWrapperTask();
        wrapperTask.setTasks(getAllTask());
        wrapperTask.setSubTasks(getAllSubTask());
        wrapperTask.setEpicTasks(getAllEpicTask());
        Map<Integer,TaskType> history = new HashMap<>();
        for (Task task : getHistory()){
            if(task instanceof EpicTask)
                history.put(task.getId(),TaskType.EPICTASK);
            else if (task instanceof SubTask)
                history.put(task.getId(),TaskType.SUBTASK);
            else
                history.put(task.getId(),TaskType.TASK);
        }
        wrapperTask.setHistory(history);
        try {
            client.put(KEY,new Gson().toJson(wrapperTask));
        } catch (IOException | URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void loud() {
        try {
            DataWrapperTask wrapperTask =  gson.fromJson(client.load(KEY),DataWrapperTask.class);
            if(wrapperTask == null) return;
            final HashMap<Integer, Task> taskHashMap = getTaskHashMap();
            final HashMap<Integer, EpicTask> epicTaskHashMap = getEpicTaskHashMap();
            final HashMap<Integer, SubTask> subTaskHashMap = getSubTaskHashMap();
            int id = 0;
            for (Task task : wrapperTask.getTasks()){
                taskHashMap.put(task.getId(),task);
                if(id < task.getId()) id = task.getId();
            }
            for (EpicTask epicTask : wrapperTask.getEpicTasks()){
                epicTaskHashMap.put(epicTask.getId(),epicTask);
                if(id < epicTask.getId()) id = epicTask.getId();
            }
            for (SubTask subTask : wrapperTask.getSubTasks()){
                subTaskHashMap.put(subTask.getId(),subTask);
                if(id < subTask.getId()) id = subTask.getId();
            }
            setNewIndeficator(++id);
            updateSortedTasksByTime();

            for (Map.Entry<Integer,TaskType> task: wrapperTask.getHistory().entrySet()){
                switch (task.getValue()){
                    case TASK:
                        getTask(task.getKey());
                        break;
                    case EPICTASK:
                        getEpicTask(task.getKey());
                        break;
                    case SUBTASK:
                        getSubTask(task.getKey());
                        break;
                }
            }
        } catch (IOException | URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // оболочка для хранения задач
    private static class DataWrapperTask{
        private List<Task> tasks;
        private List<SubTask> subTasks;
        private List<EpicTask> epicTasks;
        private Map<Integer, TaskType> history;

        public List<Task> getTasks() {
            return tasks;
        }

        public void setTasks(List<Task> tasks) {
            this.tasks = tasks;
        }

        public List<SubTask> getSubTasks() {
            return subTasks;
        }

        public void setSubTasks(List<SubTask> subTasks) {
            this.subTasks = subTasks;
        }

        public List<EpicTask> getEpicTasks() {
            return epicTasks;
        }

        public void setEpicTasks(List<EpicTask> epicTasks) {
            this.epicTasks = epicTasks;
        }

        public Map<Integer, TaskType> getHistory() {
            return history;
        }

        public void setHistory(Map<Integer, TaskType> history) {
            this.history = history;
        }
    }
}