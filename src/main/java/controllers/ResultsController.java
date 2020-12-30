package controllers;

import googleSheet.SendWarning;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import googleSheet.SBMT_Sheet;
import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class ResultsController {

    @FXML
    private AnchorPane rootPane;
    @FXML @SuppressWarnings( "unused" )
    private TableView<String> mileageInfo_Table;
    @FXML @SuppressWarnings( "unused" )
    private TableColumn<String, String> dateColumn;
    @FXML @SuppressWarnings( "unused" )
    private TableColumn<String, String> mileColumn;
    @FXML
    private Label date_Label;
    @FXML
    private Label totalMileage_Label;
    @FXML
    private Label mileAVG_Label;

    SBMT_Sheet sbmtSheet = new SBMT_Sheet();
    SendWarning sendWarning = new SendWarning();
    String mileWarningCounter = sbmtSheet.getMileageWarningCount();
    double mileWarningCounterAsDouble = Double.parseDouble( mileWarningCounter );

    public ResultsController() throws IOException, GeneralSecurityException {}

    public void initialize() throws IOException, GeneralSecurityException, MessagingException {
        setDate_Label();
        setMileAVG_Label();
        updateTotalMileage();
        checkFor_HighMileage();
        setTotalMileage_Label();
    }

    public void launchUI(String uiPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(uiPath));
        Parent root = loader.load();
        rootPane.getChildren().setAll( root );
    }

    public void setMileAVG_Label() throws IOException, GeneralSecurityException {
        if ( sbmtSheet.getSheetData().size() < 10) { mileAVG_Label.setText( "N/A" ); }
        else mileAVG_Label.setText( String.valueOf( sbmtSheet.getLastTenEntries_MileAvg())); }

    public void setTotalMileage_Label()
    { totalMileage_Label.setText( String.valueOf( sbmtSheet.getTotalMileage())); }

    public void setDate_Label() throws IOException, GeneralSecurityException
    { date_Label.setText( sbmtSheet.getEntryDates_AsObservableList().get( 1 )); }

    public void launchResultsUI() throws IOException
    { launchUI( "/ui/main.fxml" ); }

    public void checkFor_HighMileage() throws IOException, MessagingException, GeneralSecurityException {
        double totalMileage = sbmtSheet.getTotalMileage();
        if ( totalMileage >= this.mileWarningCounterAsDouble ) {
            sbmtSheet.updateSheet( "sbMileage!A1", String.valueOf( mileWarningCounterAsDouble + 250 ));
            sendWarningToUser();
        }
    }

    public void updateTotalMileage() throws IOException, GeneralSecurityException
    { sbmtSheet.updateSheet( "sbMileage!C1", String.valueOf(sbmtSheet.getTotalMileage() )); }

    public void sendWarningToUser() throws IOException, MessagingException
    { sendWarning.sendNotificationAsTextMSG( "219-359-6331", "Sprint" ); }
}