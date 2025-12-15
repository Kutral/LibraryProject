package com.eswar.library.dao;

import com.eswar.library.model.User;
import java.util.Optional;

public interface UserDAO {
    boolean createUser(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    java.util.List<User> findAll();
}
