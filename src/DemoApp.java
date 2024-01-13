import UserAuthentication.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
// import java.util.HashMap;
// import java.util.Map;
import java.util.Properties;
import java.util.Calendar;
import java.util.Scanner;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;
import oracle.net.nt.ConnOption;

public class DemoApp {
    private static boolean marketOpen = false;
    public static void main(String args[]) {
        return;
    }
    
    public static void enter(Connection connection, Scanner sc) throws SQLException {
        start(connection, sc);
        return;
    }

    private static void start(Connection connection, Scanner sc) throws SQLException {
        boolean running = true;
        while (running) {
            java.sql.Date displayDate = getDate(connection);
            System.out.println("\n+----------------+");
            System.out.println("| Demo Interface |");
            System.out.println("+----------------+");
            System.out.println("Current Date: " + displayDate);
            System.out.println("(1) Open Market");
            System.out.println("(2) Close Market");
            System.out.println("(3) Change Stock Price");
            System.out.println("(4) Change Date");
            System.out.println("(5) Exit");
            System.out.println("Select: ");
            String userType = sc.nextLine();

            if (!(userType.equals("1") || userType.equals("2") || userType.equals("3") || userType.equals("4") || userType.equals("5") || userType.equals("6"))) {
                System.out.println("Invalid Input. Try again.\n");
                System.out.println("\n+----------------+");
                System.out.println("| Demo Interface |");
                System.out.println("+----------------+");
                System.out.println("Current Date: " + displayDate);
                System.out.println("(1) Open Market");
                System.out.println("(2) Close Market");
                System.out.println("(3) Change Stock Price");
                System.out.println("(4) Change Date");
                System.out.println("(5) Exit");
                System.out.println("Select: ");
                userType = sc.nextLine();

                Calendar calendar;
                switch (userType){
                    case "1": // Open market
                        System.out.println("Market Open: ");
                        marketOpen = true;
                        calendar = Calendar.getInstance();
                        calendar.setTime(DemoApp.getDate(connection));
                        if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
                            ManagerApp.updateStartBalance(connection);
                            resetProfits(connection);
                        }
                        break;
                    case "2": // Close market
                        System.out.println("Market Closed ");
                        marketOpen = false;
                        break;
                    case "3": // Change stock price
                        System.out.print("Enter Stock Symbol:  ");
                        
                        System.out.print("Current Stock Price:  ");
                        String symbol = sc.nextLine();
                        getCurrentStockPrice(connection, symbol);
                        System.out.print("Enter New Stock Price:  ");
                        String price = sc.nextLine();
                        double new_price = Integer.parseInt(price);
                        changeStockPrice(connection, new_price, symbol);
                        UpdateDailyClosingPrice(connection, getCurrentStockPrice(connection, symbol), symbol);
                        break;
                    case "4": // Change date 
                        System.out.print("Enter new date (YYYY-MM-DD): ");
                        String newDate = sc.nextLine();
                        changeDate(connection, java.sql.Date.valueOf(newDate));
                        System.out.print("Current Date Changed to " + getDate(connection));
                        break;
                    case "5":
                        System.out.println("Exiting ...");
                        running = false;
                        break;
                    default:
                        break;
                }
                continue;
            }
            Calendar calendar;
            switch (userType){
                case "1": // Open market
                    System.out.println("Market Open: ");
                    marketOpen = true;
                    calendar = Calendar.getInstance();
                    calendar.setTime(DemoApp.getDate(connection));
                    if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
                        ManagerApp.updateStartBalance(connection);
                    }
                    break;
                case "2": // Close market
                    System.out.println("Market Closed ");
                    marketOpen = false;
                    break;
                case "3": // Change stock price
                    System.out.print("Enter Stock Symbol:  ");
                    
                    System.out.print("Current Stock Price:  ");
                    String symbol = sc.nextLine();
                    getCurrentStockPrice(connection, symbol);
                    System.out.print("Enter New Stock Price:  ");
                    String price = sc.nextLine();
                    double new_price = Integer.parseInt(price);
                    changeStockPrice(connection, new_price, symbol);
                    UpdateDailyClosingPrice(connection, getCurrentStockPrice(connection, symbol), symbol);
                    break;
                case "4": // Change date 
                    System.out.print("Enter new date (YYYY-MM-DD): ");
                    String newDate = sc.nextLine();
                    changeDate(connection, java.sql.Date.valueOf(newDate));
                    System.out.print("Current Date Changed to " + getDate(connection));
                    break;
                case "5":
                    System.out.println("Exiting ...");
                    running = false;
                    break;
                default:
                    break;
            }
        }
    }

    public static void changeDate(Connection connection, java.sql.Date newDate) {
        String sql = "UPDATE CurrentDate SET Curr_Date = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, newDate);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                // System.out.println(getDate(connection));
            } else {
                System.out.println("ERROR: No account balance");
            }

        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
    }

    public static java.sql.Date getDate(Connection connection) {
        String sql = "SELECT curr_date FROM CurrentDate";
    
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDate("curr_date");
                } else {
                    System.out.println("ERROR: No date set");
                }
            } catch (Exception e1) {
                System.out.println("ERROR:");
                System.out.println(e1);
            }
    
        } catch (SQLException e) {
            System.out.println("ERROR: Could not fetch current date");
            e.printStackTrace();  // Print the stack trace for debugging
        }
    
        return new java.sql.Date(0);
    }

    public static boolean getMarketStatus() {
        return marketOpen;
    }

    public static void resetProfits(Connection connection) {
        String sql = "UPDATE Accounts SET profit = 0";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
    }

    public static double getCurrentStockPrice(Connection connection, String symbol) {
        String sql = "SELECT * FROM Stocks WHERE symbol = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, symbol);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    double sharePrice = resultSet.getDouble("current_price");
                    return sharePrice;
                } else {
                    System.out.println("ERROR: Stock not found");
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch stock");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }

        return 0.0;
    }

    public static void changeStockPrice(Connection connection, double price, String symbol){
        String sql = "UPDATE Stocks SET current_price = ? WHERE symbol = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, price);
            preparedStatement.setString(2, symbol);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Stock Account Updated");
            } else {
                System.out.println("Failed to update stock account");
            }
        } catch (Exception e) {
            System.out.println("ERROR: update failed");
            System.out.println(e);
        }
    }
    public static void UpdateDailyClosingPrice(Connection connection, double price, String symbol){
        String sql = "UPDATE Stocks SET daily_closing_price = ? WHERE symbol = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, price);
            preparedStatement.setString(2, symbol);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Stock Account Updated");
            } else {
                System.out.println("Failed to update stock account");
            }
        } catch (Exception e) {
            System.out.println("ERROR: update failed");
            System.out.println(e);
        }
    }
}
