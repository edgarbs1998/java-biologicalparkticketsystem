package biologicalparkticketsystem.controller;

import biologicalparkticketsystem.LoggerManager;
import biologicalparkticketsystem.model.MainModel;
import biologicalparkticketsystem.model.StatisticsModel;
import biologicalparkticketsystem.model.course.CourseManagerException;
import biologicalparkticketsystem.model.course.ICriteriaStrategy;
import biologicalparkticketsystem.model.course.PointOfInterest;
import biologicalparkticketsystem.model.document.Client;
import biologicalparkticketsystem.view.ClientDialog;
import biologicalparkticketsystem.view.IMainView;
import biologicalparkticketsystem.view.IStatisticsView;
import biologicalparkticketsystem.view.StatisticsView;
import digraph.IVertex;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * Class responsable to control the main view and model
 */
public class MainController {
    
    private MainModel model;
    private IMainView view;
    
    /**
     * Constructor of the class MainController, where the view and model are initialized
     * @param model main model
     * @param view main view
     */
    public MainController(MainModel model, IMainView view) {
        this.model = model;
        this.view = view;
        this.view.setTriggers(this);
        this.model.addObserver(view);
    }
    /**
     * Method to open the statistics window
     */
    public void openStatistics() {
        LoggerManager.getInstance().log(LoggerManager.Component.STATISTICS_CHECKS);
        
        StatisticsModel statisticsModel = new StatisticsModel();
        IStatisticsView statisticsView = new StatisticsView(statisticsModel);
        
        Stage statisticsStage = new Stage();
        statisticsStage.setTitle("Statistics");
        Scene scene = statisticsView.getScene();
        statisticsStage.setScene(scene);
        statisticsStage.show();
    }
    
    /**
     * Handler to change the selected points of interest
     * @param poi selected point of interest
     * @param oldValue old field value
     * @param newValue new field value
     */
    public void changePointOfInterest(IVertex<PointOfInterest> poi, boolean oldValue, boolean newValue) {
        if (newValue) {
            this.model.addVisitPointOfInterest(poi.element());
            this.view.markPoiToVisit(poi);
        } else {
            this.model.removeVisitPointOfInterest(poi.element());
            this.view.unmarkPoiToVisit(poi);
        }
    }
    
    /**
     * method to open the issue ticket window
     */
    public void issueTicket() {
        this.view.showNifQuestionDialog(this);
    }
    
    /**
     * Method to open the nif form
     * @param type button type
     */
    public void nifDialogResponse(ButtonType type) {
        if (type == ButtonType.YES) {
            new ClientDialog().showAndWait().ifPresent(client -> {
                this.generateDocuments(client);
            });
        } else {
            this.generateDocuments(null);
        }
    }
    
    /**
     * Method to generate the documents of the ticket and the invoice
     * @param client
     */
    private void generateDocuments(Client client) {
        this.model.generateDocuments(client);
        this.view.showSuccess("Your ticket has been issued!");
        
        this.model.clearCalculatedCourses();
        this.view.resetInput();
    }
    
    /**
     * Handler to calculate the selected path
     */
    public void calculatePath() {
        try {
            ICriteriaStrategy criteria = this.view.getCriteriaComboBox();
            boolean navigability = this.view.getNavigability();
            this.model.calculatePath(criteria, navigability);
        } catch (CourseManagerException ex) {
            this.view.showError(ex.getMessage());
        }
    }
    
    /**
     * Handler to undo the selected path
     */
    public void undoCalculate() {
        this.model.undoCalculatedCourse();
    }
    
}
