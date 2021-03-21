package def.screeps;
import jsweet.lang.Number;
import jsweet.lang.Object;
@jsweet.lang.Interface
public abstract class StoreDefinition extends jsweet.lang.Object {
    //native public Number $get(String resource);
    @jsweet.lang.Optional
    public double energy;
    @jsweet.lang.Optional
    public double power;

    native public double getCapacity(String resource);

    native public double getFreeCapacity(String resource);
    native public double getFreeCapacity();

    native public double getUsedCapacity(String resource);

    //public native double getCapacity();

    //public native double getFreeCapacity();

    //public native double getUsedCapacity();
}

