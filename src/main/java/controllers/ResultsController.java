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
    private Label numOfEntries_Label;
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
    private Label mileAVGLast10_Label;
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
            setTotalMileage_Label();
            setNumOfEntries_Label(); }
        catch ( IOException | GeneralSecurityException | MessagingException exception )
        { exception.printStackTrace(); }
    }

    private void setTableProperties() throws IOException, GeneralSecurityException {
        dateColumn.setCellValueFactory( new PropertyValueFactory <>( "entryDate" ) );
        mileColumn.setCellValueFactory( new PropertyValueFactory <>( "mileage" ) );
        ObservableList<SheetEntry> entries = getEntries();
        mileageInfo_Table.setItems( entries );
    }

    private ObservableList< SheetEntry > getEntries() throws IOException, GeneralSecurityException {
        List<List<Object>> sheetData = sbmtSheet.getSheetData();
        ObservableList<SheetEntry> sheetEntries = FXCollections.observableArrayList();
        for ( List <Object> row : sheetData )
        { sheetEntries.add( new SheetEntry( row.get( 0 ).toString(), row.get( 1 ).toString())); }
        sbmtSheet.removePlaceHolderRows( sheetEntries );
        return sheetEntries;
    }

    private void setMileAVG_Label() throws IOException, GeneralSecurityException {
        mileAVG_Label.setText( sbmtSheet.get_MileAvg() + " mi" );
        if ( sbmtSheet.getSheetData().size() < 15 ) { mileAVGLast10_Label.setText( "N/A" ); }
        else mileAVGLast10_Label.setText( sbmtSheet.getLastTenEntries_MileAvg() + " mi");
    }

    private void setNumOfEntries_Label() throws IOException, GeneralSecurityException
    { numOfEntries_Label.setText( String.valueOf( sbmtSheet.getSheetData().size() - 5 ) ); }

    private void setTotalMileage_Label()
    { totalMileage_Label.setText( sbmtSheet.getTotalMileage() + " mi"); }

    private void setDate_Label() throws IOException, GeneralSecurityException
    { date_Label.setText( sbmtSheet.getStartDate()); }

    @FXML
    private void launchMainUI() throws IOException
    { launchUI( "/ui/main.fxml", rootPane ); }

    private void checkFor_HighMileage() throws IOException, MessagingException, GeneralSecurityException {
        String A1 = "sbMileage!A1"; double totalMileage = sbmtSheet.getTotalMileage();
        if ( totalMileage >= Double.parseDouble( sbmtSheet.getMileageWarningThreshold() )
                && !sbmtSheet.getMileageWarningThreshold().equals( String.valueOf( 0 ) ) ) {
            sbmtSheet.updateSheet( A1, Double.toString(  mileWarningThreshold_AsDouble + sbmtSheet.getTotalMileage() ));

            sendHighMileage_Warning();  }
    }

    private void updateTotalMileage() throws IOException, GeneralSecurityException
    { sbmtSheet.updateSheet( "sbMileage!C1", String.valueOf(sbmtSheet.getTotalMileage() )); }

    private void sendWarningToUserAsText( String phoneNumber, String carrier ) throws IOException, MessagingException, GeneralSecurityException
    { sendWarning.sendNotificationAsTextMSG( phoneNumber, carrier ); }

    private void sendWarningToUserAsEmail( String email ) throws IOException, MessagingException, GeneralSecurityException
    { sendWarning.sendNotificationAsEmail( email ); }

    private void sendHighMileage_Warning() throws IOException, GeneralSecurityException, MessagingException {
        if (!sbmtSheet.getUserPhoneNum().equals( "empty" ) && !sbmtSheet.getUserCarrier().equals( "empty" ))
        { sendWarningToUserAsText( sbmtSheet.getUserPhoneNum(), sbmtSheet.getUserCarrier()); }
        if (!sbmtSheet.getUserEmail().equals( "empty" ))
        { sendWarningToUserAsEmail( sbmtSheet.getUserEmail() ); }
    }
}