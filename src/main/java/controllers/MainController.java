package controllers;

import googleSheet.SBMT_Sheet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    public void initialize( URL location , ResourceBundle resources )
    { hideAlL_Labels(); }

    private void hideAlL_Labels() {
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

    @FXML
    private void byPassToLoadingUI() throws IOException, GeneralSecurityException {
        if ( !sbmtSheet.canSheetBeReset() ) {
          displayPromptFor3secs( noHistory_Label ); }
        else {  launchUI( "/ui/loadingResults.fxml", rootPane ); }
    }

    @FXML
    private void launchSettings() throws IOException
    { launchUI( "/ui/loadingSettings.fxml", rootPane ); }
}
