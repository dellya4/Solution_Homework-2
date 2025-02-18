package room;

import iGameEntity.IGameEntity;
import item.Item;
import npc.NPC;

public class Place implements IGameEntity  {
    private String description;
    private Item item;
    private NPC npc;


    public Place(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public NPC getNPC() {
        return npc;
    }

    public void setNpc(NPC npc) {
        this.npc = npc;
    }

    public void describe() {
        System.out.println(description);
    }

    public static class PlaceFactory {
        public static Place createPlace(String description) {
            return new Place(description);
        }
    }
}
