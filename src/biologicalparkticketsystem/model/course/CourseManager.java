package biologicalparkticketsystem.model.course;

import biologicalparkticketsystem.LoggerManager;
import digraph.IEdge;
import digraph.IVertex;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class responsible to manage course calculations
 */
public class CourseManager {
    
    private final MapManager mapManager;
    private CalculatedPathCareTaker calculatedPathCareTaker;
    private CalculatedPath calculatedPath;
    
    public CourseManager(MapManager mapManager) {
        this.mapManager = mapManager;
        this.calculatedPathCareTaker = new CalculatedPathCareTaker();
        this.calculatedPath = null;
    }
    
    public CalculatedPath getCalculatedPath() {
        return this.calculatedPath;
    }
    
    /**
     * Method to undo last calculated course
     */
    public void undoCalculatedCourse() {
        this.calculatedPathCareTaker.restoreState(this.calculatedPath);
    }
    
    /**
     * Method to clear calculated courses historic
     */
    public void clearCalculatedCourses() {
        this.calculatedPathCareTaker.clearStates();
        this.calculatedPath = null;
    }
    
    /**
     * Method to return the amount of calculated courses in the historic
     * @return amount of calculated courses
     */
    public int countCalculatedCourses() {
        return this.calculatedPathCareTaker.countStates();
    }
    
    /**
     * Calculates the minimum cost path based on a criteria for the selected pois
     * @param criteria criteria strategy used for calculation
     * @param navigability true if user is on bike
     * @param mustVisitPois list of pois which musr be visited
     * @throws biologicalparkticketsystem.model.course.CourseManagerException
     */
    public void minimumCriteriaPath(ICriteriaStrategy criteria,
            boolean navigability,
            List<PointOfInterest> mustVisitPois) throws CourseManagerException {
        
        if (mustVisitPois.isEmpty()) {
            throw new CourseManagerException("To generate a path a minimum of one point of interest must be selected.");
        }
        
        CalculatedPath oldCalculatedPath = this.calculatedPath;
        
        try {
            this.calculatedPath = new CalculatedPath();

            IVertex<PointOfInterest> startPoi = this.mapManager.getStartVertex();

            Map<IVertex<PointOfInterest>, CalculatedDijkstra> calculatedDijkstras = new HashMap<>();

            dijkstraAlgorithm(criteria, navigability, startPoi, calculatedDijkstras);
            for (PointOfInterest poi : mustVisitPois) {
                IVertex<PointOfInterest> vertexPoi = this.mapManager.checkPointOfInterest(poi);
                dijkstraAlgorithm(criteria, navigability, vertexPoi, calculatedDijkstras);
            }

            heapsAlgorithm(mustVisitPois.size(), mustVisitPois, startPoi, calculatedDijkstras);

            this.calculatedPath.setCriteria(criteria);
            this.calculatedPath.setNavigability(navigability);
            this.calculatedPath.setMustVisit(mustVisitPois);
            
            if (oldCalculatedPath != null) {
                this.calculatedPathCareTaker.saveState(oldCalculatedPath);
            }
            
            LoggerManager.getInstance().log(LoggerManager.Component.COURSE_CALCULATIONS);
        } catch (MapManagerException | CourseManagerException ex) {
            LoggerManager.getInstance().log(ex);
            this.calculatedPath = oldCalculatedPath;
            throw new CourseManagerException(ex.getMessage());
        }
    }
    
    private void heapsAlgorithm(int n, List<PointOfInterest> mustVisitPois,
            IVertex<PointOfInterest> startPoi,
            Map<IVertex<PointOfInterest>, CalculatedDijkstra> calculatedDijkstras) throws MapManagerException, CourseManagerException {
        if (n == 1) {
            CalculatedPath tempCalculatedPath = calculateMustVisitPOIs(startPoi, mustVisitPois, calculatedDijkstras);
            if (tempCalculatedPath.getCost() <= this.calculatedPath.getCost()) {
                this.calculatedPath = tempCalculatedPath;
            }
        } else {
            for (int i = 0; i < n - 1; ++i) {
                heapsAlgorithm(n - 1, mustVisitPois, startPoi, calculatedDijkstras);
                if ((n & 1) == 0) { // Check if n is even or odd
                    Collections.swap(mustVisitPois, i, n-1);
                } else {
                    Collections.swap(mustVisitPois, 0, n-1);
                }
            }
            heapsAlgorithm(n - 1, mustVisitPois, startPoi, calculatedDijkstras);
        }
    }
    
    private CalculatedPath calculateMustVisitPOIs(IVertex<PointOfInterest> startPoi,
            List<PointOfInterest> mustVisitPois,
            Map<IVertex<PointOfInterest>, CalculatedDijkstra> calculatedDijkstras) throws MapManagerException, CourseManagerException {
        
        CalculatedPath tempCalculatedPath = new CalculatedPath();
        int cost = 0;
        
        IVertex<PointOfInterest> origin = startPoi;
        IVertex<PointOfInterest> destination;
        for (PointOfInterest poi : mustVisitPois) {
            destination = this.mapManager.checkPointOfInterest(poi);
            cost += getMinimumPathFromTwoPOIs(origin, destination, calculatedDijkstras, tempCalculatedPath.getPointsOfInterest(), tempCalculatedPath.getConnections());
            origin = destination;
        }
        destination = startPoi;
        cost += getMinimumPathFromTwoPOIs(origin, destination, calculatedDijkstras, tempCalculatedPath.getPointsOfInterest(), tempCalculatedPath.getConnections());
        
        tempCalculatedPath.getPointsOfInterest().add(0, startPoi.element());
        tempCalculatedPath.setCost(cost);
        
        return tempCalculatedPath;
        
    }
    
