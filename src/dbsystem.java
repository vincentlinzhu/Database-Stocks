import UserAuthentication.*;

import java.sql.SQLException;
import java.sql.Connection;
// import java.util.HashMap;
// import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;
import oracle.net.nt.ConnOption;

public class dbsystem {
    final static String DB_URL = "jdbc:oracle:thin:@firstdb_tp?TNS_ADMIN=/Users/bryan/Documents/174a/cs174a-jdbc/lib/Wallet_firstdb";
    final static String DB_USER = "ADMIN";
    final static String DB_PASSWORD = "Chubber69420";

    public static void main(String[] args) throws SQLException {
        Properties info = new Properties();

        System.out.println("Initializing connection properties...");
        info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER);
        info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD);
        info.put(OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20");

        System.out.println("Creating OracleDataSource...");
        OracleDataSource ods = new OracleDataSource();

        System.out.println("Setting connection properties...");
        ods.setURL(DB_URL);
        ods.setConnectionProperties(info);

        try (OracleConnection connection = (OracleConnection) ods.getConnection()) {
            Scanner sc = new Scanner(System.in);
            while (true) {
                System.out.println("\n+---------------------------+");
                System.out.println("| StarsRus Brokerage System |");
                System.out.println("+---------------------------+");
                System.out.println("(1) Customer App");
                System.out.println("(2) Manager App");
                System.out.println("(3) Demo System");
                System.out.println("(4) Exit");
                
                System.out.println("Select:");

                String option = sc.nextLine();
                switch (option) {
                    case "1": 
                        System.out.println("Initializing Customer Interface ... ");
                        TraderApp.enter(connection, sc);
                        break;
                    case "2": 
                        System.out.println("Initializing Manager Interface ... ");
                        ManagerApp.enter(connection, sc);
                        break;
                    case "3": 
                        System.out.println("Demo System Initializing ... ");
                        DemoApp.enter(connection, sc);
                        break;
                    case "4": 
                        System.out.println("Exiting ...");
                        sc.close();
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("CONNECTION ERROR:");
            System.out.println(e);
        }
    }
    
    // Trader dashboard
}
