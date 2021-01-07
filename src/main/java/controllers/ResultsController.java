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
import javafx.scene.layout.AnchorPane;
import googleSheet.SBMT_Sheet;
import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.ResourceBundle;

public class ResultsController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<SheetEntry> mileageInfo_Table;
    @FXML
    private TableColumn<SheetEntry, String> dateColumn;
    @FXML
    private TableColumn<SheetEntry, String> mileColumn;
    @FXML
    private Label date_Label;
    @FXML
    private Label totalMileage_Label;
    @FXML
    private Label mileAVG_Label;

    SBMT_Sheet sbmtSheet = new SBMT_Sheet();
    SendWarning sendWarning = new SendWarning();

    private final String mileWarningThreshold    = sbmtSheet.getStored_MileageWarningThreshold();
    private final double mileWarningThreshold_AsDouble = Double.parseDouble( mileWarningThreshold );

    public ResultsController() throws IOException, GeneralSecurityException {}

    @Override
    public void initialize( URL location , ResourceBundle resources ) {
        try {
            setDate_Label();
            setMileAVG_Label();
            setTableProperties();
            updateTotalMileage();
            checkFor_HighMileage();
            setTotalMileage_Label(); }
        catch ( IOException | GeneralSecurityException | MessagingException exception )
        { exception.printStackTrace(); }
    }

    private void setTableProperties() throws IOException, GeneralSecurityException {
        dateColumn.setCellValueFactory( new PropertyValueFactory <>( "entryDate" ) );
        mileColumn.setCellValueFactory( new PropertyValueFactory <>( "mileage" ) );
        ObservableList<SheetEntry> entries = getEntries();
        mileageInfo_Table.setItems(entries);
    }

    private ObservableList< SheetEntry > getEntries() throws IOException, GeneralSecurityException {
        List<List<Object>> sheetData = sbmtSheet.getSheetData();
        ObservableList<SheetEntry> sheetEntries = FXCollections.observableArrayList();
        for ( List <Object> row : sheetData )
        { sheetEntries.add( new SheetEntry( row.get( 0 ).toString(), row.get( 1 ).toString())); }
        removePlaceHolderRows( sheetEntries );
        return sheetEntries;
    }

    private void removePlaceHolderRows( ObservableList<SheetEntry> sheetEntries )
    { sheetEntries.subList( 0 , 5 ).clear(); }

    private void setMileAVG_Label() throws IOException, GeneralSecurityException {
        if ( sbmtSheet.getSheetData().size() < 10) { mileAVG_Label.setText( "N/A" ); }
        else mileAVG_Label.setText( String.valueOf( sbmtSheet.getLastTenEntries_MileAvg()));
    }

    private void setTotalMileage_Label()
    { totalMileage_Label.setText( String.valueOf(sbmtSheet.getTotalMileage())); }

    private void setDate_Label() throws IOException, GeneralSecurityException
    { date_Label.setText( sbmtSheet.getStartDate()); }

    @FXML
    private void launchMainUI() throws IOException
    { launchUI( "/ui/main.fxml", rootPane ); }

    private void checkFor_HighMileage() throws IOException, MessagingException, GeneralSecurityException {
        String A1 = "sbMileage!A1"; double totalMileage = sbmtSheet.getTotalMileage();
        if ( totalMileage >= Double.parseDouble( sbmtSheet.getMileageWarningThreshold() )
                && !sbmtSheet.getMileageWarningThreshold().equals( String.valueOf( 0 ) ) ) {
            sbmtSheet.updateSheet( A1,
                    String.valueOf( mileWarningThreshold_AsDouble + sbmtSheet.getTotalMileage() ));
            sendHighMileage_Warning(); }
    }

    private void updateTotalMileage() throws IOException, GeneralSecurityException
    { sbmtSheet.updateSheet( "sbMileage!C1", String.valueOf(sbmtSheet.getTotalMileage() )); }

    private void sendWarningToUserAsText( String phoneNumber, String carrier ) throws IOException, MessagingException, GeneralSecurityException
    { sendWarning.sendNotificationAsTextMSG( phoneNumber, carrier ); }

    private void sendWarningToUserAsEmail( String email ) throws IOException, MessagingException, GeneralSecurityException
    { sendWarning.sendNotificationAsEmail( email ); }

    private void sendHighMileage_Warning() throws IOException, GeneralSecurityException, MessagingException {
        if (!sbmtSheet.getUserPhoneNum().equals( "empty" ) && !sbmtSheet.getUserCarrier().equals( "empty" ))
        { sendWarningToUserAsText(sbmtSheet.getUserPhoneNum(), sbmtSheet.getUserCarrier()); }
        if (!sbmtSheet.getUserEmail().equals( "empty" ))
        { sendWarningToUserAsEmail( sbmtSheet.getUserEmail() ); }
    }
}