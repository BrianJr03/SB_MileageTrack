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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static main.Main.displayPromptFor3secs;
import static main.Main.launchUI;

public class MainController implements Initializable {

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
        invalidMile_Label.setVisible( false );
    }

    @FXML
    private void launchLoadingUI() throws IOException, GeneralSecurityException {
        if (isValid_MileInput( mileEntry_Field.getText() )) {
            sbmtSheet.addEntryToSheet( mileEntry_Field.getText() );
            launchUI( "/ui/loadingResults.fxml", rootPane );
        }
        else displayPromptFor3secs( invalidMile_Label );
    }

    private static boolean isValid_MileInput(String mile) {
        Pattern pattern = Pattern.compile("^[0-9]*(\\.)?[0-9]+$");
        Matcher matcher = pattern.matcher(mile);
        if (mile.equals( "0" )) return false;
        return (matcher.find() && matcher.group().equals(mile));
    }

    @FXML
    private void launchSettings() throws IOException
    { launchUI( "/ui/loadingSettings.fxml", rootPane ); }
}
