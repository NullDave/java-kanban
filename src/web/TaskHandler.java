package web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.EpicTask;
import task.SubTask;
import task.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

// обработчик для /tasks
public class TaskHandler implements HttpHandler {
    private final TaskManager manager;
    private final TaskConvertor convertor;

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public TaskHandler(TaskManager manager) {
        this.manager = manager;
        convertor = new TaskConvertor();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Request request = Request.parse(exchange);
        switch (request.getRequestMethodType()) {
            case GET:
                getHandler(request,exchange);
                break;
            case POST:
                postHandler(request,exchange);
                break;
            case DELETE:
                deleteHandler(request,exchange);
                break;
            default:
                writeResponse(exchange,"неверный метод запроса",405);
        }
    }

    private void deleteHandler(Request request, HttpExchange exchange) throws IOException {
        switch (request.getEndPoint()) {
            case TASK:
                if(request.getId() != 0)
                    manager.removeTask(request.getId());
                else
                    manager.clearAllTask();
                break;
            case EPICTASK:
                if(request.getId() != 0)
                    manager.removeEpicTask(request.getId());
                else
                    manager.clearAllEpicTask();
                break;
            case SUBTASK:
                if(request.getId() != 0)
                    manager.removeSubTask(request.getId());
                else
                    manager.clearAllSubTask();
                break;
            default:
                writeResponse(exchange,"неверный запрос",400);
                return;
        }
        writeResponse(exchange,"запрос выполнен",200);
    }

    private void postHandler(Request request, HttpExchange exchange) throws IOException {
        switch (request.getEndPoint()) {
            case TASK:
                Task task = convertor.getTask(request.getTask());
                if(task.getId()!= 0)
                    manager.updateTask(task);
                else
                    manager.addTask(task);
                break;
            case EPICTASK:
                EpicTask epicTask = convertor.getEpicTask(request.getTask());
                if(epicTask.getId()!= 0)
                    manager.updateEpicTask(epicTask);
                else
                    manager.addEpicTask(epicTask);
                break;
            case SUBTASK:
                SubTask subTask = convertor.getSubTask(request.getTask());
                if(subTask.getId()!= 0)
                    manager.updateSubTask(subTask);
                else
                    manager.addSubTask(subTask);
                break;
            default:
                writeResponse(exchange,"неверный запрос",400);
                return;
        }
        writeResponse(exchange,"запрос выполнен",200);

    }

    private void getHandler(Request request, HttpExchange exchange) throws IOException {
        switch (request.getEndPoint()){

            case TASK:
                if(request.getId() != 0)
                    writeResponse(exchange,convertor.toJson(manager.getTask(request.getId())),200);
                else
                    writeResponse(exchange,convertor.toJson(manager.getAllTask()),200);
                break;
            case EPICTASK:
                System.out.println(manager.getEpicTask(request.getId()));

                if(request.getId() != 0)
                    writeResponse(exchange,convertor.toJson(manager.getEpicTask(request.getId())),200);
                else
                    writeResponse(exchange,convertor.toJson(manager.getAllEpicTask()),200);
                break;
            case SUBTASK:
                if(request.getId() != 0)
                    writeResponse(exchange,convertor.toJson(manager.getSubTask(request.getId())),200);
                else
                    writeResponse(exchange,convertor.toJson(manager.getAllSubTask()),200);
                break;
            case HISTORY:
                writeResponse(exchange,convertor.toJson(manager.getHistory()),200);
                break;
            case SUBTASK_FOR_EPICTASK:
                EpicTask epicTask = manager.getEpicTask(request.getId());
                if (epicTask == null){
                    writeResponse(exchange,"неверный id эпика",400);
                    break;
                }
                List<SubTask> subTasks = new ArrayList<>();
                for(int id:epicTask.getListSubTaskId())
                    subTasks.add(manager.getSubTask(id));
                writeResponse(exchange,convertor.toJson(subTasks),200);
                break;
            case PRIORITIZED_TASK:
                writeResponse(exchange,convertor.toJson(manager.getPrioritizedTasks()),200);
                break;
            case UNKNOWN:
                writeResponse(exchange,"неверный запрос",400);
                break;
        }
    }

    private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }

}