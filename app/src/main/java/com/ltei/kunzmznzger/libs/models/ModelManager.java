package com.ltei.kunzmznzger.libs.models;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.ltei.kunzmznzger.libs.Helpers;
import com.ltei.kunzmznzger.libs.api.ApiQuery;
import com.ltei.kunzmznzger.libs.api.ApiQueryBuilder;
import com.ltei.kunzmznzger.libs.api.ApiResponse;
import com.ltei.kunzmznzger.libs.api.Http;
import com.ltei.kunzmznzger.libs.api.UrlParametersMap;
import com.ltei.kunzmznzger.libs.configs.ApiConfig;
import com.ltei.kunzmznzger.libs.time.Date;
import com.ltei.kunzmznzger.libs.time.Time;

/**
 * @author Robin
 * @date 15/06/2017
 */
public abstract class ModelManager<T extends Model>
{
    public static DateTimeFormatter dateFormatter = Helpers.getDateFormatter();
    public static DateTimeFormatter timeFormatter = Helpers.getTimeFormatter();
    public static DateTimeFormatter dateTimeFormatter = Helpers.getDatetimeFormatter();

    /**
     * URL of the API
     */
    public static final String API_URL = "http://asc.robinmarechal.fr/api/";

    /**
     * Get last inserted record (base on the id)
     *
     * @return the last inserted record
     */
    public CompletableFuture<T> getLast() {
        return getLast(new UrlParametersMap());
    }

    /**
     * Get last inserted record (base on the id)
     *
     * @param parametersMap additional parameters
     * @return the last inserted record
     */
    public CompletableFuture<T> getLast(@NotNull UrlParametersMap parametersMap) {
        parametersMap = parametersMap.orderBy("-id").limit(1);

        ApiQuery query = ApiQueryBuilder.create(getUrl()).params(parametersMap).getQuery();
        return query.execute()
                .thenCompose(apiResponse -> CompletableFuture.supplyAsync(() -> {
                    JSONArray json = apiResponse.getJson();

                    ArrayList<T> list = new ArrayList<>();
                    for (Object obj : json) {
                        try {
                            list.add(this.buildFromJson(((JSONObject) obj)));
                        } catch (ReflectiveOperationException e) {
                            list.add(null);
                            e.printStackTrace();
                        }
                    }

                    return list.isEmpty() ? null : list.get(0);
                }));
    }

    /**
     * Find a record of an ID
     *
     * @param id            the wanted ID
     * @param parametersMap additional parameters
     * @return the record of this ID
     */
    public CompletableFuture<T> find(int id, @NotNull UrlParametersMap parametersMap) {
        ApiQuery query = ApiQueryBuilder.create(getUrl() + "/" + id).params(parametersMap).getQuery();
        return query.execute().thenCompose(apiResponse -> CompletableFuture.supplyAsync(() -> {
            try {
                JSONArray array = apiResponse.getJson();
                if (array != null && !((JSONObject) array.get(0)).isEmpty()) {
                    return buildFromJson((JSONObject) array.get(0));
                }

                return null;
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
                return null;
            }
        }));

    }


    /**
     * Find a record of an ID
     *
     * @param id the wanted ID
     * @return the record of this ID
     */
    public CompletableFuture<T> find(int id) {
        return find(id, new UrlParametersMap());
    }

    /**
     * Get all record of the table
     *
     * @param parametersMap additional parameters
     * @return a list of record of this table
     */
    public CompletableFuture<ArrayList<T>> all(@NotNull UrlParametersMap parametersMap) {
        ApiQuery q = ApiQueryBuilder.create(getUrl()).params(parametersMap).getQuery();
        return q.execute().thenCompose(apiResponse -> CompletableFuture.supplyAsync(() -> {
            ArrayList<T> list = new ArrayList<>();
            JSONArray array = apiResponse.getJson();

            array.forEach(jsonObject -> {
                try {
                    list.add(buildFromJson((JSONObject) jsonObject));
                } catch (ReflectiveOperationException e) {
                    list.add(null);
                    e.printStackTrace();
                }
            });

            return list;
        }));
    }


    /**
     * Get all record of the table
     *
     * @return a list of record of this table
     */
    public CompletableFuture<ArrayList<T>> all() {
        return all(new UrlParametersMap());
    }

    /**
     * Perform a custom request
     *
     * @param url the right part url (after http://....../api/)
     * @return A list of records
     */
    public CompletableFuture<ArrayList<T>> ofUrl(String url) {
        return ofUrl(url, new UrlParametersMap());
    }


    /**
     * Perform a custom request
     *
     * @param url              the right part url (after http://....../api/)
     * @param urlParametersMap additional parameters
     * @return A list of records
     */
    public CompletableFuture<ArrayList<T>> ofUrl(String url, UrlParametersMap urlParametersMap) {
        ApiQuery q = ApiQueryBuilder.create(ApiConfig.getApiUrl() + url).params(urlParametersMap).getQuery();

        return q.execute().thenCompose(apiResponse -> CompletableFuture.supplyAsync(() -> {
            ArrayList<T> list = new ArrayList<>();
            JSONArray json = apiResponse.getJson();
            for (Object obj : json) {
                try {
                    list.add(buildFromJson(((JSONObject) obj)));
                } catch (ReflectiveOperationException e) {
                    list.add(null);
                    e.printStackTrace();
                }
            }

            return list;
        }));
    }

