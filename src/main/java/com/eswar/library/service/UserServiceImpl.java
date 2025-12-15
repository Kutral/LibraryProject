package com.eswar.library.service;

import com.eswar.library.dao.UserDAO;
import com.eswar.library.dao.UserDAOImpl;
import com.eswar.library.model.User;

public class UserServiceImpl implements UserService {

    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public boolean registerUser(User user) {

        return userDAO.createUser(user);
    }

    @Override
    public User login(String username, String password) {
        return userDAO.findByUsername(username)
                .filter(u -> u.getPassword().equals(password))
                .orElse(null);
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return userDAO.findByUsername(username).isPresent();
    }

    @Override
    public boolean isEmailTaken(String email) {
        return userDAO.findByEmail(email).isPresent();
    }
}
