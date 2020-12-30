package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import util.SheetsAndJava;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class ResultsController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<String> mileageInfo_Table;
    @FXML
    private TableColumn<String, String> dateColumn;
    @FXML
    private TableColumn<String, String> mileColumn;
    @FXML
    private Label date_Label;
    @FXML
    private Label totalMileage_Label;
    @FXML
    private Label mileAVG_Label;

    SheetsAndJava sheetsAndJava = new SheetsAndJava();
    List< List <Object> > sheet = sheetsAndJava.getSheetData();

    public ResultsController( ) throws IOException, GeneralSecurityException {}

    public void initialize() throws IOException, GeneralSecurityException {
        setDate_Label();
        setMileAVG_Label();
        setTotalMileage_Label();
    }

    public void launchUI(String uiPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(uiPath));
        Parent root = loader.load();
        rootPane.getChildren().setAll( root );
    }

    public void setMileAVG_Label() throws IOException, GeneralSecurityException
    { mileAVG_Label.setText( String.valueOf( sheetsAndJava.getLastTenEntries_MileAvg())); }

    public void setTotalMileage_Label() throws IOException, GeneralSecurityException
    { totalMileage_Label.setText( String.valueOf( sheetsAndJava.findTotalMileage())); }

    public void setDate_Label() throws IOException, GeneralSecurityException
    { date_Label.setText( sheetsAndJava.getEntryDates_AsObservableList().get( 0 )); }

    public void launchResultsUI() throws IOException
    { launchUI( "/ui/main.fxml" ); }
}