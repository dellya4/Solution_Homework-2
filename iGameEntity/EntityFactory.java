package iGameEntity;

import item.Item;
import room.Place;
import npc.NPC;

public class EntityFactory {
    public static IGameEntity createEntity(String type, String name, String description){
        switch (type.toLowerCase()){
            case "item":
                return Item.ItemFactory.createItem(name, description);
            case "place":
                return Place.PlaceFactory.createPlace(description);
            case "npc":
                return NPC.NPCFactory.createNPC(name, description);
            default:
                System.out.println("Invalid type");
                return null;
        }
    }
}
