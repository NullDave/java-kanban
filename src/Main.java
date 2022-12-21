import jdk.jfr.Timespan;
import manager.*;
import task.EpicTask;
import task.StatusType;
import task.SubTask;
import task.Task;

public class Main {
    static TaskManger manager = Managers.getDefault();
    public static void main(String[] args) {

        manager.addEpicTask(new EpicTask("Важный эпик","бла бла"));
        int subTaskIdOne = manager.addSubTask(new SubTask(1,"подзадача 1","бла бла бла"));
        manager.addSubTask(new SubTask(1,"подзадача 2","бла бла бла бла"));
        manager.addEpicTask(new EpicTask("Важный эпик 2","бла бла бла бла бла"));
        int subTaskIdTwo = manager.addSubTask(new SubTask(4,"подзадача 1","бла бло бли"));
        manager.addSubTask(new SubTask(1,"подзадача 3","бла бла бла бла"));
        manager.addSubTask(new SubTask(4,"подзадача 4","бла бла бла бла"));

        print();
        printHistory();
        SubTask subTaskOne = manager.getSubTask(subTaskIdOne);
        subTaskOne.setStatus(StatusType.IN_PROGRESS);
        manager.updateSubTask(subTaskOne);
        SubTask subTaskTwo = manager.getSubTask(subTaskIdTwo);
        subTaskTwo.setStatus(StatusType.DONE);
        manager.updateSubTask(subTaskTwo);
        print();
        manager.removeEpicTask(1);
        print();
        printHistory();
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

    public static void printHistory(){
        System.out.println("__история просмотров__");
        for (Task task:manager.getHistory()) {
            System.out.println(task);
        }
        System.out.printf("последние %s запросов \n", manager.getHistory().size());
        System.out.println("/".repeat(100));

    }



}
