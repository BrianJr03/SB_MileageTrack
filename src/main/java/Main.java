import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application implements EventHandler <ActionEvent> {

    public static void main( String[] args )
    { launch( args );  }

    @Override
    public void start( Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource( "/ui/main.fxml" ));
        primaryStage.setTitle( "SB Mileage Track" );
        primaryStage.setScene(new Scene(root,615,315));
        primaryStage.setResizable( false );
        primaryStage.show();
    }

    @Override
    public void handle(ActionEvent event) {}
}
