package controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import util.SheetsAndJava;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController {

    public TextField mileEntry_Field;
    public Label invalidMile_Label;
    SheetsAndJava sheetsAndJava = new SheetsAndJava();
    public AnchorPane rootPane;

    public MainController( ) throws IOException, GeneralSecurityException {}

    public void initialize()
    { invalidMile_Label.setVisible( false ); }

    public void launchUI(String uiPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(uiPath));
        Parent root = loader.load();
        rootPane.getChildren().setAll( root );
    }

    public void launchResultsUI() throws IOException, GeneralSecurityException {
        if (isValid_MileInput( mileEntry_Field.getText() )) {
            sheetsAndJava.addEntryToSheet( mileEntry_Field.getText() );
            launchUI( "/ui/results.fxml" );
        }
        else displayPromptFor3secs( invalidMile_Label );
    }

    public static boolean isValid_MileInput(String mile) {
        Pattern pattern = Pattern.compile("^[0-9]*(\\.)?[0-9]+$");
        Matcher matcher = pattern.matcher(mile);
        return (matcher.find() && matcher.group().equals(mile));
    }

    public static void displayPromptFor3secs(Label prompt) {
        prompt.setVisible(true);
        PauseTransition visiblePause = new PauseTransition( Duration.seconds(3));
        visiblePause.setOnFinished( event -> prompt.setVisible(false) );
        visiblePause.play();
    }
}
