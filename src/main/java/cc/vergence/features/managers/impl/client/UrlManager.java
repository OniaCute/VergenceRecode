package cc.vergence.features.managers.impl.client;

import cc.vergence.features.managers.Manager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public final class UrlManager extends Manager {
    private static final HttpClient CLIENT = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).followRedirects(HttpClient.Redirect.NORMAL).build();

    public UrlManager() {
        super("Url Manager");
    }

    public String get(String url) {
        try {
            HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
            HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
            return resp.body();
        } catch (Exception e) {
            return "!Exception";
        }
    }

    public boolean available(String url) {
        try {
            HttpRequest req = HttpRequest.newBuilder(URI.create(url)).method("HEAD", HttpRequest.BodyPublishers.noBody()).build();
            HttpResponse<Void> resp = CLIENT.send(req, HttpResponse.BodyHandlers.discarding());
            return resp.statusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean get(String url, String value) {
        return get(url).equals(value);
    }

    public long ping(String url) {
        long start = System.currentTimeMillis();
        try {
            HttpURLConnection conn = (HttpURLConnection) URI.create(url).toURL().openConnection();
            conn.setRequestMethod("HEAD");
            conn.setConnectTimeout(6000);
            conn.setReadTimeout(9000);
            return System.currentTimeMillis() - start;
        } catch (IOException e) {
            return -1L; // cant connect
        }
    }
}