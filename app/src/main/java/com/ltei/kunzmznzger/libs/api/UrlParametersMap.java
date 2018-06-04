package com.ltei.kunzmznzger.libs.api;


import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import com.ltei.kunzmznzger.libs.Helpers;
import com.ltei.kunzmznzger.libs.api.parameters.Where;

/**
 * @author Robin
 * @date 26/06/2017
 */
public class UrlParametersMap extends HashMap<String, Object>
{
    private static String KW_WHERE = "where[]";
    private static String KW_WITH = "with";
    private static String KW_ORDER_BY = "orderby";
    private static String KW_SELECT = "select";

    private static String KW_LIMIT = "limit";
    private static String KW_OFFSET = "offset";
    private static String KW_FROM = "from";
    private static String KW_TO = "to";
    private static String KW_DISTINCT = "distinct";
    private static String KW_ALL = "all";
    private static String KW_WITH_ALL = "*";
    private static String KW_SYNC_WITHOUT_DETACHING = "sync_without_detaching";

    private String query = "";

    /**
     * Order by a field. add a '-' before the field for a descending order. <br/>
     * Ex: <code>new UrlParametersMap().orderBy("-age", "name")</code>
     *
     * @param fields a list of fields (with or without a '-')
     * @return this
     */
    public UrlParametersMap orderBy(String... fields) {
        ArrayList<String> orders = (ArrayList<String>) this.getOrDefault(KW_ORDER_BY, new ArrayList<String>());
        orders.addAll(Arrays.asList(fields));
        this.set(KW_ORDER_BY, orders);
        return this;
    }

    /**
     * Load all relations
     *
     * @return this
     */
    public UrlParametersMap forceLoadAll() {
        this.set(KW_ALL, "true");
        return this;
    }

    /**
     * Perform a SELECT DISTINCT query
     *
     * @return this
     */
    public UrlParametersMap distinct() {
        this.set(KW_DISTINCT, "true");
        return this;
    }

    /**
     * Perform a sync query without detaching already synced models
     *
     * @return this
     */
    public UrlParametersMap syncWithoutDetaching() {
        set(KW_SYNC_WITHOUT_DETACHING, "true");
        return this;
    }

    /**
     * Limit the number of record to fetch (equivalent to sql LIMIT and OFFSET)
     *
     * @param limit  the max number of records to get
     * @param offset the number of records to skip
     * @return this
     */
    public UrlParametersMap limit(int limit, int offset) {
        this.limit(limit);
        this.offset(offset);
        return this;
    }

    /**
     * Limit the number of record to fetch (equivalent to sql LIMIT)
     *
     * @param limit the max number of records to get
     * @return
     */
    public UrlParametersMap limit(int limit) {
        set(KW_LIMIT, limit);
        return this;
    }

    /**
     * Limit the number of record to fetch (equivalent to sql OFFSET)
     *
     * @param offset the number of records to skip
     * @return
     */
    public UrlParametersMap offset(int offset) {
        set(KW_OFFSET, offset);
        return this;
    }

    /**
     * Select models from a certain date
     *
     * @param from the from date
     * @return this
     */
    public UrlParametersMap from(DateTime from) {
        set(KW_FROM, from);
        return this;
    }

    /**
     * Select models until a certain date
     *
     * @param to the until date
     * @return this
     */
    public UrlParametersMap to(DateTime to) {
        this.set(KW_TO, to);
        return this;
    }

    /**
     * Select record with a specific date
     *
     * @param date the date
     * @return this
     */
    public UrlParametersMap ofDate(Date date) {
        this.ofDate(new DateTime(date));
        return this;
    }

    /**
     * Select record with a specific date
     *
     * @param date the date
     * @return this
     */
    public UrlParametersMap ofDate(DateTime date) {
        this.from(date);
        this.to(date.plusDays(1));
        return this;
    }

    /**
     * Select specific fields instead of all <b/>
     *
     * @param fields
     * @return
     */
    public UrlParametersMap select(String... fields) {
        set(KW_SELECT, Helpers.arrayJoin(fields, ","));
        return this;
    }

