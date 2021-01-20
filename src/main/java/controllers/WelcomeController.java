package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @Override
    public void initialize( URL location , ResourceBundle resources ) {
        new WelcomeScreen().start();
    }

    class WelcomeScreen extends Thread {
        @Override
        public void run() { try {
                Thread.sleep( 900 );
                Platform.runLater( () -> {
                    Parent root = null;
                    try { root = FXMLLoader.load(getClass().getResource("/ui/main.fxml"));
                    } catch ( IOException exception )
                    { exception.printStackTrace(); }
                    rootPane.getChildren().setAll( root ); } );
            } catch ( InterruptedException e )
            { e.printStackTrace(); }
        }
    }
}
