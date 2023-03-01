package web;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final URL url;
    private final String api;
    private final HttpClient client;

    public KVTaskClient(URL url) throws IOException, URISyntaxException, InterruptedException {
        this.url = url;
        client = HttpClient.newHttpClient();
        api = send(new URL(url,"register/"),null);
    }

    public void put(String key, String json) throws IOException, URISyntaxException, InterruptedException {
       URL save = new URL(url,String.format("save/%s?API_TOKEN=%s",key,api));
       send(save,json);
    }

    public String load(String key) throws IOException, URISyntaxException, InterruptedException {
        URL load = new URL(url,String.format("load/%s?API_TOKEN=%s",key,api));
        return send(load,null);
    }

    private String send(URL url,String data) throws URISyntaxException {
        HttpRequest request;
        try {
        if(data != null) {
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(data);
            request = HttpRequest.newBuilder().uri(url.toURI()).POST(body).build();
        }else {
            request = HttpRequest.newBuilder().uri(url.toURI()).GET().build();
        }
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() == 200)
            return response.body();

        } catch (IOException | InterruptedException ignored) {
        }
        return null;
    }
}
