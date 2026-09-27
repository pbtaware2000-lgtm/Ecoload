package com.super_x.controller.admincontroller;

import com.super_x.dao.admindao.DriverDAO;
import com.super_x.dao.admindao.SupportTicketDAO;
import com.super_x.dao.admindao.UserDAO;
import com.super_x.model.adminmodel.Driver;
import com.super_x.model.adminmodel.SupportTicket;
import com.super_x.model.adminmodel.User;

import java.util.List;

public class DashboardController {

    private final DriverDAO driverDAO;
    private final UserDAO userDAO;
    private final SupportTicketDAO supportTicketDAO;

    public DashboardController() {

        driverDAO = new DriverDAO();
        userDAO = new UserDAO();
        supportTicketDAO = new SupportTicketDAO();
    }

    // ============================================================
    // GET ALL USERS
    // ============================================================

    public List<User> getAllUsers() {

        return userDAO.getAllUsers();
    }

    // ============================================================
    // GET ALL DRIVERS
    // ============================================================

    public List<Driver> getAllDrivers() {

        return driverDAO.getAllDrivers();
    }

    // ============================================================
    // GET ACTIVE USERS COUNT
    // ============================================================

    public int getActiveUsersCount() {

        int userCount = userDAO.getAllUsers().size();
        int driverCount = driverDAO.getAllDrivers().size();

        return userCount + driverCount;
    }

    // ============================================================
    // GET ALL SUPPORT TICKETS
    // ============================================================

    public List<SupportTicket> getAllSupportTickets() {

        return supportTicketDAO.getAllTickets();
    }

    // ============================================================
    // GET HIGH PRIORITY / SOS ALERTS
    // ============================================================

    public List<SupportTicket> getHighPriorityTickets() {

        return supportTicketDAO.getActiveHighPriorityTickets();
    }

    // ============================================================
    // GET SOS ALERT COUNT
    // ============================================================

    public int getSOSAlertCount() {

        return supportTicketDAO
                .getActiveHighPriorityTickets()
                .size();
    }

    // ============================================================
    // GET DRIVER VERIFICATIONS
    // ============================================================

    public List<Driver> getDriverVerifications() {

        return driverDAO.getAllDrivers();
    }

    // ============================================================
    // GET PENDING DRIVERS
    // ============================================================

    public List<Driver> getPendingDrivers() {

        return driverDAO.getPendingDrivers();
    }
}