package inventory;
import item.Item;

public class Inventory {
    Item[] items;
    int index;

    public Inventory() {
        this.items = new Item[3];
        this.index = 0;
    }

    public void addItem(Item item) {
        if (index < items.length) {
            items[index] = item;
            index++;
        }
        else {
            System.out.println("You can't add more than 3 items");
        }
    }

    public boolean isEmpty() {
        return index == 0;
    }

    public void printInventory() {
        if (isEmpty()) {
            System.out.println("You have no items");
        } else {
            for (Item item : items) {
                if (item != null) {
                    item.describe();
                }
            }
        }
    }

}
