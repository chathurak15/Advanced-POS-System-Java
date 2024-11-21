package org.example.repo;

import org.example.entity.User;
import org.example.service.UserService;
import org.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

        while (rs.next()) {
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
