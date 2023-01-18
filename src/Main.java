import manager.*;
import task.EpicTask;
import task.StatusType;
import task.SubTask;
import task.Task;

public class Main {
    static TaskManger manager = Managers.getDefault();
    public static void main(String[] args) {

        int epicTaskIdOne = manager.addEpicTask(new EpicTask("Важный эпик","бла бла"));
        int subTaskIdOne = manager.addSubTask(new SubTask(1,"подзадача 1","бла бла бла"));
        int subTaskIdTwo = manager.addSubTask(new SubTask(1,"подзадача 2","бла бла бла бла"));
        manager.addSubTask(new SubTask(1,"подзадача 3","бла бла бла бла"));
        int epicTaskIdTwo =  manager.addEpicTask(new EpicTask("Важный эпик 2","бла бла бла бла бла"));

        System.out.println(manager.getEpicTask(epicTaskIdOne));
        System.out.println(manager.getSubTask(subTaskIdTwo));
        System.out.println(manager.getSubTask(subTaskIdOne));
        System.out.println(manager.getEpicTask(epicTaskIdTwo));
        printHistory();
        System.out.println(manager.getSubTask(subTaskIdTwo));
        System.out.println(manager.getEpicTask(epicTaskIdOne));
        System.out.println(manager.getEpicTask(epicTaskIdTwo));
        System.out.println(manager.getSubTask(subTaskIdTwo));
        printHistory();
        manager.removeEpicTask(subTaskIdOne);
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

    }

    public static void printHistory(){
        System.out.println("/".repeat(100));
        System.out.println("__история просмотров__");
        for (Task task:manager.getHistory()) {
            System.out.println(task);
        }
        System.out.println("-".repeat(100));

    }



}
