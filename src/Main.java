import manager.TaskManagerDefault;
import manager.TaskManger;
import task.EpicTask;
import task.StatusType;
import task.SubTask;

public class Main {
    static TaskManger manager = new TaskManagerDefault();

    public static void main(String[] args) {

        manager.addEpicTask(new EpicTask("Важный эпик","бла бла"));
        manager.addSubTask(new SubTask(1,"подзадача 1","бла бла бла"));
        manager.addSubTask(new SubTask(1,"подзадача 2","бла бла бла бла"));
        manager.addEpicTask(new EpicTask("Важный эпик 2","бла бла бла бла бла"));
        manager.addSubTask(new SubTask(4,"подзадача 1","бла бло бли"));
        print();
        manager.updateSubTask(new SubTask(4,5,"подзадача 1","бла бло бли", StatusType.IN_PROGRESS) );
        manager.updateSubTask(new SubTask(1,3,"подзадача 2","бла бла бла бла",StatusType.DONE));
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
