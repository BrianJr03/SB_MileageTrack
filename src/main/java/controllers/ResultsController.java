package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import util.SheetsAndJava;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class ResultsController {
    public ListView<String> entryHistory_ListView;
    public AnchorPane rootPane;
    SheetsAndJava sheetsAndJava = new SheetsAndJava();

    public void initialize() throws IOException, GeneralSecurityException {
        entryHistory_ListView.setItems( sheetsAndJava.getEntryMiles_AsObservableList() );
    }

    public void launchUI(String uiPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(uiPath));
        Parent root = loader.load();
        rootPane.getChildren().setAll( root );
    }

    public void launchResultsUI() throws IOException {
        launchUI( "/ui/main.fxml" );
    }
}
