package com.example.root.kfgdealerpaymentv1.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by root on 2/9/20.
 */

public class PostgresqlConnection {
    public Connection conn;
    public String error;
    public Connection getConn(){
        try{
            Class.forName("org.postgresql.Driver");


            conn = DriverManager.getConnection("jdbc:postgresql://***.***.***.***:****/******", "*****", "******");

            System.out.println(conn);

        }catch(ClassNotFoundException e){
            error = e.getMessage();
        }catch (SQLException sqlex){
            error = sqlex.getMessage();
        }catch (Exception ex){
            error = ex.getMessage();
        }
        return conn;
    }
}
