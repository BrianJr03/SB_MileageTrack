package controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoadingController implements Initializable {

    public AnchorPane rootPane;

    @Override
    public void initialize( URL location , ResourceBundle resources ) {
        new SplashScreen().start();
    }

    class SplashScreen extends Thread {
        public void run() {
            try {
                Thread.sleep( 100 );
                Platform.runLater( () -> {
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource("/ui/results.fxml"));
                    } catch ( IOException exception ) {
                        exception.printStackTrace();
                    }
                    assert root != null;
                    Scene scene = new Scene( root );
                    Stage stage = new Stage();
                    stage.setScene( scene );
                    stage.show();
                    rootPane.getScene().getWindow().hide();
                } );

            }
            catch ( InterruptedException e )
            { e.printStackTrace(); }
        }
    }

}