/*
Интефейс обработчика.
Как я понял ТЗ дальше возможно будем хранить задачи где то БД. обработчик позволит легко добавить работу с БД и т.п.
*/

import java.util.List;

public interface Handler {

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

    void clearAllTaskForType(TaskType taskType);

    void removeTaskForType(TaskType taskType, int id);
}
