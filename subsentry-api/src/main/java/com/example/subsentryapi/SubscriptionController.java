package com.example.subsentryapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class SubscriptionController {
    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String dbUser;
    @Value("${spring.datasource.password}")
    private String dbPassword;

    // Pull my access key from application.properties
    @Value("${subsentry.api.key}")
    private String apiKey;

    // Helper method that queries the web API to convert values live
    private double convertCurrency(String from, String to, double amount) {
        if (from.equalsIgnoreCase(to)) return amount;

        String apiURL = "https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/" + from + "/" + to;

        try {
            java.net.URI uri = java.net.URI.create(apiURL);
            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();

            // Set a quick 3-second network timeout so the app doesn't freeze waiting for an offline server
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder(uri)
                    .timeout(java.time.Duration.ofSeconds(3))
                    .GET()
                    .build();

            java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

            String body = response.body();
            if (body != null && body.contains("\"conversion_rate\":")) {
                int index = body.indexOf("\"conversion_rate\":") + 18;
                int endIndex = body.indexOf(",", index);
                if (endIndex == -1) endIndex = body.indexOf("}", index);

                double rate = Double.parseDouble(body.substring(index, endIndex).trim());
                return amount * rate;
            }
        } catch (Exception e) {
            // 🔴 GRACEFUL OFFLINE FALLBACK LAYER:
            System.out.println("⚠️ System Offline/Firewalled. Using internal backup exchange rates...");

            // Simple hardcoded fallback rules for testing when disconnected
            if (from.equals("GBP") && to.equals("USD")) return amount * 1.25;
            if (from.equals("USD") && to.equals("GBP")) return amount * 0.80;
            if (from.equals("EUR") && to.equals("USD")) return amount * 1.08;
            if (from.equals("USD") && to.equals("EUR")) return amount * 0.92;
        }

        return amount; // Absolute safety fallback
    }

    @GetMapping("/burn-rate/{userId}/{baseCurrency}")
    public Map<String, Object> getMonthlyBurnRate(@PathVariable int userId, @PathVariable String baseCurrency) {
        Map<String, Object> responseData = new HashMap<>();

        // Pull all individual elements dynamically to run server-side math
        String sqlQuery = "SELECT cost, currency, billing_cycle FROM subscriptions WHERE user_id = ?;";
        double combinedMonthlyBleed = 0.0;

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    double rawCost = rs.getDouble("cost");
                    String itemCurrency = rs.getString("currency");
                    String cycle = rs.getString("billing_cycle");

                    // 1. Normalize time intervals
                    double monthlyCostInNativeCurrency = cycle.equals("yearly") ? (rawCost / 12) : rawCost;

                    // 2. Normalize currency dynamically via web API integration mapping
                    double costInTargetBaseCurrency = convertCurrency(itemCurrency, baseCurrency, monthlyCostInNativeCurrency);

                    combinedMonthlyBleed += costInTargetBaseCurrency;
                }

                responseData.put("userId", userId);
                responseData.put("totalMonthlyBleed", Math.round(combinedMonthlyBleed * 100.0) / 100.0);
                responseData.put("estimatedAnnualCost", Math.round((combinedMonthlyBleed * 12) * 100.0) / 100.0);
                responseData.put("status", "success");
            }
        } catch (SQLException e) {
            responseData.put("status", "error");
            responseData.put("message", e.getMessage());
        }
        return responseData;
    }

    @PostMapping("/add-subscription")
    public Map<String, Object> addSubscription(@org.springframework.web.bind.annotation.RequestBody Map<String, Object> payload) {
        Map<String, Object> responseData = new HashMap<>();

        // 1. Extract values safely from the incoming frontend JSON package
        int userId = (int) payload.get("userId");
        String serviceName = (String) payload.get("serviceName");
        // Handle numbers safely since JSON numbers can parse as Double or Integer
        double cost = Double.parseDouble(payload.get("cost").toString());

        String currency = (String) payload.get("currency");
        String billingCycle = (String) payload.get("billingCycle");
        String nextBillingDate = (String) payload.get("nextBillingDate"); // Format: YYYY-MM-DD

        // 2. Prepare our SQL insert statement matching our table fields
        String sqlQuery = "INSERT INTO subscriptions (user_id, service_name, cost, currency, billing_cycle, next_billing_date) " +
                "Values (?, ?, ?, ?, ?, ?);";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            // 3. Bind the parameters sequentially to prevent SQL Injection
            pstmt.setInt(1, userId);
            pstmt.setString(2, serviceName);
            pstmt.setDouble(3, cost);
            pstmt.setString(4, currency);
            pstmt.setString(5, billingCycle);
            pstmt.setDate(6, java.sql.Date.valueOf(nextBillingDate)); //Converts String date to SQL Date object

            // 4. Run the query on MySQL
            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                responseData.put("status", "success");
                responseData.put("message", "Subscription added successfully!");
            } else {
                responseData.put("status", "error");
                responseData.put("message", "Failed to insert subscription record.");
            }
        } catch (SQLException e) {
            responseData.put("status", "error");
            responseData.put("message", "Database error: " + e.getMessage());
        }
        return responseData;
    }

    @GetMapping("/subscriptions/{userId}")
    public java.util.List<Map<String, Object>> getAllsubscriptions(@PathVariable int userId) {
        java.util.List<Map<String, Object>> subList = new java.util.ArrayList<>();

        String sqlQuery = "SELECT subscription_id, service_name, cost, currency, billing_cycle, next_billing_date " +
                "FROM subscriptions WHERE user_id = ? ORDER BY next_billing_date ASC;";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> sub = new HashMap<>();
                    sub.put("id", rs.getInt("subscription_id"));
                    sub.put("serviceName", rs.getString("service_name"));
                    sub.put("cost", rs.getDouble("cost"));
                    sub.put("billingCycle", rs.getString("billing_cycle"));
                    sub.put("nextBillingDate", rs.getDate("next_billing_date").toString());
                    subList.add(sub);
                }
            }
        } catch (SQLException e) {
            // Log the error to my console
            e.printStackTrace();
        }
        return subList; // Spring Boot automatically converts this List of Maps into a JSON array!
    }

    @DeleteMapping("/delete-subscription/{id}")
    public Map<String, Object> deleteSubscription(@PathVariable int id) {
        Map<String, Object> responseData = new HashMap<>();

        // SQL command to remove a row based on its unique identity ID primary key
        String sqlQuery = "DELETE FROM subscriptions WHERE subscription_id = ?;";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                responseData.put("status", "success");
                responseData.put("message", "Subscription untracked successfully.");

            } else {
                responseData.put("status", "error");
                responseData.put("message", "Record not found or already deleted.");
            }
        } catch (SQLException e) {
            responseData.put("status", "error");
            responseData.put("message", "Database error: " + e.getMessage());
        }
        return responseData;
    }

    @GetMapping("/spend-by-category/{userId}/{baseCurrency}")
    public java.util.List<Map<String, Object>> getSpendByCategory(@PathVariable int userId, @PathVariable String baseCurrency) {
        java.util.List<Map<String, Object>> categoryDataList = new java.util.ArrayList<>();

        // SQL Query to pull cost, currency, billing cycle, and our brand new category column
        String sqlQuery = "SELECT cost, currency, billing_cycle, category FROM subscriptions WHERE user_id = ?;";

        // A map to accumulate costs per category locally on our server
        Map<String, Double> categoryTotals = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    double rawCost = rs.getDouble("cost");
                    String itemCurrency = rs.getString("currency");
                    String cycle = rs.getString("billing_cycle");
                    String category = rs.getString("category");

                    // Convert and normalise values using the existing multi-currency logic block
                    double monthlyCostInNative = cycle.equals("yearly") ? (rawCost / 12) : rawCost;
                    double costInTargetBaseCurrency = convertCurrency(itemCurrency, baseCurrency, monthlyCostInNative);

                    // Accumulate totals per category key string inside our hashmap tracker
                    categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + costInTargetBaseCurrency);
                }

                // Pack our accumulated hashmap cleanly into a List structure for easy React mapping
                for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("name", entry.getKey()); // Recharts reads the 'name' field for graph labels
                    row.put("value", Math.round(entry.getValue() * 100.0) / 100.0); // Recharts reads 'value' field ofr chart portions
                    categoryDataList.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryDataList;
    }
}
