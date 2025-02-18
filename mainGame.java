import controllers.MUDController;
import iGameEntity.*;
import item.Item;
import player.Player;
import npc.NPC;
import room.*;
import java.util.Scanner;

public class mainGame {
    static Scanner scanner = new Scanner(System.in);
    static IGameEntity item1 = EntityFactory.createEntity(
            "item",
            "Pearl Necklace",
            "This is an exquisite jewel made of shining pearls.");
    static IGameEntity item2 = EntityFactory.createEntity(
            "item",
            "Healing Potion",
            "This is a small bottle of red health-restoring liquid.");
    static IGameEntity item3 = EntityFactory.createEntity(
            "item",
            "Moldy bread",
            "It's an old piece of bread that you can eat, " +
                    "but it's not particularly tasty.");
    static IGameEntity item4 = EntityFactory.createEntity(
            "item",
            "The ancient Compass",
            "It is a mysterious artifact that " +
                    "points the way to hidden treasures.");

    static IGameEntity npc1 = EntityFactory.createEntity(
            "npc",
            "Gogz Many-eyed",
            "He is a mysterious old man with five eyes who " +
                    "claims to see the future, but confuses the past.");
    static IGameEntity npc2 = EntityFactory.createEntity(
            "npc",
            "Lupus Whisperer",
            "This is a man who never speaks loudly, " +
                    "but his whisper can be heard even in a noisy crowd.");

    static IGameEntity place1 = EntityFactory.createEntity(
            "place",
            null,
            "Next to the door there is a hanger on which an old raincoat is hanging.");
    static IGameEntity place2 = EntityFactory.createEntity(
            "place",
            null,
            "A tattered carpet with a faded pattern is spread out on the floor.");
    static IGameEntity place3 = EntityFactory.createEntity(
            "place",
            null,
            "There's a painting on the wall, but its colors have almost completely worn off.");
    static IGameEntity place4 = EntityFactory.createEntity(
            "place",
            null,
            "A dim oil lamp flickers under the ceiling.");
    static IGameEntity place5 = EntityFactory.createEntity(
            "place",
            null,
            "In the center of the room is a round wooden table with three chairs.");
    static IGameEntity place6 = EntityFactory.createEntity(
            "place",
            null,
            "There is a large window with a view of the backyard. ");
    static IGameEntity place7 = EntityFactory.createEntity(
            "place",
            null,
            "A gold-framed mirror, darkened by time, hangs by the fireplace.");
    static IGameEntity place8 = EntityFactory.createEntity(
            "place",
            null,
            "There's an old bookcase against the wall, full of dusty volumes.");
    static IGameEntity place9 = EntityFactory.createEntity(
            "place",
            null,
            "There is a wrought-iron chest with a heavy lock in the corner of the room.");
    static IGameEntity[] places = {
            place1, place2, place3, place4,
            place5, place6, place7, place8, place9
    };


    public static void main(String[] args) {
        Room room = new Room("Old Living Room with Relics of the Past");
        addElementsOnPlace(room);
        Player player = beginGame(room);
        getInfo();
        MUDController mudController = new MUDController(player);
        mudController.runGameLoop(room);
    }

    static void addElementsOnPlace(Room room) {
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                room.setRoomPlace(i, j, (Place) places[index] );
                index++;
            }
        }
        room.getRoomPlace(0,2).setNpc((NPC) npc1);
        room.getRoomPlace(1,2).setNpc((NPC) npc2);
        room.getRoomPlace(1,0).setItem((Item) item2);
        room.getRoomPlace(1,2).setItem((Item) item4);
        room.getRoomPlace(2,0).setItem((Item) item3);
        room.getRoomPlace(2,2).setItem((Item) item1);
    }

    static Player beginGame(Room room) {
        System.out.println("""
                ===============================================
                
                WELCOME TO THE DUNGEON QUEST
                A text-based multiplayer adventure awaits!
                
                ===============================================
                Type 'help' to see available commands.
                """);

        System.out.println("Please enter your name: ");
        String name = scanner.nextLine();
        return new Player(name, room.getRoomPlaces());
    }

    static void getInfo() {
        System.out.println("""
                Look Around,
                Move Between Points,
                Check Inventory,
                Interact with NPC,
                Help Command,""");
    }
}
