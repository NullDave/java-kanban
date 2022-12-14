import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DefaultHendler implements Handler{

    private final HashMap<Integer,Task> taskHashMap;
    private final HashMap<Integer,EpicTask> epicTaskHashMap;
    private final HashMap<Integer,SubTask> subTaskHashMap;
    private int indeficator;

    public DefaultHendler() {
        taskHashMap = new HashMap<>();
        epicTaskHashMap = new HashMap<>();
        subTaskHashMap = new HashMap<>();
        indeficator = 1;
    }

    @Override
    public Task getTask(int id) {
        return taskHashMap.get(id);
    }

    @Override
    public EpicTask getEpicTask(int id) {
        return epicTaskHashMap.get(id);
    }

    @Override
    public SubTask getSubTask(int id) {
        return subTaskHashMap.get(id);
    }

    @Override
    public List<Task> getAllTask() {
        return new ArrayList<>(epicTaskHashMap.values());
    }

    @Override
    public List<EpicTask> getAllEpicTask() {
        return new ArrayList<>(epicTaskHashMap.values());
    }

    @Override
    public List<SubTask> getAllSubTask() {
        return new ArrayList<>(subTaskHashMap.values());
    }

    @Override
    public void addTask(Task task) {
        task.setId(getNewId());
        taskHashMap.put(task.getId(),task);
    }

    @Override
    public void addEpicTask(EpicTask epicTask) {
        epicTask.setId(getNewId());
        epicTaskHashMap.put(epicTask.getId(),epicTask);
    }

    @Override
    public void addSubTask(SubTask subTask) {
        subTask.setId(getNewId());
        epicTaskHashMap.get(subTask.getEpicTaskId()).addSubTaskId(subTask.getId());
        subTaskHashMap.put(subTask.getId(),subTask);
    }

    @Override
    public void updateTask(Task task) {
        taskHashMap.put(task.getId(),task);
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        if (subTaskHashMap.size() < 1) {
            for (int id : epicTaskHashMap.get(epicTask.getId()).getListSubTaskId()) {
                epicTask.addSubTaskId(id);
            }
        }
        epicTaskHashMap.put(epicTask.getId(),epicTask);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTaskHashMap.put(subTask.getId(),subTask);
    }

    @Override
    public void clearAllTaskForType(TaskType taskType) {
        switch (taskType) {
            case TASK:
                taskHashMap.clear();
                break;
            case EPIC:
                epicTaskHashMap.clear();
                break;
            case SUBTASK:
                subTaskHashMap.clear();
                break;
        }
    }

    @Override
    public void removeTaskForType(TaskType taskType, int id) {
        switch (taskType) {
            case TASK:
                taskHashMap.remove(id);
                break;
            case EPIC:
                epicTaskHashMap.remove(id);
                break;
            case SUBTASK:
                epicTaskHashMap.get(subTaskHashMap.get(id).getEpicTaskId()).removeSubTaskId(id);
                subTaskHashMap.remove(id);
                break;
        }
    }

    private int getNewId(){
        return indeficator++;
    }
}
