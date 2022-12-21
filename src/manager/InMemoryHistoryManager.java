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
        tasks.add(0,task); // добавляем task в начало списка
        if(MAX_SIZE < tasks.size()) tasks.remove(MAX_SIZE); // если MAX_SIZE достигнут, то удаляем последний task

    }

    @Override
    public List<Task> getHistory() {
        return tasks;
    }

}
