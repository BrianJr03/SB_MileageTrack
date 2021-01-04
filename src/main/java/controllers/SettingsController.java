package controllers;

import googleSheet.SBMT_Sheet;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsController {

    @FXML
    private ComboBox <String> carrierComboBox;
    @FXML
    private TextField phoneNum_Field;
    @FXML
    private TextField emailAddress_Field;
    @FXML
    private Label phoneNumUpdated_Label;
    @FXML
    private Label emailUpdated_Label;
    @FXML
    private Label invalidPhoneNum_Label;
    @FXML
    private Label invalidEmail_Label;

    public AnchorPane rootPane;

    SBMT_Sheet sbmt_sheet = new SBMT_Sheet();
    public final ObservableList <String> carrierOptions = FXCollections.observableArrayList();

    public SettingsController() throws IOException, GeneralSecurityException {}

    public void initialize() throws IOException, GeneralSecurityException {
        hideLabels();
        verifyStoredText();
        verifyStoredEmail();
        addCarriersToDropdown();
    }

    public void launchMainUI() throws IOException
    { launchUI( "/ui/main.fxml" ); }

    public void launchUI(String uiPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(uiPath));
        Parent root = loader.load();
        rootPane.getChildren().setAll( root );
    }

    public void addCarriersToDropdown() {
        carrierOptions.add("AT&T");
        carrierOptions.add("Sprint");
        carrierOptions.add("T-Mobile");
        carrierOptions.add("Verizon");
        carrierComboBox.setItems( carrierOptions );
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\" +
                ".[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\" +
                ".)+[a-zA-Z]{2,7}$");
        return pattern.matcher(email).matches();
    }

    public static boolean isValidPhoneNumber( String phoneNumber ) {
        Pattern pattern = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
        Matcher matcher = pattern.matcher(phoneNumber);
        return (matcher.find() && matcher.group().equals(phoneNumber));
    }

    public void updatePhoneNumber() throws IOException, GeneralSecurityException {
         if (isValidPhoneNumber( phoneNum_Field.getText()) && isComboBoxNotEmpty()) {
            sbmt_sheet.updateSheet( "sbMileage!A2", phoneNum_Field.getText() );
            sbmt_sheet.updateSheet( "sbMileage!A4", carrierComboBox.getValue() );
            displayPromptFor3secs( phoneNumUpdated_Label ); }
        else displayPromptFor3secs( invalidPhoneNum_Label );

    }

    private boolean isComboBoxNotEmpty()
    { return carrierComboBox.getValue() != null; }

    public void updateEmailAddress() throws IOException, GeneralSecurityException {
        if (isValidEmail( emailAddress_Field.getText() )) {
            sbmt_sheet.updateSheet( "sbMileage!A3", emailAddress_Field.getText() );
            displayPromptFor3secs( emailUpdated_Label ); }
        else displayPromptFor3secs( invalidEmail_Label );
    }

    public static void displayPromptFor3secs(Label prompt) {
        prompt.setVisible(true);
        PauseTransition visiblePause = new PauseTransition( Duration.seconds(3));
        visiblePause.setOnFinished( event -> prompt.setVisible(false) );
        visiblePause.play();
    }

    public void hideLabels() {
        invalidEmail_Label.setVisible( false );
        emailUpdated_Label.setVisible( false );
        invalidPhoneNum_Label.setVisible( false);
        phoneNumUpdated_Label.setVisible( false );
    }

    public void verifyStoredText() throws IOException, GeneralSecurityException {
        if (!sbmt_sheet.getEntryDates_AsObservableList().get( 1 ).equals( "0" ))
        { phoneNum_Field.setText( sbmt_sheet.getEntryDates_AsObservableList().get( 1 ) ); }
    }

    public void verifyStoredEmail() throws IOException, GeneralSecurityException {
        if (!sbmt_sheet.getEntryDates_AsObservableList().get( 2 ).equals( "0" ))
        { emailAddress_Field.setText( sbmt_sheet.getEntryDates_AsObservableList().get( 2 ) ); }
    }
}
