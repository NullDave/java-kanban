package manager;

import task.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static task.StatusType.*;

public class InMemoryTaskManager implements TaskManager {

    private final HashMap<Integer, Task> taskHashMap;
    private final HashMap<Integer, EpicTask> epicTaskHashMap;
    private final HashMap<Integer, SubTask> subTaskHashMap;
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final Set<Task> sortedTasksByTime; // сортированный список задач и подзадач
    private int id;

    public InMemoryTaskManager() {
        taskHashMap = new HashMap<>();
        epicTaskHashMap = new HashMap<>();
        subTaskHashMap = new HashMap<>();
        sortedTasksByTime = new TreeSet<>(new TaskSortingByTimeComparator());
        id = 1;
    }

    @Override
    public Task getTask(int id) {
        Task task = taskHashMap.get(id);

        if (task != null) historyManager.addTask(task);
        return task;
    }

    @Override
    public EpicTask getEpicTask(int id) {
        EpicTask epicTask = epicTaskHashMap.get(id);

        if (epicTask != null) historyManager.addTask(epicTask);
        return epicTask;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = subTaskHashMap.get(id);

        if (subTask != null) historyManager.addTask(subTask);
        return subTask;
    }

    @Override
    public List<Task> getAllTask() {
        return new ArrayList<>(taskHashMap.values());
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
        taskTimeValidator(task);
        task.setId(getNewId());
        taskHashMap.put(task.getId(),task);
        sortedTasksByTime.add(task);
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
        taskTimeValidator(subTask);
        subTask.setId(getNewId());
        epicTaskHashMap.get(subTask.getEpicTaskId()).addSubTaskId(subTask.getId());
        subTaskHashMap.put(subTask.getId(),subTask);
        updateEpicTaskDynamicValue(subTask.getEpicTaskId());
        sortedTasksByTime.add(subTask);
        return subTask.getId();
    }

    @Override
    public void updateTask(Task task) {
        taskTimeValidator(task);
        taskHashMap.put(task.getId(),task);
        sortedTasksByTime.remove(task);
        sortedTasksByTime.add(task);
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        epicTask.setListSubTaskId(epicTaskHashMap.get(epicTask.getId()).getListSubTaskId());
        epicTaskHashMap.put(epicTask.getId(),epicTask);

    }

    @Override
    public void updateSubTask(SubTask subTask) {
        taskTimeValidator(subTask);
        subTaskHashMap.put(subTask.getId(),subTask);
        updateEpicTaskDynamicValue(subTask.getEpicTaskId());
        sortedTasksByTime.remove(subTask);
        sortedTasksByTime.add(subTask);
    }

    @Override
    public void clearAllTask() {
        taskHashMap.clear();
        updateSortedTasksByTime();
    }

    @Override
    public void clearAllEpicTask() {
        epicTaskHashMap.clear();
        subTaskHashMap.clear();
        updateSortedTasksByTime();
    }

    @Override
    public void clearAllSubTask() {
        subTaskHashMap.clear();
        for (EpicTask epicTask : epicTaskHashMap.values()){
           epicTask.getListSubTaskId().clear();
           updateEpicTaskDynamicValue(epicTask.getId());
        }
        updateSortedTasksByTime();
    }

    @Override
    public void removeTask(int id) {
        Task task = taskHashMap.remove(id);
        historyManager.removeTask(id);
        sortedTasksByTime.remove(task);
    }

    @Override
    public void removeEpicTask(int id) {
        epicTaskHashMap.remove(id);
        subTaskHashMap.values().removeIf(subTask -> subTask.getEpicTaskId() == id);
        updateSortedTasksByTime();
        historyManager.removeTask(id);
    }

    @Override
    public void removeSubTask(int id) {
        int epicTaskID = subTaskHashMap.get(id).getEpicTaskId();
        SubTask subTask = subTaskHashMap.remove(id);
        epicTaskHashMap.get(epicTaskID).removeSubTaskId(id);
        updateEpicTaskDynamicValue(epicTaskID);
        historyManager.removeTask(id);
        sortedTasksByTime.remove(subTask);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortedTasksByTime);
    }

    private void updateEpicTaskDynamicValue(int id){
        EpicTask epicTask = epicTaskHashMap.get(id);
        int size = epicTask.getListSubTaskId().size();
        if(size < 1){
            epicTask.setStatus(NEW);
            epicTask.setStartTime(null);
            epicTask.setEndTime(null);
            epicTask.setDuration(0);
        }
        else {
            int i = 0;
            LocalDateTime startTime = LocalDateTime.MAX;
            LocalDateTime endTime = LocalDateTime.MIN;

            for (int subId : epicTask.getListSubTaskId()) {
                SubTask subTask = subTaskHashMap.get(subId);
                switch (subTask.getStatus()) {
                    case NEW:
                        ++i;
                        break;
                    case DONE:
                        --i;
                        break;
                }
                if(subTask.getStartTime() != null){
                if(subTask.getStartTime().isBefore(startTime)){
                    startTime = subTask.getStartTime();
                }
                    if(subTask.getEndTime().isAfter(endTime)){
                        endTime = subTask.getEndTime();
                    }
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
            if (startTime != LocalDateTime.MAX) {
                epicTask.setDuration(Duration.between(startTime, endTime).toMinutes());
                epicTask.setStartTime(startTime);
                epicTask.setEndTime(endTime);
            }
        }
    }


    public void updateSortedTasksByTime(){
        sortedTasksByTime.clear();
        sortedTasksByTime.addAll(taskHashMap.values());
        sortedTasksByTime.addAll(subTaskHashMap.values());
    }

    // проверка на пересечение интервалов если пересекают кидаем ошибку
    private void taskTimeValidator(Task task){
        if (task.getStartTime() == null) return;
        for (Task sortTask : sortedTasksByTime) {
            if(sortTask.getStartTime() == null) return; // список отсортирован если встретиться null, дальнейшая проверка бессмысленна
            if (sortTask.getStartTime().isBefore(task.getEndTime()) && task.getStartTime().isBefore(sortTask.getEndTime())){
               throw new RuntimeException("Ошибка: Задача ("+task.getTitle()+") пересекается с ("+sortTask.getTitle()+") по времени");
            }
        }
    }



    private int getNewId(){
        return id++;
    }


    public void setNewIndeficator(int id){
        this.id = id;
    }

    public HashMap<Integer, Task>getTaskHashMap(){
        return taskHashMap;
    }

    public HashMap<Integer, EpicTask> getEpicTaskHashMap(){
        return epicTaskHashMap;
    }

    public HashMap<Integer, SubTask> getSubTaskHashMap(){
        return subTaskHashMap;
    }

}
