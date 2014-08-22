package org.canbadia.utils;

import java.io.*;

/**
 * Author: Marc Badia Cendros (randolph)
 * Date: 05/02/12
 * Time: 00:13
 * Mail: marc.badiac@gmail.com
 */
public abstract class AbstractBaseUtils<T> implements BaseIOUtils {

    private Class<T> object;

    protected AbstractBaseUtils() {
        object = getObject();
    }

    protected abstract Class<T> getObject();

    public T load(String path) throws Exception {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path));
        T object = (T) objectInputStream.readObject();
        objectInputStream.close();
        return object;
    }

    public void save(Object object, String path) throws Exception {
        ObjectOutputStream objectInputStream = new ObjectOutputStream(new FileOutputStream(path));
        objectInputStream.writeObject(object);
        objectInputStream.flush();
        objectInputStream.close();
    }
}
