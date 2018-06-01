package com.ltei.kunzmznzger.libs.api;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 * @author Robin
 * @date 22/06/2017
 */
public class ApiQuery
{
    private final String completeUrl;
    private final Http httpMethod;
    private JSONObject data;
    private UrlParametersMap urlParams;
    private String suffix;

    public ApiQuery (String completeUrl, Http method, JSONObject data)
    {
        this.httpMethod = method;
        this.completeUrl = completeUrl;
        this.data = data;
    }

    public CompletableFuture<ApiResponse> execute ()
    {
        String url = completeUrl;
        if (suffix != null && !suffix.isEmpty()) {
            url += suffix;
        }

        return Api.sendRequest(url, data, httpMethod.toString());
    }

    @Override
    public String toString ()
    {
        return "[" + httpMethod + "] " + completeUrl + (suffix == null || suffix.length() == 0 ? "" : suffix);
    }

    public ApiQuery build ()
    {
        suffix = "";
        if (urlParams != null) {
            suffix = urlParams.toString();
        }

        return this;
    }

    public void setUrlParams (UrlParametersMap urlParams)
    {
        this.urlParams = urlParams;
        this.suffix = urlParams == null ? "" : urlParams.toString();
    }
}
