package org.example.repo;

import java.util.List;

public interface CrudRepository <T, ID>{

    boolean save(T t) throws Exception;

    boolean update(T t) throws Exception;

    boolean delete(ID Id) throws Exception;

    T search(ID id) throws Exception;

    List<T> getAll() throws Exception;
}
