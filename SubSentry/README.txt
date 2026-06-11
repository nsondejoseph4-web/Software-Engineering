README 

🛡️ SubSentry: Full-Stack Subscription Analytics Platform

SubSentry is a modern, decoupled full-stack web application designed to solve "subscription fatigue" by giving users a central, data-driven dashboard to track, categorize, and optimize their recurring financial commitments.

Built using a highly scalable architectural pipeline, the application handles complex multi-currency normalization and live financial data tracking.

⚙️ Core System Features Built

Dynamic Financial Metrics Dashboard: Calculates a user’s total monthly monetary "bleed" rate and projects a 12-month annual cost drain.

Live Interactive Data Visualization: Renders a secure, browser-compliant Doughnut Pie Chart mapping out relative financial spending concentrations across custom categories (Entertainment, Software, Gym, Food, Utilities).

Global Multi-Currency Normalization System: Employs a backend exchange engine with automatic local fallback overrides allowing users to toggle their preferred base currency (USD, GBP, EUR) live, instantly adjusting all metrics across mixed-currency inputs.

Full CRUD Operational Pipeline:

Create: A formal tracking input layout form that securely pushes payload metrics across network routes.

Read: Multi-stream asynchronous data aggregation utilizing Promise.all fetching.

Delete: A secure individual item un-tracking engine mapping to direct relational database deletions.

🛠️ The Technical Stack Engineered

Frontend (User Interface Layer): Modern React (Vite) employing hooks (useState, useEffect) for state synchronisation, asynchronous network pipelines (fetch), and Chart.js (react-chartjs-2) for secure vector rendering.

Backend (Business Logic Layer): Java (Spring Boot Framework) using structural REST controller endpoints (@RestController, @CrossOrigin), secure precompiled statement queries (PreparedStatement) to prevent SQL Injection, and dynamic multi-origin resource routing.

Database (Storage Layer): MySQL Relational Database Engine utilizing exact currency constraints (DECIMAL(10,2)), enumeration data type boundaries (ENUM), and structural tracking dependency cleanups via automated cascading deletions (ON DELETE CASCADE).

🧠 Advanced Engineering Concepts Mastered

Decoupled Architecture: Building a distinct frontend repo that communicates with a distinct backend server via strict RESTful contract structures.

Robust Error Boundary Fallbacks: Engineering defensive backend pipelines that intercept third-party network drops without breaking core server processing.

Data Consistency: Resolving safe-update constraints and string parsing glitches directly via low-level database operations and script modifications.

