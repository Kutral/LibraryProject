CREATE DATABASE IF NOT EXISTS library;
USE library;

DROP TABLE IF EXISTS borrowing_history;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    available_copies INT DEFAULT 1
);

CREATE TABLE borrowing_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    borrow_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    return_date TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);

INSERT INTO users (username, password, email, role) VALUES ('admin', 'admin', 'admin@library.com', 'ADMIN');
INSERT INTO users (username, password, email, role) VALUES ('user', 'user', 'user@library.com', 'USER');

INSERT INTO books (title, author, isbn, available_copies) VALUES 
('The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', 5),
('To Kill a Mockingbird', 'Harper Lee', '9780061120084', 3),
('1984', 'George Orwell', '9780451524935', 4),
('Pride and Prejudice', 'Jane Austen', '9780141439518', 2),
('The Catcher in the Rye', 'J.D. Salinger', '9780316769480', 3);