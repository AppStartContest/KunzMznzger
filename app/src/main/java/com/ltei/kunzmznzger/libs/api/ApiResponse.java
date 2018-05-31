package com.ltei.kunzmznzger.libs.api;

import org.json.simple.JSONArray;

/**
 * @author Robin
 * @date 20/06/2017
 */
public class ApiResponse
{
    private final int code;
    private final JSONArray json;

    public ApiResponse (int code, JSONArray json)
    {
        this.code = code;
        this.json = json;
    }

    public int getCode ()
    {
        return code;
    }

    public JSONArray getJson ()
    {
        return json;
    }
}
