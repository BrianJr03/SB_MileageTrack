package controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import googleSheet.SBMT_Sheet;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController {

    @FXML
    private TextField mileEntry_Field;
    @FXML
    private Label invalidMile_Label;
    @FXML
    private AnchorPane rootPane;

    SBMT_Sheet sbmtSheet = new SBMT_Sheet();

    public MainController() throws IOException, GeneralSecurityException {}

    public void initialize()
    { invalidMile_Label.setVisible( false ); }

    public void launchUI(String uiPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(uiPath));
        Parent root = loader.load();
        rootPane.getChildren().setAll( root );
    }

    public void launchResultsUI() throws IOException, GeneralSecurityException {
        if (isValid_MileInput( mileEntry_Field.getText() )) {
            sbmtSheet.addEntryToSheet( mileEntry_Field.getText() );
            launchUI( "/ui/results.fxml" );
        }
        else displayPromptFor3secs( invalidMile_Label );
    }

    public static boolean isValid_MileInput(String mile) {
        Pattern pattern = Pattern.compile("^[0-9]*(\\.)?[0-9]+$");
        Matcher matcher = pattern.matcher(mile);
        if (mile.equals( "0" )) return false;
        return (matcher.find() && matcher.group().equals(mile));
    }

    public static void displayPromptFor3secs(Label prompt) {
        prompt.setVisible(true);
        PauseTransition visiblePause = new PauseTransition( Duration.seconds(3));
        visiblePause.setOnFinished( event -> prompt.setVisible(false) );
        visiblePause.play();
    }
}
