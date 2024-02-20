/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;
import java.sql.*;

/**
 *
 * @author sachi
 */
public class DbConnection {
      static final String Db_url="jdbc:mysql://localhost:3306/socialMedia";
    static final String user="root";
    static final String pass="kiyo1209";
    public static Connection connectDB(){
        Connection conn = null;
    
        try{
             //Class.forName("com.mysql.cj.jdbc.Driver");
             conn = DriverManager.getConnection(Db_url,user,pass);
             return conn;
           }catch (Exception ex){
               System.out.println("There were errors connecting...");
               return null;
           }
    }
    public static void main(String[] args) {

        new DbConnection();

    }

}
