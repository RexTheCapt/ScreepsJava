package def.screeps;
/**
 * A structure that can store huge amount of resource units. Only one structure
 * per room is allowed that can be addressed by `Room.storage` property.
 */
public class StructureFactory extends OwnedStructure {

    public StoreDefinition store;

    public double storeCapacity;

    native public double transfer(Creep target, String resourceType);

    public double cooldown;

    public double level;

    native public double produce(String resource);

}
