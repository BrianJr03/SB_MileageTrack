package controllers;

import javafx.fxml.Initializable;
import googleSheet.SendWarning;
import googleSheet.SheetEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import static main.Main.launchUI;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import googleSheet.SBMT_Sheet;
import javafx.scene.paint.Color;
import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.ResourceBundle;

public class ResultsController implements Initializable {

    @FXML
    private Label totalMileageString_Label;
    @FXML
    private Label mileAvgString_Label;
    @FXML
    private Label mileAVGLast10String_Label;
    @FXML
    private ImageView bkGrnd_ImageView;
    @FXML
    private TableColumn<SheetEntry, String> entryCountColumn;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<SheetEntry> mileageInfo_Table;
    @FXML
    private TableColumn<SheetEntry, String> dateColumn;
    @FXML
    private TableColumn<SheetEntry, String> mileColumn;
    @FXML
    private Label startDate_Label;
    @FXML
    private Label totalMileage_Label;
    @FXML
    private Label mileAVGLast10_Label;
    @FXML
    private Label mileAVG_Label;

    SBMT_Sheet sbmtSheet = new SBMT_Sheet();
    SendWarning sendWarning = new SendWarning();

    private final String mileWarningThreshold = sbmtSheet.getStored_MileageWarningThreshold();
    private final double mileWarningThreshold_AsDouble = Double.parseDouble( mileWarningThreshold );

    public ResultsController() throws IOException, GeneralSecurityException {}

    @Override
    public void initialize( URL location , ResourceBundle resources ) {
        try {
            setDate_Label();
            setMileAVG_Label();
            updateMileageText();
            setTableProperties();
            updateTotalMileage();
            checkFor_HighMileage();
            setTotalMileage_Label();
            sbmtSheet.setBkGrnd( bkGrnd_ImageView );
        }
        catch ( IOException | GeneralSecurityException | MessagingException exception )
        { exception.printStackTrace(); }
    }

    private void updateMileageText() throws IOException, GeneralSecurityException {
        if ( sbmtSheet.getBkGrndIndex().equals( "1" ) ) {
             mileAVG_Label.setTextFill( Color.BLACK );
             totalMileage_Label.setTextFill( Color.BLACK );
             mileAVGLast10_Label.setTextFill( Color.BLACK );
             startDate_Label.setTextFill( Color.BLACK );
             mileAVGLast10String_Label.setTextFill( Color.BLACK );
             mileAvgString_Label.setTextFill( Color.BLACK );
             totalMileageString_Label.setTextFill( Color.BLACK );
        }
    }

    private void setTableProperties() throws IOException, GeneralSecurityException {
        dateColumn.setCellValueFactory( new PropertyValueFactory <>( "entryDate" ) );
        mileColumn.setCellValueFactory( new PropertyValueFactory <>( "mileage" ) );
        entryCountColumn.setCellValueFactory( new PropertyValueFactory<>( "entryCount" ));
        ObservableList<SheetEntry> entries = getEntries();
        mileageInfo_Table.setItems( entries );
    }

    private ObservableList< SheetEntry > getEntries() throws IOException, GeneralSecurityException {
        List<List<Object>> sheetData = sbmtSheet.getSheetData();
        ObservableList<SheetEntry> sheetEntries = FXCollections.observableArrayList();
        int entryCount = -5;
        for ( List <Object> row : sheetData ) {
            entryCount++;
            sheetEntries.add( new SheetEntry( row.get( 0 ).toString() ,
                row.get( 1 ).toString(), String.valueOf( entryCount ))); }
        sbmtSheet.removePlaceHolderRows( sheetEntries );
        return sheetEntries;
    }

    private void setMileAVG_Label() throws IOException, GeneralSecurityException {
        mileAVG_Label.setText( sbmtSheet.get_MileAvg() + " mi" );
        if ( sbmtSheet.getSheetData().size() < 15 ) { mileAVGLast10_Label.setText( "N/A" ); }
        else mileAVGLast10_Label.setText( sbmtSheet.getLastTenEntries_MileAvg() + " mi");
    }

    private void setTotalMileage_Label()
    { totalMileage_Label.setText( sbmtSheet.getTotalMileage() + " mi"); }

    private void setDate_Label() throws IOException, GeneralSecurityException
    { startDate_Label.setText( sbmtSheet.getStartDate()); }

    @FXML
    private void launchMainUI() throws IOException
    { launchUI( "/ui/main.fxml", rootPane ); }

    private void checkFor_HighMileage() throws IOException, MessagingException, GeneralSecurityException {
        String A1 = "sbMileage!A1"; double totalMileage = sbmtSheet.getTotalMileage();
        if ( totalMileage >= Double.parseDouble( sbmtSheet.getMileageWarningThreshold() )
                && !sbmtSheet.getMileageWarningThreshold().equals( String.valueOf( 0 ) ) ) {
            sbmtSheet.updateSheet( A1, Double.toString(  mileWarningThreshold_AsDouble + sbmtSheet.getTotalMileage() ));
            sendHighMileage_Warning(); }
        if ( totalMileage >= 700 )
        { sendWheelWarning(); }
    }

    private void updateTotalMileage() throws IOException, GeneralSecurityException
    { sbmtSheet.updateSheet( "sbMileage!C1", String.valueOf(sbmtSheet.getTotalMileage() )); }

    private void sendWarningToUserAsText( String phoneNumber, String carrier ) throws  MessagingException
    { sendWarning.sendNotificationAsTextMSG( phoneNumber, carrier ); }

    private void sendWarningToUserAsEmail( String email ) throws MessagingException
    { sendWarning.sendNotificationAsEmail( email ); }

    private boolean phoneFields_NotEmpty() throws IOException, GeneralSecurityException
    { return !sbmtSheet.getUserPhoneNum().equals( "empty" ) && !sbmtSheet.getUserCarrier().equals( "empty" ); }

    private boolean emailField_NotEmpty() throws IOException, GeneralSecurityException
    { return !sbmtSheet.getUserEmail().equals( "empty" ); }

    private void sendWheelWarning() throws IOException, GeneralSecurityException, MessagingException {
        sendWarning.writeWheelNotification();
        if ( phoneFields_NotEmpty() )
        { sendWarning.sendNotificationAsEmail( sbmtSheet.getUserPhoneNum() ); }
        if ( emailField_NotEmpty() )
        { sendWarningToUserAsEmail( sbmtSheet.getUserEmail() ); }
    }

    private void sendHighMileage_Warning() throws IOException, GeneralSecurityException, MessagingException {
        sendWarning.writeMileNotification();
        if ( phoneFields_NotEmpty() )
        { sendWarningToUserAsText( sbmtSheet.getUserPhoneNum(), sbmtSheet.getUserCarrier()); }
        if ( emailField_NotEmpty() )
        { sendWarningToUserAsEmail( sbmtSheet.getUserEmail() ); }
    }
}