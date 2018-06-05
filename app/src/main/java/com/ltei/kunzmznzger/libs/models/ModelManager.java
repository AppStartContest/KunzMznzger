package com.ltei.kunzmznzger.libs.models;

import android.support.annotation.Nullable;

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
    public CompletableFuture<T> findLast() {
        return this.findLast(new UrlParametersMap());
    }

    /**
     * Get last inserted record (base on the id)
     *
     * @param parametersMap additional parameters
     * @return the last inserted record
     */
    public CompletableFuture<T> findLast(@NotNull UrlParametersMap parametersMap) {
        parametersMap = parametersMap.orderBy("-id").limit(1);

        ApiQuery query = ApiQueryBuilder.create(url()).params(parametersMap).getQuery();
        return query.execute().thenCompose(apiResponse -> CompletableFuture.supplyAsync(() -> {
            ArrayList<T> list = handleResponseList(apiResponse);
            if (list == null || list.isEmpty())
                return null;
            return list.get(0);
        }));
//                    handleResponseList(apiResponse);
//                    JSONArray json = apiResponse.getJson();
//
//                    ArrayList<T> list = new ArrayList<>();
//                    for (Object obj : json) {
//                        try {
//                            list.add(this.buildFromJson(((JSONObject) obj)));
//                        } catch (ReflectiveOperationException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    return list.isEmpty() ? null : list.get(0);
//                }));
    }

    /**
     * Find a record of an ID
     *
     * @param id            the wanted ID
     * @param parametersMap additional parameters
     * @return the record of this ID
     */
    public CompletableFuture<T> find(int id, @NotNull UrlParametersMap parametersMap) {
        ApiQuery query = ApiQueryBuilder.create(url() + "/" + id).params(parametersMap).getQuery();
        return query.execute().thenCompose(apiResponse -> CompletableFuture.supplyAsync(() -> handleResponseObject(apiResponse)));
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
        ApiQuery q = ApiQueryBuilder.create(url()).params(parametersMap).getQuery();
        return q.execute().thenCompose(apiResponse -> CompletableFuture.supplyAsync(() -> handleResponseList(apiResponse)));
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
        return q.execute().thenCompose(apiResponse -> CompletableFuture.supplyAsync(() -> handleResponseList(apiResponse)));
    }

    /**
     * Build the managed model class from the fetched JSON
     *
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

            Field field;
            try {
                field = clazz.getDeclaredField(camelCaseKey);
            } catch (ReflectiveOperationException e) {
                Helpers.log(
                        String.format("No field '%s' (for json field '%s') in model class %s", camelCaseKey, key, clazz.getSimpleName()),
                        "BUILD_FROM_JSON"
                );
                continue;
            }

            Method setter;
            String setterName = "set" + camelCaseKey.substring(0, 1).toUpperCase() + camelCaseKey.substring(1);

            Class<?> fieldType = field.getType();

            if (fieldType.isAssignableFrom(Integer.class)) {
                int v = Integer.valueOf(value.toString());
                setter = clazz.getDeclaredMethod(setterName, Integer.class);
                setter.invoke(instance, v);
            }
            else if (fieldType.isAssignableFrom(Long.class)) {
                long v = Long.valueOf(value.toString());
                setter = clazz.getDeclaredMethod(setterName, Long.class);
                setter.invoke(instance, v);
            }
            else if (fieldType.isAssignableFrom(Float.class)) {
                float v = Float.valueOf(value.toString());
                setter = clazz.getDeclaredMethod(setterName, Float.class);
                setter.invoke(instance, v);
            }
            else if (fieldType.isAssignableFrom(Short.class)) {
                short v = Short.valueOf(value.toString());
                setter = clazz.getDeclaredMethod(setterName, Short.class);
                setter.invoke(instance, v);
            }
            else if (fieldType.isAssignableFrom(Double.class)) {
                double v = Double.valueOf(value.toString());
                setter = clazz.getDeclaredMethod(setterName, Double.class);
                setter.invoke(instance, v);
            }
            else if (fieldType.isAssignableFrom(Boolean.class)) {
                boolean v = Boolean.valueOf(value.toString());
                setter = clazz.getDeclaredMethod(setterName, Boolean.class);
                setter.invoke(instance, v);
            }
            else if (fieldType.isAssignableFrom(Byte.class)) {
                byte v = Byte.valueOf(value.toString());
                setter = clazz.getDeclaredMethod(setterName, Byte.class);
                setter.invoke(instance, v);
            }
            else if (fieldType.isAssignableFrom(Character.class)) {
                char v = value.toString().charAt(0);
                setter = clazz.getDeclaredMethod(setterName, Character.class);
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
     *
     * @return the resource's namespace
     */
    public abstract String getNamespace();

    /**
     * Get the complete URL (base API url + namespace)
     *
     * @return the complete URL
     */
    public String url() {
        return ModelManager.API_URL + this.getNamespace();
    }

    /**
     * Get the model's class
     * Ex: <code>UserDAO</code> class should return something like <code>User.class</code>
     *
     * @return the model's class
     */
    protected abstract Class<T> getModelInstanceClass();

    /**
     * Create a model in the database
     *
     * @param model the new model to create
     * @return the created model
     */
    public CompletableFuture<T> create(Model<T> model) {
        ApiQuery query = new ApiQueryBuilder(url(), Http.POST).data(model.toJson()).getQuery();
        System.out.println(query);
        return query.execute().thenCompose(apiResponse -> CompletableFuture.supplyAsync(() -> this.handleResponseObject(apiResponse)));
    }

    /**
     * Update a model in the database
     *
     * @param model the model to update
     * @return the updated model
     */
    public CompletableFuture<T> update(Model<T> model) {
        ApiQuery query = new ApiQueryBuilder(url() + "/" + model.getId(), Http.PUT).data(model.toJson()).getQuery();
        return query.execute().thenCompose(apiResponse -> CompletableFuture.supplyAsync(() -> this.handleResponseObject(apiResponse)));
    }

    /**
     * Delete a model from the database
     *
     * @param model the model to delete
     * @return true if everything was ok, false otherwise
     */
    public CompletableFuture<T> delete(Model<T> model) {
        ApiQuery query = new ApiQueryBuilder(url() + "/" + model.getId(), Http.DELETE).getQuery();
        return query.execute().thenCompose(apiResponse -> CompletableFuture.supplyAsync(() -> handleResponseObject(apiResponse)));
    }


    /**
     * Handle API response and parse JSON
     *
     * @param apiResponse the response to handle
     * @return the parse model, or null
     */
    @Nullable
    protected T handleResponseObject(@NotNull ApiResponse apiResponse) {
        if (apiResponse.getCode() >= 300) {
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

    @Nullable
    private ArrayList<T> handleResponseList(@NotNull ApiResponse apiResponse) {
        if (apiResponse.getCode() >= 300) {
            return null;
        }

        ArrayList<T> list = new ArrayList<>();
        JSONArray array = apiResponse.getJson();

        for (Object obj : array) {
            try {
                list.add(buildFromJson(((JSONObject) obj)));
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    // ----- Attach, detach, sync

    CompletableFuture<T> attach(Model left, Model right, JSONObject data) {
        UrlParametersMap params = this.buildAttachDetachOrSyncUrlParams(right);
        return this.AttachDetachOrSync(left, right, data, params, Http.POST); // Attach = POST request
    }

    CompletableFuture<T> sync(Model left, Model right, JSONObject data, boolean withoutDetaching) {
        UrlParametersMap params = this.buildAttachDetachOrSyncUrlParams(right).set("syncWithoutDetaching", withoutDetaching);
        return this.AttachDetachOrSync(left, right, data, params, Http.PUT); // Sync = PUT request
    }

    CompletableFuture<T> detach(Model left, Model right) {
        UrlParametersMap params = this.buildAttachDetachOrSyncUrlParams(right);
        return this.AttachDetachOrSync(left, right, null, params, Http.DELETE); // Sync = PUT request
    }

    private UrlParametersMap buildAttachDetachOrSyncUrlParams(Model right) {
        String rightNamespace = right.getManagerInstance().getNamespace();
        return new UrlParametersMap().with(rightNamespace);
    }

    private CompletableFuture<T> AttachDetachOrSync(Model left, Model right, JSONObject data, UrlParametersMap params, Http method) {
        String url = url() + "/" +
                left.getId() + "/" +
                right.getManagerInstance().getNamespace() + "/" +
                right.getId();

        // Sync = PUT method
        ApiQuery query = new ApiQueryBuilder(url, method).params(params).data(data).getQuery();
        return query.execute().thenCompose(apiResponse -> CompletableFuture.supplyAsync(() -> this.handleResponseObject(apiResponse)));
    }
}
