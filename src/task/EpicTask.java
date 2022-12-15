package task;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {
    private ArrayList<Integer> listSubTaskId; // индификаторы подзадач

    public EpicTask( String title, String description) {
        super(title, description);
        listSubTaskId = new ArrayList<>();
    }

    public EpicTask(int id, String title, String description) {
        super(id, title, description, StatusType.NEW);
        listSubTaskId = new ArrayList<>();
    }

    public List<Integer> getListSubTaskId() {
        return listSubTaskId;
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
