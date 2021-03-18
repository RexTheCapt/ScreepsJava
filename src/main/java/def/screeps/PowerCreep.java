package def.screeps;

import com.frump.screeps.memoryDef.PowerCreepMemory;
import jsweet.lang.Array;
import jsweet.lang.Interface;
import jsweet.lang.Object;

@Interface
public class PowerCreep extends RoomObject{
    public Array<Effect> effects;
    public String className;
    public double deleteTime;
    public double hits;
    public double hitsMax;
    public String id;
    public PowerCreepMemory memory;
    public boolean my;
    public Owner owner;
    public String name;
    public StoreDefinition store;
    public Powers powers;
    public String saying;
    public String shard;
    public double spawnCooldownTime;
    public double ticksToLive;

    native public double cancelOrder(String methodName);
    native public double drop(String resourceType, double amount);
    native public double move(double direction);
    native public double move(Creep creep);
    native public double moveByPath(PathStep[] path);
    native public double moveTo(double x, double y, MoveToOpts opts);
    native public double moveTo(RoomPosition target, MoveToOpts opts);
    native public double moveTo(Creep target);
    native public double moveTo(Creep target, MoveToOpts opts);
    native public double notifyWhenAttacked(Boolean enabled);
    native public double pickup(Resource target);
    native public double say(String message, Boolean toPublic);
    native public double suicide();
    native public double transfer(Creep target, String resourceType, double amount);
    native public double withdraw(Structure target, String resourceType, double amount);
    native public double drop(String resourceType);
    native public double moveTo(double x, double y);
    native public double moveTo(RoomPosition target);
    native public double say(String message);
    native public double transfer(Creep target, String resourceType);
    native public double withdraw(Structure target, String resourceType);
    native public double moveByPath(RoomPosition[] path);
    native public double moveByPath(String path);
    native public double moveTo(double x, double y, FindPathOpts opts);
    native public double moveTo(RoomPosition target, FindPathOpts opts);
    native public double moveTo(Object target, FindPathOpts opts);
    native public double moveTo(Object target, MoveToOpts opts);
    native public double transfer(Structure target, String resourceType, double amount);
    native public double transfer(StructureSpawn target, String resourceType, double amount);
    native public double moveTo(Object target);
    native public double transfer(Structure target, String resourceType);
    native public double transfer(StructureSpawn target, String resourceType);

    native public double rename(String name);
    native public double enableRoom(StructureController controller);
    native public double usePower(double power, Object target);
    native public double usePower(double power);
    native public double spawn(StructurePowerSpawn sps);

    native public double travelTo(double x, double y, TravelToOpts opts);
    native public double travelTo(RoomPosition target, TravelToOpts opts);
    native public double travelTo(RoomPosition target);
    native public double travelTo(Creep target);
    native public double travelTo(Creep target, TravelToOpts opts);
    native public double travelTo(Creep.Target target, TravelToOpts opts);

    public static class Power {
        public double level;
        public double cooldown;
    }

    @jsweet.lang.ObjectType
    public static class Powers extends jsweet.lang.Object {
        native public Power $get(double pwrConstant);
    }
}