    /**
     * Build the managed model class from the fetched JSON
     * @param json the JSON containing data to parse
     * @return the parsed model instance
     * @throws ReflectiveOperationException if reflection error occurs
     */
    protected T buildFromJson(JSONObject json) throws ReflectiveOperationException {
        Class<T> clazz = getModelInstanceClass();

        T instance = clazz.newInstance();

        for (Object item : json.keySet()) {
            String key = item.toString();
            Object value = json.get(key);

            if (value == null || key.equals("pivot")) {
                continue;
            }

            if (key.equals("id")) {
                instance.setId(Integer.valueOf(value.toString()).intValue());
                continue;
            }

            String camelCaseKey = Helpers.camelCase(key);
            Field field = clazz.getDeclaredField(camelCaseKey);

            Method setter;
            String setterName = "set" + camelCaseKey.substring(0, 1).toUpperCase() + camelCaseKey.substring(1);

            Class<?> fieldType = field.getType();

            if (fieldType.isAssignableFrom(Integer.class)) {
                int v = Integer.valueOf(value.toString());
                setter = clazz.getDeclaredMethod(setterName, int.class);
                setter.invoke(instance, v);
            }
            else if (fieldType.isAssignableFrom(Long.class)) {
                long v = Long.valueOf(value.toString());
                setter = clazz.getDeclaredMethod(setterName, long.class);
                setter.invoke(instance, v);
            }
            else if (fieldType.isAssignableFrom(Float.class)) {
                float v = Float.valueOf(value.toString());
                setter = clazz.getDeclaredMethod(setterName, float.class);
                setter.invoke(instance, v);
            }
            else if (fieldType.isAssignableFrom(Short.class)) {
                short v = Short.valueOf(value.toString());
                setter = clazz.getDeclaredMethod(setterName, short.class);
                setter.invoke(instance, v);
            }
            else if (fieldType.isAssignableFrom(Double.class)) {
                double v = Double.valueOf(value.toString());
                setter = clazz.getDeclaredMethod(setterName, double.class);
                setter.invoke(instance, v);
            }
            else if (fieldType.isAssignableFrom(Boolean.class)) {
                boolean v = Boolean.valueOf(value.toString());
                setter = clazz.getDeclaredMethod(setterName, boolean.class);
                setter.invoke(instance, v);
            }
            else if (fieldType.isAssignableFrom(Byte.class)) {
                byte v = Byte.valueOf(value.toString());
                setter = clazz.getDeclaredMethod(setterName, byte.class);
                setter.invoke(instance, v);
            }
            else if (fieldType.isAssignableFrom(Character.class)) {
                char v = value.toString().charAt(0);
                setter = clazz.getDeclaredMethod(setterName, char.class);
                setter.invoke(instance, v);
            }
            else if (fieldType.isAssignableFrom(String.class)) {
                setter = clazz.getDeclaredMethod(setterName, String.class);
                setter.invoke(instance, value.toString());
            }
            else if (fieldType.isAssignableFrom(Date.class)) {
                String strDate = value.toString();
                DateTime dateTime = DateTime.parse(strDate, ModelManager.dateFormatter);
                Date date = Date.of(dateTime);

                setter = clazz.getDeclaredMethod(setterName, Date.class);
                setter.invoke(instance, date);
            }
            else if (fieldType.isAssignableFrom(Time.class)) {
                String strTime = value.toString();
                DateTime dateTime = DateTime.parse(strTime, ModelManager.timeFormatter);
                Time time = Time.of(dateTime);

                setter = clazz.getDeclaredMethod(setterName, Time.class);
                setter.invoke(instance, time);
            }
            else if (fieldType.isAssignableFrom(DateTime.class)) {
                String strDateTime = value.toString();
                DateTime dateTime = DateTime.parse(strDateTime, ModelManager.dateTimeFormatter);

                setter = clazz.getDeclaredMethod(setterName, DateTime.class);
                setter.invoke(instance, dateTime);
            }
            else if (fieldType.isAssignableFrom(ArrayList.class)) // ArrayList<Model>
            {
                ParameterizedType listType = (ParameterizedType) field.getGenericType();

                String typeName = listType.getActualTypeArguments()[0].toString().replace("class ", "");

                String factoryClassName = Helpers.getManagerOfModel(typeName);

                Class<?> modelClazz = Class.forName(typeName);
                Class<?> factoryClazz = Class.forName(factoryClassName);

                ModelManager factory = (ModelManager) factoryClazz.newInstance();

                String addMethodName = Helpers.getAddingMethodName(camelCaseKey);
                Method addMethod = clazz.getDeclaredMethod(addMethodName, modelClazz);

                JSONArray array = (JSONArray) value;

                for (Object v : array) {
                    JSONObject vJson = ((JSONObject) v);

                    Object result = factory.buildFromJson(vJson);

                    addMethod.invoke(instance, modelClazz.cast(result));
                }
            }
            else if (fieldType.isAssignableFrom(RelationsMap.class)) {
                ParameterizedType listType = (ParameterizedType) field.getGenericType();

                Type[] typesArgs = listType.getActualTypeArguments();
                String modelClassName = typesArgs[0].toString().replace("class ", "");
                String pivotClassName = typesArgs[1].toString().replace("class ", "");

                String modelManagerClassName = Helpers.getManagerOfModel(modelClassName);
                String pivotManagerClassName = Helpers.getManagerOfModel(pivotClassName);

                Class<?> modelClazz = Class.forName(modelClassName);
                Class<?> pivotClazz = Class.forName(pivotClassName);

                Class<?> modelManagerClazz = Class.forName(modelManagerClassName);
                Class<?> pivotManagerClazz = Class.forName(pivotManagerClassName);

                ModelManager modelManager = (ModelManager) modelManagerClazz.newInstance();
                ModelManager pivotManager = (ModelManager) pivotManagerClazz.newInstance();

                String addMethodName = Helpers.getAddingMethodName(camelCaseKey);
                Method addMethod = clazz.getDeclaredMethod(addMethodName, modelClazz, pivotClazz);

                JSONArray array = (JSONArray) value;

                for (Object v : array) {
                    JSONObject modelJson = ((JSONObject) v);
                    Object model = modelManager.buildFromJson(modelJson);

                    JSONObject pivotJson = (JSONObject) modelJson.get("pivot");
                    Object pivot = pivotJson == null ? null : pivotManager.buildFromJson(pivotJson);

                    addMethod.invoke(instance, modelClazz.cast(model), pivotClazz.cast(pivot));
                }
            }
            else if (Helpers.isModel(fieldType)) { // Model
                String objName = field.getType().getName();

                String managerClassName = Helpers.getManagerOfModel(objName);

                Class<?> modelClazz = Class.forName(objName);
                Class<?> managerClazz = Class.forName(managerClassName);

                ModelManager manager = (ModelManager) managerClazz.newInstance();

                Method managerBuildMethod = managerClazz.getMethod("buildFromJson", JSONObject.class);

                Object result = managerBuildMethod.invoke(manager, ((JSONObject) value));

                setter = clazz.getDeclaredMethod(setterName, modelClazz);
                setter.invoke(instance, modelClazz.cast(result));
            }
        }

        return instance;
    }

