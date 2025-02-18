package controllers;

import java.util.Objects;
import java.util.Scanner;
import player.Player;
import inventory.Inventory;
import item.Item;
import room.Place;
import room.Room;


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
        }
        else {
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
            System.out.println("You moved "+direction);
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
                System.out.println("You picked up "+item.getName());
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
}
