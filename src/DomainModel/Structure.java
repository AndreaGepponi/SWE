package DomainModel;

import java.util.ArrayList;

public class Structure {
    private String Name, Place, Type, M;
    private ArrayList<Room> Rooms;
    private ArrayList<Review> Reviews;
    
    public Structure(String N, String m, String P, int[]r, int []s, String type){
        int c = 45;
        M = m;
        Name = N;
        Place = P;
        Reviews = new ArrayList<>();
        Rooms = new ArrayList<>();
        Type = type;
        int id = 0;
        for(int i=0;i<r.length;i++){
            for(int j=0;j<s[i];j++){
                id += 1;
                Room R = new Room(s[i],id, c*s[i], this.Name);
                Rooms.add(R);
            }
        }
    }

    public String getName() {
        return Name;
    }
    public String getPlace() {
        return Place;
    }
    public ArrayList<Room> getRooms() {
        return Rooms;
    }
    public ArrayList<Review> getReviews() {
        return Reviews;
    }
    public String getM() {
        return M;
    }
    public void getRoom(int id){
        for (Room room : Rooms) {
            if (id == room.getId()) {
                Rooms.get(id).getDate().remove(0);
                break;
            }
        }
    }
    public String getType(){
        return Type;
    }
}
