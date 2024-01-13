import UserAuthentication.*;

import java.sql.SQLException;
import java.sql.Connection;
// import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.sql.Connection;
import java.util.UUID;

import javax.naming.spi.DirStateFactory.Result;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;
import oracle.net.nt.ConnOption;

public class TraderApp {
    private static String user = "";
    private static String accountID = "";
    private static String username = "";

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
            System.out.println("\n+--------------------+");
            System.out.println("| StarsRus Brokerage |");
            System.out.println("+--------------------+");
            System.out.println("(1) Register");
            System.out.println("(2) Log In");
            System.out.println("(3) Exit");
            System.out.println("Select: ");
            String userType = sc.nextLine();

            if (!(userType.equals("1") || userType.equals("2") || userType.equals("3"))) {
                System.out.println("Invalid Input. Try again.\n");
                System.out.println("+--------------------+");
                System.out.println("| StarsRus Brokerage |");
                System.out.println("+--------------------+");
                System.out.println("(1) Register");
                System.out.println("(2) Log In");
                System.out.println("(3) Exit");
                System.out.println("Select: ");
                userType = sc.nextLine();

                if (userType.equals("1")) { // New user - UserSignUp
                    UserSignUp.main(connection, sc);
                } else if (userType.equals("2")) { // Returning user - UserLogIn
                    UserLogIn.main(connection, sc);
                    // UserLogIn.traderUI(connection, sc);
                    if (UserLogIn.getUser().length() > 0) {
                        user = UserLogIn.getUser();
                        username = UserLogIn.getUsername();
                        accountID = UserLogIn.getAccountID(connection, username);
                        do {
                            TraderInterface(connection, sc);
                        } while (user.length() > 0);
                    }
                } else if (userType.equals("3")) {
                    running = false;
                    break;
                }
                continue;
            }
            if (userType.equals("1")) { // New user - UserSignUp
                UserSignUp.main(connection, sc);
            } else if (userType.equals("2")) { // Returning user - UserLogIn
                UserLogIn.main(connection, sc);
                // UserLogIn.traderUI(connection, sc);
                if (UserLogIn.getUser().length() > 0) {
                    user = UserLogIn.getUser();
                    username = UserLogIn.getUsername();
                    accountID = UserLogIn.getAccountID(connection, username);
                    do {
                        TraderInterface(connection, sc);
                    } while (user.length() > 0);
                }
            } else if (userType.equals("3")) {
                running = false;
                break;
            }
        }
    }

    public static void TraderInterface(Connection connection, Scanner sc) throws SQLException {
        System.out.println("\nWelcome, " + user + "!");
        System.out.println("+------------------+");
        System.out.println("| Trader Dashboard |");
        System.out.println("+------------------+");
        System.out.println("(1) Display Balance");
        System.out.println("(2) Deposit Amount");
        System.out.println("(3) Withdraw Amount");
        System.out.println("(4) Display All Current Available Stocks");
        System.out.println("(5) Display My Stocks");
        System.out.println("(6) Buy Stocks");
        System.out.println("(7) Sell Stocks");
        System.out.println("(8) Cancel Last Transaction");
        System.out.println("(9) Display Transaction History");
        System.out.println("(10) Display Current Stock Price");
        System.out.println("(11) Display Movie Details");
        System.out.println("(12) Display Reviews for a Movie");
        System.out.println("(13) List Top Movies");
        System.out.println("(0) Logout");
        
        System.out.println("Select:");

        String option = sc.nextLine();
        double amt = 0.0;
        String movieTitle = "";
        int prodYear = 2000;
        switch (option) {
            case "1": 
                displayBalance(connection, accountID);
                break;
            case "2": 
                System.out.print("Deposit amount: ");
                amt = getAmount(connection, sc);
                deposit(connection, accountID, amt);
                break;
            case "3": 
                System.out.print("Withdraw amount: ");
                amt = getAmount(connection, sc);
                withdraw(connection, accountID, amt);
                break;
            case "4": 
                DisplayAllStocks(connection);
                break;
            case "5": 
                DisplayMyStocks(connection);
                break;
            case "6": 
                if (DemoApp.getMarketStatus()) {
                    System.out.println("Buy New Stocks");
                    System.out.print("Which stock would you like to buy (3 character symbol): ");
                    String symbol = sc.nextLine();
                    System.out.print("How many shares would you like to purchase: ");
                    String shares = sc.nextLine();
                    int num_shares = Integer.parseInt(shares);
                    buyStock(connection, symbol, num_shares);
                } else {
                    System.out.println("Market is Closed! Please try again later");
                }
                break;
            case "7": 
                if (DemoApp.getMarketStatus()) {
                    System.out.println("Sell My Stocks");
                    System.out.print("Which stock would you like to sell (3 character symbol): ");
                    String sellSymbol = sc.nextLine();
                    System.out.print("How many shares would you like to sell: ");
                    String sellShares = sc.nextLine();
                    int sell_num_shares = Integer.parseInt(sellShares);
                    sellStock(connection, sellSymbol, sell_num_shares);
                } else {
                    System.out.println("Market is Closed! Please try again later");
                }
                break;
            case "8": 
                if (DemoApp.getMarketStatus()) {
                    System.out.println("Canceling Transaction");
                    beginCancelTransaction(connection);
                } else {
                    System.out.println("Market is Closed! Please try again later");
                }
                break;
            case "9": 
                System.out.println("Transaction History for: " + user);
                DisplayTransactionHistory(connection, accountID);
                break;
            case "10": 
                System.out.print("Enter a stock: ");
                String sym = sc.nextLine();
                DisplayStockPriceOf(connection, sym);
                break;
            case "11": 
                System.out.println("Enter a movie title: ");
                movieTitle = sc.nextLine();
                System.out.println("Enter the movies production year: ");
                prodYear = Integer.parseInt(sc.nextLine());
                getMovieDetails(connection, movieTitle, prodYear);
                break;
            case "12": 
                System.out.println("Enter a movie title: ");
                movieTitle = sc.nextLine();
                System.out.println("Enter the movies production year: ");
                prodYear = Integer.parseInt(sc.nextLine());
                getReviewDetails(connection, movieTitle, prodYear);
                break;
            case "13":
                System.out.print("Enter a year range (YYYY-YYYY):");
                String yearRange = sc.nextLine();
                String yearFrom = yearRange.substring(0, 4); 
                int start = Integer.parseInt(yearFrom);
                String yearTo = yearRange.substring(5);
                int end = Integer.parseInt(yearTo);
                getTopMovies(connection, start, end);
                break;
            case "0": 
                System.out.println("Logged Out");
                user = "";
                break;
            default:
                break;
        }
    }

    public static boolean existsStockAccount(Connection connection, String symbol) {
        String sql = "SELECT * FROM Stock_Accounts WHERE symbol = ? AND account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, symbol);
            preparedStatement.setString(2, accountID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch stock");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
        return false;
    }
    public static void createStockAccount(Connection connection, String symbol, int share_count) {
       String sql = "INSERT INTO Stock_Accounts (account_id, symbol, share_count) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, accountID);
            preparedStatement.setString(2, symbol);
            preparedStatement.setInt(3, share_count);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Stock Account Created");
            } else {
                System.out.println("Failed to create a stock account");
            }
        } catch (Exception e) {
            System.out.println("ERROR: insertion failed");
            System.out.println(e);
        }
    }
    public static int getShareCount(Connection connection, String symbol) {
        String sql = "SELECT share_count FROM Stock_Accounts WHERE symbol = ? AND account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, symbol);
            preparedStatement.setString(2, accountID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("share_count");
                } else {
                    System.out.println("ERROR: No share count");
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch share count");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
        return -1;
    }
    public static void updateStockAccountBuy(Connection connection, String symbol, int share_count) {
        String sql = "UPDATE Stock_Accounts SET share_count = ? WHERE account_id = ? AND symbol = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, getShareCount(connection, symbol) + share_count);
            preparedStatement.setString(2, accountID);
            preparedStatement.setString(3, symbol);

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

    public static void updateStockAccountSell(Connection connection, String symbol, int share_count) {
        String sql = "UPDATE Stock_Accounts SET share_count = ? WHERE account_id = ? AND symbol = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int resultingShareCount = getShareCount(connection, symbol) - share_count;
            preparedStatement.setInt(1, resultingShareCount);
            preparedStatement.setString(2, accountID);
            preparedStatement.setString(3, symbol);

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

    public static void updateBalance(Connection connection, double amount) {
        String sql = "UPDATE Market_Accounts SET balance = ? WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, amount);
            preparedStatement.setString(2, accountID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Balance Updated");
            } else {
                System.out.println("Failed to update balance");
            }
        } catch (Exception e) {
            System.out.println("ERROR: update failed");
            System.out.println(e);
        }
    }

    public static int getCurrentNumTraded(Connection connection) {
        String sql = "SELECT num_traded FROM Accounts WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, accountID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("num_traded");
                } else {
                    System.out.println("ERROR: No num_traded");
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch num_traded");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
        return -1;
    }

    public static void updateNumTraded(Connection connection, int numTraded) {
        String sql = "UPDATE Accounts SET num_traded = ? WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, getCurrentNumTraded(connection) + numTraded);
            preparedStatement.setString(2, accountID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Num Traded Updated");
            } else {
                System.out.println("Failed to update num traded");
            }
        } catch (Exception e) {
            System.out.println("ERROR: update failed");
            System.out.println(e);
        }
    }

    public static double getProfits(Connection connection, String aid) {
        String sql = "SELECT profit FROM Customer_Profits WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, aid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("profit");
                } else {
                    System.out.println("ERROR: No profits");
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch profits");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
        return -1;
    }

    public static void updateProfits(Connection connection, String aid, double amount) {
        String sql = "UPDATE Customer_Profits SET profit = ? WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, getProfits(connection, aid) + amount);
            preparedStatement.setString(2, aid);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("ERROR: update failed");
            System.out.println(e);
        }
    }

    public static void buyStock(Connection connection, String symbol, int numShares) {
        String sql = "SELECT * FROM Stocks WHERE symbol = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, symbol);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    double sharePrice = resultSet.getDouble("current_price");
                    double totalCost = sharePrice * numShares;
                    double currBalance = getBalance(connection, accountID);
                    if (totalCost + 20 > currBalance) {
                        System.out.println("Account balance not sufficient. Could not buy stock");
                        return;
                    } else {
                        updateBalance(connection, currBalance - totalCost - 20);
                    }
                    updateNumTraded(connection, numShares);
                    String tID = makeTransaction(connection, "buy");
                    java.sql.Date currDate = DemoApp.getDate(connection);
                    createBuy(connection, tID, currDate, symbol, numShares, sharePrice);
                    updateLatestTransaction(connection, tID, accountID);
                    updateInvolves(connection, tID, symbol, currDate);
                    updateProfits(connection, accountID, -1 * (totalCost + 20));

                    if (!existsStockAccount(connection, symbol)) {
                        createStockAccount(connection, symbol, numShares);
                    } else {
                        updateStockAccountBuy(connection, symbol, numShares);
                    }
                    
                    
                    System.out.println("Buy complete");
                    System.out.print("Current Balance: ");
                    System.out.printf("%.2f", getBalance(connection, accountID));
                    System.out.print("\n");
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
    }

    public static int getNumOwnedShares(Connection connection, String symbol) {
        String sql = "SELECT * FROM Stock_Accounts WHERE symbol = ? AND account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, symbol);
            preparedStatement.setString(2, accountID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("share_count");
                } else {
                    System.out.println("ERROR: None owned");
                    return 0;
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch share count");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
        return 0;
    }

    public static void sellStock(Connection connection, String symbol, int numShares) {
        if (!existsStockAccount(connection, symbol)) {
            System.out.println("ERROR: Stock not owned");
            return;
        }

        String sql = "SELECT * FROM Stocks WHERE symbol = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, symbol);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int numOwnedShares = getNumOwnedShares(connection, symbol);
                    if (numShares > numOwnedShares) {
                        System.out.println("ERROR: Not enough shares owned");
                        return;
                    }

                    double sharePrice = resultSet.getDouble("current_price");
                    double totalValue = sharePrice * numShares;
                    double currBalance = getBalance(connection, accountID);

                    if (currBalance + totalValue - 20 < 0) {
                        System.out.println("Account balance not sufficient. Could not sell stock. ($20 transactional fee)");
                        return;
                    }
                    
                    updateNumTraded(connection, numShares);
                    updateStockAccountSell(connection, symbol, numShares);
                    updateBalance(connection, currBalance + totalValue - 20);
                    String tID = makeTransaction(connection, "sell");
                    java.sql.Date currDate = DemoApp.getDate(connection);
                    createSell(connection, tID, currDate, symbol, numShares, sharePrice);
                    updateInvolves(connection, tID, symbol, currDate);
                    updateLatestTransaction(connection, tID, accountID);
                    updateProfits(connection, accountID, totalValue - 20);
                    
                    System.out.println("Sell complete");
                    System.out.print("Current Balance: ");
                    System.out.printf("%.2f", getBalance(connection, accountID));
                    System.out.print("\n");
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
    }

    public static void updateInvolves(Connection connection, String tID, String symbol, java.sql.Date date) {
        String sql = "INSERT INTO Involves (transaction_id, symbol, transaction_date) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tID);
            preparedStatement.setString(2, symbol);
            preparedStatement.setDate(3, date);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Involves Updated");
            } else {
                System.out.println("Failed to update involves");
            }
        } catch (Exception e) {
            System.out.println("ERROR: update failed");
            System.out.println(e);
        }
    }

    public static void createBuy(Connection connection, String tID, java.sql.Date date, String symbol, int numShares, double sharePrice) {
        String sql = "INSERT INTO Buys (transaction_id, transaction_date, share_buy_price, num_buy_shares) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tID);
            preparedStatement.setDate(2, date);
            preparedStatement.setDouble(3, sharePrice);
            preparedStatement.setInt(4, numShares);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Buy Transaction Logged");
            } else {
                System.out.println("Failed to log transaction");
            }
        } catch (Exception e) {
            System.out.println("ERROR: insertion failed");
            System.out.println(e);
        }
    }

    public static void createSell(Connection connection, String tID, java.sql.Date date, String symbol, int numShares, double sharePrice) {
        String sql = "INSERT INTO Sells (transaction_id, transaction_date, share_sell_price, num_sell_shares) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tID);
            preparedStatement.setDate(2, date);
            preparedStatement.setDouble(3, sharePrice);
            preparedStatement.setInt(4, numShares);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Sell Transaction Logged");
            } else {
                System.out.println("Failed to log transaction");
            }
        } catch (Exception e) {
            System.out.println("ERROR: insertion failed");
            System.out.println(e);
        }
    }

    public static double getAmount(Connection connection, Scanner sc) {
        double amt = 0.0;
        while (true) {
            try {
                amt = Double.parseDouble(sc.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("Error: bad input, try again.");
            }
        }
        return amt;
    }

    public static void cancelTransaction(Connection connection, int num_shares, double share_cost, boolean isBuy, String canceled_transaction) throws SQLException {
        double currBalance = getBalance(connection, accountID);
        String sql = "UPDATE Market_Accounts SET balance = ? WHERE account_id = ?";
        double cancelFee = 20;

        if (isBuy) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setDouble(1, currBalance + (num_shares * share_cost) - cancelFee);
                preparedStatement.setString(2, accountID);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Transaction Canceled");
                    System.out.print("Current Balance: ");
                    System.out.printf("%.2f", getBalance(connection, accountID));
                    System.out.print("\n");
                } else {
                    System.out.println("Failed to cancel transaction");
                }
            } catch (Exception e) {
                System.out.println("ERROR: update failed");
                System.out.println(e);
            }

            String symbol = getLatestSymbol(connection, canceled_transaction);
            revertBuy(connection, num_shares, symbol);
            updateProfits(connection, symbol, (num_shares * share_cost) - cancelFee);

            // sql = "DELETE FROM Buys WHERE transaction_id = ?";
            // try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            //     preparedStatement.setString(1, canceled_transaction);

            //     int rowsAffected = preparedStatement.executeUpdate();

            //     if (rowsAffected > 0) {
            //         System.out.println("Buy Transaction Deleted");
            //     } else {
            //         System.out.println("Failed to delete transaction");
            //     }
            // } catch (Exception e) {
            //     System.out.println("ERROR: deletion failed");
            //     System.out.println(e);
            // }
        } else {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setDouble(1, currBalance - (num_shares * share_cost) - cancelFee);
                preparedStatement.setString(2, accountID);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Transaction Canceled");
                    System.out.print("Current Balance: ");
                    System.out.printf("%.2f", getBalance(connection, accountID));
                    System.out.print("\n");
                } else {
                    System.out.println("Failed to cancel transaction");
                }
            } catch (Exception e) {
                System.out.println("ERROR: update failed");
                System.out.println(e);
            }

            String symbol = getLatestSymbol(connection, canceled_transaction);
            revertSell(connection, num_shares, symbol);
            updateProfits(connection, symbol, -1 * (num_shares * share_cost) - cancelFee);

            // sql = "DELETE FROM Sells WHERE transaction_id = ?";
            // try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            //     preparedStatement.setString(1, canceled_transaction);

            //     int rowsAffected = preparedStatement.executeUpdate();

            //     if (rowsAffected > 0) {
            //         System.out.println("Sell Transaction Deleted");
            //     } else {
            //         System.out.println("Failed to delete transaction");
            //     }
            // } catch (Exception e) {
            //     System.out.println("ERROR: deletion failed");
            //     System.out.println(e);
            // }
        }

        String tID = makeTransaction(connection, "cancel");

        sql = "INSERT INTO Cancels (transaction_id, transaction_date, cancel_tid, cancel_fee) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tID);
            preparedStatement.setDate(2, DemoApp.getDate(connection));
            preparedStatement.setString(3, canceled_transaction);
            preparedStatement.setDouble(4, 20);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Cancel Transaction Logged");
            } else {
                System.out.println("Failed to log transaction");
            }
        } catch (Exception e) {
            System.out.println("ERROR: insertion failed");
            System.out.println(e);
        }

        updateLatestTransaction(connection, tID, accountID);
    }

    public static String getLatestSymbol(Connection connection, String tid) {
        String sql = "SELECT symbol FROM Involves WHERE transaction_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("symbol");
                } else {
                    System.out.println("ERROR: No symbol");
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch symbol");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
        return "";
    }

    public static void revertBuy(Connection connection, int num_shares, String symbol) {
        String sql = "UPDATE Stock_Accounts SET share_count = ? WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, getShareCount(connection, symbol) - num_shares);
            preparedStatement.setString(2, accountID);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("ERROR: update failed");
            System.out.println(e);
        }
    }

    public static void revertSell(Connection connection, int num_shares, String symbol) {
        String sql = "UPDATE Stock_Accounts SET share_count = ? WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, getShareCount(connection, symbol) + num_shares);
            preparedStatement.setString(2, accountID);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("ERROR: update failed");
            System.out.println(e);
        }
    }

    public static void beginCancelTransaction(Connection connection) throws SQLException {
        double currBalance = getBalance(connection, accountID);
        if (20 > currBalance) {
            System.out.println("Account balance not sufficient. Could not cancel latest transaction");
        }

        String sql = "SELECT latest_transaction_id FROM Accounts WHERE account_id=?";
        String sql1 = "SELECT transaction_date FROM Transactions WHERE transaction_id = ?";
        String sql2 = "SELECT * FROM Buys WHERE transaction_id=?";
        String sql3 = "SELECT * FROM Sells WHERE transaction_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, accountID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String latest_tid = resultSet.getString("latest_transaction_id");
                    if (latest_tid != null && !latest_tid.trim().isEmpty()) {
                        // if current date == transaction date for latest_transaction_id
                        PreparedStatement statement1 = connection.prepareStatement(sql1);
                        statement1.setString(1, latest_tid);
                        ResultSet resultSet1 = statement1.executeQuery();
                        if (resultSet1.next()) {
                            java.sql.Date d = resultSet1.getDate("transaction_date");
                            // System.out.println(d);
                            if (d.equals(DemoApp.getDate(connection))) {
                                // System.out.println("Hello");
                                // if latest_transaction_id in buy or sell tables
                                PreparedStatement statement2 = connection.prepareStatement(sql2);
                                statement2.setString(1, latest_tid);
                                ResultSet rsBuy = statement2.executeQuery();
                                if (rsBuy.next()) {
                                    double share_cost = rsBuy.getDouble("share_buy_price");
                                    int num_shares = rsBuy.getInt("num_buy_shares");
                                    cancelTransaction(connection, num_shares, share_cost, true, latest_tid);
                                } 
                                PreparedStatement statement3 = connection.prepareStatement(sql3);
                                statement3.setString(1, latest_tid);
                                ResultSet rsSell = statement3.executeQuery();
                                if (rsSell.next()) {
                                    double share_cost = rsSell.getDouble("share_sell_price");
                                    int num_shares = rsSell.getInt("num_sell_shares");
                                    // System.out.println("HERE");
                                    cancelTransaction(connection, num_shares, share_cost, false, latest_tid);
                                } 
                            }
                        } 
                    }
                } else {
                    System.out.println("ERROR: No transactions to cancel");
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch latest transaction id");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
    }

    public static void updateLatestTransaction(Connection connection, String tID, String aID) throws SQLException {
        String sql = "UPDATE Accounts SET latest_transaction_id = ? WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tID);
            preparedStatement.setString(2, aID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Thanking you for using StarsRus");
            } else {
                System.out.println("Failed to update latest transaction");
            }
        } catch (Exception e) {
            System.out.println("ERROR: update failed");
            System.out.println(e);
        }
    }

    public static boolean transactionIDTaken(Connection connection, String tid){
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tid);

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

    public static String getUUID(Connection connection) {
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        while(transactionIDTaken(connection, uuidAsString)) {
            uuid = UUID.randomUUID();
            uuidAsString = uuid.toString();
            // System.out.println(uuidAsString);
        }
        return uuidAsString;
    }

    public static String makeTransaction(Connection connection, String transaction_type) {
        String tid = getUUID(connection);
        java.sql.Date d = DemoApp.getDate(connection);
        int latestTransactionNumber = getLatestTransactionNumber(connection);
        int transactionNumber = latestTransactionNumber + 1;

        String sql = "INSERT INTO Transactions (transaction_id, transaction_date, account_id, transaction_order, transaction_type) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tid);
            preparedStatement.setDate(2, d);
            preparedStatement.setString(3, accountID);
            preparedStatement.setInt(4, transactionNumber);
            preparedStatement.setString(5, transaction_type);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Transaction Logged");
            } else {
                System.out.println("Failed to log transaction");
            }
        } catch (Exception e) {
            System.out.println("ERROR: insertion failed");
            System.out.println(e);
        }
        return tid;
    }

    public static int getLatestTransactionNumber(Connection connection) {
        String sql = "SELECT MAX(transaction_order) AS latest_transaction FROM Transactions";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("latest_transaction");
                } else {
                    System.out.println("ERROR: No transactions found");
                }
            } catch (Exception e1) {
                System.out.println("ERROR:");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
        return -1;
    }

    // public static void createTransaction(Connection connection, String tID, java.sql.Date date, String aID) {
    //     int latestTransactionNumber = getLatestTransactionNumber(connection);
    //     int transactionNumber = latestTransactionNumber + 1;

    //     String sql = "INSERT INTO transaction (transaction_id, transaction_date, account_id, transaction_order) VALUES (?, ?, ?, ?)";
    //     try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
    //         preparedStatement.setString(1, tID);
    //         preparedStatement.setDate(2, date);
    //         preparedStatement.setString(3, aID);
    //         preparedStatement.setInt(4, transactionNumber);

    //         int rowsAffected = preparedStatement.executeUpdate();

    //         if (rowsAffected > 0) {
    //             System.out.println("Transaction Logged");
    //         } else {
    //             System.out.println("Failed to log transaction");
    //         }
    //     } catch (Exception e) {
    //         System.out.println("ERROR: insertion failed");
    //         System.out.println(e);
    //     }
    // }

    public static void createAccrue(Connection connection, String tID, java.sql.Date date, Double interest) {
        String sql = "INSERT INTO Accrue_Interests (transaction_id, transaction_date, monthly_interest_rate) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tID);
            preparedStatement.setDate(2, date);
            preparedStatement.setDouble(3, interest);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Accrue Transaction Logged");
            } else {
                System.out.println("Failed to log transaction");
            }
        } catch (Exception e) {
            System.out.println("ERROR: insertion failed");
            System.out.println(e);
        }
    }

    public static void createWithdrawal(Connection connection, String tID, java.sql.Date date, Double wAmount) {
        String sql = "INSERT INTO withdraws (transaction_id, transaction_date, amount) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tID);
            preparedStatement.setDate(2, date);
            preparedStatement.setDouble(3, wAmount);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Withdraw Transaction Logged");
            } else {
                System.out.println("Failed to log transaction");
            }
        } catch (Exception e) {
            System.out.println("ERROR: insertion failed");
            System.out.println(e);
        }
    }

    public static void createDeposit(Connection connection, String tID, java.sql.Date date, Double dAmount) {
        String sql = "INSERT INTO Deposits (transaction_id, transaction_date, amount) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tID);
            preparedStatement.setDate(2, date);
            preparedStatement.setDouble(3, dAmount);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Deposit Transaction Logged");
            } else {
                System.out.println("Failed to log transaction");
            }
        } catch (Exception e) {
            System.out.println("ERROR: insertion failed");
            System.out.println(e);
        }
    }

    public static double getBalance(Connection connection, String aID) throws SQLException {
         // Statement and ResultSet are AutoCloseable and closed automatically. 
        String sql = "SELECT balance FROM Market_Accounts WHERE account_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, aID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("balance");
                } else {
                    System.out.println("ERROR: No account balance");
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch account balance");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
        return -1.0;
    }
    
    public static void displayBalance(Connection connection, String aID) throws SQLException{
        // Statement and ResultSet are AutoCloseable and closed automatically. 
        String sql = "SELECT balance FROM Market_Accounts WHERE account_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, aID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.print("Account Balance ($): ");
                if (resultSet.next()) {
                    Double balanceDB = resultSet.getDouble("balance");
                    System.out.printf("%.2f", balanceDB);
                    System.out.print("\n");
                } else {
                    System.out.println("ERROR: No account balance");
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch account balance");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
    }

    public static void withdraw(Connection connection, String aID, double amount) throws SQLException {
        double currBalance = getBalance(connection, aID);
        if (amount > currBalance) {
            System.out.println("Account balance not sufficient. Could not withdraw");
            return;
        }
        String sql = "UPDATE Market_Accounts SET balance = ? WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, currBalance - amount);
            preparedStatement.setString(2, aID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                String latest_tid = makeTransaction(connection, "withdraw");
                java.sql.Date currDate = DemoApp.getDate(connection);
                createWithdrawal(connection, latest_tid, currDate, amount);
                updateLatestTransaction(connection, latest_tid, accountID);
                System.out.println("Withdrawal Complete");
                System.out.print("Current Balance: ");
                System.out.printf("%.2f", getBalance(connection, aID));
                System.out.print("\n");
            } else {
                System.out.println("Failed to withdraw");
            }
        } catch (Exception e) {
            System.out.println("ERROR: update failed");
            System.out.println(e);
        }
    }

    public static void deposit(Connection connection, String accountID, double amount) throws SQLException {
        String sql = "UPDATE Market_Accounts SET balance = ? WHERE account_id = ?";
        double currentBalance = getBalance(connection, accountID);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, amount + currentBalance);
            preparedStatement.setString(2, accountID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                String latest_tid = makeTransaction(connection, "deposit");
                java.sql.Date currDate = DemoApp.getDate(connection);
                createDeposit(connection, latest_tid, currDate, amount);
                updateLatestTransaction(connection, latest_tid, accountID);
                System.out.println("Deposit complete");
                System.out.print("Current Balance: ");
                System.out.printf("%.2f", getBalance(connection, accountID));
                System.out.print("\n");
            } else {
                System.out.println("Failed to deposit");
            }
        } catch (Exception e) {
            System.out.println("ERROR: Update failed");
            System.out.println(e);
        }
    }

    public static String getInvolvedStock(Connection connection, String tid) {
        String sql = "SELECT symbol FROM Involves WHERE transaction_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("symbol");
                } else {
                    System.out.println("ERROR: No stock found");
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch stock");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
        return "";
    }

    public static void displayBuyTransaction(Connection connection, String tid) {
        String sql = "SELECT * FROM Buys WHERE transaction_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tid);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("\nBUY Transaction " + tid);
                    System.out.println(resultSet.getDate("transaction_date") + "");
                    System.out.println("Stock: " + getInvolvedStock(connection, tid) + "\t|\t" + "Number of Shares: " + resultSet.getInt("num_buy_shares") + "\t|\t" + "Share Price: " + resultSet.getDouble("share_buy_price"));
                } else {
                    System.out.println("ERROR: No buy transaction found");
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch buy transaction");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        } 
    }

    public static void displaySellTransaction(Connection connection, String tid) {
        String sql = "SELECT * FROM Sells WHERE transaction_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tid);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("\nSELL Transaction " + tid);
                    System.out.println(resultSet.getDate("transaction_date") + "");
                    System.out.println("Stock: " + getInvolvedStock(connection, tid) + "\t|\t" + "Number of Shares: " + resultSet.getInt("num_sell_shares") + "\t|\t" + "Share Price: " + resultSet.getDouble("share_sell_price"));
                } else {
                    System.out.println("ERROR: No sell transaction found");
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch sell transaction");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        } 
    }

    public static void displayWithdrawTransaction(Connection connection, String tid) {
        String sql = "SELECT * FROM Withdraws WHERE transaction_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tid);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("\nWITHDRAW Transaction " + tid);
                    System.out.println(resultSet.getDate("transaction_date") + "");
                    System.out.println("Amount: " + resultSet.getDouble("amount"));
                } else {
                    System.out.println("ERROR: No withdraw transaction found");
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch withdraw transaction");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        } 
    }

    public static void displayDepositTransaction(Connection connection, String tid) {
        String sql = "SELECT * FROM Deposits WHERE transaction_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tid);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("\nDEPOSIT Transaction " + tid);
                    System.out.println(resultSet.getDate("transaction_date") + "");
                    System.out.println("Amount: " + resultSet.getDouble("amount"));
                } else {
                    System.out.println("ERROR: No deposit transaction found");
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch deposit transaction");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        } 
    }

    public static void displayCancelTransaction(Connection connection, String tid) {
        String sql = "SELECT * FROM Cancels WHERE transaction_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tid);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("\nCANCEL Transaction " + tid);
                    System.out.println(resultSet.getDate("transaction_date") + "");
                    System.out.println("Canceled Transaction ID: " + resultSet.getString("cancel_tid") + "\t|\t" + "Cancel Fee: " + resultSet.getDouble("cancel_fee"));
                } else {
                    System.out.println("ERROR: No cancel transaction found");
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch cancel transaction");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        } 
    }

    public static void displayAccrueTransaction(Connection connection, String tid) {
        String sql = "SELECT * FROM Accrue_Interests WHERE transaction_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tid);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("\nACCRUE Transaction " + tid);
                    System.out.println(resultSet.getDate("transaction_date") + "");
                    System.out.println("Monthly Interest Rate: " + resultSet.getDouble("monthly_interest_rate"));
                } else {
                    System.out.println("ERROR: No accrue transaction found");
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch accrue transaction");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        } 
    }

    public static void DisplayTransactionHistory(Connection connection, String aid) {
        // Statement and ResultSet are AutoCloseable and closed automatically. 
        String sql = "SELECT * FROM Transactions WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, aid);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String tid = resultSet.getString("transaction_id");
                    String transaction_type = resultSet.getString("transaction_type");
                    switch (transaction_type) {
                        case "buy":
                            displayBuyTransaction(connection, tid);
                            break;
                        case "sell":
                            displaySellTransaction(connection, tid);
                            break;
                        case "deposit":
                            displayDepositTransaction(connection, tid);
                            break;
                        case "withdraw":
                            displayWithdrawTransaction(connection, tid);
                            break;
                        case "cancel":
                            displayCancelTransaction(connection, tid);
                            break;
                        case "accrue":
                            displayAccrueTransaction(connection, tid);
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

    public static void getTopMovies(Connection connection, int start, int end) {
        // Statement and ResultSet are AutoCloseable and closed automatically. 
        String sql = "SELECT * FROM Movies WHERE production_year BETWEEN ? AND ? AND rating = 10.0";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, start);
            preparedStatement.setInt(2, end);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("Top Movies: ");
                System.out.println("==================");
                if (resultSet.next()) {
                    System.out.println("Title: " + resultSet.getString("title"));
                } else {
                    System.out.println("ERROR: No movies found between those years with rating = 10");
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch top movie");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
    }
    
    public static void getReviewDetails(Connection connection, String movieTitle, int prodYear) {
        // Statement and ResultSet are AutoCloseable and closed automatically. 
        String sql = "SELECT * FROM Reviews WHERE title = ? AND production_year = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, movieTitle);
            preparedStatement.setInt(2, prodYear);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Reviews for " + movieTitle + " (" + prodYear + ")");
                    System.out.println("Review: " + resultSet.getString("review_content"));
                } else {
                    System.out.println("ERROR: No movie reviews found");
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch movie reviews");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
    }

    public static void getMovieDetails(Connection connection, String movieTitle, int prodYear) {
        // Statement and ResultSet are AutoCloseable and closed automatically. 
        String sql = "SELECT * FROM Movies WHERE title = ? AND production_year = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, movieTitle);
            preparedStatement.setInt(2, prodYear);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("Movie Details: ");
                System.out.println("==================");
                if (resultSet.next()) {
                    System.out.println("Title: " + resultSet.getString("title"));
                    System.out.println("Production Year: " + resultSet.getInt("production_year"));
                    System.out.println("Rating: " + resultSet.getInt("rating"));
                } else {
                    System.out.println("ERROR: No movie found");
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch movie details");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
    }

    public static void DisplayAllStocks(Connection connection) {
        // Statement and ResultSet are AutoCloseable and closed automatically. 
        String sql = "SELECT * FROM Stocks";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("\n+---------------------------------+");
                System.out.println("| All Currently Availible Stocks: |");
                System.out.println("+---------------------------------+");
                while (resultSet.next()) {
                    System.out.println("Stock Symbol: " + resultSet.getString("symbol"));
                    System.out.println("Actor/Director Name: " + resultSet.getString("actor_name"));
                    System.out.println("Date of Birth: " + resultSet.getDate("dob"));
                    System.out.println("Current Stock Price: " + resultSet.getDouble("current_price"));
                    displayActorMovieContracts(connection, resultSet.getString("symbol"));
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch stocks");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
    }

    public static void displayActorMovieContracts(Connection connection, String symbol) {
        String sql = "SELECT * FROM Contracts WHERE symbol = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, symbol);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("Contracts Associated with " + symbol + ": ");
                while (resultSet.next()) {
                    System.out.println("==================");
                    System.out.println("Movie Title: " + resultSet.getString("title"));
                    System.out.println("Production Year: " + resultSet.getInt("production_year"));
                    System.out.println("Contract Year: " + resultSet.getInt("contract_year"));
                    System.out.println("Total Value: " + resultSet.getDouble("total_value"));
                    System.out.println("Role: " + resultSet.getString("contract_role"));
                    System.out.println("==================");
                }
                System.out.println("\n");
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch stocks");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
    }

    public static void DisplayStockPriceOf(Connection connection, String symbol) {
        // Statement and ResultSet are AutoCloseable and closed automatically. 
        String sql = "SELECT * FROM Stocks WHERE symbol=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, symbol);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Stock Symbol: " + resultSet.getString("symbol"));
                    System.out.println("Current Price: " + resultSet.getDouble("current_price"));
                    displayActorMovieContracts(connection, resultSet.getString("symbol"));
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch movie details");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
    }

    public static void DisplayMyStocks(Connection connection) {
        // Statement and ResultSet are AutoCloseable and closed automatically. 
        String sql = "SELECT symbol, share_count FROM Stock_Accounts WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, accountID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("\n+------------------+");
                System.out.println("| My Owned Stocks: |");
                System.out.println("+------------------+");
                while (resultSet.next()) {
                    System.out.println("Stock Symbol: " + resultSet.getString("symbol"));
                    System.out.println("Number of Shares: " + resultSet.getString("share_count"));
                    System.out.println("\n");
                }
            } catch (Exception e1) {
                System.out.println("ERROR: Could not fetch user owned stocks");
                System.out.println(e1);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
        }
    }
}
