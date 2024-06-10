import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");
        String url = "jdbc:mysql://localhost:3306/vaidik";

        String username = "root";
//        String password = " ";

        try(Connection connection = DriverManager.getConnection(url,username , "")){
            System.out.println("connected to database ");

        }
        catch (SQLException e){
            System.err.println("Connection failed: " + e.getMessage());
        }
    }
}