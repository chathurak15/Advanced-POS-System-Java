package org.example.service;

import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.repo.UserRepo;
//import org.example.util.PasswordUtils;

public class UserService {
    private final UserRepo userRepo;


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

    private User convertDTOtoEntity(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user.setPassword(userDTO.getPassword());
        return user;
    }

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
