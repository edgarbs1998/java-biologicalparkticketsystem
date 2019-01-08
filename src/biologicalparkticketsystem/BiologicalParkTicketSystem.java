package biologicalparkticketsystem;

import biologicalparkticketsystem.controller.ConfigManager;
import biologicalparkticketsystem.controller.CourseManager;
import biologicalparkticketsystem.controller.DaoManager;
import biologicalparkticketsystem.controller.DocumentManager;
import biologicalparkticketsystem.controller.MapManager;
import biologicalparkticketsystem.model.Client;
import biologicalparkticketsystem.model.PointOfInterest;
import biologicalparkticketsystem.view.BiologicalParkTicketSystemUI;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 *
 * @author maasbs
 */
public class BiologicalParkTicketSystem extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        BiologicalParkTicketSystemUI view = new BiologicalParkTicketSystemUI();
        
        //root
        BorderPane root = new BorderPane();
        root.setCenter(view.getGraphView());
        root.setBottom(view.getbottomMenu());
        root.setRight(view.getRightMenu());
        root.setPadding(new Insets(20));
        
        //scene
        Scene scene = new Scene(root, 800, 600);
        view.getGraphView().plotGraph();

        //stage build
        primaryStage.setTitle("Biological Park");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        
//        ConfigManager config = ConfigManager.getInstance();
//        
////        LoggerManager logger = LoggerManager.getInstance();
////        logger.getLogger().warning("Teste");
////        logger.getLogger().fine("Teste3");
////        logger.getLoggerCourseCalculations().info("Teste2");
////        logger.getLoggerCourseCalculations().fine("Teste4");
//        
//        MapManager mapManager = new MapManager(config.getProperties().getProperty("map.file"));
//        System.out.println(mapManager .toString());
//        
//        CourseManager courseManager = new CourseManager(mapManager);
//        System.out.println(courseManager.toString());
//        
//        List<PointOfInterest> mustVisitPois = new ArrayList<>();
//        PointOfInterest poi5 = mapManager.getPointOfInterestById(5);
//        mustVisitPois.add(poi5);
//        PointOfInterest poi8 = mapManager.getPointOfInterestById(8);
//        mustVisitPois.add(poi8);
//        PointOfInterest poi4 = mapManager.getPointOfInterestById(4);
//        mustVisitPois.add(poi4);
////        PointOfInterest poi6 = mapManager.getPointOfInterestById(6);
////        mustVisitPois.add(poi6);
//        PointOfInterest poi7 = mapManager.getPointOfInterestById(7);
//        mustVisitPois.add(poi7);
//        
////        courseManager.minimumCriteriaPath(CourseManager.Criteria.DISTANCE, false, mustVisitPois);
////        System.out.println(courseManager.toString());
////        courseManager.minimumCriteriaPath(CourseManager.Criteria.COST, false, mustVisitPois);
////        System.out.println(courseManager.toString());
////        courseManager.minimumCriteriaPath(CourseManager.Criteria.DISTANCE, true, mustVisitPois);
////        System.out.println(courseManager.toString());
//        courseManager.minimumCriteriaPath(CourseManager.Criteria.COST, true, mustVisitPois);
//        System.out.println(courseManager.toString());
//        
//        Client companyData = new Client(
//                config.getProperties().get("company.name").toString(),
//                config.getProperties().get("company.nif").toString()
//        );
//        Client.Address companyAddress = companyData.new Address(
//                config.getProperties().get("company.address.adress").toString(),
//                config.getProperties().get("company.address.postal_code").toString(),
//                config.getProperties().get("company.address.location").toString(),
//                config.getProperties().get("company.address.country").toString()
//        );
//        companyData.setAddress(companyAddress);
//        
//        Client client = new Client("Edgar Santos", "267400292");
//        Client.Address clientAddress = client.new Address("Est. da Charneca", "2665-506", "Venda do Pinheiro", "Portugal");
//        client.setAddress(clientAddress);
//        
//        DaoManager daoManager = DaoManager.getInstance();
//        daoManager.init(config);
//
//        DocumentManager documentManager = new DocumentManager(config.getProperties().getProperty("documents.folder"), companyData, Double.parseDouble(config.getProperties().get("vat").toString()));
//        documentManager.generateDocuments(courseManager.getCalculatedPath(), client);
//        //documentManager.generateDocuments(courseManager.getCalculatedPath(), null);
//        
////        for (Ticket ticket : daoManager.getTicketDao().selectTickets()) {
////            System.out.println(ticket);
////        }
//        
////        for (Invoice invoice : daoManager.getInvoiceDao().selectInvoices()) {
////            System.out.println(invoice);
////        }
//
//        System.out.println("Sold Bike Tickets: " + daoManager.getStatisticsDao().getSoldBikeTickets());
//        System.out.println("Sold Foot Tickets: " + daoManager.getStatisticsDao().getSoldFootTickets());
//        System.out.println("Sold Tickets Price Average: " + daoManager.getStatisticsDao().getSoldTicketsPriceAverage());
//        System.out.println("Top 10 Visited Pois:");
//        int count = 0;
//        for (int poiId : daoManager.getStatisticsDao().getTop10VisitedPois().keySet()) {
//            PointOfInterest poi = mapManager.getPointOfInterestById(poiId);
//            System.out.println("\t" + ++count + " - Name: " + poi.getPoiName() + "; Visits: " + daoManager.getStatisticsDao().getTop10VisitedPois().get(poiId));
//        }
    }
    
}
