package manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.EpicTask;
import web.KVServer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    private KVServer server;
    private static URL url;

    @BeforeAll
    public static void setUrl(){
        try {
            url = new URL("http://localhost:8078");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void stop(){
        server.stop();
    }

    @Override
    public HttpTaskManager getTaskManager() {
        try {
            server = new KVServer();
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HttpTaskManager(url);
    }

    @Test
    public void loadFromServerTaskAndHistoryEmpty(){
        assertEquals(0,manager.getAllTask().size());
        assertEquals(0,manager.getAllEpicTask().size());
        assertEquals(0,manager.getAllSubTask().size());
        assertEquals(0,manager.getHistory().size());
        TaskManager loadManager = new HttpTaskManager(url);
        assertEquals(0,loadManager.getAllTask().size());
        assertEquals(0,loadManager.getAllEpicTask().size());
        assertEquals(0,loadManager.getAllSubTask().size());
        assertEquals(0,loadManager.getHistory().size());
    }

    @Test
    public void loadFromServerEpicTaskEmpty(){
        EpicTask epicTask = new EpicTask("Эпик задача","эпик для проверки");
        int id = manager.addEpicTask(epicTask);
        manager.getEpicTask(id);
        TaskManager loadManager = new HttpTaskManager(url);
        assertEquals(1,loadManager.getHistory().size());
        assertTrue(loadManager.getHistory().get(0) instanceof EpicTask);
        EpicTask loadEpicTask = loadManager.getEpicTask(id);
        assertEquals(epicTask,loadEpicTask);
        assertEquals(epicTask.getTitle(),loadEpicTask.getTitle());
        assertEquals(epicTask.getDescription(),loadEpicTask.getDescription());
        assertEquals(0,loadManager.getPrioritizedTasks().size());

    }
}