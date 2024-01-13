package UserAuthentication;

import java.util.Arrays;
// import java.util.HashMap;
import java.util.List;
// import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
// import java.sql.Statement;
// import java.util.Properties;
import java.sql.PreparedStatement;

// import oracle.jdbc.pool.OracleDataSource;
// import oracle.jdbc.OracleConnection;
// import java.sql.DatabaseMetaData;

public class UserSignUp {
    public static void main(Connection connection, Scanner sc) throws SQLException {
        signup(connection, sc);
    }

    private static void signup(Connection connection, Scanner sc) {
        System.out.println("Enter username:");
        String username = requestUsername(connection, sc);

        System.out.println("Enter password:");
        String password = requestPassword(sc);

        // Name
        System.out.println("Enter name:");
        String name = requestName(sc);
        // 2 letter state
        System.out.println("Enter state:");
        String state = requestState(sc);
        // Phone number (10-digit)
        System.out.println("Enter phone number:");
        String phone = requestPhone(sc);
        // email
        System.out.println("Enter email:");
        String email = requestEmail(sc);
        // tax identification number (9-digits possibly with leading 0s)
        System.out.println("Enter tax ID:");
        String taxID = requestTaxID(sc);
        try {
            addUser(connection, username, password, name, state, phone, email, taxID);
            MarketAccount market = new MarketAccount(connection, username);
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
    }

    public static boolean checkUsernameTaken(Connection connection, String username) throws SQLException {
        String sql = "SELECT username FROM Customer_Profiles WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Username already taken");
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                System.out.println("ERROR: Username already taken");
                System.out.println(e);
            }
        } catch (Exception e) {
            System.out.println("ERROR: Connection error");
            System.out.println(e);
        }

        return true;
    }

    public static void addUser(Connection connection, String username, String password, String name, String state, String phone, String email, String taxID) throws SQLException {
        String sql = "INSERT INTO Customer_Profiles VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, name);
            preparedStatement.setString(5, state);
            preparedStatement.setString(6, taxID);
            preparedStatement.setString(7, phone);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User Registered");
            } else {
                System.out.println("Failed to Register user");
            }
        } catch (Exception e) {
            System.out.println("ERROR: insertion failed");
            System.out.println(e);
        }
    }

    /*
     * Request for user information
     */

    private static String requestUsername(Connection connection, Scanner sc) {
        String username = sc.nextLine();
        while (!isValidUsername(username, connection)) {
            System.out.println("Username must be at least 1 character long. Enter a valid username:");
            username = sc.nextLine();
        }
        return username;
    }

    private static String requestPassword(Scanner sc) {
        String password = sc.nextLine();
        while (!isValidPassword(password)) {
            System.out.println("Password must be at least 1 character long. Enter a valid password:");
            password = sc.nextLine();
        }
        return password;
    }

    private static String requestName(Scanner sc) {
        String name = sc.nextLine();
        while (!isValidName(name)) {
            name = sc.nextLine();
        }
        return name;
    }

    private static String requestState(Scanner sc) {
        String state = sc.nextLine();
        while (!isValidState(state)) {
            System.out.println("State must be a 2-letter acronym. Enter a valid state:");
            state = sc.nextLine();
        }
        return state;
    }
    
    private static String requestPhone(Scanner sc) {
        String phone = sc.nextLine();
        phone = phone.replaceAll("[^\\d.]", "");
        while (!isValidPhone(phone)) {
            phone = sc.nextLine();
        }
        return phone;
    }

    private static String requestEmail(Scanner sc) {
        String email = sc.nextLine();
        while (!isValidEmail(email)) {
            email = sc.nextLine();
        }
        return email;
    }
    
    private static String requestTaxID(Scanner sc) {
        String taxID = sc.nextLine();
        while (!isValidTaxID(taxID)) {
            taxID = sc.nextLine();
        }
        return String.format("%09d", Integer.parseInt(taxID));
    }

    /*
     * Checks for validity
     */

    private static boolean isValidUsername(String username, Connection connect) {
        int minLength = 1;
        int maxLength = 255;
        try {
            return username.length() >= minLength && username.length() < maxLength && !checkUsernameTaken(connect, username);
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }

        return false;
    }

    private static boolean isValidPassword(String password) {
        int minLength = 1;
        int maxLength = 255;

        return password.length() >= minLength && password.length() < maxLength;
    }

    private static boolean isValidName(String name) {
        int minLength = 1;
        int maxLength = 255;

        return name.length() >= minLength && name.length() < maxLength;
    }
    
    private static boolean isValidState(String state) {
        List<String> validStateAcronyms = Arrays.asList(
                "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
                "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
                "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
                "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC",
                "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"
        );

        return validStateAcronyms.contains(state.toUpperCase());
    }

    private static boolean isValidPhone(String phone) {
        return phone.length() == 10;
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    private static boolean isValidTaxID(String taxID) {
        return taxID.matches("\\d+") && taxID.length() <= 9;
    }
}
