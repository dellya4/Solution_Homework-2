package room;


public class Room {
    private String roomName;
    private Place [][] roomPlaces;

    public Room(String roomName) {
        this.roomName = roomName;
        this.roomPlaces = new Place[3][3];
    }

    public String getRoomName() {
        return roomName;
    }

    public Place getRoomPlace(int row, int col) {
        return roomPlaces[row][col];
    }

    public void setRoomPlace(int row, int col, Place place) {
        roomPlaces[row][col] = place;
    }

    public Place[][] getRoomPlaces() {
        return roomPlaces;
    }
}
