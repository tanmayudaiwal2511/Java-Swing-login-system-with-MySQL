package dao;

import db.DBConnection;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /** Returns null on success, or an error message. */
    public String register(String username, String email, String password) {
        String sql = "INSERT INTO users (username, email, password_hash) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, BCrypt.hashpw(password, BCrypt.gensalt()));
            ps.executeUpdate();
            return null;
        } catch (SQLIntegrityConstraintViolationException e) {
            return "Username or email already exists.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error: " + e.getMessage();
        }
    }

    /** Returns the User on success, or null if credentials are wrong. */
    public User login(String username, String password) {
        String sql = "SELECT id, username, email, role, last_login, password_hash "
                + "FROM users WHERE username = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && BCrypt.checkpw(password, rs.getString("password_hash"))) {
                    User user = new User(rs.getInt("id"), rs.getString("username"),
                            rs.getString("email"), rs.getString("role"),
                            rs.getTimestamp("last_login"));
                    try (PreparedStatement up = con.prepareStatement(
                            "UPDATE users SET last_login = NOW() WHERE id = ?")) {
                        up.setInt(1, user.getId());
                        up.executeUpdate();
                    }
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT id, username, email, role, last_login FROM users ORDER BY id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new User(rs.getInt("id"), rs.getString("username"),
                        rs.getString("email"), rs.getString("role"),
                        rs.getTimestamp("last_login")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countUsers() {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM users");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean deleteUser(int id) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM users WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Returns null on success, or an error message. */
    public String changePassword(int id, String oldPass, String newPass) {
        try (Connection con = DBConnection.getConnection()) {
            String hash;
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT password_hash FROM users WHERE id = ?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) return "User not found.";
                    hash = rs.getString(1);
                }
            }
            if (!BCrypt.checkpw(oldPass, hash)) return "Current password is incorrect.";
            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE users SET password_hash = ? WHERE id = ?")) {
                ps.setString(1, BCrypt.hashpw(newPass, BCrypt.gensalt()));
                ps.setInt(2, id);
                ps.executeUpdate();
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error: " + e.getMessage();
        }
    }
}