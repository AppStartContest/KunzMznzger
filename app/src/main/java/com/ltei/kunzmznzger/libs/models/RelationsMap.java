package com.ltei.kunzmznzger.libs.models;

import org.jetbrains.annotations.NotNull;

import java.util.*;


/**
 * @author Robin
 * @date 29/06/2017
 */
public class RelationsMap<M extends Model<M>, P extends Model<P>> implements Map<M, P>
{

    private List<Pair<M, P>> pairs = new ArrayList<>();

    /**
     * Returns the number of model-pivot mappings in this map.  If the
     * map contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of model-pivot mappings in this map
     */
    @Override
    public int size ()
    {
        return pairs.size();
    }

    /**
     * Returns <tt>true</tt> if this map contains no model-pivot mappings.
     *
     * @return <tt>true</tt> if this map contains no model-pivot mappings
     */
    @Override
    public boolean isEmpty ()
    {
        return pairs.isEmpty();
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the specified
     * model.  More formally, returns <tt>true</tt> if and select if
     * this map contains a mapping for a model <tt>k</tt> such that
     * <tt>(model==null ? k==null : model.equals(k))</tt>.  (There can be
     * at most one such mapping.)
     *
     * @param key model whose presence in this map is to be tested
     * @return <tt>true</tt> if this map contains a mapping for the specified
     * model
     * @throws ClassCastException   if the model is of an inappropriate type for
     *                              this map
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified model is null and this map
     *                              does not permit null keys
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean containsKey (Object key)
    {
        for (Pair<M, P> node : pairs)
        {
            if (node.getKey()
                    .equals(key))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns <tt>true</tt> if this map maps one or more keys to the
     * specified pivot.  More formally, returns <tt>true</tt> if and select if
     * this map contains at least one mapping to a pivot <tt>v</tt> such that
     * <tt>(pivot==null ? v==null : pivot.equals(v))</tt>.  This operation
     * will probably require time linear in the map size for most
     * implementations of the <tt>Map</tt> interface.
     *
     * @param value pivot whose presence in this map is to be tested
     * @return <tt>true</tt> if this map maps one or more keys to the
     * specified pivot
     * @throws ClassCastException   if the pivot is of an inappropriate type for
     *                              this map
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified pivot is null and this
     *                              map does not permit null values
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean containsValue (Object value)
    {
        for (Pair<M, P> node : pairs)
        {
            if (node.getValue()
                    .equals(value))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the pivot to which the specified model is mapped,
     * or {@code null} if this map contains no mapping for the model.
     *
     * <p>More formally, if this map contains a mapping from a model
     * {@code k} to a pivot {@code v} such that {@code (model==null ? k==null :
     * model.equals(k))}, then this method returns {@code v}; otherwise
     * it returns {@code null}.  (There can be at most one such mapping.)
     *
     * <p>If this map permits null values, then a return pivot of
     * {@code null} does not <i>necessarily</i> indicate that the map
     * contains no mapping for the model; it's also possible that the map
     * explicitly maps the model to {@code null}.  The {@link #containsKey
     * containsKey} operation may be used to distinguish these two cases.
     *
     * @param key the model whose associated pivot is to be returned
     * @return the pivot to which the specified model is mapped, or
     * {@code null} if this map contains no mapping for the model
     * @throws ClassCastException   if the model is of an inappropriate type for
     *                              this map
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified model is null and this map
     *                              does not permit null keys
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public P get (Object key)
    {
        for (Pair<M, P> node : pairs)
        {
            if (node.getKey()
                    .equals(key))
            {
                return node.getValue();
            }
        }

        return null;
    }

    /**
     * Associates the specified pivot with the specified model in this map
     * (optional operation).  If the map previously contained a mapping for
     * the model, the old pivot is replaced by the specified pivot.  (A map
     * <tt>m</tt> is said to contain a mapping for a model <tt>k</tt> if and select
     * if {@link #containsKey(Object) m.containsKey(k)} would return
     * <tt>true</tt>.)
     *
     * @param key   model with which the specified pivot is to be associated
     * @param value pivot to be associated with the specified model
     * @return the previous pivot associated with <tt>model</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>model</tt>.
     * (A <tt>null</tt> return can also indicate that the map
     * previously associated <tt>null</tt> with <tt>model</tt>,
     * if the implementation supports <tt>null</tt> values.)
     * @throws UnsupportedOperationException if the <tt>put</tt> operation
     *                                       is not supported by this map
     * @throws ClassCastException            if the class of the specified model or pivot
     *                                       prevents it from being stored in this map
     * @throws NullPointerException          if the specified model or pivot is null
     *                                       and this map does not permit null keys or values
     * @throws IllegalArgumentException      if some property of the specified model
     *                                       or pivot prevents it from being stored in this map
     */
    @Override
    public P put (M key, P value)
    {
        int index    = -1;
        P   oldValue = null;

        for (int i = 0; i < pairs.size(); i++)
        {
            if (pairs.get(i)
                     .getKey()
                     .equals(key))
            {
                index = i;
                oldValue = pairs.get(i)
                                .getValue();
                break;
            }
        }

        Pair<M, P> node = new Pair<>(key);
        node.setValue(value);

        if (index == -1)
        {
            pairs.add(node);
        }
        else
        {
            pairs.set(index, node);
        }

        return oldValue;
    }

    /**
     * Removes the mapping for a model from this map if it is present
     * (optional operation).   More formally, if this map contains a mapping
     * from model <tt>k</tt> to pivot <tt>v</tt> such that
     * <code>(model==null ?  k==null : model.equals(k))</code>, that mapping
     * is removed.  (The map can contain at most one such mapping.)
     *
     * <p>Returns the pivot to which this map previously associated the model,
     * or <tt>null</tt> if the map contained no mapping for the model.
     *
     * <p>If this map permits null values, then a return pivot of
     * <tt>null</tt> does not <i>necessarily</i> indicate that the map
     * contained no mapping for the model; it's also possible that the map
     * explicitly mapped the model to <tt>null</tt>.
     *
     * <p>The map will not contain a mapping for the specified model once the
     * call returns.
     *
     * @param key model whose mapping is to be removed from the map
     * @return the previous pivot associated with <tt>model</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>model</tt>.
     * @throws UnsupportedOperationException if the <tt>remove</tt> operation
     *                                       is not supported by this map
     * @throws ClassCastException            if the model is of an inappropriate type for
     *                                       this map
     *                                       (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException          if the specified model is null and this
     *                                       map does not permit null keys
     *                                       (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public P remove (Object key)
    {
        return null;
    }

    /**
     * Copies all of the mappings from the specified map to this map
     * (optional operation).  The effect of this call is equivalent to that
     * of calling {@link #put(Object, Object) put(k, v)} on this map once
     * for each mapping from model <tt>k</tt> to pivot <tt>v</tt> in the
     * specified map.  The behavior of this operation is undefined if the
     * specified map is modified while the operation is in progress.
     *
     * @param m mappings to be stored in this map
     * @throws UnsupportedOperationException if the <tt>putAll</tt> operation
     *                                       is not supported by this map
     * @throws ClassCastException            if the class of a model or pivot in the
     *                                       specified map prevents it from being stored in this map
     * @throws NullPointerException          if the specified map is null, or if
     *                                       this map does not permit null keys or values, and the
     *                                       specified map contains null keys or values
     * @throws IllegalArgumentException      if some property of a model or pivot in
     *                                       the specified map prevents it from being stored in this map
     */
    @Override
    public void putAll (@NotNull Map<? extends M, ? extends P> m)
    {
        m.forEach((k, v) -> put(k, v));
    }

    /**
     * Removes all of the mappings from this map (optional operation).
     * The map will be empty after this call returns.
     *
     * @throws UnsupportedOperationException if the <tt>clear</tt> operation
     *                                       is not supported by this map
     */
    @Override
    public void clear ()
    {
        pairs.clear();
    }

    /**
     * Returns a {@link Set} view of the keys contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own <tt>remove</tt> operation), the results of
     * the iteration are undefined.  The set supports element removal,
     * which removes the corresponding mapping from the map, via the
     * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>,
     * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt>
     * operations.  It does not support the <tt>add</tt> or <tt>addAll</tt>
     * operations.
     *
     * @return a set view of the keys contained in this map
     */
    @NotNull
    @Override
    public Set<M> keySet ()
    {
        Set<M> set = new TreeSet<>();

        pairs.forEach(node -> set.add(node.getKey()));

        return set;
    }

    /**
     * Returns a {@link Collection} view of the values contained in this map.
     * The collection is backed by the map, so changes to the map are
     * reflected in the collection, and vice-versa.  If the map is
     * modified while an iteration over the collection is in progress
     * (except through the iterator's own <tt>remove</tt> operation),
     * the results of the iteration are undefined.  The collection
     * supports element removal, which removes the corresponding
     * mapping from the map, via the <tt>Iterator.remove</tt>,
     * <tt>Collection.remove</tt>, <tt>removeAll</tt>,
     * <tt>retainAll</tt> and <tt>clear</tt> operations.  It does not
     * support the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a collection view of the values contained in this map
     */
    @NotNull
    @Override
    public Collection<P> values ()
    {
        Collection<P> collection = new ArrayList<>();

        pairs.forEach(node -> collection.add(node.getValue()));

        return collection;
    }

    @Override
    public String toString ()
    {
        return "RelationWithPivot{" + "pairList=" + pairs + '}';
    }

    /**
     * Returns a {@link Set} view of the mappings contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own <tt>remove</tt> operation, or through the
     * <tt>setValue</tt> operation on a map entry returned by the
     * iterator) the results of the iteration are undefined.  The set
     * supports element removal, which removes the corresponding
     * mapping from the map, via the <tt>Iterator.remove</tt>,
     * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and
     * <tt>clear</tt> operations.  It does not support the
     * <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a set view of the mappings contained in this map
     */
    @NotNull
    @Override
    public Set<Entry<M, P>> entrySet ()
    {
        return new TreeSet<>(pairs);
    }

    public List<M> getModelList ()
    {
        return new ArrayList<>(keySet());
    }

    public List<P> getPivotList ()
    {
        return new ArrayList<>(values());
    }

    public Pair<M, P> get (int index)
    {
        return pairs.get(index);
    }

    public M getModel (int index)
    {
        return pairs.get(index)
                    .getModel();
    }

    public P getPivot (int index)
    {
        return pairs.get(index)
                    .getPivot();
    }

    public void addModels (ArrayList<M> models)
    {
        models.forEach(m -> pairs.add(new Pair<M, P>(m, null)));
    }

    public List<Pair<M, P>> pairList ()
    {
        return pairs;
    }


    public static class Pair<M, P> implements Map.Entry<M, P>
    {
        private final M model;
        private P pivot;

        public Pair (M key, P value)
        {
            this.model = key;
            this.pivot = value;
        }

        public Pair (M key)
        {
            this.model = key;
        }

        /**
         * Returns the model corresponding to this entry.
         *
         * @return the model corresponding to this entry
         * @throws IllegalStateException implementations may, but are not
         *                               required to, throw this exception if the entry has been
         *                               removed from the backing map.
         */
        @Override
        public M getKey ()
        {
            return model;
        }

        /**
         * Returns the pivot corresponding to this entry.  If the mapping
         * has been removed from the backing map (by the iterator's
         * <tt>remove</tt> operation), the results of this call are undefined.
         *
         * @return the pivot corresponding to this entry
         * @throws IllegalStateException implementations may, but are not
         *                               required to, throw this exception if the entry has been
         *                               removed from the backing map.
         */
        @Override
        public P getValue ()
        {
            return pivot;
        }

        /**
         * Replaces the pivot corresponding to this entry with the specified
         * pivot (optional operation).  (Writes through to the map.)  The
         * behavior of this call is undefined if the mapping has already been
         * removed from the map (by the iterator's <tt>remove</tt> operation).
         *
         * @param value new pivot to be stored in this entry
         * @return old pivot corresponding to the entry
         * @throws UnsupportedOperationException if the <tt>put</tt> operation
         *                                       is not supported by the backing map
         * @throws ClassCastException            if the class of the specified pivot
         *                                       prevents it from being stored in the backing map
         * @throws NullPointerException          if the backing map does not permit
         *                                       null values, and the specified pivot is null
         * @throws IllegalArgumentException      if some property of this pivot
         *                                       prevents it from being stored in the backing map
         * @throws IllegalStateException         implementations may, but are not
         *                                       required to, throw this exception if the entry has been
         *                                       removed from the backing map.
         */
        @Override
        public P setValue (P value)
        {
            P old = this.pivot;
            this.pivot = value;
            return old;
        }

        public M get ()
        {
            return this.model;
        }

        public M getModel ()
        {
            return this.model;
        }

        public P pivot ()
        {
            return this.pivot;
        }

        public P getPivot ()
        {
            return this.pivot;
        }

        @Override
        public String toString ()
        {
            return "Node{" + "model=" + model + ", pivot=" + pivot + '}';
        }
    }
}
