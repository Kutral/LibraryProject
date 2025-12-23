package com.eswar.library.model;

import java.sql.Timestamp;

public class BookRequest {
    private int id;
    private int userId;
    private String title;
    private String author;
    private String isbn;
    private String status; // PENDING, APPROVED, REJECTED
    private Timestamp requestDate;
    
    // Additional field for display purposes (joined with user table)
    private String username;

    public BookRequest() {}

    public BookRequest(int userId, String title, String author, String isbn) {
        this.userId = userId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.status = "PENDING";
        this.requestDate = new Timestamp(System.currentTimeMillis());
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getRequestDate() { return requestDate; }
    public void setRequestDate(Timestamp requestDate) { this.requestDate = requestDate; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
