package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import util.SheetsAndJava;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainController {

    public TextField mileEntry_Field;
    SheetsAndJava sheetsAndJava = new SheetsAndJava();
    public AnchorPane rootPane;

    public void launchUI(String uiPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(uiPath));
        Parent root = loader.load();
        rootPane.getChildren().setAll( root );
    }

    public void launchResultsUI() throws IOException, GeneralSecurityException {
        sheetsAndJava.addEntryToSheet( mileEntry_Field.getText() );
        launchUI( "/ui/results.fxml" );
    }
}
