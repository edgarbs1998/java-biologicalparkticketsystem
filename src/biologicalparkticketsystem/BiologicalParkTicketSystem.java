package biologicalparkticketsystem;

import biologicalparkticketsystem.controller.MainController;
import biologicalparkticketsystem.model.MainModel;
import biologicalparkticketsystem.view.IMainView;
import biologicalparkticketsystem.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BiologicalParkTicketSystem extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        MainModel model = new MainModel();
        IMainView view = new MainView(model);
        MainController controller = new MainController(model, view);
        
        primaryStage.setTitle("Biological Park");
        Scene scene = view.getScene();
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
        
        view.plotGraph();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
