package BusinessLogic;

import DomainModel.Booking;
import DomainModel.Room;
import ORM.*;

import DomainModel.Customer;
import DomainModel.Review;
import org.checkerframework.checker.units.qual.C;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class CustomerProfileController {
    Customer customer;

    public CustomerProfileController(Customer customer) { this.customer = customer; }

    public void updateName(String newName) throws SQLException, ClassNotFoundException {

        CustomerDAO customerDAO = new CustomerDAO();

        CustomerDAO.updateName(customer.getUsername(), newName);//non static?
        customer.setName(newName);
        System.out.println("Name updated successfully!");

    }

    public void updateSurname(String newSurname) throws SQLException, ClassNotFoundException {

        CustomerDAO userDAO = new CustomerDAO();

        userDAO.updateSurname(customer.getUsername(), newSurname);
        customer.setSurname(newSurname);
        System.out.println("Surname updated successfully!");

    }

    public void updateAge(int newAge) throws SQLException, ClassNotFoundException {

        CustomerDAO userDAO = new CustomerDAO();

        userDAO.updateAge(customer.getUsername(), newAge);
        customer.setAge(newAge);
        System.out.println("Age updated successfully!");

    }

    public ArrayList<String> getAllUsernames() throws SQLException, ClassNotFoundException {

        CustomerDAO userDAO = new CustomerDAO();

        return userDAO.getAllUsernames();

    }

    public void updateUsername(String newUsername) throws SQLException, ClassNotFoundException {

        CustomerDAO userDAO = new CustomerDAO();

        userDAO.updateUsername(customer.getUsername(), newUsername);
        customer.updateUsername(newUsername);
        System.out.println("Username updated successfully!");

    }


    public void updatePassword(String newPassword) throws SQLException, ClassNotFoundException {

        CustomerDAO userDAO = new CustomerDAO();

        userDAO.updatePassword(customer.getUsername(), newPassword);
        customer.updatePassword(newPassword);//completa
        System.out.println("Password updated successfully!");

    }

    public void updateCreditCard(String cardNumber, String cardExpirationDate, String cardSecurityCode) throws SQLException, ClassNotFoundException {

        CustomerDAO userDAO = new CustomerDAO();

        userDAO.updateCreditCard(customer.getUsername(), cardNumber, cardExpirationDate, cardSecurityCode);
        //customer.setCC(cardNumber, null, cardExpirationDate, cardSecurityCode); cambia setCC

        System.out.println("Credit Card updated successfully!");

    }

    public ArrayList<Integer> findRoom(String City, int space, Calendar S, Calendar E, String T) throws SQLException, ParseException, ClassNotFoundException {
        StructureDAO structDAO = new StructureDAO();
        return structDAO.findVacant(City, space, S, E, T);
    }

    public Room Associate(int id)throws SQLException, ClassNotFoundException{
        RoomDAO roomDAO = new RoomDAO();
        String H_name = roomDAO.getHotel(id);
        int price = roomDAO.getPrice(id);
        int beds = roomDAO.getBeds(id);
        return new Room(beds, id, price, H_name);
    }

    public void bookRoom(int Id, Calendar S, Calendar E, String user) throws SQLException {
        RoomDAO roomDAO = new RoomDAO();
        String h = roomDAO.getHotel(Id);

        BookingDAO bookingDAO = new BookingDAO();
        String s_calendar = bookingDAO.calendarToString(S);
        String e_calendar = bookingDAO.calendarToString(E);
        String tot = s_calendar + "-" + e_calendar;

        bookingDAO.addBooking(user, h, tot, Id);
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

    public float avgUser(String Un) throws SQLException, ClassNotFoundException {
        ReviewDAO reviewDAO = new ReviewDAO();
        ArrayList<Review> reviews = reviewDAO.getAllUserReview(Un);
        float tot = 0;
        for (Review review : reviews) {
            tot += review.getScore();
        }
        return tot /= reviews.size();
    }

    public void addReview(String Hn, String Un, String txt, int score) throws SQLException, ClassNotFoundException{
        ReviewDAO reviewDAO = new ReviewDAO();
        reviewDAO.addReview(Hn, Un, txt,score);
    }

    public void removeReview(String Un, String Hn)throws SQLException, ClassNotFoundException{
        ReviewDAO reviewDAO = new ReviewDAO();
        reviewDAO.removeReview(Un, Hn);
    }

    public void removeBooking(String Un, String Hn, String P) throws SQLException, ClassNotFoundException{
        BookingDAO bookingDAO = new BookingDAO();
        bookingDAO.removeSingleBooking(Un, Hn, P);
    }

    public Review getSingleReview(String Un, String Hn) throws SQLException, ClassNotFoundException{
        ReviewDAO reviewDAO = new ReviewDAO();
        return reviewDAO.getReview(Un, Hn);
    }

}
