package org.example.service.custom.impl;

import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.repo.custom.impl.UserRepoIMPL;
import org.example.service.custom.UserService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//import org.example.util.PasswordUtils;


public class UserServiceIMPL implements UserService {
    private UserRepoIMPL userRepo = new UserRepoIMPL();

    public UserServiceIMPL() {
    }

    public UserServiceIMPL(UserRepoIMPL userRepoIMPL) {
        this.userRepo = userRepoIMPL;
    }


    public String save(UserDTO userDTO) {

        User user = convertDTOtoEntity(userDTO);
        try {
            userRepo.save(user);
            return "User Saved";
        } catch (SQLException e){
            if(e.getErrorCode()==1062){
                return "Duplicate User";
            };
        } catch (Exception e) {
            return "something went wrong";
        }
        return "error";
    }


//    Handles user login and conversion but should avoid returning null.
    public UserDTO loginUser(String username, String password) {
        try {
            User user = userRepo.login(username, passhash(password));
            if (user != null) {
                return convertEntityToDTO(user);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during login: " + e.getMessage());
        }
        return null;
    }


//    Handles fetching users, converting to DTOs, and exceptions but could improve error differentiation
    public List<UserDTO> getAll() {
        try {
            List<User> all = userRepo.getAll();
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

    public boolean delete(int id) {
        try {
            return userRepo.delete(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public String update(UserDTO userDTO) {
        try {
            userRepo.update(convertDTOtoEntity(userDTO));
            return "User Updated";
        } catch (SQLException e){
            System.out.printf(e.getMessage());
            if(e.getErrorCode()==1062){
                return "Duplicate User";
            };
        } catch (Exception e) {
            return "something went wrong";
        }
        return "error";
    }

    @Override
    public UserDTO search(Integer integer) {
        return null;
    }

    //convert DTO to Entity
    private User convertDTOtoEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user.setPassword(passhash(userDTO.getPassword()));
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

    private String passhash(String password) {
        try {
            // Get a MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convert the input string into bytes and compute the hash
            byte[] hashBytes = digest.digest(password.getBytes());

            // Convert the hash bytes to a hexadecimal format
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                // Convert each byte to a hexadecimal string
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0'); // Add leading zero if needed
                }
                hexString.append(hex);
            }

            // Return the resulting hash as a string
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
