package def.screeps;

import com.frump.screeps.memoryDef.StructureMemory;

/**
 * Contains energy which can be spent on spawning bigger creeps. Extensions can
 * be placed anywhere in the room, any spawns will be able to use them regardless
 * of distance.
 */
public class StructureExtension extends OwnedStructure {
    /**
     * The amount of energy containing in the extension.
     */
    @Deprecated
    public double energy;
    /**
     * The total amount of energy the extension can contain.
     */
    @Deprecated
    public double energyCapacity;

    /**
     * A shorthand to Memory.structures[room.name]. You can use it for quick access the Structures specific memory data object.
     */
    public StructureMemory memory;
    /**
     * An object with the storage contents.
     */
    public StoreDefinition store;
}

