package ru.otus.hw.utils;

import org.eclipse.jetty.http.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

public final class HttpUrlConnectionHelper {
    private HttpUrlConnectionHelper() {
    }

    public static HttpURLConnection sendRequest(String url, HttpMethod method) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod(method.asString());
        return connection;
    }

    public static String buildUrl(String host, String path, List<String> pathParams) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(host);
        stringBuilder.append(path);
        Optional.ofNullable(pathParams).ifPresent(paramsMap -> paramsMap.forEach(p -> stringBuilder.append("/").append(p)));
        return stringBuilder.toString();
    }

    public static String encodedLoginAndPassword(String login, String password) {
        return Base64.getEncoder().encodeToString((login + ":" + password).getBytes(StandardCharsets.UTF_8));
    }


}
