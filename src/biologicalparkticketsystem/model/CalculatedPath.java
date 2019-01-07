/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biologicalparkticketsystem.model;

import biologicalparkticketsystem.controller.CourseManager.Criteria;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author golds
 */
public class CalculatedPath {
    
    private Criteria criteria;
    private boolean navigability;
    private int cost;
    private final List<PointOfInterest> pointsOfInterest;
    private final List<Connection> connections;
    private List<PointOfInterest> mustVisit;
    
    public CalculatedPath() {
        this.cost = Integer.MAX_VALUE;
        this.pointsOfInterest = new ArrayList<>();
        this.connections = new ArrayList<>();
        this.mustVisit = new ArrayList<>();
    }
    
    public Criteria getCriteria() {
        return this.criteria;
    }
    
    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }
    
    public boolean getNavigability() {
        return this.navigability;
    }
    
    public void setNavigability(boolean navigability) {
        this.navigability = navigability;
    }
    
    public int getCost() {
        return cost;
    }
    
    public void setCost(int cost) {
        this.cost = cost;
    }
    
    public List<PointOfInterest> getPointsOfInterest() {
        return this.pointsOfInterest;
    }
    
    public List<Connection> getConnections() {
        return this.connections;
    }
    
    public List<PointOfInterest> getMustVisit() {
        return this.mustVisit;
    }
    
    public void setMustVisit(List<PointOfInterest> mustVisit) {
        this.mustVisit = mustVisit;
    }
    
}