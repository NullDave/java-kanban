package manager;

import task.EpicTask;
import task.SubTask;
import task.Task;

import java.util.List;

public interface TaskManger {

    Task getTask(int id);

    EpicTask getEpicTask(int id);

    SubTask getSubTask(int id);

    List<Task> getAllTask();

    List<EpicTask> getAllEpicTask();

    List<SubTask> getAllSubTask();

    void addTask(Task task);

    void addEpicTask(EpicTask epicTask);

    void addSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateEpicTask(EpicTask epicTask);

    void updateSubTask(SubTask subTask);

    void clearAllTask();

    void clearAllEpicTask();

    void clearAllSubTask();

    void removeTask(int id);

    void removeEpicTask(int id);

    void removeSubTask(int id);
}
