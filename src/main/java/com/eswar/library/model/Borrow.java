package com.eswar.library.model;

import java.sql.Timestamp;

public class Borrow {
    private int id;
    private int userId;
    private int bookId;
    private String bookTitle;
    private String bookAuthor;
    private Timestamp borrowDate;
    private Timestamp returnDate;

    public Borrow() {}

    public Borrow(int userId, int bookId) {
        this.userId = userId;
        this.bookId = bookId;
        this.borrowDate = new Timestamp(System.currentTimeMillis());
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getBookAuthor() { return bookAuthor; }
    public void setBookAuthor(String bookAuthor) { this.bookAuthor = bookAuthor; }

    public Timestamp getBorrowDate() { return borrowDate; }
    public void setBorrowDate(Timestamp borrowDate) { this.borrowDate = borrowDate; }

    public Timestamp getReturnDate() { return returnDate; }
    public void setReturnDate(Timestamp returnDate) { this.returnDate = returnDate; }
}