    /**
     * Get resource's namespace (http://....../api/{resource}) <br/>
     * Ex: for URL like <b>http://.../api/users</b>, the resource is <b>users</b>
     * @return the resource's namespace
     */
    public abstract String getNamespace();

    /**
     * Get the complete URL (base API url + namespace)
     * @return the complete URL
     */
    public String getUrl() {
        return ModelManager.API_URL + this.getNamespace();
    }

    /**
     * Get the model's class
     * Ex: <code>UserDAO</code> class should return something like <code>User.class</code>
     * @return the model's class
     */
    protected abstract Class<T> getModelInstanceClass();

    /**
     * Create a model in the database
     * @param model the new model to create
     * @return the created model
     */
    public CompletableFuture<T> create(Model<T> model) {
        ApiQuery query = new ApiQueryBuilder(getUrl(), Http.POST).setData(model.toJson()).getQuery();
        System.out.println(query);

        return query.execute().thenCompose(apiResponse -> CompletableFuture.supplyAsync(() -> this.handleApiResponse(apiResponse)));

    }

    /**
     * Update a model in the database
     * @param model the model to update
     * @return the updated model
     */
    public CompletableFuture<T> update(Model<T> model) {
        ApiQuery query = new ApiQueryBuilder(getUrl() + "/" + model.getId(), Http.PUT).setData(model.toJson()).getQuery();
        return query.execute().thenCompose(apiResponse -> CompletableFuture.supplyAsync(() -> this.handleApiResponse(apiResponse)));
    }

    /**
     * Delete a model from the database
     * @param model the model to delete
     * @return true if everything was ok, false otherwise
     */
    public CompletableFuture<Boolean> delete(Model<T> model) {
        ApiQuery query = new ApiQueryBuilder(getUrl() + "/" + model.getId(), Http.DELETE).getQuery();
        return query.execute().thenCompose(apiResponse -> CompletableFuture.supplyAsync(() -> {
            if (apiResponse.getCode() >= 400) {
                return false;
            }

            JSONArray json = apiResponse.getJson();

            return json != null && !json.isEmpty() && !((JSONObject) json.get(0)).isEmpty();
        }));
    }

    /**
     * Handle API response and parse JSON
     * @param apiResponse the response to handle
     * @return the parse model, or null
     */
    private T handleApiResponse(ApiResponse apiResponse) {
        if (apiResponse.getCode() >= 400) {
            return null;
        }

        JSONArray json = apiResponse.getJson();

        if (json == null || json.isEmpty() || ((JSONObject) json.get(0)).isEmpty()) {
            return null;
        }

        T res = null;
        try {
            res = buildFromJson(((JSONObject) json.get(0)));
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return res;
    }
}
