package DomainModel;

import java.util.ArrayList;

public class Room {
    private int Person;
    private int id;
    private int Cost;
    private String H;
    private ArrayList<Booking> Date;

    public Room(int P, int i, int C, String h){
        this.Person = P;
        this.id = i;
        this.Cost = C;
        this.H = h;
        this.Date = new ArrayList<>();
    }

    public int getPerson() {
        return Person;
    }

    public int getId() {
        return id;
    }

    public int getCost() {
        return Cost;
    }

    public String getH() {
        return H;
    }

    public ArrayList<Booking> getDate() {
        return Date;
    }

}
