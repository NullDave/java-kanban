package task;

public class SubTask extends Task {
    private int epicTaskId; // индификатор эпика в рамках которого существует данная подзадача

    public SubTask(int epicTaskId, String title, String description) {
        super(title, description);
        this.epicTaskId = epicTaskId;
    }

    public SubTask(int epicTaskId,int id, String title, String description, StatusType status) {
        super(id, title, description,status);
        this.epicTaskId = epicTaskId;
    }

    public int getEpicTaskId() {
        return epicTaskId;
    }

    public void setEpicTaskId(int epicTaskId) {
        this.epicTaskId = epicTaskId;
    }
}
