package controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoadingSettingsController implements Initializable {

    public AnchorPane rootPane;

    @Override
    public void initialize( URL location , ResourceBundle resources )
    { new LoadingSettings().start(); }

    class LoadingSettings extends Thread {
        public void run() {
            try { Thread.sleep( 100 );
                Platform.runLater( () -> {
                    try { Parent root = FXMLLoader.load(getClass().getResource("/ui/settings.fxml"));
                        rootPane.getChildren().setAll( root );
                    } catch ( IOException ignored ) {}
                } );
            }
            catch ( InterruptedException e )
            { e.printStackTrace(); }
        }
    }
}
