package org.example.repo.custom;

import org.example.entity.User;
import org.example.repo.CrudRepository;

public interface UserRepo extends CrudRepository <User, Integer> {
}
