package manager;

import task.EpicTask;
import task.SubTask;
import task.Task;

import java.util.List;

public interface TaskManager {

    Task getTask(int id);

    EpicTask getEpicTask(int id);

    SubTask getSubTask(int id);

    List<Task> getAllTask();

    List<EpicTask> getAllEpicTask();

    List<SubTask> getAllSubTask();

    int addTask(Task task);

    int addEpicTask(EpicTask epicTask);

    int addSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateEpicTask(EpicTask epicTask);

    void updateSubTask(SubTask subTask);

    void clearAllTask();

    void clearAllEpicTask();

    void clearAllSubTask();

    void removeTask(int id);

    void removeEpicTask(int id);

    void removeSubTask(int id);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}
