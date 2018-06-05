package com.ltei.kunzmznzger.libs.api;

import com.ltei.kunzmznzger.libs.models.Model;
import com.ltei.kunzmznzger.libs.models.ModelManager;

import org.json.simple.JSONObject;

/**
 * @author Robin
 * @date 22/06/2017
 */
public class ApiQueryBuilder<T extends Model>
{
    private String url;

    private ApiQuery query;

    private Http httpMethod;
    private JSONObject data;
    private UrlParametersMap urlParams;

    public ApiQueryBuilder(ModelManager modelManager) {
        this(modelManager.url());
    }

    public ApiQueryBuilder(String url) {
        this(url, Http.GET);
    }

    public ApiQueryBuilder(String url, Http httpMethod) {
        this.httpMethod = httpMethod;
        this.url = url;
    }

    public static ApiQueryBuilder create(String url) {
        return new ApiQueryBuilder(url);
    }

    public static ApiQueryBuilder create(String url, Http httpMethod) {
        return new ApiQueryBuilder(url, httpMethod);
    }

    public ApiQueryBuilder atUrl(String url) {
        return atUrl(url, Http.GET);
    }

    public ApiQueryBuilder atUrl(String url, Http httpMethod) {
        this.url = url;
        this.httpMethod = httpMethod;
        return this;
    }

    public ApiQueryBuilder setHttpMethod(Http method) {
        this.httpMethod = method;
        return this;
    }

    public ApiQueryBuilder data(JSONObject data) {
        this.data = data;
        return this;
    }

    public ApiQuery buildQuery() {
        this.query = new ApiQuery(url, httpMethod, data);
        query.setUrlParams(urlParams);
        return query;
    }

    public ApiQuery getQuery() {
        return this.buildQuery();
    }

    @Override
    public String toString() {
        return query.toString();
    }

    public ApiQueryBuilder params(UrlParametersMap urlParams) {
        this.urlParams = urlParams;
        return this;
    }
}
