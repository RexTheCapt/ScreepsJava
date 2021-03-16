package def.screeps;
/**
 * A global object representing the in-game market. You can use this object to track resource transactions to/from your
 * terminals, and your buy/sell orders (in development). The object is accessible via the singleton Game.market property.
 */
public class Market extends jsweet.lang.Object {
    /**
     * An array of the last 100 incoming transactions to your terminals
     */
    public Transaction[] incomingTransactions;
    /**
     * An array of the last 100 outgoing transactions from your terminals
     */
    public Transaction[] outgoingTransactions;

    public double credits;

    public MarketOrders orders;

    native public double calcTransactionCost(double amount, String roomName1, String roomName2);

    native public double cancelOrder(String orderId);
    native public double changeOrderPrice(String orderId, double newPrice);
    native public double createOrder(MarketOrder order);
    native public double deal(String orderId, double amount, String roomName);
    native public double deal(String orderId, double amount);

    native public double extendOrder(String orderId, double addAmount);

    native public MarketOrder[] getAllOrders();

    native public MarketOrder[] getAllOrders(MarketOrderFilter filter);

    native public MarketHistory[] getHistory();
    native public MarketHistory[] getHistory(String resource);

    @jsweet.lang.ObjectType
    public static class MarketOrders extends jsweet.lang.Object {
        native public MarketOrder $get(String ordeRId);
    }
}

