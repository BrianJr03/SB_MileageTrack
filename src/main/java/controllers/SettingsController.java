package controllers;

import googleSheet.SBMT_Sheet;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SettingsController {

    public TextField phoneNum_Field;
    public TextField emailAddress_Field;
    SBMT_Sheet sbmt_sheet = new SBMT_Sheet();

    public AnchorPane rootPane;

    public SettingsController() throws IOException, GeneralSecurityException {}

    public void initialize() throws IOException, GeneralSecurityException {
        if (!sbmt_sheet.getEntryDates_AsObservableList().get( 1 ).equals( "0" ))
        { phoneNum_Field.setText( sbmt_sheet.getEntryDates_AsObservableList().get( 1 ) ); }
    }

    public void launchMainUI() throws IOException {
        launchUI( "/ui/main.fxml" );
    }

    public void launchUI(String uiPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(uiPath));
        Parent root = loader.load();
        rootPane.getChildren().setAll( root );
    }

    public void updatePhoneNumber() throws IOException, GeneralSecurityException
    { sbmt_sheet.updateSheet( "sbMileage!A2", phoneNum_Field.getText() ); }

    public void updateEmailAddress() throws IOException, GeneralSecurityException
    { sbmt_sheet.updateSheet( "sbMileage!A3", emailAddress_Field.getText() ); }

}
