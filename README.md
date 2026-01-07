<div align="center">

# 📚 Library Management System (Backend)

[![Java](https://img.shields.io/badge/Java-21-orange.svg?style=flat-square&logo=java)](https://www.oracle.com/java/)
[![Servlet](https://img.shields.io/badge/Servlet-API-blue.svg?style=flat-square)](https://jakarta.ee/specifications/servlet/)
[![MySQL](https://img.shields.io/badge/MySQL-DB-4479A1.svg?style=flat-square&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36.svg?style=flat-square&logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-Open%20Source-green.svg?style=flat-square)]()

A robust, enterprise-grade backend for a **Library Management System** engineered with core Java technologies.  
Designed for scalability, security, and ease of integration.

[**Explore Live Demo**](https://librarybackend-1dvi.onrender.com) · [**Test Dashboard**](https://librarybackend-1dvi.onrender.com/test-dashboard) · [**Report Bug**](https://github.com/kutraleeswaran/library-backend/issues)

</div>

---

## 🚀 Live Demo & Links
- **🌐 Frontend App:** [https://libraryfrontend-1ter.onrender.com](https://libraryfrontend-1ter.onrender.com)
- **⚙️ Backend API:** [https://librarybackend-1dvi.onrender.com](https://librarybackend-1dvi.onrender.com)
- **🧪 Test Dashboard:** [https://librarybackend-1dvi.onrender.com/test-dashboard](https://librarybackend-1dvi.onrender.com/test-dashboard)

---

## 📖 Overview

This project serves as the backbone for a modern library application. It provides a comprehensive **REST API** to manage books, users, borrowing transactions, and administrative tasks. Built with a layered architecture, it ensures separation of concerns and maintainability.

> **Note:** The backend is currently hosted on **Render** and connects to a cloud-managed **Aiven MySQL** database.

---

## 🚀 Key Features

### 👤 For Users
- **🔐 Secure Authentication:** Robust Login and Signup with password handling.
- **🔍 Smart Discovery:** Real-time search by Title, Author, or ISBN.
- **📖 Borrowing Engine:** Transaction-safe borrowing system that prevents conflicts.
- **↩️ Easy Returns:** One-click return process updating inventory instantly.
- **📜 History Tracking:** Full view of past and active borrows.

### 🛡️ For Administrators
- **📚 Inventory Control:** complete CRUD operations for books.
- **👥 User Management:** Promote users to Admin roles or ban accounts.
- **📝 Request Approval:** Review and process user requests for new books.
- **📊 Analytics Dashboard:** Real-time insights on users, books, and activity.
- **⚙️ Auto-Configuration:** Database schema automatically initializes on startup.

---

## 🛠️ Tech Stack

| Component | Technology | Description |
| :--- | :--- | :--- |
| **Language** | Java 21 | Latest LTS version for performance. |
| **Server** | Apache Tomcat 10.1 | Robust servlet container (Dockerized). |
| **Database** | MySQL | Relational data management (Cloud/Aiven). |
| **Connectivity** | JDBC | Low-level, high-performance data access. |
| **Data Format** | JSON (Gson) | Lightweight data interchange. |
| **Build Tool** | Maven | Dependency management and build automation. |

---

## ⚡ Getting Started

### 1️⃣ Prerequisites
Ensure you have the following installed:
*   **Java JDK 21+**
*   **Maven**
*   **MySQL Server** (for local development)

### 2️⃣ Database Setup
Configure your local database connection in `src/main/resources/db.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/library
db.username=YOUR_USERNAME
db.password=YOUR_PASSWORD
```

### 3️⃣ Installation & Run
The application includes a `DatabaseInitializer` that sets up your schema automatically.

```bash
# Clone the repository
git clone https://github.com/your-username/library-backend.git

# Navigate to project directory
cd library-backend

# Build the project
mvn clean package

# Deploy the WAR file from /target to your Tomcat server
```

---

## 📡 API Reference

### 🔐 Auth & Users
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/auth/login` | Authenticate user & get session. |
| `POST` | `/api/auth/signup` | Register a new account. |
| `GET` | `/api/users` | List all users (Admin only). |

### 📚 Books & Search
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/books` | Get all available books. |
| `GET` | `/api/books?query={term}` | Search by title, author, or ISBN. |
| `POST` | `/api/requests` | Request a book not in stock. |

### 🔄 Borrowing System
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/borrow` | Borrow a specific book. |
| `PUT` | `/api/borrow` | Return a borrowed book. |
| `GET` | `/api/borrow?userId={id}` | View personal borrow history. |

### 🛠️ Administration
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/admin/books` | Add new inventory. |
| `PUT` | `/api/admin/books` | Update book details. |
| `DELETE` | `/api/admin/books` | Remove a book. |
| `GET` | `/api/admin/stats` | System-wide statistics. |

---

## 🧪 Interactive Test Dashboard

We've built a custom **Test Dashboard** right into the backend!
Navigate to `/test-dashboard` on your deployment to:
*   Test every API endpoint with a GUI.
*   View raw JSON responses.
*   Seed the database with test data in one click.

---

## 🔒 Security Measures
*   **SQL Injection Protection:** All queries use `PreparedStatement`.
*   **Concurrency Control:** `FOR UPDATE` locking prevents double-booking.
*   **Data Privacy:** Sensitive fields (passwords) are sanitized from responses.
*   **Role-Based Access:** Admin endpoints are strictly gated.

---

<div align="center">

### Made with ❤️ by Kutraleeswaran

[![GitHub](https://img.shields.io/badge/GitHub-Profile-black?style=for-the-badge&logo=github)](https://github.com/kutraleeswaran)

</div>