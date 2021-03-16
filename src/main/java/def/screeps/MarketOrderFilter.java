package def.screeps;


public class MarketOrderFilter extends jsweet.lang.Object{
    public MarketOrderFilter(){}

    public String type;
    public String resourceType;

    public MarketOrderFilter(String type, String resourceType) {
        this.type = type;
        this.resourceType = resourceType;
    }
}
