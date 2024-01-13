import UserAuthentication.*;

import java.sql.SQLException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.sql.Connection;
import java.util.Calendar;
import java.util.UUID;

import javax.naming.spi.DirStateFactory.Result;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;
import oracle.net.nt.ConnOption;


public class ManagerApp {
    private static int months[] = new int[12];

    public static void populateMonths() {
        months[0] = 31;
        months[1] = 28;
        months[2] = 31;
        months[3] = 30;
        months[4] = 31;
        months[5] = 30;
        months[6] = 31;
        months[7] = 31;
        months[8] = 30;
        months[9] = 31;
        months[10] = 30;
        months[11] = 31;

    }
    public static void main(String args[]) {

    }
    
    public static void enter(Connection connection, Scanner sc) throws SQLException {
        populateMonths();
        start(connection, sc);
        return;
    }

    private static void start(Connection connection, Scanner sc) throws SQLException {
        boolean running = true;
        while (running) {
            System.out.println("\n+-------------------+");
            System.out.println("| Manager Interface |");
            System.out.println("+-------------------+");
            System.out.println("(1) Apply Interest");
            System.out.println("(2) Generate Monthly Statement");
            System.out.println("(3) Display Active Customers");
            System.out.println("(4) Generate Government Drug and Tax Evasion Report");
            System.out.println("(5) Generate Customer Report");
            System.out.println("(6) Delete Transactions");
            System.out.println("(7) Change Montly Interest Rate");
            System.out.println("(8) Exit");
            System.out.println("Select: ");
            String userType = sc.nextLine();

            if (!(userType.equals("1") || userType.equals("2") || userType.equals("3") || userType.equals("4") || userType.equals("5") || userType.equals("6") || userType.equals("7") || userType.equals("8"))) {
                System.out.println("Invalid Input. Try again.\n");
                System.out.println("\n+-------------------+");
                System.out.println("| Manager Interface |");
                System.out.println("+-------------------+");
                System.out.println("(1) Apply Interest");
                System.out.println("(2) Generate Monthly Statement");
                System.out.println("(3) Display Active Customers");
                System.out.println("(4) Generate Government Drug and Tax Evasion Report");
                System.out.println("(5) Generate Customer Report");
                System.out.println("(6) Delete Transactions");
                System.out.println("(7) Change Montly Interest Rate");
                System.out.println("(8) Exit");
                System.out.println("Select: ");
                userType = sc.nextLine();

                Calendar calendar;
                String uname = "";
                switch (userType){
                    case "1": // Apply interest
                        calendar = Calendar.getInstance();
                        calendar.setTime(DemoApp.getDate(connection));
                        if (months[calendar.get(Calendar.MONTH)] == calendar.get(Calendar.DAY_OF_MONTH)) {
                            System.out.println("Enter monthly interest rate: ");
                            // String interest = sc.nextLine();
                            // double ir = Double.parseDouble(interest);
                            // double ir = 0.10;
                            double ir = getMonthlyInterest(connection);
                            System.out.println("Applying interest...");
                            applyMonthlyInterest(connection, ir);
                            resetNumTraded(connection);
                        } else {
                            System.out.println("It is not the last day of the month! Try again some other day.");
                        } 
                        break;
                    case "2": // Generate monthly statement
                        System.out.println("\nEnter the username of a customer: ");
                        uname = sc.nextLine();
                        System.out.println("\nGenerating monthly statement...");
                        generateMonthlyStatement(connection, uname);
                        break;
                    case "3": // Display active customers
                        System.out.println("\nDisplaying active customers...");
                        displayActiveTraders(connection);
                        break;
                    case "4": // Generate government drug and tax evasion report
                        System.out.println("\nGenerating government drug and tax evasion report...");
                        generateDTER(connection);
                        break;
                    case "5": // Generate customer report
                        System.out.println("\nEnter the username of a customer: ");
                        uname = sc.nextLine();
                        System.out.println("\nGenerating customer report...");
                        generateCustomerReport(connection, uname);
                        break;
                    case "6": // Delete transactions
                        System.out.println("\nDeleting transactions...");
                        calendar = Calendar.getInstance();
                        calendar.setTime(DemoApp.getDate(connection));
                        if (months[calendar.get(Calendar.MONTH)] == calendar.get(Calendar.DAY_OF_MONTH)) {
                            deleteAllTransactions(connection);
                        }
                        break;
                    case "7": // Change monthly interest rate
                        System.out.println("Enter new monthly interest rate: ");
                        String interest = sc.nextLine();
                        double ir = Double.parseDouble(interest);
                        updateMonthlyInterest(connection, ir);
                        System.out.println("Changing monthly interest rate...");
                        break;
                    case "8":
                        System.out.println("Exiting ...");
                        running = false;
                        break;
                    default:
                        break;
                }
                continue;
            }
            Calendar calendar;
            String uname = "";
            switch (userType){
                case "1": // Apply interest
                    calendar = Calendar.getInstance();
                    calendar.setTime(DemoApp.getDate(connection));
                    if (months[calendar.get(Calendar.MONTH)] == calendar.get(Calendar.DAY_OF_MONTH)) {
                        System.out.println("Enter monthly interest rate: ");
                        // String interest = sc.nextLine();
                        // double ir = Double.parseDouble(interest);
                        double ir = getMonthlyInterest(connection);
                        System.out.println("Applying interest...");
                        applyMonthlyInterest(connection, ir);
                        resetNumTraded(connection);
                    } else {
                        System.out.println("It is not the last day of the month! Try again some other day.");
                    } 
                    break;
                case "2": // Generate monthly statement
                    System.out.println("\nEnter the username of a customer: ");
                    uname = sc.nextLine();
                    System.out.println("\nGenerating monthly statement...");
                    generateMonthlyStatement(connection, uname);
                    break;
                case "3": // Display active customers
                    System.out.println("\nDisplaying active customers...");
                    displayActiveTraders(connection);
                    break;
                case "4": // Generate government drug and tax evasion report
                    System.out.println("\nGenerating government drug and tax evasion report...");
                    generateDTER(connection);
                    break;
                case "5": // Generate customer report
                    System.out.println("\nEnter the username of a customer: ");
                    uname = sc.nextLine();
                    System.out.println("\nGenerating customer report...");
                    generateCustomerReport(connection, uname);
                    break;
                case "6": // Delete transactions
                    System.out.println("\nDeleting transactions...");
                    calendar = Calendar.getInstance();
                    calendar.setTime(DemoApp.getDate(connection));
                    if (months[calendar.get(Calendar.MONTH)] == calendar.get(Calendar.DAY_OF_MONTH)) {
                        deleteAllTransactions(connection);
                    }
                    break;
                case "7": // Change monthly interest rate
                    System.out.println("Enter new monthly interest rate: ");
                    String interest = sc.nextLine();
                    double ir = Double.parseDouble(interest);
                    updateMonthlyInterest(connection, ir);
                    System.out.println("Changing monthly interest rate...");
                    break;
                case "8":
                    System.out.println("Exiting ...");
                    running = false;
                    break;
                default:
                    break;
            }
        }
    }

