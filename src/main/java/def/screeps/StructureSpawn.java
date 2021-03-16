package def.screeps;
import jsweet.lang.Ambient;
import jsweet.lang.Object;
import com.frump.screeps.memoryDef.SpawnMemory;

/**
 * Spawns are your colony centers. You can transfer energy into it and create new creeps using createCreep() method.
 */
public class StructureSpawn extends OwnedStructure {
    /**
     * The amount of energy containing in the spawn.
     */
    @Deprecated
    public double energy;
    /**
     * The total amount of energy the spawn can contain
     */
    @Deprecated
    public double energyCapacity;
    /**
     * The current amount of hit points of the spawn.
     */
    public double hits;
    /**
     * The maximum amount of hit points of the spawn.
     */
    public double hitsMax;
    /**
     * A unique object identificator. You can use Game.getObjectById method to retrieve an object instance by its id.
     */
    public String id;
    /**
     * A shorthand to Memory.spawns[spawn.name]. You can use it for quick access the spawn???s specific memory data object.
     */
    public com.frump.screeps.memoryDef.SpawnMemory memory;
    /**
     * Whether it is your spawn or foe.
     */
    public Boolean my;
    /**
     * Spawn???s name. You choose the name upon creating a new spawn, and it cannot be changed later. This name is a hash key to access the spawn via the Game.spawns object.
     */
    public String name;
    /**
     * An object with the spawn???s owner info containing the following properties: username
     */
    public Owner owner;
    /**
     * An object representing the position of this spawn in a room.
     */
    public RoomPosition pos;
    /**
     * The link to the Room object of this spawn.
     */
    public Room room;
    /**
     * Always equal to ???spawn???.
     */
    public String structureType;
    /**
     * If the spawn is in process of spawning a new creep, this object will contain the new creep???s information, or null otherwise.
     * @param name The name of a new creep.
     * @param needTime Time needed in total to complete the spawning.
     * @param remainingTime Remaining time to go.
     */
    public Spawning spawning;
    /**
     * An object with the storage contents.
     */
    public StoreDefinition store;

    /**
     * Check if a creep can be created.
     * @param body An array describing the new creep???s body. Should contain 1 to 50 elements with one of these constants: WORK, MOVE, CARRY, ATTACK, RANGED_ATTACK, HEAL, TOUGH, CLAIM
     * @param name The name of a new creep. It should be unique creep name, i.e. the Game.creeps object should not contain another creep with the same name (hash key). If not defined, a random name will be generated.
     */
    @Deprecated
    native public double canCreateCreep(String[] body, String name);
    /**
     * Start the creep spawning process.
     * The name of a new creep or one of these error codes
     * ERR_NOT_OWNER	-1	You are not the owner of this spawn.
     * ERR_NAME_EXISTS	-3	There is a creep with the same name already.
     * ERR_BUSY	-4	The spawn is already in process of spawning another creep.
     * ERR_NOT_ENOUGH_ENERGY	-6	The spawn and its extensions contain not enough energy to create a creep with the given body.
     * ERR_INVALID_ARGS	-10	Body is not properly described.
     * ERR_RCL_NOT_ENOUGH	-14	Your Room Controller level is not enough to use this spawn.
     * @param body An array describing the new creep???s body. Should contain 1 to 50 elements with one of these constants: WORK, MOVE, CARRY, ATTACK, RANGED_ATTACK, HEAL, TOUGH, CLAIM
     * @param name The name of a new creep. It should be unique creep name, i.e. the Game.creeps object should not contain another creep with the same name (hash key). If not defined, a random name will be generated.
     * @param memory The memory of a new creep. If provided, it will be immediately stored into Memory.creeps[name].
     */
    @Deprecated
    native public jsweet.util.union.Union<Double,String> createCreep(String[] body, String name, Object memory);

