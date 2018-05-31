package com.ltei.kunzmznzger.libs.configs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * @author Robin
 * @date 22/06/2017
 */
public class ApiConfig
{
    private static String apiUrl = "";
    private static String appKey = "";

    private static String filePath = "/configs/api.properties";

    public static String getApiUrl ()
    {
        return apiUrl;
    }

    public static void setApiUrl (String apiUrl)
    {
        ApiConfig.apiUrl = apiUrl;
    }

    public static String getAppKey ()
    {
        return appKey;
    }

    public static void setAppKey (String appKey)
    {
        ApiConfig.appKey = appKey;
    }

    public static void load ()
    {
        Properties p = new Properties();
        try
        {
            String path = new URI(ApiConfig.class.getResource(filePath)
                                                 .toString()).getPath();
            FileInputStream in = new FileInputStream(path);

            p.load(in);
            apiUrl = p.getProperty("API_URL");
            appKey = p.getProperty("APP_KEY");

            in.close();
            System.out.println("API configs loaded");
        }
        catch (IOException | NullPointerException | URISyntaxException e)
        {
            System.err.println("CONFIG ERROR >>> API properties file not found...");
            e.printStackTrace();
        }
    }

    public static void save ()
    {
        Properties p = new Properties();

        try
        {
            String path = new URI(ApiConfig.class.getResource(filePath)
                                                 .toString()).getPath();
            FileOutputStream out = new FileOutputStream(path);

            p.setProperty("API_URL", apiUrl);
            p.setProperty("APP_KEY", appKey);

            p.store(out, null);
            out.close();
            System.out.println("API configs saved");
        }
        catch (IOException | NullPointerException | URISyntaxException e)
        {
            System.err.println("CONFIG ERROR >>> API properties file not found...");
            e.printStackTrace();
        }
    }
}
