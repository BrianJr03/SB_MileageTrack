package controllers;

import googleSheet.SBMT_Sheet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static main.Main.displayPromptFor3secs;

public class SettingsController {

    @FXML
    private Label fieldsEmptyAlready_Label;
    @FXML
    private ImageView email_CheckMarkImage;
    @FXML
    private Label sheetReset_Label;
    public Label sheetAlreadyReset_Label;
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
    @FXML
    private ImageView phoneNum_CheckMarkImage;

    SBMT_Sheet sbmt_sheet = new SBMT_Sheet();

    public SettingsController() throws IOException, GeneralSecurityException {}

    public void initialize() throws IOException, GeneralSecurityException {
        showCheckMarks();
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

    public void showCheckMarks() throws IOException, GeneralSecurityException {
        phoneNum_CheckMarkImage.setVisible( isValidPhoneNumber( sbmt_sheet.getUserPhoneNum() ) );
        email_CheckMarkImage.setVisible( isValidEmail( sbmt_sheet.getUserEmail() ) );
    }

    @FXML
    private void updateEmailAddress() throws IOException, GeneralSecurityException {
        if ( isValidEmail( emailAddress_Field.getText() )) {
            sbmt_sheet.updateSheet( "sbMileage!A3" , emailAddress_Field.getText() );
            displayEmail_CheckMark();
            displayPromptFor3secs( emailUpdated_Label );
        } else { email_CheckMarkImage.setVisible(false); displayPromptFor3secs( invalidEmail_Label );  }
    }

    private void displayPhoneNum_CheckMark()
    { phoneNum_CheckMarkImage.setVisible(true); }

    private void displayEmail_CheckMark()
    { email_CheckMarkImage.setVisible(true); }

    @FXML
    private void updatePhoneNumber() throws IOException, GeneralSecurityException {
        if ( isValidPhoneNumber( phoneNum_Field.getText() ) && isValidCarrier( carrier_Field.getText() )) {
            sbmt_sheet.updateSheet( "sbMileage!A2" , phoneNum_Field.getText() );
            sbmt_sheet.updateSheet( "sbMileage!A4" , carrier_Field.getText() );
            displayPhoneNum_CheckMark();
            displayPromptFor3secs( phoneNumUpdated_Label );
        } else { phoneNum_CheckMarkImage.setVisible( false ); displayPromptFor3secs( invalidPhoneNum_Label ); }
    }

    private void hideLabels() {
        sheetReset_Label.setVisible( false);
        invalidEmail_Label.setVisible( false );
        emailUpdated_Label.setVisible( false );
        invalidPhoneNum_Label.setVisible( false );
        phoneNumUpdated_Label.setVisible( false );
        sheetAlreadyReset_Label.setVisible( false );
        fieldsEmptyAlready_Label.setVisible( false );
    }

    @FXML
    private void resetPhoneFields() throws IOException, GeneralSecurityException {
        phoneNum_Field.clear(); carrier_Field.clear();
        if ( phoneNum_Field.getText().equals( " " ) && carrier_Field.getText().equals( " " )) {
            sbmt_sheet.updateSheet( "sbMileage!A2", "empty" );
            sbmt_sheet.updateSheet( "sbMileage!A4", "empty" );
            phoneNum_Field.clear(); carrier_Field.clear();
            phoneNum_CheckMarkImage.setVisible( false );
        }
        else displayPromptFor3secs( fieldsEmptyAlready_Label );
    }

    @FXML
    private void resetEmailFields() throws IOException, GeneralSecurityException {
        emailAddress_Field.clear();
        if ( emailAddress_Field.getText().equals( " " )) {
            sbmt_sheet.updateSheet( "sbMileage!A3", "empty" );
            emailAddress_Field.clear(); email_CheckMarkImage.setVisible( false );
        }
        else displayPromptFor3secs( fieldsEmptyAlready_Label );
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

    public void resetSheet() throws IOException, GeneralSecurityException {
        if ( sbmt_sheet.canSheetBeReset() ) {
            sbmt_sheet.resetSheet();
            displayPromptFor3secs( sheetReset_Label );
        }
        else displayPromptFor3secs( sheetAlreadyReset_Label );
    }
}
