package def.screeps;

public class Deposit extends jsweet.lang.Object{
    public String id;
    public RoomPosition pos;
    public Room room;
    public double cooldown;
    public String depositType;
    public double lastCooldown;
    public double ticksToDecay;
}
