package main;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application implements EventHandler <ActionEvent> {

    public static Stage stage;
    private final String icon
            = String.valueOf(getClass().getResource("/png/sb_Logo.png" ));

    public static void main( String[] args )
    { launch( args );  }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource( "/ui/welcome.fxml" ));
        stage.setResizable( false );
        stage.getIcons().add( new Image(icon));
        stage.setTitle( "SB Mileage Track");
        stage.setScene(new Scene(root,615,315));
        stage.show();
    }

    @Override
    public void handle(ActionEvent event) {}
}
