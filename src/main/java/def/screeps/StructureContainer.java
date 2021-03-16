package def.screeps;
import com.frump.screeps.memoryDef.StructureMemory;
import jsweet.lang.Object;

/**
 * Contains up to 2,000 resource units. Can be constructed in neutral rooms. Decays for 5,000 hits per 100 ticks.
 */
public class StructureContainer extends Structure {
    /**
     * An object with the structure contents. Each object key is one of the RESOURCE_* constants, values are resources
     * amounts. Use _.sum(structure.store) to get the total amount of contents
     */
    public StoreDefinition store;
    /**
     * The total amount of resources the structure can contain.
     */
    public double storeCapacity;

    /**
     * A shorthand to Memory.structures[room.name]. You can use it for quick access the Structures specific memory data object.
     */
    public StructureMemory memory;
}

