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
- **Book Requests:** Approve or reject user requests for new books.
- **Dashboard Stats:** View key metrics (total users, books, active borrows).
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

### 🔐 Authentication
| Method | Endpoint | Description | Request Body |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/login` | Login user | `{ "username": "...", "password": "..." }` |
| `POST` | `/api/auth/signup` | Register new user | `{ "username": "...", "password": "...", "email": "..." }` |

### 📚 Books (Public)
| Method | Endpoint | Description | Parameters |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/books` | List all books | None |
| `GET` | `/api/books?query=...` | Search books | `query` (title/author/isbn) |

### 📖 Borrowing (User)
| Method | Endpoint | Description | Request Body / Params |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/borrow?userId=...` | View borrow history | `userId` (Param) |
| `POST` | `/api/borrow` | Borrow a book | `{ "userId": 1, "bookId": 5 }` |
| `PUT` | `/api/borrow` | Return a book | `{ "borrowId": 10 }` |

### 🙋 Book Requests (User)
| Method | Endpoint | Description | Request Body / Params |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/requests` | Request a new book | `{ "userId": 1, "title": "..." }` |
| `GET` | `/api/requests?userId=...` | View my requests | `userId` (Param) |

### 🛡️ Admin: Books
| Method | Endpoint | Description | Request Body / Params |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/admin/books` | Add a new book | `{ "title": "...", "author": "...", "isbn": "...", "availableCopies": 5 }` |
| `PUT` | `/api/admin/books` | Update book details | `{ "id": 1, "title": "...", ... }` |
| `DELETE` | `/api/admin/books?id=...` | Delete a book | `id` (Param) |

### 🛡️ Admin: Users
| Method | Endpoint | Description | Request Body / Params |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/users` | List all users | None |
| `PUT` | `/api/admin/users` | Update user | `{ "id": 2, "role": "ADMIN", ... }` |
| `DELETE` | `/api/admin/users?id=...` | Delete a user | `id` (Param) |

### 🛡️ Admin: Requests & Stats
| Method | Endpoint | Description | Headers / Body |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/admin/requests` | List all requests | **Header:** `X-User-ID: {admin_id}` |
| `PUT` | `/api/admin/requests/{id}` | Approve/Reject | **Header:** `X-User-ID` <br> Body: `{ "status": "APPROVED" }` |
| `GET` | `/api/admin/stats` | Dashboard Stats | None |

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
- **Admin Security:** Protected endpoints require `X-User-ID` header validation.

---

## 📝 License
This project is open-source. Feel free to use and modify it!