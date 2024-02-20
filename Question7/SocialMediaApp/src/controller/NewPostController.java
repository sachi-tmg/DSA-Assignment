package controller;

import database.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class NewPostController {
    
    private String username;

    public NewPostController(String username) {
        this.username = username;
    }

    // Use the username as needed in your controller methods
    public void insertPost(String post, String username) {
        System.out.println("Retrieved username: " + username);

        try (Connection conn = (Connection) DbConnection.connectDB();
             PreparedStatement getUsername = conn.prepareStatement("SELECT user_id FROM Users where uName = ?");
             PreparedStatement uploadPost = conn.prepareStatement("INSERT INTO Posts (user_id, content) VALUES (?, ?)")) {

            getUsername.setString(1, username);
            ResultSet rs = getUsername.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                uploadPost.setInt(1, userId);
                uploadPost.setString(2, post);
                int rowsAffected = uploadPost.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Data Inserted");
                } else {
                    System.out.println("Failed to insert data");
                }
            } else {
                System.out.println("User not found with given username"); // Handle missing user
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public String getFullName(String username) {
        try (Connection conn = (Connection) DbConnection.connectDB();
             PreparedStatement getName = conn.prepareStatement("SELECT fName, lName FROM Users where uName = ?")) {

            getName.setString(1, username);
            ResultSet rs = getName.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("fName");
                String lastName = rs.getString("lName");
                return firstName + " " + lastName;
            } else {
                return "User not found";
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return "Error retrieving name";
        }
    }
    
     public Map<String, String> getPosts(String username) {
        Map<String, String> postsMap = new HashMap<>();

        try (Connection conn = (Connection) DbConnection.connectDB();
             PreparedStatement getPosts = conn.prepareStatement("SELECT u.uName, p.content FROM Users u JOIN Posts p ON u.user_id = p.user_id WHERE u.uName = ?")) {

            getPosts.setString(1, username);
            ResultSet rs = getPosts.executeQuery();

            while (rs.next()) {
                String postUsername = rs.getString("uName");
                String postContent = rs.getString("content");
                postsMap.put(postUsername, postContent);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return postsMap;
    }

    
//    public void insertPost(String post) {
//        
//    }
    
    public int getLikesCount(int postId) {
        int likesCount = 0;

        try (Connection conn = (Connection) DbConnection.connectDB();
             PreparedStatement pst = conn.prepareStatement("SELECT likeCount FROM post WHERE id = ?")) {

            pst.setInt(1, postId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    likesCount = rs.getInt("likeCount");
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return likesCount;
    }
    
    public void incrementLikes(int postId) {
        try (Connection conn = (Connection) DbConnection.connectDB();
             PreparedStatement pst = conn.prepareStatement("UPDATE post SET likeCount = likeCount+ 1 WHERE id = ?")) {

            pst.setInt(1, postId);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public void decrementLikes(int postId) {
        try (Connection conn = (Connection) DbConnection.connectDB();
             PreparedStatement pst = conn.prepareStatement("UPDATE post SET likeCount = likeCount- 1 WHERE id = ?")) {

            pst.setInt(1, postId);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
}
