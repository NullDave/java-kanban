package task;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private int id;
    private String title;
    private String description;
    private StatusType status;
    private long duration; //длительность в минутах
    private LocalDateTime startTime;

    // новая задача
    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        status = StatusType.NEW;
        duration = 0;
    }

    // новая задача с заданной длительностью и датой
    public Task(String title, String description, long duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        status = StatusType.NEW;
        this.duration = duration;
        this.startTime = startTime;
    }


    // обновленная задача
    public Task(int id, String title, String description, StatusType status,long duration, LocalDateTime startTime ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime(){
        if(startTime != null)
            return startTime.plusMinutes(duration);
        return null;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + getEndTime() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && duration == task.duration
                && Objects.equals(title, task.title)
                && Objects.equals(description, task.description)
                && status == task.status
                && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, status, duration, startTime);
    }
}
