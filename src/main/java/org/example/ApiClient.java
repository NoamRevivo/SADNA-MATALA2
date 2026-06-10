package org.example;
import com.google.gson.Gson;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {

    // קבועי ה-API
    private static final String BASE_URL = "https://backend-qcf9.onrender.com/fm1";
    private static final String ENDPOINT_CONFIG = "/get-render-config";
    private static final String ENDPOINT_MAZE = "/get-maze-image";
    // קבועי ה-API

    private HttpClient httpClient;
    private Gson gson;
    public ApiClient()
    {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }
    public RenderConfig fetchRenderConfig() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + ENDPOINT_CONFIG))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), RenderConfig.class);
    }
    public BufferedImage fetchMazeImage(int width, int height) throws Exception {
        String fullUrl = BASE_URL + ENDPOINT_MAZE + "?width=" + width + "&height=" + height;
        URL url = new URL(fullUrl);
        return ImageIO.read(url);
    }
}