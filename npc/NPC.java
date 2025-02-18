package npc;
import iGameEntity.IGameEntity;

public class NPC implements IGameEntity {
    private String name;
    private String description;

    NPC(String n, String d) {
        name = n;
        description = d;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void describe() {
        System.out.println(name + " " + description);
    }

    public static class NPCFactory {
        public static NPC createNPC(String n, String d) {
            return new NPC(n, d);
        }
    }
}
