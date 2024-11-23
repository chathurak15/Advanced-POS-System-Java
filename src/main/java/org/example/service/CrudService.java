package org.example.service;

import org.example.dto.UserDTO;

import java.util.List;

public interface CrudService <T,ID> {

    String save(T t);

    String update(T t);

    T search(ID id);

    List<T> getAll();

    boolean delete(int id);

}
