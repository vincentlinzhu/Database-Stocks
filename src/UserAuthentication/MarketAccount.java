package UserAuthentication;

// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.List;
import java.util.UUID;
// import java.util.Map;
// import java.util.Scanner;
// import java.util.regex.Matcher;
// import java.util.regex.Pattern;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
// import java.sql.Statement;
// import java.util.Properties;
import java.sql.PreparedStatement;

// import oracle.jdbc.pool.OracleDataSource;
// import oracle.jdbc.OracleConnection;
// import java.sql.DatabaseMetaData;

public class MarketAccount {
    public MarketAccount(Connection connection, String username) throws SQLException {
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        while(accountIDTaken(connection, uuidAsString)) {
            uuid = UUID.randomUUID();
            uuidAsString = uuid.toString();
            // System.out.println(uuidAsString);
        }
        createAccount(connection, username, uuidAsString);
        createProfitEntry(connection, uuidAsString);
        int balance = 1000;
        createMarketAccount(connection, uuidAsString, balance);
    }

    public static boolean accountIDTaken(Connection connection, String uuid){
        String sql = "SELECT * FROM Accounts WHERE account_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, uuid);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (Exception e) {
            System.out.println("ERROR: Connection error");
            System.out.println(e);
        }

        return true;
    }

    public static void createMarketAccount(Connection connection, String accountID, int balance) throws SQLException {
        String sql = "INSERT INTO Market_Accounts VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, accountID);
            preparedStatement.setDouble(2, balance);
            preparedStatement.setDouble(3, balance);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Market Account Created");
            } else {
                System.out.println("Failed to create a market account");
            }
        } catch (Exception e) {
            System.out.println("ERROR: insertion failed");
            System.out.println(e);
        }
    }

    public static void createAccount(Connection connection, String username, String accountID) throws SQLException {
        String sql = "INSERT INTO Accounts (account_id, username, num_traded) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, accountID);
            preparedStatement.setString(2, username);
            preparedStatement.setInt(3, 0);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Account Created");
            } else {
                System.out.println("Failed to create an Account");
            }
        } catch (Exception e) {
            System.out.println("ERROR: insertion failed");
            System.out.println(e);
        }
    }

    public static void createProfitEntry(Connection connection, String aid) {
        double p = 1.0;
        String sql = "INSERT INTO Customer_Profits (account_id, profit) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, aid);
            preparedStatement.setDouble(2, p);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            return;
        }
    }
}
