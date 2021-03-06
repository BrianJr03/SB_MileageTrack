package controllers;

import googleSheet.SBMT_Sheet;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;
import static controllerLogic.ControllerLogic.*;
import static controllerLogic.ControllerLogic.formatPhoneNumber;
import static main.Main.displayPromptFor3secs;

public class SettingsController {

    @FXML
    private ImageView bkGrnd_ImageView;
    @FXML
    private Label carrierUpdated_Label;
    @FXML
    private ImageView carrier_CheckMarkImage;
    @FXML
    private Label loading_Label;
    @FXML
    private ImageView mileThreshold_CheckMarkImage;
    @FXML
    private Label invalidMileThreshold_Label;
    @FXML
    private Label mileThresholdUpdated_Label;
    @FXML
    private TextField mileThreshold_Field;
    @FXML
    private Label fieldsEmptyAlready_Label;
    @FXML
    private ImageView email_CheckMarkImage;
    @FXML
    private Label sheetReset_Label;
    @FXML
    private Label sheetAlreadyReset_Label;
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
        hideAll_Labels();
        verifyStoredEmail();
        verifyStoredCarrier();
        verifyStoredPhoneNum();
        verifyStoredMileCount();
        sbmt_sheet.setBkGrnd( bkGrnd_ImageView );
    }

    @FXML
    private void launchMainUI() throws IOException {
        FXMLLoader loader = new FXMLLoader( getClass().getResource( "/ui/main.fxml" ) );
        Parent root = loader.load();
        rootPane.getChildren().setAll( root );
    }

    @FXML
    private void updateEmailAddress() throws IOException, GeneralSecurityException {
        if ( isValidEmail( emailAddress_Field.getText() )) {
            sbmt_sheet.updateSheet( "sbMileage!A3" , emailAddress_Field.getText() );
            displayEmail_CheckMark(); displayPromptFor3secs( emailUpdated_Label );
        } else { email_CheckMarkImage.setVisible(false); displayPromptFor3secs( invalidEmail_Label );  }
    }

    @FXML
    private void updateCarrier() throws IOException, GeneralSecurityException {
        if ( isValidCarrier( carrier_Field.getText() )) {
            sbmt_sheet.updateSheet( "sbMileage!A4" , carrier_Field.getText() );
            displayCarrier_CheckMark(); displayPromptFor3secs( carrierUpdated_Label ); }
        else carrier_CheckMarkImage.setVisible( false );
        if ( !isValidCarrier( carrier_Field.getText() ) )
         displayPromptFor3secs( invalidPhoneNum_Label );
    }

    @FXML
    private void updatePhoneNumber() throws IOException, GeneralSecurityException {
        String phoneNum = phoneNum_Field.getText();
        String formattedPhoneNum = formatPhoneNumber( phoneNum );
        if ( isValidPhoneNumber( formattedPhoneNum ) ) {
            phoneNum_Field.setText( formattedPhoneNum );
            sbmt_sheet.updateSheet( "sbMileage!A5" , formattedPhoneNum );
            displayPhoneNum_CheckMark(); displayPromptFor3secs( phoneNumUpdated_Label ); }
        else phoneNum_CheckMarkImage.setVisible( false );
        if ( !isValidPhoneNumber( phoneNum_Field.getText() ))
         displayPromptFor3secs( invalidPhoneNum_Label );
    }

    @FXML
    private void updateMileageThreshold() throws IOException, GeneralSecurityException {
        if ( isValidMileThreshold( mileThreshold_Field.getText() ) ) {
            sbmt_sheet.updateSheet( "sbMileage!A1", mileThreshold_Field.getText() );
            sbmt_sheet.updateSheet( "sbMileage!A2", mileThreshold_Field.getText() );
            sbmt_sheet.updateSheet( "sbMileage!C1", String.valueOf( 0 ) );
            displayMile_CheckMark(); displayPromptFor3secs( mileThresholdUpdated_Label ); }
        else { mileThreshold_CheckMarkImage.setVisible( false );
        displayPromptFor3secs( invalidMileThreshold_Label ); }
    }

    @FXML
    private void resetPhoneFields() throws IOException, GeneralSecurityException {
        if ( phoneNum_Field.getText().isBlank() && carrier_Field.getText().isBlank() )
        { displayPromptFor3secs( fieldsEmptyAlready_Label ); }
        else {
            phoneNum_Field.clear(); carrier_Field.clear();
            phoneNum_CheckMarkImage.setVisible( false );
            carrier_CheckMarkImage.setVisible( false );
            sbmt_sheet.updateSheet( "sbMileage!A4", "empty" );
            sbmt_sheet.updateSheet( "sbMileage!A5", "empty" );
        }
    }

    @FXML
    private void resetEmailField() throws IOException, GeneralSecurityException {
        if ( emailAddress_Field.getText().isBlank() )
        { displayPromptFor3secs( fieldsEmptyAlready_Label ); }
        else {
            emailAddress_Field.clear();
            email_CheckMarkImage.setVisible( false );
            sbmt_sheet.updateSheet( "sbMileage!A3", "empty" );
        }
    }

    private void resetMileThreshold() throws IOException, GeneralSecurityException {
        if ( mileThreshold_Field.getText().isBlank() )
        { displayPromptFor3secs( fieldsEmptyAlready_Label ); }
        else if ( sbmt_sheet.sheetAllowsForReset() ) {
            clearFields(); hideCheckMarks();
            mileThreshold_CheckMarkImage.setVisible( false );
            sbmt_sheet.resetSheet();
        }
    }

    private void resetSheet() throws IOException, GeneralSecurityException {
        clearFields(); hideCheckMarks();
        if ( sbmt_sheet.sheetAllowsForReset() ) {
            sbmt_sheet.resetSheet();
            displayPromptFor3secs( sheetReset_Label );
        }
        else displayPromptFor3secs( sheetAlreadyReset_Label );
    }

    @FXML
    private void newBackground() throws IOException, GeneralSecurityException
    { sbmt_sheet.verifyBkGrnd( bkGrnd_ImageView ); }

    private Alert createAlert() {
        Stage stage = ( Stage ) rootPane.getScene().getWindow();
        Alert.AlertType warning = Alert.AlertType.CONFIRMATION;
        Alert alert = new Alert(warning,"");
        alert.initModality( Modality.APPLICATION_MODAL );
        alert.initOwner( stage );
        alert.getDialogPane().setHeaderText( "WARNING" );
        alert.getDialogPane().setContentText( "This will reset your data." );
        return alert;
    }

    @FXML
    private void displayData_Alert() {
        Alert alert = createAlert();
        Optional< ButtonType > result = alert.showAndWait();
        if ( result.orElse(null) == ButtonType.OK )
        { loadSheetReset(); }
    }

    @FXML
    private void displayMile_Alert() {
        if ( !mileThreshold_Field.getText().isBlank() && isValidMileThreshold( mileThreshold_Field.getText() ) ) {
            Alert alert = createAlert();
            Optional< ButtonType > result = alert.showAndWait();
            if ( result.orElse(null) == ButtonType.OK )
            { loadMileReset(); } }
        else displayPromptFor3secs( fieldsEmptyAlready_Label );
    }

    private void displayCarrier_CheckMark( )
    { carrier_CheckMarkImage.setVisible( true ); }

    private void displayPhoneNum_CheckMark()
    { phoneNum_CheckMarkImage.setVisible(true); }

    private void displayEmail_CheckMark()
    { email_CheckMarkImage.setVisible(true); }

    private void displayMile_CheckMark()
    { mileThreshold_CheckMarkImage.setVisible( true ); }

    private void verifyStoredMileCount() throws IOException, GeneralSecurityException
    {  verifyStoredInfo( 1 ); }

    private void verifyStoredEmail() throws IOException, GeneralSecurityException
    { verifyStoredInfo( 2 ); }

    private void verifyStoredCarrier() throws IOException, GeneralSecurityException
    { verifyStoredInfo( 3 ); }

    private void verifyStoredPhoneNum() throws IOException, GeneralSecurityException
    { verifyStoredInfo( 4 ); }

    private void clearFields() {
        phoneNum_Field.clear(); mileThreshold_Field.clear();
        emailAddress_Field.clear(); carrier_Field.clear();
    }

    private void hideCheckMarks() {
        email_CheckMarkImage.setVisible( false );
        carrier_CheckMarkImage.setVisible( false );
        phoneNum_CheckMarkImage.setVisible( false );
        mileThreshold_CheckMarkImage.setVisible( false );
    }

    private void showCheckMarks() throws IOException, GeneralSecurityException {
        phoneNum_CheckMarkImage.setVisible( isValidPhoneNumber( sbmt_sheet.getUserPhoneNum() ) );
        carrier_CheckMarkImage.setVisible( isValidCarrier( sbmt_sheet.getUserCarrier() ) );
        email_CheckMarkImage.setVisible( isValidEmail( sbmt_sheet.getUserEmail() ) );
        mileThreshold_CheckMarkImage.setVisible( isValidMileThreshold( sbmt_sheet.getMileageWarningThreshold() )
                && !sbmt_sheet.getStored_MileageWarningThreshold().equals( String.valueOf( 0 ) ) );
    }

    private void hideAll_Labels() {
        loading_Label.setVisible( false );
        sheetReset_Label.setVisible( false);
        invalidEmail_Label.setVisible( false );
        emailUpdated_Label.setVisible( false );
        carrierUpdated_Label.setVisible( false );
        invalidPhoneNum_Label.setVisible( false );
        phoneNumUpdated_Label.setVisible( false );
        sheetAlreadyReset_Label.setVisible( false );
        fieldsEmptyAlready_Label.setVisible( false );
        invalidMileThreshold_Label.setVisible( false );
        mileThresholdUpdated_Label.setVisible( false );
    }

    private void verifyStoredInfo( int index ) throws IOException, GeneralSecurityException {
        if ( index == 1 ) { if ( sbmt_sheet.isSheetCellEmpty( index ) ) {
                    mileThreshold_Field.setText( sbmt_sheet.getEntryDates_AsObservableList().subList( 0, 5 ).get( index ) ); } }
        if ( index == 2 ) { if ( sbmt_sheet.isSheetCellEmpty( index ) ) {
                    emailAddress_Field.setText( sbmt_sheet.getEntryDates_AsObservableList().subList( 0, 5 ).get( index ) ); } }
        if ( index == 3 ) { if ( sbmt_sheet.isSheetCellEmpty( index ) ) {
                    carrier_Field.setText( sbmt_sheet.getEntryDates_AsObservableList().subList( 0, 5 ).get( index ) ); } }
        if ( index == 4  ) { if ( sbmt_sheet.isSheetCellEmpty( index ) ) {
                    phoneNum_Field.setText( sbmt_sheet.getEntryDates_AsObservableList().subList( 0, 5 ).get( index ) ); } }
    }

    private void loadSheetReset()
    { new LoadingSheetReset_Label().start(); }

    private void loadMileReset()
    { new LoadingMileReset_Label().start(); }

    @FXML
    private void loadingEmailReset()
    { new LoadingEmailReset_Label().start(); }

    @FXML
    private void loadingPhoneReset()
    { new LoadingPhoneReset_Label().start(); }

    @FXML
    private void loadingNewBckGrnd()
    { new LoadingNewBckGrnd_Label().start(); }

    class LoadingSheetReset_Label extends Thread {
        public void run() {
            loading_Label.setVisible(true);
            try { Thread.sleep( 100 );
                Platform.runLater( () -> {
                    try { resetSheet(); }
                    catch ( IOException | GeneralSecurityException exception )
                    { exception.printStackTrace(); }
                    loading_Label.setVisible(false); } ); }
            catch ( InterruptedException e )
            { e.printStackTrace(); }
        }
    }

    class LoadingNewBckGrnd_Label extends Thread {
        public void run() {
            loading_Label.setVisible(true);
            try { Thread.sleep( 100 );
                Platform.runLater( () -> {
                    try { newBackground(); }
                    catch ( IOException | GeneralSecurityException exception )
                    { exception.printStackTrace(); }
                    loading_Label.setVisible(false); } ); }
            catch ( InterruptedException e )
            { e.printStackTrace(); }
        }
    }

    class LoadingMileReset_Label extends Thread {
        public void run() {
            loading_Label.setVisible(true);
            try { Thread.sleep( 100 );
                Platform.runLater( () -> {
                    try { resetMileThreshold(); }
                    catch ( IOException | GeneralSecurityException exception )
                    { exception.printStackTrace(); }
                    loading_Label.setVisible(false); } ); }
            catch ( InterruptedException e )
            { e.printStackTrace(); }
        }
    }

    class LoadingEmailReset_Label extends Thread {
        public void run() {
            loading_Label.setVisible(true);
            try { Thread.sleep( 100 );
                Platform.runLater( () -> {
                    try { resetEmailField(); }
                    catch ( IOException | GeneralSecurityException exception )
                    { exception.printStackTrace(); }
                    loading_Label.setVisible(false); } ); }
            catch ( InterruptedException e )
            { e.printStackTrace(); }
        }
    }

    class LoadingPhoneReset_Label extends Thread {
        public void run() {
            loading_Label.setVisible(true);
            try { Thread.sleep( 100 );
                Platform.runLater( () -> {
                    try { resetPhoneFields(); }
                    catch ( IOException | GeneralSecurityException exception )
                    { exception.printStackTrace(); }
                    loading_Label.setVisible(false); } ); }
            catch ( InterruptedException e )
            { e.printStackTrace(); }
        }
    }
}