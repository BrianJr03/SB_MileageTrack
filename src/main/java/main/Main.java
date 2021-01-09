package main;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Main extends Application implements EventHandler <ActionEvent> {

    public static Stage stage;
    private final String icon
            = String.valueOf(getClass().getResource("/png/sb_Logo.png" ));

    public static void main( String[] args )
    { launch( args );  }

    @Override
    public void start( Stage primaryStage ) throws Exception {
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource( "/ui/welcome.fxml" ));
        stage.setResizable( false );
        stage.getIcons().add( new Image( icon ));
        stage.setTitle( "SB Mileage Track");
        stage.setScene(new Scene(root,615,315));
        stage.show();
    }

    @Override
    public void handle( ActionEvent event ) {}

    public static void displayPromptFor3secs( Label prompt ) {
        prompt.setVisible( true );
        PauseTransition visiblePause = new PauseTransition( Duration.seconds( 3 ) );
        visiblePause.setOnFinished( event -> prompt.setVisible( false ) );
        visiblePause.play();
    }

    public static void launchUI( String uiPath,  AnchorPane rootPane ) throws IOException {
        FXMLLoader loader = new FXMLLoader( Main.class.getResource( uiPath ) );
        Parent root = loader.load();
        rootPane.getChildren().setAll( root );
    }
}