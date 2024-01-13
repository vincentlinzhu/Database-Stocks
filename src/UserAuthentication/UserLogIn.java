package UserAuthentication;

// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
import java.util.Scanner;
import java.io.IOError;
import java.io.IOException;
// import java.util.regex.Matcher;
// import java.util.regex.Pattern;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
// import java.util.Properties;
import java.sql.PreparedStatement;

// import oracle.jdbc.pool.OracleDataSource;
// import oracle.jdbc.OracleConnection;
// import java.sql.DatabaseMetaData;

public class UserLogIn {
    private static String user = "";
    private static String usern = "";

    public static void main(Connection connection, Scanner sc) throws SQLException {
        // Scanner sc = new Scanner(System.in);
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        while (!login(connection, username, password)) {
            System.out.println("Wrong credentials, try again.");
            System.out.print("Username: ");
            username = sc.nextLine();
            System.out.print("Password: ");
            password = sc.nextLine();
        }
        
    }


    public static boolean login(Connection connection, String username, String password){
        String sql = "SELECT * FROM Customer_Profiles WHERE username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set the value for the condition (e.g., WHERE clause)
            preparedStatement.setString(1, username);
            System.out.println("Login Attempting...");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Retrieve the value of the column
                    String usernameDB = resultSet.getString("username");
                    String nameDB = resultSet.getString("name");
                    String passwordDB = resultSet.getString("password");
                    if (passwordDB.equals(password)) {
                        System.out.println("Login Successful");
                        user = nameDB;
                        usern = usernameDB;
                        return true;
                    }
                } else {
                    System.out.println("Incorrect Password");
                    return false;
                }
            } catch (Exception e1) {
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR: Connection error");
            System.out.println(e);
        }
        return false;
    }


    public static String getUser() {
        return user;
    }


    public static String getUsername() {
        return usern;
    }


    public static String getAccountID(Connection connection, String username) {
        String sql = "SELECT * FROM Accounts WHERE username = ?";
        String accountID = "";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set the value for the condition (e.g., WHERE clause)
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Retrieve the value of the column
                    // String usernameDB = resultSet.getString("username");
                    accountID = resultSet.getString("account_id");
                } else {
                    System.out.println("No user was found");
                }
            } catch (Exception e1) {
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR");
            System.out.println(e);
        }
        return accountID;
    }


    public static boolean logout(Connection connection) {
        return false;
    }
}
