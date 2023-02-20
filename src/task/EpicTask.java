package task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {
    private ArrayList<Integer> listSubTaskId; // идентификаторы подзадач
    private LocalDateTime endTime;

    public EpicTask( String title, String description) {
        super(title, description);
        listSubTaskId = new ArrayList<>();
    }

    public EpicTask(int id, String title,String description, StatusType status,long duration, LocalDateTime startTime) {
        super(id, title, description, status,duration, startTime);
        listSubTaskId = new ArrayList<>();
    }
    public List<Integer> getListSubTaskId() {
        return listSubTaskId;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime(){
            return endTime;
    }

    public void setListSubTaskId(List<Integer> listSubTaskId) {
        this.listSubTaskId =  new ArrayList<>(listSubTaskId);
    }

    public void addSubTaskId(int id) {
        listSubTaskId.add(id);
    }

    public void removeSubTaskId(int id) {
        listSubTaskId.remove((Integer) id);
    }

}
