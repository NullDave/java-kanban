package manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import task.EpicTask;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{

    private static final String PATH = "test/testsave.csv";
    public File testFile = Paths.get(PATH).toFile();

    @Override
    public FileBackedTasksManager getTaskManager() {
        return FileBackedTasksManager.loadFromFile(testFile);
    }

    @AfterEach
    public void clearTestFile(){ // чистим тестовый файл после каждого теста
        try (FileWriter file = new FileWriter(testFile)) {
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loadFromFileTaskAndHistoryEmpty(){
        assertEquals(0,manager.getAllTask().size());
        assertEquals(0,manager.getAllEpicTask().size());
        assertEquals(0,manager.getAllSubTask().size());
        assertEquals(0,manager.getHistory().size());
        TaskManager loadManager = FileBackedTasksManager.loadFromFile(testFile);
        assertEquals(0,loadManager.getAllTask().size());
        assertEquals(0,loadManager.getAllEpicTask().size());
        assertEquals(0,loadManager.getAllSubTask().size());
        assertEquals(0,loadManager.getHistory().size());
    }

    @Test
    public void loadFromFileEpicTaskEmpty(){
        EpicTask epicTask = new EpicTask("Эпик задача","эпик для проверки");
        int id = manager.addEpicTask(epicTask);
        TaskManager loadManager = FileBackedTasksManager.loadFromFile(testFile);
        EpicTask loadEpicTask = loadManager.getEpicTask(id);
        assertEquals(epicTask,loadEpicTask);
        assertEquals(epicTask.getTitle(),loadEpicTask.getTitle());
        assertEquals(epicTask.getDescription(),loadEpicTask.getDescription());

    }
}