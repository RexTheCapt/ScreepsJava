package def.screeps;

@jsweet.lang.Interface
public class TravelToOpts extends jsweet.lang.Object {
    /**
     * This option enables reusing the path found along multiple game ticks. It allows to save CPU time, but can result in a slightly
     * slower creep reaction behavior. The path is stored into the creep's memory to the _move property. The reusePath value defines
     * the amount of ticks which the path should be reused for. The default value is 5. Increase the amount to save more CPU, decrease
     * to make the movement more consistent. Set to 0 if you want to disable path reusing.
     */
    @jsweet.lang.Optional
    public double reusePath;
    /**
     * If reusePath is enabled and this option is set to true, the path will be stored in memory in the short serialized form using
     * Room.serializePath. The default value is true.
     */
    @jsweet.lang.Optional
    public Boolean serializeMemory;
    /**
     * If this option is set to true, moveTo method will return ERR_NOT_FOUND if there is no memorized path to reuse. This can
     * significantly save CPU time in some cases. The default value is false.
     */
    @jsweet.lang.Optional
    public Boolean noPathFinding;

    @jsweet.lang.Optional
    public Boolean ignoreCreeps;

    @jsweet.lang.Optional
    public Boolean offRoad;

    @jsweet.lang.Optional
    public Boolean allowHostile;

    @jsweet.lang.Optional
    public Boolean allowSK;

    @jsweet.lang.Optional
    public Boolean ignoreStructures;

    @jsweet.lang.Optional
    public Boolean ignoreRoads;

    @jsweet.lang.Optional
    public double maxOps;

    @jsweet.lang.Optional
    public double heuristicWeight;

    @jsweet.lang.Optional
    public double maxRooms;

    @jsweet.lang.Optional
    public double swampCost;

    @jsweet.lang.Optional
    public double plainCost;

    @jsweet.lang.Optional
    public double restrictDistance;

    @jsweet.lang.Optional
    public Boolean useFindRoute;

    @jsweet.lang.Optional
    public double range;

    @jsweet.lang.Optional
    public Boolean movingTarget;

    @jsweet.lang.Optional
    public double repath;

    @jsweet.lang.Optional
    public Boolean ensurePath;

    @jsweet.lang.Optional
    public Boolean freshMatrix;




}

