package manager;

import org.junit.jupiter.api.Test;
import task.StatusType;
import task.Task;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {


    @Test
    public void addEmptyAndClone(){
        HistoryManager historyManager = new InMemoryHistoryManager();
        assertEquals(0,historyManager.getHistory().size());
        Task task1 = new Task(1,"задач 1","задача", StatusType.NEW,0,null);
        Task task2 = new Task(2,"задач 2","задача", StatusType.NEW,0,null);
        Task task3 = new Task(3,"задач 3","задача", StatusType.NEW,0,null);
        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.addTask(task3);
        assertEquals(3,historyManager.getHistory().size());
        historyManager.addTask(task1);
        assertEquals(3,historyManager.getHistory().size());

    }
    @Test
    public void remove(){
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task(1,"задач 1","задача", StatusType.NEW,0,null);
        Task task2 = new Task(2,"задач 2","задача", StatusType.NEW,0,null);
        Task task3 = new Task(3,"задач 3","задача", StatusType.NEW,0,null);
        Task task4 = new Task(4,"задач 4","задача", StatusType.NEW,0,null);
        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.addTask(task3);
        historyManager.addTask(task4);
        historyManager.removeTask(1);
        assertEquals(3,historyManager.getHistory().size());
        historyManager.removeTask(4);
        assertEquals(2,historyManager.getHistory().size());
    }
}