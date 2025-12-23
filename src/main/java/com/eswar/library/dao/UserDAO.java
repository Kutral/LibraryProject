package com.eswar.library.dao;

import com.eswar.library.model.User;
import java.util.Optional;

public interface UserDAO {
    boolean createUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(int id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findById(int id);
    java.util.List<User> findAll();
    int countUsers();
}