    private int getMinimumPathFromTwoPOIs(IVertex<PointOfInterest> origin,
            IVertex<PointOfInterest> destination,
            Map<IVertex<PointOfInterest>, CalculatedDijkstra> calculatedDijkstras,
            List<PointOfInterest> pois,
            List<Connection> connections) throws CourseManagerException {
        
        List<PointOfInterest> tempPois = new ArrayList<>();
        List<Connection> tempConnections = new ArrayList<>();
        
        CalculatedDijkstra calculatedDijkstra = calculatedDijkstras.get(origin);
        
        int cost = (int) Math.round(calculatedDijkstra.getCosts().get(destination));
        
        while (destination != origin) {
            tempPois.add(0, destination.element());
            if (!calculatedDijkstra.getEdges().containsKey(destination) || calculatedDijkstra.getEdges().get(destination) == null) {
                throw new CourseManagerException("It is not possible to calculate a path for the selected point(s) of interest.");
            }
            tempConnections.add(0, calculatedDijkstra.getEdges().get(destination).element());
            destination = calculatedDijkstra.getPredecessors().get(destination);
        }
        
        pois.addAll(tempPois);
        connections.addAll(tempConnections);
        
        return cost;
        
    }
    
    private void dijkstraAlgorithm(ICriteriaStrategy criteria,
            boolean navigability,
            IVertex<PointOfInterest> orig,
            Map<IVertex<PointOfInterest>, CalculatedDijkstra> calculatedDijkstras) {
        
        Map<IVertex<PointOfInterest>, Double> costs = new HashMap<>();
        Map<IVertex<PointOfInterest>, IVertex<PointOfInterest>> predecessors = new HashMap<>();
        Map<IVertex<PointOfInterest>, IEdge<Connection, PointOfInterest>> edges = new HashMap<>();
        
        Set<IVertex<PointOfInterest>> visited = new HashSet<>();
        Set<IVertex<PointOfInterest>> unvisited = new HashSet<>();
        
        for (IVertex<PointOfInterest> vertex : this.mapManager.getDiGraph().vertices()) {
            costs.put(vertex, Double.MAX_VALUE);
            predecessors.put(vertex, null);
            edges.put(vertex, null);
        }
        costs.put(orig, 0.0);
        unvisited.add(orig);
        
        while (!unvisited.isEmpty()) {
            IVertex<PointOfInterest> lowerCostVertex = findLowerCostVertex(unvisited, costs);
            unvisited.remove(lowerCostVertex);
            for (IEdge<Connection, PointOfInterest> edge : this.mapManager.getDiGraph().accedentEdges(lowerCostVertex)) {
                if (navigability == false || (navigability == true && edge.element().getNavigability() == navigability)) {
                    IVertex<PointOfInterest> opposite = this.mapManager.getDiGraph().opposite(lowerCostVertex, edge);
                    if (!visited.contains(opposite)) {
                        double edgeWeight = criteria.getEdgeWeight(edge.element());

                        double sourceCost = costs.get(lowerCostVertex);
                        if (sourceCost + edgeWeight < costs.get(opposite)) {
                            costs.put(opposite, sourceCost + edgeWeight);
                            predecessors.put(opposite, lowerCostVertex);
                            edges.put(opposite, edge);
                        }
                        unvisited.add(opposite);
                    }
                }
            }
            visited.add(lowerCostVertex);
        }
        
        CalculatedDijkstra calculatedDijkstra = new CalculatedDijkstra();
        calculatedDijkstra.setCosts(costs);
        calculatedDijkstra.setPredecessors(predecessors);
        calculatedDijkstra.setEdges(edges);
        
        calculatedDijkstras.put(orig, calculatedDijkstra);
    }

    private IVertex<PointOfInterest> findLowerCostVertex(Set<IVertex<PointOfInterest>> unvisited, 
            Map<IVertex<PointOfInterest>, Double> costs) {
        
        double min = Double.MAX_VALUE;
        IVertex<PointOfInterest> minCostVertex = null;
        for (IVertex<PointOfInterest> vertex : unvisited){
            if (costs.get(vertex) <= min){
                minCostVertex = vertex;
                min = costs.get(vertex);
            }
        }
        
        return minCostVertex;
    }
    
    @Override
    public String toString() {
        String returnString = "COURSE MANAGER\n";
        
        if (this.calculatedPath == null) {
            returnString += "\t(the path has not yet been calculated)\n";
        } else {
            returnString += "\tBest (" + this.calculatedPath.getCriteria() + ") path for the selected points of interest (onBike: " + this.calculatedPath.getNavigability() + ")\n";
            returnString += "\tTotal cost (" + this.calculatedPath.getCriteria().getUnit() + ") = " + this.calculatedPath.getCost() + "\n";
            
            returnString += "\tPoints of Interest:\n";
            for (PointOfInterest pointOfInterest : this.calculatedPath.getPointsOfInterest()) {
                returnString += "\t\t" + pointOfInterest + "\n";
            }
            
            returnString += "\tConnections:\n";
            for (Connection connection : this.calculatedPath.getConnections()) {
                returnString += "\t\t" + connection + "\n";
            }
        }
        
        returnString += "\n";
        
        return returnString;
    }
    
}
