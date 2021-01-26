package controllers;

import googleSheet.SBMT_Sheet;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ResourceBundle;

public class LoadingResultsController implements Initializable {

    @FXML
    private ImageView bkGrnd_ImageView;
    @FXML
    private AnchorPane rootPane;

    SBMT_Sheet sbmt_sheet = new SBMT_Sheet();

    public LoadingResultsController() throws IOException, GeneralSecurityException {}

    @Override
    public void initialize( URL location , ResourceBundle resources ) {
        try { sbmt_sheet.setBkGrnd( bkGrnd_ImageView ); }
        catch ( IOException | GeneralSecurityException exception )
        { exception.printStackTrace(); }
        new LoadingResults().start();
    }

     class LoadingResults extends Thread {
        public void run() {
            try { Thread.sleep( 100 );
                Platform.runLater( () -> {
                    try { Parent root = FXMLLoader.load(getClass().getResource("/ui/results.fxml"));
                          rootPane.getChildren().setAll( root );
                    } catch ( IOException ignored ) {}
                } );
            }
            catch ( InterruptedException e )
            { e.printStackTrace(); }
        }
    }
}
