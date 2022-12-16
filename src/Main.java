import manager.TaskManagerImpl;
import manager.TaskManger;
import task.EpicTask;
import task.StatusType;
import task.SubTask;

public class Main {
    static TaskManger manager = new TaskManagerImpl();

    public static void main(String[] args) {

        manager.addEpicTask(new EpicTask("Важный эпик","бла бла"));
        int subTaskIdOne = manager.addSubTask(new SubTask(1,"подзадача 1","бла бла бла"));
        manager.addSubTask(new SubTask(1,"подзадача 2","бла бла бла бла"));
        manager.addEpicTask(new EpicTask("Важный эпик 2","бла бла бла бла бла"));
        int subTaskIdTwo = manager.addSubTask(new SubTask(4,"подзадача 1","бла бло бли"));
        print();
        // Простите, я комментарий не заметил на предущем ревью.
        SubTask subTaskOne = manager.getSubTask(subTaskIdOne);
        subTaskOne.setStatus(StatusType.IN_PROGRESS);
        manager.updateSubTask(subTaskOne);
        SubTask subTaskTwo = manager.getSubTask(subTaskIdTwo);
        subTaskTwo.setStatus(StatusType.DONE);
        manager.updateSubTask(subTaskTwo);
        print();
        manager.removeSubTask(2);
        print();
        manager.clearAllSubTask();
        print();

    }

    public static void print(){
        for (EpicTask epicTask : manager.getAllEpicTask()){
            System.out.println(epicTask);
            for (int id :epicTask.getListSubTaskId()){
                System.out.println(manager.getSubTask(id));
            }
            System.out.println("-".repeat(100));
        }
        System.out.println("/".repeat(100));

    }
}
