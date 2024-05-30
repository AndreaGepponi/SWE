package ORM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;


import DomainModel.Booking;
import DomainModel.Manager;
import DomainModel.Structure;
import DomainModel.Room;
import org.checkerframework.checker.units.qual.C;

public class StructureDAO {

    private static Connection connection;
    public StructureDAO(){
        try {
            connection = ConnectionManager.getInstance().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void addStructure(String name, String place, String Manager, String type, int[]r, int []s, int c) throws SQLException,ClassNotFoundException{
        String sql = String.format("INSERT INTO \"Structure\" (name, place, manager, type, rooms)" +
                "VALUES ('%s', '%s', '%s', '%s', '%d')", name, place, Manager, type, Arrays.stream(r).sum());
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            System.out.println("Structure added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

        RoomDAO roomDAO = new RoomDAO();

        for(int i=0;i<r.length;i++){
            for(int j=0;j<s[i];j++){
                roomDAO.addRoom(name, c*s[i], s[i]);
            }
        }

    }

    public void removeStructure(String name) throws SQLException, ClassNotFoundException{
        String sql = String.format("DELETE FROM \"Structure\" WHERE name = '%s'", name);

        PreparedStatement preparedStatement = null;

        // remove Booking (CASCADE DELETE)
        // remove Room (CASCADE DELETE)
        // remove Review (CASCADE DELETE)
        removeBooking(name);
        removeRoom(name);
        removeReview(name);

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            System.out.println("Structure removed successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }
    }

    public void removeBooking(String name) throws SQLException, ClassNotFoundException{
        BookingDAO bookingDAO = new BookingDAO();
        bookingDAO.removeHotelBooking(name);
    }

    public void removeRoom(String name) throws SQLException, ClassNotFoundException{
        RoomDAO roomDAO = new RoomDAO();
        roomDAO.removeHotelRooms(name);
    }

    public void removeReview(String name) throws SQLException, ClassNotFoundException{
        ReviewDAO reviewDAO = new ReviewDAO();
        reviewDAO.removeHotelReview(name);
    }

    public ArrayList<Integer> findVacant(String City, int space, Calendar S, Calendar E, String Type) throws SQLException, ParseException, ClassNotFoundException {
        ArrayList<String> hotels = new ArrayList<>();

        String sql = String.format("SELECT * FROM \"Structure\" WHERE place = '%s' AND type = '%s'", City, Type);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                hotels.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        ArrayList<Integer> rooms = new ArrayList<>();

        for (String hotel : hotels) {
            String r_sql = String.format("SELECT * FROM \"Room\" WHERE hotel = '%s' AND bed >= '%d'", hotel, space);
            PreparedStatement room_preparedStatement = null;
            ResultSet roomSet = null;

            try {
                room_preparedStatement = connection.prepareStatement(r_sql);
                roomSet = room_preparedStatement.executeQuery();
                if (roomSet.next()) {
                    rooms.add(roomSet.getInt("id"));
                }
            } catch (SQLException e) {
                System.err.println("Error: " + e.getMessage());
            } finally {
                if (room_preparedStatement != null) {
                    room_preparedStatement.close();
                }
                if (roomSet != null) {
                    roomSet.close();
                }
            }
        }


        BookingDAO bookingDAO = new BookingDAO();
        ArrayList<Booking> bks = new ArrayList<>();

        ArrayList<Integer> available = new ArrayList<>();

        for (Integer room : rooms) {
            bks.addAll(bookingDAO.getAllRoomBookings(room));

            boolean accept = true;

            for(Booking B : bks){
                if(S.before(B.getP().getEnd()) || E.after(B.getP().getStart()))
                    accept = false;
            }

            if(accept){
                available.add(room);
            }

            bks.clear();
        }

        return available;
    }

    public ArrayList<String> getAllStructure(String username) throws SQLException {
        ArrayList<String> hotels = new ArrayList<>();

        String sql = String.format("SELECT name FROM \"Structure\" WHERE manager = '%s'", username);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                hotels.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return hotels;
    }

    public Structure getStructure(String Mn, String Hn) throws SQLException, ClassNotFoundException {
        String sql = String.format("SELECT * FROM \"Structure\" WHERE manager = '%s' AND name = '%s'", Mn, Hn);

        ManagerDAO managerDAO = new ManagerDAO();
        Manager manager = managerDAO.getUser(Mn);

        RoomDAO roomDAO = new RoomDAO();
        ArrayList<Room> rooms = roomDAO.getAllHotelRooms(Hn);

        rooms.sort(Comparator.comparingInt(Room::getPerson));

        ArrayList<Integer> R = new ArrayList<>();
        ArrayList<Integer> S = new ArrayList<>();
        int index = 0;

        for(Room r : rooms){
            int space = r.getId();
            if(!S.contains(space)){
                S.add(space);
                R.add(index, 1);
                index++;
            }
            else {
                int value = R.get(index) +1;
                R.add(index, value);
            }
        }

        int []  r1 = new int[R.size()];

        for(int i=0;i<R.size();i++){
            r1[i]  = R.get(i);
        }

        int [] s1 = new int[S.size()];

        for(int i=0;i<S.size();i++){
            s1[i] = S.get(i);
        }

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;


        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Structure(resultSet.getString("name"), manager.getUsername(), resultSet.getString("place"),
                        r1, s1, resultSet.getString("type"));
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }
        Structure hotel = null;
        return hotel;
    }

    public ArrayList<Booking> getAllBooking(String hotel) throws SQLException, ParseException, ClassNotFoundException {
        BookingDAO bookingDAO = new BookingDAO();
        return bookingDAO.getAllHotelBookings(hotel);
    }
}
