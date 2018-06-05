package com.ltei.kunzmznzger.libs.models;

import com.ltei.kunzmznzger.libs.api.UrlParametersMap;
import com.ltei.kunzmznzger.libs.models.exceptions.RelationException;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Robin
 * @date 12/06/2017
 */
public abstract class Model<T extends Model> implements Comparable<Model<T>>
{
    public int id;

    protected ModelManager<T> manager;

    public Model() {
        this.manager = this.getManagerInstance();
    }

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
     *
     * @return true if exists, false otherwise
     */
    public CompletableFuture<Boolean> existsInDatabase() {
        // We shouldn't have to check the database. The id can't be changed outside this package.
        return CompletableFuture.completedFuture(getId() > 0);
    }

    public boolean isValid() {
        return id > 0;
    }


    /**
     * Update the model or create it if it doesn't exist
     *
     * @return the updated/created model
     */
    public CompletableFuture<T> save() {
        return this.existsInDatabase()
                .thenComposeAsync(exists -> {
                    if (exists)
                        return getManagerInstance().update(this);
                    else
                        return getManagerInstance().create(this);
                });
    }

    /**
     * Load a relation to this. The relation name is the name of the attribute but in snake_case.
     *
     * @param relation The relation name which is the name of the attribute but in snake_case
     * @return updated this in a CompletableFuture
     */
    public CompletableFuture<T> load(String relation) {
        return getManagerInstance().find(this.id, new UrlParametersMap().with(relation))
                .thenCompose((T t) -> CompletableFuture.supplyAsync(() -> this.copyRelation(relation, t)));
    }

    /**
     * Load a relation to this. The relation name is the name of the attribute but in snake_case.
     *
     * @param relation        The relation name which is the name of the attribute but in snake_case
     * @param nestedRelations additional parameters
     * @return updated this in a CompletableFuture
     */
    public CompletableFuture<T> load(String relation, String... nestedRelations) {
        UrlParametersMap params = new UrlParametersMap().with(relation);
        for (String nested : nestedRelations) {
            params.with(relation + '.' + nested);
        }

        return getManagerInstance().find(this.id, params.with(relation))
                .thenCompose((T t) -> CompletableFuture.supplyAsync(() -> this.copyRelation(relation, t)));
    }

    /**
     * <b>Kotlin example:</b><br/>
     * <code>
     * val map = hashMapOf(<br/>
     * &nbsp; &nbsp; "rooms" to listOf("users", "events", "expenses.messages.user"),<br/>
     * &nbsp; &nbsp; "expenses" to listOf("user", "room")<br/>
     * )<br/>
     * <br/>
     * user.load(map).thenAccept { ... }
     * </code>
     *
     * @param relationMap list of ArrayList. <br/>
     *                    For each arrayList, <code>[0]</code> => <b>primary relation</b>, <code>[1..n]</code> => <b>nested</b>
     * @return updated this in a CompletableFuture
     */
    public CompletableFuture<T> load(HashMap<String, List<String>> relationMap) {
        UrlParametersMap params = new UrlParametersMap();
        for (Map.Entry<String, List<String>> entry : relationMap.entrySet()) {
            String primary = entry.getKey();
            List<String> secondaries = entry.getValue();

            if (secondaries.isEmpty()) {
                params.with(primary);
            }

            for (String secondary : secondaries) {
                params.with(primary + '.' + secondary);
            }
        }

        return getManagerInstance().find(this.id, params)
                .thenCompose((T fetched) -> CompletableFuture.supplyAsync(() -> {
                    for (String relation : relationMap.keySet()) {
                        this.copyRelation(relation, fetched);
                    }
                    return (T) this;
                }));
    }

    protected abstract T copyRelation(String relation, T model);

    public CompletableFuture<T> delete() {
        return getManagerInstance().delete(this);
    }

    /**
     * Convert this to JSONObject
     *
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
     *
     * @param model the model to copy
     */
    public abstract void recopy(T model);

    /**
     * @return the related DAO instance
     */
    public abstract ModelManager<T> getManagerInstance();

    // Attach, sync, detach

