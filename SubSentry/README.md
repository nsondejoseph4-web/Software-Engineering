# 🛡️ SubSentry: Full-Stack Subscription Analytics Platform

**SubSentry** is a modern, decoupled full-stack FinTech web application.
It provides users with an interactive, data-driven dashboard.
Users can track, categorize, and optimize recurring financial commitments.
The system handles complex metrics under dynamic multi-currency constraints.

---

## 🚀 Key Architectural Features

*   **Financial Metrics Dashboard Engine:** Aggregates a user's real-time outputs.
*   **Burn Rate Calculator:** Computes monthly bleed rates and 12-month projections.
*   **Live Interactive Data Visualization:** Integrates responsive canvas layers.
*   **Doughnut Charts:** Maps expenditure concentrations across targeted categories.
*   **Custom Categories:** *Entertainment, Software, Gym, Food, Utilities*.
*   **Global Currency Normalization System:** Employs a robust foreign exchange engine.
*   **Offline Safety Fallback Layer:** Keeps calculations running if connection drops.
*   **Currency Toggle:** Users can switch base display (*USD, GBP, EUR*) live.
*   **Full CRUD Operational Pipeline:** Handles complete data entry cycles.
    *   **Create:** A formal input form that pushes data payloads safely.
    *   **Read:** Synchronized data aggregation utilizing `Promise.all` fetching.
    *   **Delete:** A secure un-tracking row link mapping to MySQL deletions.

---

## 🛠️ Tech Stack & Engineering Pipeline

*   **Frontend (UI Layer):** React (Vite) utilizing state hooks.
*   **DOM Synchronisation:** Managed via standard `useState` and `useEffect`.
*   **Network Requests:** Asynchronous data processing using `fetch`.
*   **Vector Graphics:** Rendered using **Chart.js (`react-chartjs-2`)**.
*   **Backend (Business Logic Layer):** Java (Spring Boot Framework).
*   **RESTful API controllers:** Connected using `@RestController` and `@CrossOrigin`.
*   **Data Security:** Employs `PreparedStatement` to systematically block SQL Injection.
*   **Property Injection:** Environmental details loaded cleanly using `@Value`.
*   **Database (Persistence Layer):** MySQL Relational Database Engine.
*   **Fixed-Point Math Types:** Enforces exact currency limits using `DECIMAL(10,2)`.
*   **Data Integrity Constraints:** Restricts group text entries using `ENUM`.
*   **Automated Cleanups:** Wipes orphan fields using `ON DELETE CASCADE`.
---
## 📐 Database Schema Blueprints

```text
  [ Users Table ]           [ Subscriptions Table ]          [ Alerts Table ]
  - user_id (PK)    ----->  - subscription_id (PK)   ----->  - alert_id (PK)
  - email                   - user_id (FK)                   - subscription_id (FK)
  - password_hash           - service_name                   - alert_date
                            - cost [DECIMAL]                 - is_sent [BOOLEAN]
                            - currency [VARCHAR]
                            - category [ENUM]
                            - billing_cycle [ENUM]
                            - next_billing_date [DATE]
```
---

## ⚙️ Direct Local Installation Guide

### 1. Database Setup (MySQL)
Execute the relational table creation scripts inside your local MySQL shell:
```sql
CREATE DATABASE subsentry_db;
USE subsentry_db;
-- Run structural scripts matching schema configurations...
```

### 2. Backend Boot sequence (Java / Spring Boot)
1. Navigate inside the `/subsentry-api` project folder directory.
2. Ensure your database credentials are saved inside `application.properties`.
3. Compile and execute the main configuration server class file.
4. The API will listen live at the route: `http://localhost:8080/api/`

### 3. Frontend Web Client Start (React / JavaScript)
Open your terminal inside the `/subsentry-frontend` directory folder.
Spin up the Vite development server using these commands:
```bash
npm install
npm run dev
```
The localized dashboard interface will launch instantly at: `http://localhost:5173/`

