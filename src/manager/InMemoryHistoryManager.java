package manager;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> tasks;
    private static final int MAX_SIZE = 10;

    public InMemoryHistoryManager() {
        tasks = new ArrayList<>();
    }

    @Override
    public void addTask(Task task) {
        if (tasks.size()>=MAX_SIZE) tasks.remove(0);
        tasks.add(task);

    }

    @Override
    public List<Task> getHistory() {
        return tasks;
    }

}
