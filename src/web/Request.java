package web;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

// преобразованием запрос в объект для удобства работы
public class Request {
    private final RequestMethodType requestMethodType;
    private final RequestEndPoint endPoint;
    private final int id;
    private final String task;

    public Request(RequestMethodType requestMethodType, RequestEndPoint endPoint, int id, String task) {
        this.requestMethodType = requestMethodType;
        this.endPoint = endPoint;
        this.id = id;
        this.task = task;
    }

    public static Request parse(HttpExchange exchange){
        RequestMethodType requestMethodType = RequestMethodType.from(exchange.getRequestMethod());
        RequestEndPoint endPoint = RequestEndPoint.UNKNOWN;
        int id = 0;
        String task = null;
        try {
            String path = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();
            System.out.println(query);
            String[] pathParts = path.split("/");
            if(pathParts.length >= 3){
                if(pathParts[2].equals("task")){
                    endPoint = RequestEndPoint.TASK;
                }
                if(pathParts[2].equals("subtask")){
                    endPoint = RequestEndPoint.SUBTASK;
                    if(pathParts.length == 4 && pathParts[3].equals("epic")){
                        endPoint = RequestEndPoint.SUBTASK_FOR_EPICTASK;
                    }
                }
                if(pathParts[2].equals("epic")){
                    endPoint = RequestEndPoint.EPICTASK;
                }
                if(pathParts[2].equals("history")){
                    endPoint = RequestEndPoint.HISTORY;
                }
                if(query != null && query.startsWith("id")){
                    id = Integer.parseInt(query.split("=")[1]);
                }
                task = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

            }else if(pathParts.length == 2 && pathParts[1].equals("tasks")){
                endPoint = RequestEndPoint.PRIORITIZED_TASK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Request(requestMethodType,endPoint,id,task);
    }

    public RequestMethodType getRequestMethodType() {
        return requestMethodType;
    }

    public RequestEndPoint getEndPoint() {
        return endPoint;
    }

    public int getId() {
        return id;
    }

    public String getTask() {
        return task;
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestMethodType=" + requestMethodType +
                ", endPoint=" + endPoint +
                ", id=" + id +
                ", task='" + task + '\'' +
                '}';
    }
}
