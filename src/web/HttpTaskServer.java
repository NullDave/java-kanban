package web;

import com.sun.net.httpserver.HttpServer;
import manager.FileBackedTasksManager;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Paths;

public class HttpTaskServer {
    private final TaskManager manager;
    private HttpServer httpServer;
    private static final String PATH = "save.csv";
    private final static short PORT = 8080;

    public HttpTaskServer() {
        manager = FileBackedTasksManager.loadFromFile(Paths.get(PATH).toFile());
        try {
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks",new TaskHandler(manager));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        System.out.println("Сервер запущен "+PORT);
        httpServer.start();
    }

}
