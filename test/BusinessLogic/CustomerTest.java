package BusinessLogic;

import DomainModel.Booking;
import DomainModel.Customer;
import ORM.BookingDAO;
import ORM.CustomerDAO;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CustomerTest {

    @Test
    public void BookingOperations() throws SQLException, ClassNotFoundException , ParseException {
        Calendar Start = Calendar.getInstance();
        Calendar End = Calendar.getInstance();
        Start.set(2025,Calendar.JANUARY,1);
        End.set(2025,Calendar.JANUARY,10);

        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = customerDAO.getUser("mario.rossi");
        CustomerProfileController customerProfileController = new CustomerProfileController(customer);

        BookingDAO bookingDAO = new BookingDAO();

        String s = bookingDAO.calendarToString(Start);
        String e = bookingDAO.calendarToString(End);
        String tot = s + "-" + e;


        try {
            customerProfileController.bookRoom(1, Start, End, "mario.rossi");
            Booking b = bookingDAO.getBooking("mario.rossi", "Hotel_Roma", tot);
            assertNotNull(b);
            bookingDAO.removeSingleBooking("mario.rossi", "Hotel_Roma", tot);
            Booking c = bookingDAO.getBooking("mario.rossi", "Hotel_Roma", tot);
            assertNull(c);
        } catch (SQLException | ClassNotFoundException E) {
            System.err.println(E.getMessage());
        }
    }

    @Test
    public void findRoom() throws SQLException, ClassNotFoundException, ParseException{
        Calendar Start = Calendar.getInstance();
        Calendar End = Calendar.getInstance();
        Start.set(2025,Calendar.JANUARY,1);
        End.set(2025,Calendar.JANUARY,10);

        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = customerDAO.getUser("mario.rossi");
        CustomerProfileController customerProfileController = new CustomerProfileController(customer);

        try {
            ArrayList<Integer> rooms = customerProfileController.findRoom("Roma", 4, Start, End, "Hotel");
            assertNotNull(rooms);
            rooms.forEach(System.out::println);
        } catch (SQLException | ClassNotFoundException E) {
            System.err.println(E.getMessage());
        }
    }

    @Test
    public void getReviews() throws SQLException, ClassNotFoundException, ParseException{
        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = customerDAO.getUser("mario.rossi");
        CustomerProfileController customerProfileController = new CustomerProfileController(customer);

        try {
            customerProfileController.addReview("Hotel_Roma", "mario.rossi", "text", 3);
            float avgH = customerProfileController.avgHotel("Hotel_Roma");
            float avgU = customerProfileController.avgUser("mario.rossi");
            assertEquals(avgU, 3, 0);
            assertEquals(avgH, 3, 0);
            customerProfileController.removeReview("mario.rossi", "Hotel_Roma");
        } catch (SQLException | ClassNotFoundException E) {
            System.err.println(E.getMessage());
        }
    }
}
