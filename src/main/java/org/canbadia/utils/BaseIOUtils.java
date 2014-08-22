package org.canbadia.utils;

/**
 * Author: Marc Badia Cendros (randolph)
 * Date: 05/02/12
 * Time: 01:25
 * Mail: marc.badiac@gmail.com
 */
public interface BaseIOUtils<T> {
    public T load(String path) throws Exception;

    public void save(T object, String path) throws Exception;
}
