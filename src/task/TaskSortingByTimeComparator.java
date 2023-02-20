package task;

import java.util.Comparator;

// реализация Comparator для сортировки задач по времени, с учётом starTime == null
public class TaskSortingByTimeComparator implements Comparator<Task> {

    @Override
    public int compare(Task task1,Task task2) {
        if(task1.getStartTime() == null) return Integer.compare(task1.getId(), task2.getId()); // если нет startTime, то сортируем по Id
        if(task2.getStartTime() == null) return -1; // task с startTime всегда выше по списку чем task с null
        return  task1.getStartTime().compareTo(task2.getStartTime());
    }

}
