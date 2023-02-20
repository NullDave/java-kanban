package manager;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.EpicTask;
import task.StatusType;
import task.SubTask;
import task.Task;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

 abstract class TaskManagerTest<T extends TaskManager> {
  public T manager;

     public abstract T getTaskManager();

  @BeforeEach
  public void resetTaskManager(){ // обновляем менеджер после каждого теста
      manager = getTaskManager();
  }


  // для удобства и меньше повторяющего кода
  public int createTask(){
      return manager.addTask(new Task("Задача","задача для проверки"));
  }

  public int createEpicTask(){
      return manager.addEpicTask(new EpicTask("Эпик задача","эпик для проверки"));
  }

  public int createSubTask(int idEpic){
      return manager.addSubTask(new SubTask(idEpic,"Подзадача эпика №"+idEpic,"Подзадача"));
  }

  @Test
  public void subTasksCheckingAvailabilityEpicTask(){
      int id = createEpicTask();
      int idSub1 = createSubTask(id);
      int idSub2 = createSubTask(id);
      SubTask subTask1 = manager.getSubTask(idSub1);
      SubTask subTask2 = manager.getSubTask(idSub2);
      EpicTask epicTaskForSubTask1 = manager.getEpicTask(subTask1.getEpicTaskId());
      EpicTask epicTaskForSubTask2 = manager.getEpicTask(subTask2.getEpicTaskId());
      assertNotNull(epicTaskForSubTask1);
      assertNotNull(epicTaskForSubTask2);
      assertEquals(epicTaskForSubTask1,epicTaskForSubTask2);
  }

     @Test
     public void checkingAvailabilityEpicTaskStatus(){
         int id = createEpicTask();
         int idSub1 = createSubTask(id);
         int idSub2 = createSubTask(id);
         EpicTask epicTask = manager.getEpicTask(id);
         assertEquals(StatusType.NEW,epicTask.getStatus());
         SubTask subTask1 = manager.getSubTask(idSub1);
         SubTask subTask2 = manager.getSubTask(idSub2);
         subTask1.setStatus(StatusType.DONE);
         manager.updateSubTask(subTask1);
         assertEquals(StatusType.IN_PROGRESS,epicTask.getStatus());
         subTask2.setStatus(StatusType.DONE);
         manager.updateSubTask(subTask2);
         assertEquals(StatusType.DONE,epicTask.getStatus());
     }

     @Test
     public void checkingTimeDiscrepancy(){
         manager.addTask(new Task("задача 1","задача 1",20, LocalDateTime.now()));
         Task taskError1 = new Task("задача 2","задача 2",15, LocalDateTime.now().plusMinutes(15));
         Task taskError2 = new Task("задача 3","задача 3",12, LocalDateTime.now());

         assertThrows(RuntimeException.class,() -> manager.addTask(taskError1));
         assertThrows(RuntimeException.class,() -> manager.addTask(taskError2));

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
         manager.removeSubTask(2);
         assertEquals(dateTime.plusMinutes(40),epicTask.getStartTime());
     }

     @Test
     public void endTimeEpicTaskAllSubtasksWithStartTimeAndDuration(){
         int id = manager.addEpicTask(new EpicTask("Эпик задача","эпик с подзадачами для поверки времени"));
         LocalDateTime dateTime = LocalDateTime.of(2023,2,19,1,0);
         manager.addSubTask(new SubTask(id,"Подзадача 1","Подзадача №1 длительность 30 минут",30,dateTime));
         manager.addSubTask(new SubTask(id,"Подзадача 2","Подзадача №2 длительность 20 минут",20,dateTime.plusMinutes(40)));
         EpicTask epicTask = manager.getEpicTask(id);
         assertEquals(dateTime.plusMinutes(40+20),epicTask.getEndTime());
         manager.removeSubTask(3);
         assertEquals(dateTime.plusMinutes(30),epicTask.getEndTime());

     }

     @Test
     public void durationEpicTaskAllSubtasksWithStartTimeAndDuration(){
         int id = manager.addEpicTask(new EpicTask("Эпик задача","эпик с подзадачами для поверки времени"));
         LocalDateTime dateTime = LocalDateTime.of(2023,2,19,1,0);
         manager.addSubTask(new SubTask(id,"Подзадача 1","Подзадача №1 длительность 30 минут",30,dateTime));
         manager.addSubTask(new SubTask(id,"Подзадача 2","Подзадача №2 длительность 20 минут",20,dateTime.plusMinutes(40)));
         EpicTask epicTask = manager.getEpicTask(id);
         assertEquals(60,epicTask.getDuration());
         manager.removeSubTask(2);
         assertEquals(20,epicTask.getDuration());

     }

     // getTask
     @Test
     public void getTaskEmptyAndStandartAndNotTrueID(){
         Task task = manager.getTask(0);
         assertNull(task);
         int id = createTask();
         task = manager.getTask(id);
         assertNotNull(task);
         assertEquals(StatusType.NEW,task.getStatus());
         assertEquals("Задача", task.getTitle());
         task = manager.getTask(id+1);
         assertNull(task);
     }

     // getEpicTask
     @Test
     public void getEpicTaskEmptyAndStandartAndNotTrueID(){
         EpicTask epicTask = manager.getEpicTask(0);
         assertNull(epicTask);
         int id = createEpicTask();
         epicTask = manager.getEpicTask(id);
         assertNotNull(epicTask);
         assertEquals(StatusType.NEW,epicTask.getStatus());
         assertEquals(0,epicTask.getListSubTaskId().size());
         epicTask = manager.getEpicTask(id+1);
         assertNull(epicTask);
     }

     // getSubTask
     @Test
     public void getSubTaskEmptyAndStandartAndNotTrueID(){
         SubTask subTask = manager.getSubTask(0);
         assertNull(subTask);
         int epicId = createEpicTask();
         int id = createSubTask(epicId);
         subTask = manager.getSubTask(id);
         assertNotNull(subTask);
         assertEquals(epicId,subTask.getEpicTaskId());
         subTask = manager.getSubTask(id+1);
         assertNull(subTask);
     }

     // getAllTask
     @Test
     public void getAllTaskEmptyAndStandart(){
         assertTrue(manager.getAllTask().isEmpty());
         createEpicTask();
         int id = createTask();
         assertEquals(1,manager.getAllTask().size());
         assertEquals(id,manager.getAllTask().get(0).getId());

     }

     // getAllEpicTask
     @Test
     public void getAllEpicTaskEmptyAndStandart(){
         assertTrue(manager.getAllEpicTask().isEmpty());
         createTask();
         int id = createEpicTask();
         createSubTask(id);
         assertEquals(1,manager.getAllEpicTask().size());
         assertEquals(id,manager.getAllEpicTask().get(0).getId());

     }

    // getAllSubTask
     @Test
     public void getAllSubTaskEmptyAndStandart(){
          assertTrue(manager.getAllSubTask().isEmpty());
          createTask();
          int id = createEpicTask();
          int subTaskId = createSubTask(id);
          assertEquals(1,manager.getAllSubTask().size());
          assertEquals(subTaskId,manager.getAllSubTask().get(0).getId());

     }

     // addTask
     @Test
     public void addTaskStandartAndNotTrueID(){
         int idTask1 = createTask();
         assertEquals(1, idTask1);
         int idTask2 = createTask();
         assertEquals(2, idTask2);
         Task taskNotTrueID = new Task(4,"задача 3","задача",StatusType.NEW,0,null);
         manager.addTask(taskNotTrueID);
         assertNull(manager.getTask(4));

     }

     // addEpicTask
     @Test
     public void addEpicTaskStandartAndNotTrueID(){
         int epicTask1 = createEpicTask();
         assertEquals(1, epicTask1);
         int epicTask2 = createEpicTask();
         assertEquals(2, epicTask2);
         EpicTask epicTaskNotTrueID = new EpicTask(4,"эпик 3","задача",StatusType.NEW,0,null);
         manager.addEpicTask(epicTaskNotTrueID);
         assertNull(manager.getEpicTask(4));
     }

     // addSubTask
     @Test
     public void addSubTaskStandartAndNotTrueID(){
         int epicTask1 = createEpicTask();
         int subTask1 = createSubTask(epicTask1);
         assertEquals(2, subTask1);
         int subTask2 = createSubTask(epicTask1);
         assertEquals(3, subTask2);
         SubTask subTaskNotTrueID = new SubTask(epicTask1,5,"задача 3","задача",StatusType.NEW,0,null);
         manager.addSubTask(subTaskNotTrueID);
         assertNull(manager.getSubTask(5));

     }

     // updateTask
     @Test
     public void updateTaskStandart(){
         int id = createTask();
         Task task = manager.getTask(id);
         task.setStatus(StatusType.DONE);
         manager.updateTask(task);
         assertEquals(StatusType.DONE,manager.getTask(id).getStatus());
     }

     // updateEpicTask
     @Test
     public void updateEpicTaskStandart(){
         int id = createEpicTask();
         EpicTask epicTask = manager.getEpicTask(id);
         epicTask.setTitle("ЭПИК");
         manager.updateEpicTask(epicTask);
         assertEquals(StatusType.NEW, manager.getEpicTask(id).getStatus());
         assertEquals("ЭПИК", manager.getEpicTask(id).getTitle());
         int subId = createSubTask(id);
         assertEquals(StatusType.NEW, manager.getEpicTask(id).getStatus());

     }

     // updateSubTask
     @Test
     public void updateSubTaskStandart(){
         int id = createEpicTask();
         int subId = createSubTask(id);
         SubTask subTask = manager.getSubTask(subId);
         subTask.setTitle("подзадача");
         manager.updateSubTask(subTask);
         assertEquals(StatusType.NEW, manager.getSubTask(subId).getStatus());
         assertEquals("подзадача", manager.getSubTask(subId).getTitle());

     }

     // clearAllTask
     @Test
     public void clearAllTaskStandart(){
         createTask();
         manager.clearAllTask();
         assertTrue(manager.getAllTask().isEmpty());
     }

     // clearAllEpicTask
     @Test
     public void clearAllEpicTaskStandart(){
         int id = createEpicTask();
         createSubTask(id);
         createSubTask(id);
         manager.clearAllEpicTask();
         assertTrue(manager.getAllEpicTask().isEmpty());
         assertTrue(manager.getAllSubTask().isEmpty());

     }

     // clearAllSubTask
     @Test
     public void clearAllSubTaskStandart(){
         int id = createEpicTask();
         createSubTask(id);
         createSubTask(id);
         manager.clearAllSubTask();
         assertEquals(0,manager.getEpicTask(id).getListSubTaskId().size());
         assertTrue(manager.getAllSubTask().isEmpty());

     }

     // removeTask
     @Test
     public void removeTaskStandart(){
         int id = createTask();
         manager.removeTask(id);
         assertNull(manager.getTask(id));

     }

     // removeEpicTask
     @Test
     public void removeEpicTaskStandart(){
         int id = createEpicTask();
         int subId = createSubTask(id);
         manager.removeEpicTask(id);
         assertNull(manager.getEpicTask(id));
         assertNull(manager.getSubTask(subId));

     }

     // removeSubTask
     @Test
     public void removeSubTaskStandart(){
         int id = createEpicTask();
         int subId = createSubTask(id);
         manager.removeSubTask(subId);
         assertNull(manager.getSubTask(subId));
         assertEquals(0,manager.getEpicTask(id).getListSubTaskId().size());
     }

     // getHistory
     @Test
     public void getHistoryEmptyAndStandart(){
         assertEquals(0,manager.getHistory().size());
         int id = createTask();
         manager.getTask(createTask());
         manager.getTask(id);
         assertEquals(2,manager.getHistory().size());
         manager.getTask(id);
         assertEquals(2,manager.getHistory().size());
         manager.removeTask(id);
         assertEquals(1,manager.getHistory().size());

     }

     // getPrioritizedTasks
     @Test
    public void getPrioritizedTasksEmptyAndStandart(){
         assertEquals(0,manager.getPrioritizedTasks().size());
         createTask();
         assertEquals(1,manager.getPrioritizedTasks().size());
         manager.addTask(new Task("задача 2","задача",20, LocalDateTime.now()));
         assertEquals("задача 2",manager.getPrioritizedTasks().get(0).getTitle());
         manager.addTask(new Task("задача 3","задача",15, LocalDateTime.now().minusMinutes(40)));
         assertEquals("задача 3",manager.getPrioritizedTasks().get(0).getTitle());
         assertEquals(1,manager.getPrioritizedTasks().get(2).getId());
         manager.removeTask(3);
         assertEquals("задача 2",manager.getPrioritizedTasks().get(0).getTitle());
     }
}
