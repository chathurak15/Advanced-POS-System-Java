package org.example.service.custom;

import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.repo.CrudRepository;
import org.example.service.CrudService;

public interface UserService extends CrudService<UserDTO, Integer> {
}
