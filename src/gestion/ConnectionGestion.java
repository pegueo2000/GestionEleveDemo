package gestion;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionGestion {
    Connection cn;

    public ConnectionGestion() {

        try{
            Class.forName("com.mysql.jdbc.Driver");
            cn =(Connection) DriverManager.getConnection("jdbc:mysql://localhost/gestion","root","");
            System.out.println("connection etabli");

        }
        catch (Exception e){
            System.out.println("erreur de connection");
            e.printStackTrace();
        }
    }
    public Connection maconnection()
    {
        return cn;
    }
}
