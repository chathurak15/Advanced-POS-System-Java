package org.example.repo;

import org.example.entity.User;
import org.example.service.UserService;
import org.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepo {

    public boolean saveUser(User user) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO users(name,username,email,role,password) values(?,?,?,?,?)");
        ps.setString(1, user.getName());
        ps.setString(2, user.getUsername());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getRole());
        ps.setString(5, user.getPassword());

        return ps.executeUpdate()>0;
    }

    //GET AND SET USER INFORMATION FOR USER ENTITY AFTER VALID USER LOGIN

    public User loginUser(String username, String password) throws SQLException, ClassNotFoundException {
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
    public List<User> getAllUsers() throws SQLException, ClassNotFoundException {
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
