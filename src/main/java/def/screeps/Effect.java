package def.screeps;

import jsweet.lang.Interface;
import jsweet.lang.Optional;

@Interface
public class Effect extends jsweet.lang.Object{
    public double effect;
    public double ticksRemaining;

    @Optional
    public double level;

}