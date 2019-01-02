package biologicalparkticketsystem.model;

public class ConnectionPath extends Connection {
    
    /**
     * @param id connection ID from super classe "Connection"
     * @param connectionName connection name from super classe "Connection"
     * @param cost connection cost from super classe "Connection"
     * @param distance connection distance from super classe "Connection"
     * @param navigability connection navigability (on foot or on bike) from super classe "Connection"
     */
    public ConnectionPath(int id, String connectionName, int cost, int distance, boolean navigability) {
        super(id, connectionName, cost, distance, navigability);
    }
    
    @Override
    public String toString() {
        return String.format("%s {type path, %d meters, %d €}",
                getConnectionName(), getDistance(), getCostEuros());
    }
    
}
