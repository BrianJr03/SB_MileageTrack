package controllers;

import googleSheet.SBMT_Sheet;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ResourceBundle;
import static controllerLogic.ControllerLogic.isValid_MileInput;
import static main.Main.displayPromptFor3secs;
import static main.Main.launchUI;

public class MainController implements Initializable {

    @FXML
    private ImageView bkGrnd_ImageView;
    @FXML
    private Label loading_Label;
    @FXML
    private Label noHistory_Label;
    @FXML
    private TextField mileEntry_Field;
    @FXML
    private Label invalidMile_Label;
    @FXML
    private AnchorPane rootPane;

    SBMT_Sheet sbmtSheet = new SBMT_Sheet();

    public MainController() throws IOException, GeneralSecurityException {}

    @Override
    public void initialize( URL location , ResourceBundle resources ) {
        hideAlL_Labels();
        try { sbmtSheet.setBkGrnd( bkGrnd_ImageView ); }
        catch ( IOException | GeneralSecurityException exception )
        { exception.printStackTrace(); }
    }

    private void hideAlL_Labels() {
        loading_Label.setVisible( false );
        noHistory_Label.setVisible( false );
        invalidMile_Label.setVisible( false );
    }

    @FXML
    private void launchLoadingUI() throws IOException, GeneralSecurityException {
        if (isValid_MileInput( mileEntry_Field.getText() )) {
            sbmtSheet.addEntryToSheet( mileEntry_Field.getText() );
            launchUI( "/ui/loadingResults.fxml", rootPane ); }
        else displayPromptFor3secs( invalidMile_Label );
    }

    private void byPassToLoadingUI() throws IOException, GeneralSecurityException {
        if ( !sbmtSheet.sheetAllowsForReset() ) {
          displayPromptFor3secs( noHistory_Label ); }
        else { launchUI( "/ui/loadingResults.fxml", rootPane ); }
    }

    @FXML
    private void launchSettings() throws IOException
    { launchUI( "/ui/loadingSettings.fxml", rootPane ); }

    @FXML
    private void loadUserHistory()
    { new LoadingHistory_Label().start(); }

    class LoadingHistory_Label extends Thread {
        public void run() {
            loading_Label.setVisible(true);
            try { Thread.sleep( 100 );
                Platform.runLater( () -> {
                    try { byPassToLoadingUI(); }
                    catch ( IOException | GeneralSecurityException exception )
                    { exception.printStackTrace(); }
                    loading_Label.setVisible(false); } ); }
            catch ( InterruptedException e )
            { e.printStackTrace(); }
        }
    }
}
