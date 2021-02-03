package controllers;

import googleSheet.SBMT_Sheet;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {

    @FXML
    private ImageView bkGrnd_ImageView;
    @FXML
    private AnchorPane rootPane;

    SBMT_Sheet sbmt_sheet = new SBMT_Sheet();

    public WelcomeController() throws IOException, GeneralSecurityException {}

    @Override
    public void initialize( URL location , ResourceBundle resources ) {
        try { sbmt_sheet.setBkGrnd( bkGrnd_ImageView ); }
        catch ( IOException | GeneralSecurityException exception )
        { exception.printStackTrace(); }
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
