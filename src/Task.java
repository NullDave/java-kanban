public class Task {
    private int id;
    private String title;
    private String description;
    private StatusType status;

    // новая задача
    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        status = StatusType.NEW;
    }

    // обновленная задача
    public Task(int id, String title, String description, StatusType status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
