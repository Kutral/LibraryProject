package com.eswar.library.dao;

import com.eswar.library.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookDAO {
    List<Book> findAll();
    List<Book> searchBooks(String query);
    Optional<Book> findById(int id);
    boolean createBook(Book book);
    boolean updateBook(Book book);
    boolean deleteBook(int id);
}