    /**
     * Add equal conditions to the request <br/>
     * Ex: <code>new UrlParametersMap().where("age", 18)</code> equivalent to <code>WHERE age = 18</code>
     *
     * @param left  field|value
     * @param right value|field
     * @return this
     */
    public UrlParametersMap where(String left, Object right) {
        ArrayList<Where> wheres = (ArrayList<Where>) this.getOrDefault(KW_WHERE, new ArrayList<Where>());
        wheres.add(Where.make(left, right.toString()));
        set(KW_WHERE, wheres);
        return this;
    }

    /**
     * Add equal conditions to the request <br/>
     * Ex: <code>new UrlParametersMap().where("age", ">", 18).where("age", <=, "30")</code> equivalent to <code>WHERE age > 18 AND age <= 30</code>
     *
     * @param left     field|value
     * @param operator the operator (=, !=, <, >, <=, >=, LIKE....)
     * @param right    value|field
     * @return this
     */
    public UrlParametersMap where(String left, String operator, Object right) {
        ArrayList<Where> wheres = (ArrayList<Where>) this.getOrDefault(KW_WHERE, new ArrayList<Where>());
        wheres.add(Where.make(left, operator, right.toString()));
        set(KW_WHERE, wheres);
        return this;
    }

    /**
     * Load specific relations. The relations can be nested, and it's possible to select attributes of the relations <br/>
     * Ex: <code>new UrlParametersMap().with("posts", "friends")</code> <br/>
     * Ex: <code>new UrlParametersMap().with("posts.comments:id,title", "friends:id,name,email")</code> <br/>
     * The above example will load the <b>posts</b>, and for each post, it will load its <b>comments</b> with only <b>id</b> and <b>title</b> fields
     * @implSpec If selecting fields, the ID should always be included.
     * @param relations the relation to load
     * @return
     */
    public UrlParametersMap with(String ...relations) {
        ArrayList<String> with = (ArrayList<String>) this.getOrDefault(KW_WITH, new ArrayList<String>());
        with.addAll(Arrays.asList(relations));
        set(KW_WITH, with);
        return this;
    }

    /**
     * Load all direct relations
     * @return this
     */
    public UrlParametersMap withAllRelations() {
        ArrayList<String> list = new ArrayList<>();
        list.add(KW_WITH_ALL);
        this.set(KW_WITH, list);
        return this;
    }

    /**
     * Set a query param-value couple. Shouldn't be used, expect for custom query parameters
     * @param param query keyword
     * @param value query value
     * @return this
     */
    public UrlParametersMap set(String param, Object value) {
        put(param, value);
        return this;
    }

    /**
     * Build URL query
     * @return the URL query
     */
    @Override
    public String toString() {
        return "?" + this.buildQuery();
    }

    /**
     * Build the URL query
     * @return the built URL query
     */
    private String buildQuery() {

        if (isEmpty()) {
            return "";
        }

        // WHERE
        if (containsKey(KW_WHERE)) {
            ArrayList<Where> wheres = (ArrayList<Where>) get(KW_WHERE);
            for (Where w : wheres) {
                this.appendToQuery(KW_WHERE + "=" + w.toString());
            }
            remove(KW_WHERE);
        }

        // WITH
        if (containsKey(KW_WITH)) {
            ArrayList<String> withs = (ArrayList<String>) get(KW_WITH);
            String wStr = KW_WITH + "=" + Helpers.arrayJoin(withs.toArray(), ";");
            this.appendToQuery(wStr);
            remove(KW_WITH);
        }

        // ORDER BY
        appendList(KW_ORDER_BY);

        // SELECT
        appendList(KW_SELECT);

        for (Entry<String, Object> entry : this.entrySet()) {
            String str = String.format("%s=%s", entry.getKey(), entry.getValue().toString());
            this.appendToQuery(str);
        }

        return query;
    }

    private void appendList(String key) {
        if (containsKey(key)) {
            ArrayList<String> list = (ArrayList<String>) get(key);
            String str = key + "=" + Helpers.arrayJoin(list.toArray(), ",");
            this.appendToQuery(str);
            remove(key);
        }
    }

    private void appendToQuery(String toAppend) {
        if (this.query.length() > 0) {
            this.query += "&";
        }

        this.query += toAppend;
    }
}
