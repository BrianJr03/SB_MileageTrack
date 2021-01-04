package controllers;

import googleSheet.SBMT_Sheet;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

    public AnchorPane rootPane;

    SBMT_Sheet sbmt_sheet = new SBMT_Sheet();

    public SettingsController( ) throws IOException, GeneralSecurityException {
    }

    public void initialize( ) throws IOException, GeneralSecurityException {
        hideLabels();
        verifyStoredText();
        verifyStoredEmail();
        verifyStoredCarrier();
    }

    public void launchMainUI( ) throws IOException {
        launchUI( "/ui/main.fxml" );
    }

    public void launchUI( String uiPath ) throws IOException {
        FXMLLoader loader = new FXMLLoader( getClass().getResource( uiPath ) );
        Parent root = loader.load();
        rootPane.getChildren().setAll( root );
    }


    public static boolean isValidEmail( String email ) {
        Pattern pattern = Pattern.compile( "^[a-zA-Z0-9_+&*-]+(?:\\" +
                ".[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\" +
                ".)+[a-zA-Z]{2,7}$" );
        return pattern.matcher( email ).matches();
    }

    public static boolean isValidPhoneNumber( String phoneNumber ) {
        Pattern pattern = Pattern.compile( "\\d{3}-\\d{3}-\\d{4}" );
        Matcher matcher = pattern.matcher( phoneNumber );
        return ( matcher.find() && matcher.group().equals( phoneNumber ) );
    }

    public void updatePhoneNumber( ) throws IOException, GeneralSecurityException {
        if ( isValidPhoneNumber( phoneNum_Field.getText() ) && isValidCarrier( carrier_Field.getText() ) ) {
            sbmt_sheet.updateSheet( "sbMileage!A2" , phoneNum_Field.getText() );
            sbmt_sheet.updateSheet( "sbMileage!A4" , carrier_Field.getText() );
            displayPromptFor3secs( phoneNumUpdated_Label );
        } else displayPromptFor3secs( invalidPhoneNum_Label );

    }

    private boolean isValidCarrier( String carrier ) {
        switch ( carrier ) {
            case "AT&T" , "Sprint" , "Verizon" , "T-Mobile" -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public void updateEmailAddress( ) throws IOException, GeneralSecurityException {
        if ( isValidEmail( emailAddress_Field.getText() ) ) {
            sbmt_sheet.updateSheet( "sbMileage!A3" , emailAddress_Field.getText() );
            displayPromptFor3secs( emailUpdated_Label );
        } else displayPromptFor3secs( invalidEmail_Label );
    }

    public static void displayPromptFor3secs( Label prompt ) {
        prompt.setVisible( true );
        PauseTransition visiblePause = new PauseTransition( Duration.seconds( 3 ) );
        visiblePause.setOnFinished( event -> prompt.setVisible( false ) );
        visiblePause.play();
    }

    public void hideLabels( ) {
        invalidEmail_Label.setVisible( false );
        emailUpdated_Label.setVisible( false );
        invalidPhoneNum_Label.setVisible( false );
        phoneNumUpdated_Label.setVisible( false );
    }

    public void verifyStoredText() throws IOException, GeneralSecurityException {
        if ( !sbmt_sheet.getEntryDates_AsObservableList().get( 1 ).equals( "empty" ) ) {
            phoneNum_Field.setText( sbmt_sheet.getEntryDates_AsObservableList().get( 1 ) );
        }
    }

    public void verifyStoredEmail() throws IOException, GeneralSecurityException {
        if ( !sbmt_sheet.getEntryDates_AsObservableList().get( 2 ).equals( "empty" ) ) {
            emailAddress_Field.setText( sbmt_sheet.getEntryDates_AsObservableList().get( 2 ) );
        }
    }

    public void verifyStoredCarrier() throws IOException, GeneralSecurityException {
        if ( !sbmt_sheet.getEntryDates_AsObservableList().get( 3 ).equals( "empty" ) ) {
            carrier_Field.setText( sbmt_sheet.getEntryDates_AsObservableList().get( 3 ) );
        }
    }

}
