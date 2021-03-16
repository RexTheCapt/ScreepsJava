package def.screeps;

import jsweet.lang.Optional;

@jsweet.lang.Interface
public class MarketHistory extends jsweet.lang.Object{
    @Optional
    public String resourceType;
    @Optional
    public String date;
    @Optional
    public double transactions;
    @Optional
    public double volume;
    @Optional
    public double avgPrice;
    @Optional
    public double stddevPrice;
}
