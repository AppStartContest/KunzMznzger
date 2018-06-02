package com.ltei.kunzmznzger.libs.api;

import com.ltei.kunzmznzger.libs.models.Model;
import com.ltei.kunzmznzger.libs.models.ModelManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 * @author Robin
 * @date 22/06/2017
 */
public class ApiQueryBuilder<T extends Model>
{
    private String url;

    private ModelManager<T> modelManager;

    private ApiQuery query;

    private Http httpMethod;
    private JSONObject data;
    private UrlParametersMap urlParams;

    public ApiQueryBuilder (ModelManager modelManager)
    {
        this(modelManager.getUrl());
        this.modelManager = modelManager;
    }

    public ApiQueryBuilder (String url)
    {
        this(url, Http.GET);
    }

    public ApiQueryBuilder (String url, Http httpMethod)
    {
        this.httpMethod = httpMethod;
        this.url = url;
    }

    public ApiQueryBuilder setModelManager (ModelManager<T> modelManager)
    {
        this.modelManager = modelManager;
        return this;
    }

    public static ApiQueryBuilder create (String url)
    {
        return new ApiQueryBuilder(url);
    }

    public static ApiQueryBuilder create (String url, Http httpMethod)
    {
        return new ApiQueryBuilder(url, httpMethod);
    }

    public ApiQueryBuilder atUrl (String url)
    {
        return atUrl(url, Http.GET);
    }

    public ApiQueryBuilder atUrl (String url, Http httpMethod)
    {
        this.url = url;
        this.httpMethod = httpMethod;
        return this;
    }

    public ApiQueryBuilder setHttpMethod (Http method)
    {
        this.httpMethod = method;
        return this;
    }

    public ApiQueryBuilder data(JSONObject data)
    {
        this.data = data;
        return this;
    }

    public ApiQuery buildQuery ()
    {
        this.query = new ApiQuery(url, httpMethod, data);
        query.setUrlParams(urlParams);
        return query;
    }

    public ApiQuery getQuery ()
    {
        return this.buildQuery();
    }

    @Override
    public String toString ()
    {
        return query.toString();
    }

    public ApiQueryBuilder params (UrlParametersMap urlParams)
    {
        this.urlParams = urlParams;
        return this;
    }
}