    /**
     * Attach two models in the database. <br/>
     * <b>Both need to have a valid ID</b>
     *
     * @param toAttach the model to attach to <code>this</code>
     * @return Completable future with the updated <code>this</code> and the corresponding relation
     * @throws RelationException if <code>this</code> or <code>toAttach</code> have no ID
     */
    public CompletableFuture<T> attach(Model toAttach) throws RelationException {
        return this.attach(toAttach, null);
    }

    /**
     * Attach two models in the database. <br/>
     * <b>Both need to have a valid ID</b>
     *
     * @param toAttach the model to attach to <code>this</code>
     * @param data     the data that links these models (i.e {quantity: 15})
     * @return Completable future with the updated <code>this</code> and the corresponding relation
     * @throws RelationException if <code>this</code> or <code>toAttach</code> have no ID
     */
    public CompletableFuture<T> attach(Model toAttach, JSONObject data) throws RelationException {
        if (this.id < 1 || toAttach.id < 1) {
            throw new RelationException("Both models need to have a valid ID");
        }
        return this.manager.attach(this, toAttach, data);
    }

    /**
     * Detach to models in the database <br/>
     * <b>Both need to have a valid ID</b>
     *
     * @param toDetach the model to detach from <code>this</code>
     * @return Completable future with the updated <code>this</code> and the corresponding relation
     * @throws RelationException if <code>this</code> or <code>toAttach</code> have no ID
     */
    public CompletableFuture<T> detach(Model toDetach) throws RelationException {
        if (this.id < 1 || toDetach.id < 1) {
            throw new RelationException("Both models need to have a valid ID");
        }
        return this.manager.detach(this, toDetach);
    }

    /**
     * Sync to models in the database <br/>
     * <b>Both need to have a valid ID</b>
     *
     * @param toSync the model to sync to <code>this</code>
     * @return Completable future with the updated <code>this</code> and the corresponding relation
     * @throws RelationException if <code>this</code> or <code>toSync</code> have no ID
     */
    public CompletableFuture<T> sync(Model toSync) throws RelationException {
        return this.sync(toSync, false);
    }

    /**
     * Sync to models in the database without detaching <br/>
     * <b>Both need to have a valid ID</b>
     *
     * @param toSync           the model to sync to <code>this</code>
     * @param withoutDetaching true: don't detach already synced models, false otherwise (false is default behavior)
     * @return Completable future with the updated <code>this</code> and the corresponding relation
     * @throws RelationException if <code>this</code> or <code>toSync</code> have no ID
     */
    public CompletableFuture<T> sync(Model toSync, boolean withoutDetaching) throws RelationException {
        return this.sync(toSync, null, withoutDetaching);
    }

    /**
     * Sync to models in the database without detaching <br/>
     * <b>Both need to have a valid ID</b>
     *
     * @param toSync the model to sync to <code>this</code>
     * @param data   the data that links these models (i.e {quantity: 15})
     * @return Completable future with the updated <code>this</code> and the corresponding relation
     * @throws RelationException if <code>this</code> or <code>toSync</code> have no ID
     */
    public CompletableFuture<T> sync(Model toSync, JSONObject data) throws RelationException {
        return this.sync(toSync, data, false);
    }

    /**
     * Sync to models in the database without detaching <br/>
     * <b>Both need to have a valid ID</b>
     *
     * @param toSync           the model to sync to <code>this</code>
     * @param data             the data that links these models (i.e {quantity: 15})
     * @param withoutDetaching true: don't detach already synced models, false otherwise (false is default behavior)
     * @return Completable future with the updated <code>this</code> and the corresponding relation
     * @throws RelationException if <code>this</code> or <code>toSync</code> have no ID
     */
    public CompletableFuture<T> sync(Model toSync, JSONObject data, boolean withoutDetaching) throws RelationException {
        if (this.id < 1 || toSync.id < 1) {
            throw new RelationException("Both models need to have a valid ID");
        }
        return this.manager.sync(this, toSync, data, withoutDetaching);
    }

    protected void putFkIfRelationDefined(JSONObject json, String key, Model model) {
        if (model != null) {
            json.put(key, model.id);
        }
    }
}
