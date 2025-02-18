package item;

import iGameEntity.IGameEntity;

public class Item implements IGameEntity{
    private String name;
    private String description;

    Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }

    public void describe() {
        System.out.println(name+". "+description);
    }

    public static class ItemFactory {
        public static Item createItem(String name, String description) {
            return new Item(name, description);
        }
    }
}
