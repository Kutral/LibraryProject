package com.eswar.library.dao;

import com.eswar.library.model.Borrow;
import java.util.List;

public interface BorrowDAO {
    boolean borrowBook(int userId, int bookId);
    boolean returnBook(int borrowId);
    List<Borrow> findByUserId(int userId);
    int countActiveBorrows();
}
