import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private final Handler handler;

    public TaskManager() {
        handler = new DefaultHendler();
    }

    public TaskManager(Handler handler) {
        this.handler = handler;
    }

    public Task getTask(int id) {
        return handler.getTask(id);
    }

    public EpicTask getEpicTask(int id) {
        return updateEpicTaskStatus(handler.getEpicTask(id));
    }

    public SubTask getSubTask(int id) {
        return handler.getSubTask(id);
    }

    public List<Task> getAllTask() {
        return handler.getAllTask();
    }

    public List<EpicTask> getAllEpicTask() {
        ArrayList<EpicTask> listForUpdate = new ArrayList<>();

        for (EpicTask epicTask: handler.getAllEpicTask()){
          listForUpdate.add(updateEpicTaskStatus(epicTask));
        }
        return listForUpdate;
    }

    public List<SubTask> getAllSubTask() {
        return handler.getAllSubTask();
    }

    public void addTask(Task task) {
        handler.addTask(task);
    }

    public void addEpicTask(EpicTask epicTask) {
        handler.addEpicTask(epicTask);
    }

    public void addSubTask(SubTask subTask) {
        handler.addSubTask(subTask);
    }

    public void updateTask(Task task) {
        handler.updateTask(task);
    }

    public void updateEpicTask(EpicTask epicTask) {
        handler.updateEpicTask(epicTask);
    }

    public void updateSubTask(SubTask subTask) {
        handler.updateSubTask(subTask);
    }

    public void clearAllTask() {
        handler.clearAllTaskForType(TaskType.TASK);
    }

    public void clearAllEpicTask() {
        handler.clearAllTaskForType(TaskType.EPIC);
    }

    public void clearAllSubTask() {
        handler.clearAllTaskForType(TaskType.SUBTASK);
    }

    public void removeTask(int id) {
      handler.removeTaskForType(TaskType.TASK,id);
    }

    public void removeEpicTask(int id) {

        handler.removeTaskForType(TaskType.EPIC,id);
    }

    public void removeSubTask(int id) {
        handler.removeTaskForType(TaskType.SUBTASK,id);
    }

    private EpicTask updateEpicTaskStatus(EpicTask epicTask){
        int size = epicTask.getListSubTaskId().size();

        if(size < 1){
            epicTask.setStatus(StatusType.NEW);
        }
        else {
            int i = 0;
            for (int id : epicTask.getListSubTaskId()) {
                switch (handler.getSubTask(id).getStatus()) {
                    case NEW:
                        ++i;
                        break;
                    case DONE:
                        --i;
                        break;
                }
            }
            if(size == i){
                epicTask.setStatus(StatusType.NEW);
            }
            else if (size == (i * -1)){
                epicTask.setStatus(StatusType.DONE);
            }else {
                epicTask.setStatus(StatusType.IN_PROGRESS);
            }
        }
        return epicTask;
    }


}
