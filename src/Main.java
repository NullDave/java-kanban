
import web.KVServer;
import web.KVTaskClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class Main {

    public static void main(String[] args) {
        try {
            KVServer server = new KVServer();
            server.start();
            KVTaskClient client = new KVTaskClient(new URL("http://127.0.0.1:8078"));
            client.put("key","data");
            System.out.println(client.load("key"));
            System.out.println(client.load("newKey"));
            server.stop();
        } catch (IOException | URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }

    }


}
