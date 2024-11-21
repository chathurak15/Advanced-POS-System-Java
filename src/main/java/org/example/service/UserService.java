package org.example.service;

import jdk.jshell.spi.ExecutionControl;
import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.repo.UserRepo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//import org.example.util.PasswordUtils;


public class UserService {
    private UserRepo userRepo = new UserRepo();

    public UserService() {
    }

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public boolean saveUser(UserDTO userDTO) {
        User user = convertDTOtoEntity(userDTO);
//        user.setPassword(PasswordUtils.hashPassword(user.getPassword())); // Secure password

        try {
            return userRepo.saveUser(user);
        } catch (Exception e) {
            throw new RuntimeException("Error saving user: " + e.getMessage());
        }
    }

    public UserDTO loginUser(String username, String password) {
        try {
            User user = userRepo.loginUser(username, password);
            if (user != null) {
                return convertEntityToDTO(user);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during login: " + e.getMessage());
        }
        return null;
    }

    public List<UserDTO> getAllUsers() {
        try {
            List<User> all = userRepo.getAllUsers();
            List<UserDTO> userDTOs = new ArrayList<>();

            if (all != null) {
                for (User user : all) {
                    userDTOs.add(convertEntityToDTO(user));
                }
                return userDTOs;
            } else {
                throw new RuntimeException("No users found");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Contact Develloper", e);
        }

    }

    //convert DTO to Entity
    private User convertDTOtoEntity(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user.setPassword(userDTO.getPassword());
        return user;
    }

    //convert entity to dto
    private UserDTO convertEntityToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                null // Exclude password in DTO
        );
    }
}
