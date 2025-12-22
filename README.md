# 📚 Library Management System (Backend)

A robust, Java-based backend for a Library Management System built with **Servlets**, **JDBC**, and **MySQL**. This system features a layered architecture, automated database initialization, and a comprehensive REST API for users and administrators.

---

## 🚀 Key Features

### 👤 User Features
- **Authentication:** Secure Login and Signup functionality.
- **Book Discovery:** Search books by Title, Author, or ISBN with real-time availability.
- **Borrowing System:**
    - Transaction-safe borrowing (prevents over-borrowing).
    - Prevents borrowing the same book twice if not returned.
    - Full borrowing history tracking.
- **Returns:** Simple one-click return process that updates stock immediately.

### 🛡️ Admin Features
- **Book Management:** Create, Update, and Delete books.
- **User Management:** Update user roles (Promote to Admin) and delete users.
- **Database Control:** Automated schema initialization on server startup.

---

## 🛠️ Tech Stack

- **Language:** Java 17+
- **Server:** Apache Tomcat 11.0.13
- **Database:** MySQL
- **Connectivity:** JDBC (Java Database Connectivity)
- **JSON Handling:** Google Gson
- **Build Tool:** Maven

---

## 📂 Project Structure

```text
src/main/java/com/eswar/library/
├── dao/        # Data Access Objects (SQL queries & DB logic)
├── listener/   # App lifecycle listeners (DB Auto-Initialization)
├── model/      # Plain Java Objects (User, Book, Borrow)
├── service/    # Business logic layer
├── servlets/   # REST API Controllers (Handles JSON requests)
└── util/       # Database connection utilities
```

---

## ⚙️ Setup & Installation

### 1. Prerequisites
- **MySQL Server** running on your machine.
- **Apache Tomcat 11** installed.
- **Java JDK 17** or higher.

### 2. Database Configuration
Update the `src/main/resources/db.properties` file with your MySQL credentials:
```properties
db.url=jdbc:mysql://localhost:3306/library
db.username=YOUR_USERNAME
db.password=YOUR_PASSWORD
```

### 3. Automatic Initialization
The system is equipped with a `DatabaseInitializer` listener. 
- Just start the Tomcat server.
- The system will automatically create the `library` database and all required tables if they don't exist.
- It also seeds a default **Admin** account (`admin` / `admin`).

---

## 📡 API Endpoints

### Public / User APIs
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/auth/login` | Login user |
| `POST` | `/api/auth/signup` | Register new user |
| `GET` | `/api/books` | Search & List all books |
| `GET` | `/api/borrow` | View user borrow history |
| `POST` | `/api/borrow` | Borrow a book |
| `PUT` | `/api/borrow` | Return a book |

### Admin APIs
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/users` | List all registered users |
| `POST` | `/api/admin/books` | Add a new book |
| `PUT` | `/api/admin/books` | Update book details |
| `DELETE` | `/api/admin/books` | Delete a book |
| `PUT` | `/api/admin/users` | Update user details/roles |
| `DELETE` | `/api/admin/users` | Delete a user |

---

## 🧪 Testing with the Dashboard

We have included a **Test Dashboard** to help you interact with the APIs immediately:
- URL: `http://localhost:8080/test-dashboard`
- Features: Interactive forms for every API endpoint, real-time JSON response viewing, and one-click data seeding.

---

## 🔒 Security Best Practices
- **SQL Injection Prevention:** Uses `PreparedStatement` for all queries.
- **Data Integrity:** Employs MySQL Transactions with Row-Level Locking (`FOR UPDATE`) for book borrowing.
- **Sensitive Data:** Passwords are cleared from User objects before sending JSON responses.

---

## 📝 License
This project is open-source. Feel free to use and modify it!
