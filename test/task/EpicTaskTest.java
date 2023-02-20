package task;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import task.EpicTask;
import task.StatusType;
import task.SubTask;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTaskTest {

    @Test
    public void statusOfEmptyEpicTask(){
        TaskManager manager = new InMemoryTaskManager();
        int id = manager.addEpicTask(new EpicTask("Эпик задача","Пустой эпик для проверки статуса"));
        EpicTask epicTask = manager.getEpicTask(id);
        assertEquals(StatusType.NEW,epicTask.getStatus());
    }

    @Test
    public void statusEpicTaskAllSubtasksWithTheStatusNew(){
        TaskManager manager = new InMemoryTaskManager();
        int id = manager.addEpicTask(new EpicTask("Эпик задача","эпик для проверки статуса с подзадачами"));
        manager.addSubTask(new SubTask(id,"Подзадача 1","Подзадача №1 статус новый"));
        manager.addSubTask(new SubTask(id,"Подзадача 2","Подзадача №2 статус новый"));
        EpicTask epicTask = manager.getEpicTask(id);
        assertEquals(StatusType.NEW,epicTask.getStatus());
    }

    @Test
    public void statusEpicTaskAllSubtasksWithTheStatusDone(){
        TaskManager manager = new InMemoryTaskManager();
        int id = manager.addEpicTask(new EpicTask("Эпик задача","эпик для проверки статуса с подзадачами"));
        int idSub1 = manager.addSubTask(new SubTask(id,"Подзадача 1","Подзадача №1"));
        int idSub2 = manager.addSubTask(new SubTask(id,"Подзадача 2","Подзадача №2"));
        SubTask subTask1 = manager.getSubTask(idSub1);
        SubTask subTask2 = manager.getSubTask(idSub2);
        subTask1.setStatus(StatusType.DONE);
        subTask2.setStatus(StatusType.DONE);
        manager.updateSubTask(subTask1);
        manager.updateSubTask(subTask2);
        EpicTask epicTask = manager.getEpicTask(id);
        assertEquals(StatusType.DONE,epicTask.getStatus());
    }

    @Test
    public void statusEpicTaskAllSubtasksWithTheStatusDoneAndNew(){
        TaskManager manager = new InMemoryTaskManager();
        int id = manager.addEpicTask(new EpicTask("Эпик задача","эпик для проверки статуса с подзадачами"));
        int idSub1 = manager.addSubTask(new SubTask(id,"Подзадача 1","Подзадача №1"));
        int idSub2 = manager.addSubTask(new SubTask(id,"Подзадача 2","Подзадача №2"));
        SubTask subTask1 = manager.getSubTask(idSub1);
        SubTask subTask2 = manager.getSubTask(idSub2);
        subTask1.setStatus(StatusType.DONE);
        subTask2.setStatus(StatusType.NEW);
        manager.updateSubTask(subTask1);
        manager.updateSubTask(subTask2);
        EpicTask epicTask = manager.getEpicTask(id);
        assertEquals(StatusType.IN_PROGRESS,epicTask.getStatus());
    }

    @Test
    public void statusEpicTaskAllSubtasksWithTheStatusInProgress(){
        TaskManager manager = new InMemoryTaskManager();
        int id = manager.addEpicTask(new EpicTask("Эпик задача","эпик для проверки статуса с подзадачами"));
        int idSub1 = manager.addSubTask(new SubTask(id,"Подзадача 1","Подзадача №1"));
        int idSub2 = manager.addSubTask(new SubTask(id,"Подзадача 2","Подзадача №2"));
        SubTask subTask1 = manager.getSubTask(idSub1);
        SubTask subTask2 = manager.getSubTask(idSub2);
        subTask1.setStatus(StatusType.IN_PROGRESS);
        subTask2.setStatus(StatusType.IN_PROGRESS);
        manager.updateSubTask(subTask1);
        manager.updateSubTask(subTask2);
        EpicTask epicTask = manager.getEpicTask(id);
        assertEquals(StatusType.IN_PROGRESS,epicTask.getStatus());
    }

    @Test
    public void startTimeEpicTaskAllSubtasksWithStartTimeAndDuration(){
        TaskManager manager = new InMemoryTaskManager();
        int id = manager.addEpicTask(new EpicTask("Эпик задача","эпик с подзадачами для поверки времени"));
        LocalDateTime dateTime = LocalDateTime.of(2023,2,19,1,0);
        manager.addSubTask(new SubTask(id,"Подзадача 1","Подзадача №1 длительность 30 минут",30,dateTime));
        manager.addSubTask(new SubTask(id,"Подзадача 2","Подзадача №2 длительность 20 минут",20,dateTime.plusMinutes(40)));
        EpicTask epicTask = manager.getEpicTask(id);
        assertEquals(dateTime,epicTask.getStartTime());
    }

    @Test
    public void endTimeEpicTaskAllSubtasksWithStartTimeAndDuration(){
        TaskManager manager = new InMemoryTaskManager();
        int id = manager.addEpicTask(new EpicTask("Эпик задача","эпик с подзадачами для поверки времени"));
        LocalDateTime dateTime = LocalDateTime.of(2023,2,19,1,0);
        manager.addSubTask(new SubTask(id,"Подзадача 1","Подзадача №1 длительность 30 минут",30,dateTime));
        manager.addSubTask(new SubTask(id,"Подзадача 2","Подзадача №2 длительность 20 минут",20,dateTime.plusMinutes(40)));
        EpicTask epicTask = manager.getEpicTask(id);
        assertEquals(dateTime.plusMinutes(40+20),epicTask.getEndTime());
    }

    @Test
    public void durationEpicTaskAllSubtasksWithStartTimeAndDuration(){
        TaskManager manager = new InMemoryTaskManager();
        int id = manager.addEpicTask(new EpicTask("Эпик задача","эпик с подзадачами для поверки времени"));
        LocalDateTime dateTime = LocalDateTime.of(2023,2,19,1,0);
        manager.addSubTask(new SubTask(id,"Подзадача 1","Подзадача №1 длительность 30 минут",30,dateTime));
        manager.addSubTask(new SubTask(id,"Подзадача 2","Подзадача №2 длительность 20 минут",20,dateTime.plusMinutes(40)));
        EpicTask epicTask = manager.getEpicTask(id);
        assertEquals(60,epicTask.getDuration());
    }
}