    public static void deleteAllTransactions(Connection connection) {
        String sql = "DELETE FROM Transactions";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
            System.out.println("Transactions Deleted");
        } catch (Exception e) {
            System.out.println("ERROR");
            System.out.println(e);
        }
    }
    
    public static void generateCustomerReport(Connection connection, String username) {        
        String sql = "SELECT * FROM Accounts WHERE username = ?";
        try (PreparedStatement preparedStatementGetUser = connection.prepareStatement(sql)) {
            preparedStatementGetUser.setString(1, username);
            try (ResultSet resultSet = preparedStatementGetUser.executeQuery()) {
                if (resultSet.next()) {
                    String aID = resultSet.getString("account_id");
                    System.out.println(username + "'s Market Account: ");
                    System.out.println("Account ID: " + aID);
                    TraderApp.displayBalance(connection, aID);
                    DisplayCustomerStocks(connection, aID, username);
                } else {
                    System.out.println("No user was found");
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch transactions");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
    }

    public static void DisplayCustomerStocks(Connection connection, String accountID, String username) {
        // Statement and ResultSet are AutoCloseable and closed automatically. 
        String sql = "SELECT symbol, share_count FROM Stock_Accounts WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, accountID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println(username + " Stocks Accounts: ");
                while (resultSet.next()) {
                    System.out.println("Stock Symbol: " + resultSet.getString("symbol"));
                    System.out.println("Number of Share: " + resultSet.getString("share_count"));
                    System.out.println("\n");
                }
            } catch (Exception e1) {
                System.out.println(username + " does not own any stock accounts.");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
    }

    public static int getTotalCommissions(Connection connection, String aid) {
        String sql = "SELECT * FROM Transactions WHERE account_id = ?";
        int count = 0;
        try (PreparedStatement preparedStatementGetUser = connection.prepareStatement(sql)) {
            preparedStatementGetUser.setString(1, aid);
            try (ResultSet resultSet = preparedStatementGetUser.executeQuery()) {
                String transaction_type = "";
                while (resultSet.next()) {
                    transaction_type = resultSet.getString("transaction_type");
                    if (transaction_type.equals("buy") || transaction_type.equals("sell") || transaction_type.equals("cancel")) {
                        count++;
                    }
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch transactions");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
        return count * 20;
    }

    public static void printUserDetails(Connection connection, String aid) {
        String sql = "SELECT * FROM Accounts A JOIN Customer_Profiles C ON A.username = C.username WHERE account_id = ?";
        String name = "";
        String state = "";
        try (PreparedStatement preparedStatementGetUser = connection.prepareStatement(sql)) {
            preparedStatementGetUser.setString(1, aid);
            try (ResultSet resultSet = preparedStatementGetUser.executeQuery()) {
                if (resultSet.next()) {
                    name = resultSet.getString("name");
                    state = resultSet.getString("state_name");
                } else {
                    System.out.println("No user was found");
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch transactions");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }

        System.out.println(name + " (" + state + ")\n");
    }

    public static void generateDTER(Connection connection) {
        System.out.println("\n+--------------------------------------+");
        System.out.println("| Government Drug & Tax Evasion Report |");
        System.out.println("+--------------------------------------+");
        String sql = "SELECT * FROM Customer_Profits WHERE profit > 1000";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String aid = resultSet.getString("account_id");
                    printUserDetails(connection, aid);
                }
            } catch (Exception e1) {
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR");
            System.out.println(e);
        }
    }

    public static void generateMonthlyStatement(Connection connection, String username) {
        System.out.println("\n+----------------------------+");
        System.out.println("| Customer Monthly Statement |");
        System.out.println("+----------------------------+");
        
        String sql = "SELECT * FROM Customer_Profiles WHERE username = ?";
        String name = "";
        String email = "";
        try (PreparedStatement preparedStatementGetUser = connection.prepareStatement(sql)) {
            preparedStatementGetUser.setString(1, username);
            try (ResultSet resultSet = preparedStatementGetUser.executeQuery()) {
                if (resultSet.next()) {
                    name = resultSet.getString("name");
                    email = resultSet.getString("email");
                } else {
                    System.out.println("No user was found");
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch transactions");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
        
        System.out.println(name + " (" + email + ")\n");

        // Get account id from the Accounts table using username
        sql = "SELECT * FROM Accounts WHERE username = ?";
        String accountID = "";
        try (PreparedStatement preparedStatementGetUser = connection.prepareStatement(sql)) {
            preparedStatementGetUser.setString(1, username);
            try (ResultSet resultSet = preparedStatementGetUser.executeQuery()) {
                if (resultSet.next()) {
                    accountID = resultSet.getString("account_id");
                } else {
                    System.out.println("No user was found");
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch transactions");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }

        System.out.println("Total Commissions: $" + getTotalCommissions(connection, accountID));
        System.out.println("Total Profits: $" + TraderApp.getProfits(connection, accountID));
        
        // Statement and ResultSet are AutoCloseable and closed automatically. 
        sql = "SELECT * FROM Transactions WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, accountID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String tid = resultSet.getString("transaction_id");
                    String transaction_type = resultSet.getString("transaction_type");
                    System.out.println(transaction_type);
                    switch (transaction_type) {
                        case "buy":
                            TraderApp.displayBuyTransaction(connection, tid);
                            break;
                        case "sell":
                            TraderApp.displaySellTransaction(connection, tid);
                            break;
                        case "deposit":
                            TraderApp.displayDepositTransaction(connection, tid);
                            break;
                        case "withdraw":
                            TraderApp.displayWithdrawTransaction(connection, tid);
                            break;
                        case "cancel":
                            TraderApp.displayCancelTransaction(connection, tid);
                            break;
                        case "accrue":
                            TraderApp.displayAccrueTransaction(connection, tid);
                            break;
                        default:
                            System.out.println("Error: Could not fetch transaction history");
                            break;
                    }
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch transactions");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
    }

    public static void updateStartBalance(Connection connection) {
        String sql = "SELECT * FROM Market_Accounts";
        String aid = "";
        double bal = 0.0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    aid = resultSet.getString("account_id");
                    bal = resultSet.getDouble("balance");
                    updateStartBalance2(connection, aid, bal);
                }
            } catch (Exception e1) {
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR");
            System.out.println(e);
        }
    }

    public static void updateStartBalance2(Connection connection, String aid, double bal) {
        String sql = "UPDATE Start_End_Balance SET Start_Balance = ? WHERE account_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, bal);
            preparedStatement.setString(2, aid);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("ERROR");
            System.out.println(e);
        }
    }

    public static void displayActiveTraders(Connection connection) {
        String sql = "SELECT * FROM Accounts WHERE num_traded >= 1000";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.println("Username: " + resultSet.getString("username") + " \t|\t ID: " + resultSet.getString("account_id"));
                }
            } catch (Exception e1) {
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR");
            System.out.println(e);
        }
    }

    public static void resetNumTraded(Connection connect) {
        String sql = "UPDATE Accounts SET num_traded = 0";

        try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Number of trades reset");
            } else {
                System.out.println("Failed to reset number of trades");
            }
        } catch (Exception e) {
            System.out.println("ERROR: insertion failed");
            System.out.println(e);
        }
    }

    public static void updateMonthlyInterest(Connection connection, double interest) {
        String sql = "UPDATE MonthlyInterestRate SET rate =?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, interest);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Interest Rate Updated");
                }
            } catch (Exception e1) {
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR");
            System.out.println(e);
        }
    }
    public static double getMonthlyInterest(Connection connection) {
        String sql = "SELECT * FROM MonthlyInterestRate";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("rate");
                }
            } catch (Exception e1) {
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR");
            System.out.println(e);
        }

        return 0.0;
    }
    public static void applyMonthlyInterest(Connection connection, double interest) {
        String sql = "SELECT * FROM Market_Accounts";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    applyInterestPerAccount(connection, resultSet.getString("account_id"), interest);
                }
                System.out.println("Interest Applied");
            } catch (Exception e1) {
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR");
            System.out.println(e);
        }
    }
    public static void applyInterestPerAccount(Connection connection, String accountID, double interest) {
        String sql = "SELECT * FROM Market_Accounts WHERE account_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, accountID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("HELP1");
                if (resultSet.next()) {
                    System.out.println("HELP2");
                    java.sql.Date date = DemoApp.getDate(connection);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    System.out.println("HELP3");
                    double mt = resultSet.getDouble("monthly_total");
                    System.out.println(months[calendar.get(Calendar.MONTH)]);
                    double interestProfit = interest * (mt/months[calendar.get(Calendar.MONTH)]);
                    System.out.println(interestProfit);
                    double new_balance = TraderApp.getBalance(connection, accountID) + interestProfit;
                    System.out.println(new_balance);
                    TraderApp.updateBalance(connection, new_balance);
                    TraderApp.updateProfits(connection, accountID, interestProfit);
                    String id = TraderApp.makeTransaction(connection, "accrue");
                    TraderApp.createAccrue(connection, id, DemoApp.getDate(connection), interest);
                }
                System.out.println("Interest Applied");
            } catch (Exception e1) {
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR");
            System.out.println(e);
        }
    }
}
