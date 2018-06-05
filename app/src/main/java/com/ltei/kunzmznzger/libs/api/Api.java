package com.ltei.kunzmznzger.libs.api;

import android.os.AsyncTask;

import com.ltei.kunzmznzger.libs.Helpers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Robin
 * @date 19/06/2017
 */
public class Api extends AsyncTask<Object, Void, ApiResponse>
{
    //    private static final String USER_AGENT = "Chrome/58.0.3029.110";

    public static CompletableFuture<ApiResponse> get(String url) {
        return sendRequest(url, null, "GET");
    }

    public static CompletableFuture<ApiResponse> post(String url, JSONObject data) {
        return sendRequest(url, data, "POST");
    }

    public static CompletableFuture<ApiResponse> put(String url, JSONObject data) {
        return sendRequest(url, data, "PUT");
    }

    public static CompletableFuture<ApiResponse> delete(String url) {
        return sendRequest(url, null, "DELETE");
    }

    public static CompletableFuture<ApiResponse> sendRequest(String url, JSONObject data, String httpMethod) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return new Api().execute(url, httpMethod, data).get();
            } catch (Exception e) {
                return new ApiResponse(-1, null);
            }
        });
    }

    @Override
    protected ApiResponse doInBackground(Object... params) {
        String url = (String) params[0];
        String httpMethod = (String) params[1];
        JSONObject data = (JSONObject) params[2];

        URL urlObj;
        HttpURLConnection connection = null;

        try {
            urlObj = new URL(url);
            connection = (HttpURLConnection) urlObj.openConnection();

            // headers
            connection.setRequestMethod(httpMethod);
            connection.setRequestProperty("models.User-Agent", "java client");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setInstanceFollowRedirects(false);

            if (!httpMethod.equals("GET") && !httpMethod.equals("DELETE") && data != null) {

                connection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

                wr.writeBytes(data.toString());
                wr.flush();
                wr.close();
            }

            int responseCode = connection.getResponseCode();

            StringBuilder builder = new StringBuilder();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = in.readLine()) != null) {
                builder.append(line);
            }

            String jsonStr = builder.toString();

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(jsonStr);
            Object dataAttr = json.get("data");

            JSONArray jsonArray;
            if (dataAttr instanceof JSONArray) {
                jsonArray = (JSONArray) dataAttr;
            }
            else {
                jsonArray = new JSONArray();
                jsonArray.add(dataAttr);
            }

            return new ApiResponse(responseCode, jsonArray);
        } catch (ParseException | IOException e) {
            if (connection != null) {
                try {
                    StringBuilder builder = new StringBuilder();

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    String line;

                    while ((line = in.readLine()) != null) {
                        builder.append(line);
                    }

                    String errorStr = builder.toString();
                    Helpers.log(errorStr, "[API_ERROR]");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            e.printStackTrace();
            return null;
        }
    }
}
