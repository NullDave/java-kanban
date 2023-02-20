package manager;

import task.*;
import tools.csv.CSVRow;
import tools.csv.CSVTable;
import tools.csv.CSVTool;
import tools.csv.CSVWriter;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager{

    private final CSVTool csvTool;
    private final static String[] COLUMN_NAMES_MANAGER = {"id","type","title","status","description","duration","startTime","epic"};
    private final static String[] COLUMN_NAMES_HISTORY = {"id","type"};
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm");


    public FileBackedTasksManager(File file) {
        super();
        this.csvTool = new CSVTool(file);
    }

    private void save() {
        try(CSVWriter writer = csvTool.rewriteCSV()) {
            writer.addTable(COLUMN_NAMES_MANAGER);
            for (Task task : getAllTask())
                writer.addRow(convertTaskManager(task,TaskType.TASK));
            for (EpicTask task : getAllEpicTask())
                writer.addRow(convertTaskManager(task,TaskType.EPICTASK));
            for (SubTask task : getAllSubTask())
                writer.addRow(convertTaskManager(task,TaskType.SUBTASK));

            writer.addTable(COLUMN_NAMES_HISTORY);
            for (Task task : getHistory())
                writer.addRow(convertTaskMHistory(task));

        } catch (IOException e) {
            e.printStackTrace();
            throw new ManagerSaveException();
        }



    }

    private String[] convertTaskMHistory(Task task) {
        String[] element = new String[2];
        element[0] = Integer.toString(task.getId());
        if(task instanceof EpicTask)
            element[1] = TaskType.EPICTASK.name();
        else if(task instanceof SubTask)
            element[1] = TaskType.SUBTASK.name();
        else
            element[1] = TaskType.TASK.name();
        return element;
    }

    private String[] convertTaskManager(Task task,TaskType type) {
        String[] element = new String[8];
        element[0] = Integer.toString(task.getId());
        element[1] = type.name();
        element[2] = task.getTitle();
        element[3] = task.getStatus().name();
        element[4] = task.getDescription();
        element[5] = Long.toString(task.getDuration());
        if(task.getStartTime() != null) {
            element[6] = task.getStartTime().format(formatter);
        }else {
            element[6] = "";
        }

        if(type == TaskType.SUBTASK) {
            element[7] = Integer.toString(((SubTask) task).getEpicTaskId());
        }else {
            element[7] = "";
        }
        return element;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
         FileBackedTasksManager manager = new FileBackedTasksManager(file);

         List<CSVTable> tables = new CSVTool(file).readAll().getAll();
         if (tables.isEmpty()) return manager;
         loadManager(manager,tables.get(0).getRows());
         loadHistory(manager,tables.get(1).getRows());
         return manager;
     }

     // загружаем историю.
    private static void loadHistory(FileBackedTasksManager manager, List<CSVRow> rows) {
        for (CSVRow row : rows) {
            TaskType type = TaskType.valueOf(row.get("type").getString());
            switch (type) {
                case TASK:
                    manager.getTask(row.get("id").getInt());
                    break;
                case EPICTASK:
                    manager.getEpicTask(row.get("id").getInt());
                    break;
                case SUBTASK:
                    manager.getSubTask(row.get("id").getInt());
                    break;
            }
        }
    }

     // загружаем менеджер.
     private static void loadManager(FileBackedTasksManager manager, List<CSVRow> rows){
         final HashMap<Integer, Task> taskHashMap = manager.getTaskHashMap();
         final HashMap<Integer, EpicTask> epicTaskHashMap = manager.getEpicTaskHashMap();
         final HashMap<Integer, SubTask> subTaskHashMap = manager.getSubTaskHashMap();

         int maxIndeficator = 0;
         for (CSVRow row : rows) {
             TaskType type = TaskType.valueOf(row.get("type").getString());
             StatusType status = StatusType.valueOf(row.get("status").getString());
             int id = row.get("id").getInt();
             if(id > maxIndeficator)maxIndeficator = id;

             LocalDateTime startTime = row.get("startTime").isNotNull() ? row.get("startTime").getDataTime(formatter) : null;
             switch (type) {
                 case TASK:
                     Task task = new Task(id,
                                         row.get("title").getString(),
                                         row.get("description").getString(),
                                         status,
                                         row.get("duration").getLong(),
                                         startTime);
                     taskHashMap.put(id, task);
                     break;
                 case EPICTASK:
                     EpicTask epicTask = new EpicTask(id,
                                                     row.get("title").getString(),
                                                     row.get("description").getString(),
                                                     status,
                                                     row.get("duration").getLong(),
                                                     startTime);
                     epicTaskHashMap.put(id,epicTask);
                     break;
                 case SUBTASK:
                     int apicID = row.get("epic").getInt();
                     SubTask subTask = new SubTask(
                             apicID,
                             id,
                             row.get("title").getString(),
                             row.get("description").getString(),
                             status,
                             row.get("duration").getLong(),
                             startTime);
                    subTaskHashMap.put(id, subTask);
                    epicTaskHashMap.get(apicID).addSubTaskId(id);
                     break;

             }
         }

         manager.setNewIndeficator(maxIndeficator);
         manager.updateSortedTasksByTime();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public EpicTask getEpicTask(int id) {
        EpicTask epicTask = super.getEpicTask(id);
        save();
        return epicTask;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = super.getSubTask(id);
        save();
        return subTask;
    }

    @Override
    public int addTask(Task task) {
        super.addTask(task);
        save();
        return task.getId();
    }

    @Override
    public int addEpicTask(EpicTask epicTask) {
        super.addEpicTask(epicTask);
        save();
        return epicTask.getId();
    }

    @Override
    public int addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
        return subTask.getId();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeEpicTask(int id) {
        super.removeEpicTask(id);
        save();
    }

    @Override
    public void removeSubTask(int id) {
        super.removeSubTask(id);
        save();
    }

    @Override
    public void clearAllTask() {
        super.clearAllTask();
        save();
    }

    @Override
    public void clearAllEpicTask() {
        super.clearAllEpicTask();
        save();
    }

    @Override
    public void clearAllSubTask() {
        super.clearAllSubTask();
        save();
    }
}
