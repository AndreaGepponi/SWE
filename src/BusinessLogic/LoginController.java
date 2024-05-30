package BusinessLogic;

import java.sql.SQLException;
import DomainModel.User;
import ORM.CustomerDAO;
import ORM.ManagerDAO;

public class LoginController {
    public LoginController() {}

    public User login(String username, String password) throws SQLException, ClassNotFoundException {

        CustomerDAO customerDAO = new CustomerDAO();

        return customerDAO.checkPassword(username, password);

    }

    public User registerCustomer(String name, String surname, int age, String username, String password, String cardNumber,
                                 String cardExpirationDate, String cardSecurityCode) throws SQLException, ClassNotFoundException {

        CustomerDAO customerDAO = new CustomerDAO();

        customerDAO.addUser(name, surname, age, username, password, cardNumber, cardExpirationDate, cardSecurityCode);

        return customerDAO.checkPassword(username, password);

    }

    public User registerManager(String name, String surname, int age, String username, String password) throws SQLException, ClassNotFoundException {

        ManagerDAO managerDAO = new ManagerDAO();

        managerDAO.addUser(name, surname, age, username, password);

        return managerDAO.checkPassword(username, password);

    }
}
