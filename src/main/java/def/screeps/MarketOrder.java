package def.screeps;

import jsweet.lang.Ambient;
import jsweet.lang.Optional;

@Ambient
public class MarketOrder extends jsweet.lang.Object{
    public MarketOrder(){}
    @Optional
    public String id;
    @Optional
    public double created;
    @Optional
    public String createdTimestamp;
    @Optional
    public String type;
    @Optional
    public String resourceType;
    @Optional
    public String roomName;
    @Optional
    public double amount;
    @Optional
    public double remainingAmount;
    @Optional
    public double price;
    @Optional
    public double totalAmount;
}
