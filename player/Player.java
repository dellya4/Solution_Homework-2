package player;

import room.Place;
import room.Room;

public class Player {
    private String name;
    private int x, y;
    private Place [][] map;

    public Player (String name, Place [][] map) {
        this.name = name;
        this.x = 0;
        this.y = 0;
        this.map = map;
    }


    public String getName() {
        return name;
    }

    public Place getCurrentPlace() {
        return map[x][y];
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isValidLocation(int x, int y) {
        return x >= 0 && y >= 0 && x < 3 && y < 3;
    }

}
