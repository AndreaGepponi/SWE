package BusinessLogic;

import DomainModel.Booking;
import DomainModel.Structure;
import DomainModel.Manager;
import ORM.StructureDAO;
import ORM.ManagerDAO;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class ManagerTest {

    @Test
    public void StructureOperations() throws SQLException, ClassNotFoundException, ParseException{
        ManagerDAO managerDAO = new ManagerDAO();
        Manager manager = managerDAO.getUser("alegialli");
        ManagerProfileController managerProfileController = new ManagerProfileController(manager);
        StructureDAO structureDAO = new StructureDAO();

        int [] s = {2, 2, 2};
        int [] r = {2, 3, 4};

        try {
           managerProfileController.addStructure("Hotel_Name", "Firenze", "alegialli", "Hotel", r, s, 25);
           Structure hotel = structureDAO.getStructure("alegialli","Hotel_Name");
           assertNotNull(hotel);
           structureDAO.removeStructure("Hotel_Name");
           Structure h = structureDAO.getStructure("alegialli","Hotel_Name");
           assertNull(h);

        } catch (SQLException | ClassNotFoundException E) {
            System.err.println(E.getMessage());
        }
    }
}
