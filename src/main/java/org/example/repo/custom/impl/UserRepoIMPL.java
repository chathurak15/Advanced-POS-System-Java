package org.example.repo.custom.impl;

import org.example.entity.User;
import org.example.repo.custom.UserRepo;
import org.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepoIMPL implements UserRepo {

    //Saves a user to the database.
    public boolean save(User user) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO users(name,username,email,role,password) VALUES(?,?,?,?,?)");
        ps.setString(1, user.getName());
        ps.setString(2, user.getUsername());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getRole());
        ps.setString(5, user.getPassword());

        return ps.executeUpdate()>0;
    }


    //GET AND SET USER INFORMATION FOR USER ENTITY AFTER VALID USER LOGIN
    public User login(String username, String password) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setRole(rs.getString("role"));

            return user;

        }
        return null;

    }

    //get all users details from db and set data for user
    public List<User> getAll() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM users");
        ResultSet rs = ps.executeQuery();
        List<User> users = new ArrayList<>();

        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setRole(rs.getString("role"));
            user.setPassword(null);
            users.add(user);
        }
        return users;
    }

    @Override
    public boolean delete(Integer integer) throws Exception {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM users WHERE id=?");
        ps.setInt(1, integer);
        return ps.executeUpdate()>0;
    }

    @Override
    public boolean update(User user) throws Exception {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement PS = connection.prepareStatement("UPDATE users SET name = ?, username=?, email=?, role=?,password=? WHERE id=?");
        PS.setString(1, user.getName());
        PS.setString(2, user.getUsername());
        PS.setString(3, user.getEmail());
        PS.setString(4, user.getRole());
        PS.setString(5, user.getPassword());
        PS.setInt(6, user.getId());

        System.out.println(PS.executeUpdate());
        return PS.executeUpdate()>0;
    }

    //Search a user to the database and get user details.
    @Override
    public User search(Integer integer) throws Exception {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE id=?");
        ps.setInt(1, integer);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setRole(rs.getString("role"));
            user.setPassword(null);
            return user;
        }
        return null;
    }

//    public Optional<User>

//    public User loginUser(String username, String password) throws SQLException, ClassNotFoundException {
//        try (Connection connection = DBConnection.getInstance().getConnection();
//             PreparedStatement ps = connection.prepareStatement(
//                     "SELECT * FROM users WHERE username=?"
//             )) {
//            ps.setString(1, username);
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    String storedPassword = rs.getString("password");
//                    if (storedPassword != null && org.example.util.PasswordUtils.verifyPassword(password, storedPassword)) {
//                        User user = new User();
//                        user.setId(rs.getInt("id"));
//                        user.setName(rs.getString("name"));
//                        user.setUsername(rs.getString("username"));
//                        user.setEmail(rs.getString("email"));
//                        user.setRole(rs.getString("role"));
//                        return user;
//                    }
//                }
//            }
//        }
//        return null;
//    }
}
