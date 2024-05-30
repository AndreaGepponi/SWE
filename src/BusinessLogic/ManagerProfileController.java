package BusinessLogic;

import DomainModel.Booking;
import DomainModel.Review;
import ORM.ManagerDAO;
import ORM.ReviewDAO;
import ORM.StructureDAO;
import DomainModel.Manager;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class ManagerProfileController {
    Manager manager;

    public ManagerProfileController(Manager manager) { this.manager = manager; }

    public void updateName(String newName) throws SQLException, ClassNotFoundException {

        ManagerDAO managerDAO = new ManagerDAO();

        managerDAO.updateName(manager.getUsername(), newName);
        manager.setName(newName);
        System.out.println("Name updated successfully!");

    }

    public void updateSurname(String newSurname) throws SQLException, ClassNotFoundException {

        ManagerDAO managerDAO = new ManagerDAO();

        managerDAO.updateSurname(manager.getUsername(), newSurname);
        manager.setSurname(newSurname);
        System.out.println("Surname updated successfully!");

    }

    public void updateAge(int newAge) throws SQLException, ClassNotFoundException {

        ManagerDAO managerDAO = new ManagerDAO();

        managerDAO.updateAge(manager.getUsername(), newAge);
        manager.setAge(newAge);
        System.out.println("Age updated successfully!");

    }

    public ArrayList<String> getAllUsernames() throws SQLException, ClassNotFoundException {

        ManagerDAO managerDAO = new ManagerDAO();

        return managerDAO.getAllUsernames();

    }

    public void updateUsername(String newUsername) throws SQLException, ClassNotFoundException {

        ManagerDAO managerDAO = new ManagerDAO();

        managerDAO.updateUsername(manager.getUsername(), newUsername);
        manager.updateUsername(newUsername);
        System.out.println("Username updated successfully!");

    }

    public void updatePassword(String newPassword) throws SQLException, ClassNotFoundException {

        ManagerDAO managerDAO = new ManagerDAO();

        managerDAO.updatePassword(manager.getUsername(), newPassword);
        manager.updatePassword(newPassword);//completa metodo
        System.out.println("Password updated successfully!");

    }

    public void addStructure(String name, String place, String Manager, String type, int[]r, int []s, int c)
            throws SQLException, ClassNotFoundException {
        StructureDAO structureDAO = new StructureDAO();
        structureDAO.addStructure(name, place, Manager, type, r, s, c);
    }

    public ArrayList<String> getStructures(String username) throws SQLException {
        ManagerDAO managerDAO = new ManagerDAO();
        return managerDAO.getStructure(username);
    }

    public ArrayList<Booking> getAllBookings(String username) throws SQLException, ParseException, ClassNotFoundException {
        ManagerDAO managerDAO = new ManagerDAO();
        return managerDAO.getAllBookings(username);
    }

    public float avgHotel(String Hn) throws SQLException, ClassNotFoundException {
        ReviewDAO reviewDAO = new ReviewDAO();
        ArrayList<Review> reviews = reviewDAO.getAllHotelReview(Hn);
        float tot = 0;
        for (Review review : reviews) {
            tot += review.getScore();
        }
        return tot /= reviews.size();
    }

    public float avgManager(String Mn) throws SQLException, ClassNotFoundException{
        ArrayList<String> hotels = getStructures(Mn);

        float tot = 0;

        for(String name : hotels){
            tot += avgHotel(name);
        }

        return tot / hotels.size();
    }
}
