import manager.*;
import task.EpicTask;
import task.SubTask;
import task.Task;
import java.nio.file.Paths;

public class Main {

    private static final String PATH = "src/save.csv";
    public static void main(String[] args) {
        TaskManager manager = new FileBackedTasksManager(Paths.get(PATH).toFile());

        int epicTaskIdOne = manager.addEpicTask(new EpicTask("Важный эпик","бла бла"));
        int subTaskIdOne = manager.addSubTask(new SubTask(1,"подзадача 1","бла бла бла"));
        int subTaskIdTwo = manager.addSubTask(new SubTask(1,"подзадача 2","бла бла бла бла"));
        int epicTaskIdTwo =  manager.addEpicTask(new EpicTask("Важный эпик 2","бла бла бла бла бла"));
        int idTask = manager.addTask(new Task("Task простой","бла бла"));

        manager.getSubTask(subTaskIdOne);
        manager.getTask(idTask);
        manager.getEpicTask(epicTaskIdOne);
        print(manager);
        printHistory(manager);

        TaskManager loadManager = FileBackedTasksManager.loadFromFile(Paths.get(PATH).toFile());
        System.out.println("проверка на соответствие: "+complianceCheck(manager,loadManager));

    }

    private static boolean complianceCheck(TaskManager manager, TaskManager loadManager) {
        boolean isEqual = true;
        for (Task task :manager.getAllTask()){
            if(!task.equals(loadManager.getTask(task.getId())))
                isEqual = false;
        }
        for (Task task :manager.getAllEpicTask()){
            if(!task.equals(loadManager.getEpicTask(task.getId())))
                isEqual = false;
        }
        for (Task task :manager.getAllSubTask()){
            if(!task.equals(loadManager.getSubTask(task.getId())))
                isEqual = false;
        }
        return isEqual;
    }

    public static void print(TaskManager manager){
        for (Task task : manager.getAllTask()) {
            System.out.println(task);
        }
        for (EpicTask epicTask : manager.getAllEpicTask()){
            System.out.println(epicTask);
            for (int id :epicTask.getListSubTaskId()){
                System.out.println(manager.getSubTask(id));
            }
            System.out.println("-".repeat(100));
        }

    }

    public static void printHistory(TaskManager manager){
        System.out.println("/".repeat(100));
        System.out.println("__история просмотров__");
        for (Task task:manager.getHistory()) {
            System.out.println(task);
        }
        System.out.println("-".repeat(100));

    }



}
