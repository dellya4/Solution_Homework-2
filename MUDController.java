import java.util.Objects;
import java.util.Scanner;

import iGameEntity.*;
import npc.NPC;
import player.Player;
import inventory.Inventory;
import item.Item;
import room.*;


/**
 * MUDController (Skeleton):
 * A simple controller that reads player input and orchestrates
 * basic commands like look around, move, pick up items,
 * check inventory, show help, etc.
 */
public class MUDController {

    private final Player player;
    private boolean running;
    private final Scanner scanner;
    private final Inventory inventory;

    public MUDController(Player player) {
        this.player = player;
        this.running = true;
        this.scanner = new Scanner(System.in);
        this.inventory = new Inventory();
    }

    /**
     * Main loop method that repeatedly reads input from the user
     * and dispatches commands until the game ends.
     */
    public void runGameLoop(Room currentRoom) {
        while (running) {
            System.out.print("> ");
            String command = scanner.nextLine();
            handleInput(command, currentRoom);
            Place location = player.getCurrentPlace();
            if (location.getNPC() != null) {
                speakWith();
            }
        }
    }

    /**
     * Handle a single command input (e.g. 'look', 'move forward', 'pick up sword').
     */
    public void handleInput(String input, Room currentRoom) {
        String[] parts = input.split(" ", 2);
        String command = parts[0];
        String args = (parts.length > 1) ? parts[1] : "";

        if (command.equals("pick") && args.startsWith("up")) {
            command = "pick up";
            args = args.length() > 3 ? args.substring(3).trim() : "";
        }
        switch (command) {
            case "look":
                lookAround(currentRoom);
                break;
            case "move":
                move(args, currentRoom);
                break;
            case "pick up":
                pickUp(args, currentRoom);
                break;
            case "inventory":
                checkInventory();
                break;
            case "help":
                showHelp();
                break;
            case "exit":
                System.out.println("Exiting game. Goodbye!");
                running = false;
                break;
            default:
                System.out.println("Unknown command. Type 'help' for available commands.");
        }
    }

    /**
     * Look around the current room: describe it and show items/NPCs.
     */
    private void lookAround(Room currentRoom) {
        Place location = player.getCurrentPlace();
        if (location != null) {
            location.describe();
            System.out.println("You are at position: (x: " + player.getX() +
                    ", y: " + player.getY() + ")");
            if (location.getItem() != null) {
                System.out.println("Item: ");
                System.out.println(location.getItem().getName());
            } else {
                System.out.println("No item.");
            }
            if (location.getNPC() != null) {
                System.out.println("NPC: ");
                System.out.println(currentRoom.getRoomPlace(player.getX(),
                        player.getY()).getNPC().getName());
            } else {
                System.out.println("No NPC.");
            }
        } else {
            System.out.println("There is nothing here.");
        }
    }

    /**
     * Move the player in a given direction (forward, back, left, right).
     */
    private void move(String direction, Room currentRoom) {
        int x = player.getX();
        int y = player.getY();

        switch (direction) {
            case "up":
                x--;
                break;
            case "down":
                x++;
                break;
            case "left":
                y--;
                break;
            case "right":
                y++;
                break;
            default:
                System.out.println("Invalid direction.");
                return;
        }

        if (player.isValidLocation(x, y)) {
            player.setLocation(x, y);
            System.out.println("You moved " + direction);
            lookAround(currentRoom);
        } else {
            System.out.println("You can't go this direction.");
        }

    }

    /**
     * Pick up an item (e.g. "pick up sword").
     */
    private void pickUp(String arg, Room currentRoom) {
        if (currentRoom != null) {
            Place location = player.getCurrentPlace();
            Item item = location.getItem();
            if (item != null && Objects.equals(item.getName(), arg)) {
                inventory.addItem(item);
                System.out.println("You picked up " + item.getName());
                location.setItem(null);
            } else {
                System.out.println("There is nothing here.");
            }
        }
    }

    /**
     * Check the player's inventory.
     */
    private void checkInventory() {
        if (inventory.isEmpty()) {
            System.out.println("You don't have any items.");
        } else {
            System.out.println("You are carrying: ");
            inventory.printInventory();
        }
    }

