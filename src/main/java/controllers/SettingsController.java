package controllers;

import googleSheet.SBMT_Sheet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static main.Main.displayPromptFor3secs;

public class SettingsController {

    @FXML
    private TextField carrier_Field;
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
    @FXML
    private AnchorPane rootPane;

    SBMT_Sheet sbmt_sheet = new SBMT_Sheet();

    public SettingsController() throws IOException, GeneralSecurityException {}

    public void initialize( ) throws IOException, GeneralSecurityException {
        hideLabels();
        verifyStoredText();
        verifyStoredEmail();
        verifyStoredCarrier();
    }

    @FXML
    private void launchMainUI() throws IOException {
        FXMLLoader loader = new FXMLLoader( getClass().getResource( "/ui/main.fxml" ) );
        Parent root = loader.load();
        rootPane.getChildren().setAll( root );
    }

    private static boolean isValidEmail( String email ) {
        Pattern pattern = Pattern.compile( "^[a-zA-Z0-9_+&*-]+(?:\\" +
                ".[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\" +
                ".)+[a-zA-Z]{2,7}$" );
        return pattern.matcher( email ).matches();
    }

    private static boolean isValidPhoneNumber( String phoneNumber ) {
        Pattern pattern = Pattern.compile( "\\d{3}-\\d{3}-\\d{4}" );
        Matcher matcher = pattern.matcher( phoneNumber );
        return ( matcher.find() && matcher.group().equals( phoneNumber ) );
    }

    private boolean isValidCarrier( String carrier ) {
        switch ( carrier ) {
            case "AT&T" , "Sprint" , "Verizon" , "T-Mobile" -> { return true; }
            default -> { return false; }
        }
    }

    @FXML
    private void updateEmailAddress() throws IOException, GeneralSecurityException {
        if ( isValidEmail( emailAddress_Field.getText() )) {
            sbmt_sheet.updateSheet( "sbMileage!A3" , emailAddress_Field.getText() );
            displayPromptFor3secs( emailUpdated_Label );
        } else displayPromptFor3secs( invalidEmail_Label );
    }

    @FXML
    private void updatePhoneNumber() throws IOException, GeneralSecurityException {
        if ( isValidPhoneNumber( phoneNum_Field.getText() ) && isValidCarrier( carrier_Field.getText() )) {
            sbmt_sheet.updateSheet( "sbMileage!A2" , phoneNum_Field.getText() );
            sbmt_sheet.updateSheet( "sbMileage!A4" , carrier_Field.getText() );
            displayPromptFor3secs( phoneNumUpdated_Label );
        } else displayPromptFor3secs( invalidPhoneNum_Label );
    }

    private void hideLabels() {
        invalidEmail_Label.setVisible( false );
        emailUpdated_Label.setVisible( false );
        invalidPhoneNum_Label.setVisible( false );
        phoneNumUpdated_Label.setVisible( false );
    }

    private void verifyStoredText() throws IOException, GeneralSecurityException
    { verifyStoredInfo( 1 ); }

    private void verifyStoredEmail() throws IOException, GeneralSecurityException
    { verifyStoredInfo( 2 ); }

    private void verifyStoredCarrier() throws IOException, GeneralSecurityException
    { verifyStoredInfo( 3 ); }

    private void verifyStoredInfo(int index) throws IOException, GeneralSecurityException {
        switch ( index ) {
            case 1 -> {
                if ( !sbmt_sheet.getEntryDates_AsObservableList().get( index ).equals( "empty" ) ) {
                    phoneNum_Field.setText( sbmt_sheet.getEntryDates_AsObservableList().get( index ) );
                }
            }
            case 2 -> {
                if ( !sbmt_sheet.getEntryDates_AsObservableList().get( index ).equals( "empty" ) ) {
                    emailAddress_Field.setText( sbmt_sheet.getEntryDates_AsObservableList().get( index ) );
                }
            }
            case 3 -> {
                if ( !sbmt_sheet.getEntryDates_AsObservableList().get( index ).equals( "empty" ) ) {
                    carrier_Field.setText( sbmt_sheet.getEntryDates_AsObservableList().get( index ) );
                }
            }
        }
    }
}
