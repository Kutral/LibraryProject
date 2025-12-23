package com.eswar.library.dao;

import com.eswar.library.model.BookRequest;
import java.util.List;
import java.util.Optional;

public interface BookRequestDAO {
    boolean createRequest(BookRequest request);
    List<BookRequest> findByUserId(int userId);
    List<BookRequest> findAll();
    boolean updateStatus(int id, String status);
    Optional<BookRequest> findById(int id);
}
