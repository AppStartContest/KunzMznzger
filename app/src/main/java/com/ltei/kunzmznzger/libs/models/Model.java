package com.ltei.kunzmznzger.libs.models;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @author Robin
 * @date 12/06/2017
 */
public abstract class Model<T extends Model> implements Comparable<Model<T>>
{
    protected int id;

    @Override
    public int compareTo(Model<T> model) {
        return Integer.compare(id, model.id);
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    /**
     * Check whether the current model exists in the database or not
     * @return true if exists, false otherwise
     */
    public CompletableFuture<Boolean> existsInDatabase() {
        // We shouldn't have to check the database. The id can't be changed outside this package.
        return CompletableFuture.completedFuture(getId() > 0);
    }


    /**
     * Update the model or create it if it doesn't exist
     * @return the updated/created model
     */
    public CompletableFuture<T> save() {
        return this.existsInDatabase()
                .thenComposeAsync(exists -> {
                    if(exists)
                        return getManagerInstance().update(this);
                    else
                        return getManagerInstance().create(this);
                });
    }

    public CompletableFuture<T> delete() {
        return getManagerInstance().delete(this);
    }

    /**
     * Convert this to JSONObject
     * @return the created JSONObject
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", getId());

        return json;
    }


    // ABSTRACT

    /**
     * Copy the param into this
     * @param model the model to copy
     */
    protected abstract void recopy(T model);

    /**
     * @return the related DAO instance
     */
    public abstract ModelManager<T> getManagerInstance();
}