    /**
     * Communicating with NPC.
     */
    private void speakWith() {
        String[] gogzDialog = {
                "Stop right there, criminal scum!",
                "What are you trying to sniff out here? " +
                        "There's definitely nothing here, especially a large chest. " +
                        "You haven't seen it?",
                "I could keep an eye on you, so that you don't do " +
                        "anything and accidentally find the che... nothing. Go away!"
        };
        String[] lupusDialog = {
                "Good day, young man",
                "*You look into the old man's thin face and notice 5 eyes on his face*",
                "or lady?",
                "Even if I have 5 eyes, it doesn't mean that I have good eyesight.",
                "As I got older, I discovered my gift. I can see the future with my own eyes.",
                "*The old man frowned*",
                "The end of our world is coming soon. I see him in 4 months.",
                "We will end up in some kind of trash bin in some system.",
                "I have not been able to decrypt this vision. " +
                        "And I hope that it can be more understandable for you.",
                "Remember! Everything you see around you is an illusion."
        };

        Place location = player.getCurrentPlace();
        if (location.getNPC() == null) {
            System.out.println("There's no one to talk to here.");
            return;
        }

        String npcName = location.getNPC().getName();
        String[] dialog = null;

        if (npcName.equals("Gogz Many-eyed")) {
            dialog = gogzDialog;
        } else if (npcName.equals("Lupus Whisperer")) {
            dialog = lupusDialog;
        }

        System.out.println("\u001B[32m" + npcName +
                " starts talking..." + "\u001B[0m"); //32 - green

        for (int index = 0; index < dialog.length; index++) {
            System.out.println("\u001B[36m" +
                    dialog[index] + "\u001B[0m"); //36 - blue
            if (index < dialog.length - 1) {
                System.out.println("\u001B[31mType 'next' " +
                        "to continue or 'stop' to end the conversation.\u001B[0m"); //31 -red
                String command = scanner.nextLine().trim().toLowerCase();

                if (command.equals("stop")) {
                    System.out.println("\u001B[31mYou stopped " +
                            "talking to " + npcName + ".\u001B[0m"); //31 - red
                    return;
                } else if (!command.equals("next")) {
                    System.out.println("\u001B[33mInvalid command. " +
                            "Type 'next' or 'stop'.\u001B[0m"); //33 - yellow
                    index--;
                }
            }
        }

        System.out.println("\u001B[32m" + npcName +
                " has finished talking." + "\u001B[0m"); //32 - green
    }

    /**
     * Show help commands
     */
    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("  look - Describe the current room");
        System.out.println("  move <up|down|left|right> - Move in a direction");
        System.out.println("  pick up <item> - Pick up an item");
        System.out.println("  inventory - Show your inventory");
        System.out.println("  help - Show this help menu");
        System.out.println("  exit - Quit the game");
    }

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
        Scanner scanner = new Scanner(System.in);
        Room room = new Room("Old Living Room with Relics of the Past");
        addElementsOnPlace(room);
        System.out.println("Hello! Please enter your name: ");
        String name = scanner.nextLine();
        Player player = beginGame(room, name);
        getInfo();
        MUDController mudController = new MUDController(player);
        mudController.runGameLoop(room);
    }

    static void addElementsOnPlace(Room room) {
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                room.setRoomPlace(i, j, (Place) places[index]);
                index++;
            }
        }
        room.getRoomPlace(0, 2).setNpc((NPC) npc1);
        room.getRoomPlace(1, 2).setNpc((NPC) npc2);
        room.getRoomPlace(1, 0).setItem((Item) item2);
        room.getRoomPlace(1, 2).setItem((Item) item4);
        room.getRoomPlace(2, 0).setItem((Item) item3);
        room.getRoomPlace(2, 2).setItem((Item) item1);
    }

    static Player beginGame(Room room, String name) {
        System.out.println("""
                    ===============================================
                                    
                    WELCOME TO THE DUNGEON QUEST
                    A text-based multiplayer adventure awaits!
                                    
                    ===============================================
                    Type 'help' to see available commands.
                    """);

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