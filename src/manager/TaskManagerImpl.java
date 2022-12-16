package manager;

import task.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static task.StatusType.*;

public class TaskManagerImpl implements TaskManger {

    private final HashMap<Integer, Task> taskHashMap;
    private final HashMap<Integer, EpicTask> epicTaskHashMap;
    private final HashMap<Integer, SubTask> subTaskHashMap;
    private int indeficator;

    public TaskManagerImpl() {
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
    public int addTask(Task task) {
        task.setId(getNewId());
        taskHashMap.put(task.getId(),task);
        return task.getId();
    }

    @Override
    public int addEpicTask(EpicTask epicTask) {
        epicTask.setId(getNewId());
        epicTaskHashMap.put(epicTask.getId(),epicTask);
        return epicTask.getId();
    }

    @Override
    public int addSubTask(SubTask subTask) {
        subTask.setId(getNewId());
        epicTaskHashMap.get(subTask.getEpicTaskId()).addSubTaskId(subTask.getId());
        subTaskHashMap.put(subTask.getId(),subTask);
        updateEpicTaskStatus(subTask.getEpicTaskId());
        return subTask.getId();
    }

    @Override
    public void updateTask(Task task) {
        taskHashMap.put(task.getId(),task);
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        epicTask.setListSubTaskId(epicTaskHashMap.get(epicTask.getId()).getListSubTaskId());
        epicTaskHashMap.put(epicTask.getId(),epicTask);

    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTaskHashMap.put(subTask.getId(),subTask);
        updateEpicTaskStatus(subTask.getEpicTaskId());
    }

    @Override
    public void clearAllTask() {
        taskHashMap.clear();
    }

    @Override
    public void clearAllEpicTask() {
        epicTaskHashMap.clear();
        subTaskHashMap.clear();
    }

    @Override
    public void clearAllSubTask() {
        subTaskHashMap.clear();
        for (EpicTask epicTask : epicTaskHashMap.values()){
           epicTask.getListSubTaskId().clear();
           updateEpicTaskStatus(epicTask.getId());
        }
    }

    @Override
    public void removeTask(int id) {
        taskHashMap.remove(id);
    }

    // Извените x2. Буду внимательней(
    @Override
    public void removeEpicTask(int id) {
        epicTaskHashMap.remove(id);
        subTaskHashMap.values().removeIf(subTask -> subTask.getEpicTaskId() == id);
    }

    @Override
    public void removeSubTask(int id) {
        int epicTaskID = subTaskHashMap.get(id).getEpicTaskId();

        subTaskHashMap.remove(id);
        epicTaskHashMap.get(epicTaskID).removeSubTaskId(id);
        updateEpicTaskStatus(epicTaskID);
    }

    private void updateEpicTaskStatus(int id){
        EpicTask epicTask = epicTaskHashMap.get(id);
        int size = epicTask.getListSubTaskId().size();

        if(size < 1){
            epicTask.setStatus(NEW);
        }
        else {
            int i = 0;
            for (int subId : epicTask.getListSubTaskId()) {
                switch (subTaskHashMap.get(subId).getStatus()) {
                    case NEW:
                        ++i;
                        break;
                    case DONE:
                        --i;
                        break;
                }
            }
            if(size == i){
                epicTask.setStatus(NEW);
            }
            else if (size == -i){
                epicTask.setStatus(DONE);
            }else {
                epicTask.setStatus(IN_PROGRESS);
            }
        }
    }

    private int getNewId(){
        return indeficator++;
    }
}
