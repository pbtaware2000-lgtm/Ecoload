package com.super_x.controller.admincontroller;

import com.super_x.dao.admindao.UserDAO;
import com.super_x.model.adminmodel.User;

import java.util.List;

public class UserController {

    private final UserDAO userDAO;

    public UserController() {
        userDAO = new UserDAO();
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
}