    /**
     * Start the creep spawning process.
     * OK	0	You are not the owner of this spawn.
     * ERR_NOT_OWNER	-1	You are not the owner of this spawn.
     * ERR_NAME_EXISTS	-3	There is a creep with the same name already.
     * ERR_BUSY	-4	The spawn is already in process of spawning another creep.
     * ERR_NOT_ENOUGH_ENERGY	-6	The spawn and its extensions contain not enough energy to create a creep with the given body.
     * ERR_INVALID_ARGS	-10	Body is not properly described.
     * ERR_RCL_NOT_ENOUGH	-14	Your Room Controller level is not enough to use this spawn.
     * @param body An array describing the new creep???s body. Should contain 1 to 50 elements with one of these constants: WORK, MOVE, CARRY, ATTACK, RANGED_ATTACK, HEAL, TOUGH, CLAIM
     * @param name The name of a new creep. It should be unique creep name, i.e. the Game.creeps object should not contain another creep with the same name (hash key). If not defined, a random name will be generated.
     * @param ops Object containing any of the following, memory,energyStructures,dryRun,directions
     */
    native public double spawnCreep(String[] body, String name, Object ops);
    @jsweet.lang.ObjectType
    public static class SpawnMemory extends Object{
        @jsweet.lang.Optional
        public Object memory;
        @jsweet.lang.Optional
        public Structure[] energyStructures;
        @jsweet.lang.Optional
        public boolean dryRun;
        @jsweet.lang.Optional
        public String[] directions;
    }
    /**
     * Destroy this spawn immediately.
     */
    native public double destroy();
    /**
     * Check whether this structure can be used. If the room controller level is not enough, then this method will return false, and the structure will be highlighted with red in the game.
     */
    native public Boolean isActive();
    /**
     * Toggle auto notification when the spawn is under attack. The notification will be sent to your account email. Turned on by default.
     * @param enabled Whether to enable notification or disable.
     */
    native public double notifyWhenAttacked(Boolean enabled);
    /**
     * Increase the remaining time to live of the target creep. The target should be at adjacent square. The spawn should not be busy with the spawning process. Each execution increases the creep's timer by amount of ticks according to this formula: floor(600/body_size). Energy required for each execution is determined using this formula: ceil(creep_cost/2.5/body_size).
     * @param target The target creep object.
     */
    native public double renewCreep(Creep target);
    /**
     * Kill the creep and drop up to 100% of resources spent on its spawning and boosting depending on remaining life time. The target should be at adjacent square. Energy return is limited to 125 units per body part
     * @param target The target creep object.
     */
    native public double recycleCreep(Creep target);

    /** This is an automatically generated object type (see the source definition). */
    @jsweet.lang.ObjectType
    public static class Spawning extends jsweet.lang.Object {
        public String name;
        public double needTime;
        public double remainingTime;
    }
    /**
     * Check if a creep can be created.
     * @param body An array describing the new creep???s body. Should contain 1 to 50 elements with one of these constants: WORK, MOVE, CARRY, ATTACK, RANGED_ATTACK, HEAL, TOUGH, CLAIM
     */
    @Deprecated
    native public double canCreateCreep(String[] body);
    /**
     * Start the creep spawning process.
     * The name of a new creep or one of these error codes
     * ERR_NOT_OWNER	-1	You are not the owner of this spawn.
     * ERR_NAME_EXISTS	-3	There is a creep with the same name already.
     * ERR_BUSY	-4	The spawn is already in process of spawning another creep.
     * ERR_NOT_ENOUGH_ENERGY	-6	The spawn and its extensions contain not enough energy to create a creep with the given body.
     * ERR_INVALID_ARGS	-10	Body is not properly described.
     * ERR_RCL_NOT_ENOUGH	-14	Your Room Controller level is not enough to use this spawn.
     * @param body An array describing the new creep???s body. Should contain 1 to 50 elements with one of these constants: WORK, MOVE, CARRY, ATTACK, RANGED_ATTACK, HEAL, TOUGH, CLAIM
     * @param name The name of a new creep. It should be unique creep name, i.e. the Game.creeps object should not contain another creep with the same name (hash key). If not defined, a random name will be generated.
     */
    @Deprecated
    native public jsweet.util.union.Union<Double,String> createCreep(String[] body, String name);
    /**
     * Start the creep spawning process.
     * The name of a new creep or one of these error codes
     * ERR_NOT_OWNER	-1	You are not the owner of this spawn.
     * ERR_NAME_EXISTS	-3	There is a creep with the same name already.
     * ERR_BUSY	-4	The spawn is already in process of spawning another creep.
     * ERR_NOT_ENOUGH_ENERGY	-6	The spawn and its extensions contain not enough energy to create a creep with the given body.
     * ERR_INVALID_ARGS	-10	Body is not properly described.
     * ERR_RCL_NOT_ENOUGH	-14	Your Room Controller level is not enough to use this spawn.
     * @param body An array describing the new creep???s body. Should contain 1 to 50 elements with one of these constants: WORK, MOVE, CARRY, ATTACK, RANGED_ATTACK, HEAL, TOUGH, CLAIM
     */
    @Deprecated
    native public jsweet.util.union.Union<Double,String> createCreep(String[] body);
}

