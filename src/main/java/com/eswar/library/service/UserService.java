package com.eswar.library.service;

import com.eswar.library.dao.UserDAO;
import com.eswar.library.dao.UserDAOImpl;
import com.eswar.library.model.User;

public interface UserService {
    boolean registerUser(User user);
    User login(String username, String password);
    boolean isUsernameTaken(String username);
    boolean isEmailTaken(String email);
}
