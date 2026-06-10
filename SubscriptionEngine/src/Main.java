import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    // 1. Connection Configurations
    private static final String URL = "jdbc:mysql://localhost:3306/subsentry_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "MySQL25.com";

    public static void main(String[] args) {
        int targetUserId = 1; // Testing with Alex (User #1)
        calculateMonthlyBurnRate(targetUserId);
    }

    public static void calculateMonthlyBurnRate(int userId) {
        // 2. Define the exact math query that was verified in MySQL Workbench
        String sqlQuery = "SELECT SUM(" +
                "  CASE " +
                "    WHEN billing_cycle = 'monthly' THEN cost " +
                "    WHEN billing_cycle = 'yearly' THEN ROUND(cost / 12, 2) " +
                "    ELSE 0 " +
                "  END" +
                ") AS total_monthly_spend " +
                "FROM subscriptions " +
                "WHERE user_id = ?;";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 3. Establish the secure pipeline to the database
            try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

                // 4. Bind the userId securely to the query to prevent SQL Injection attacks
                pstmt.setInt(1, userId);

                // 5. Execute the query and capture the returning data grid
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        double monthlySpend = rs.getDouble("total_monthly_spend");
                        double annualSpend = monthlySpend * 12;

                        // 6. Print the dynamic calculated financial results
                        System.out.println("===== SubSentry Backend Math Engine =====");
                        System.out.println("User ID: " + userId);
                        System.out.printf("Total Monthly Bleed: $%.2f%n", monthlySpend);
                        System.out.printf("Estimated Annual Cost: $%.2f%n", annualSpend);
                        System.out.println("============================================");
                    }
                }
            } catch (Exception e) {
                System.err.println("Database connection or query failed!");
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}