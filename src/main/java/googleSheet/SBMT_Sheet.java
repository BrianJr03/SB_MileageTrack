package googleSheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.decimal4j.util.DoubleRounder;

public class SBMT_Sheet {

    private Sheets sheetService;
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy @ h.mm aa");
    private final String currentDate = dateFormat.format( new Date() );
    private final static String APPLICATION_NAME = "SB Mileage Track";
    private final static String SPREADSHEET_ID= "1IbU92yUWtT9w_kG3iCt8HTw5rmYbwgpwHPE6TUPVXIg";
    private final List<List<Object>> sheet = getSheetData();

    final File blueMtn_bkGrnd_File = new File("src/main/resources/png/blueMtn.png");
    final File purple_bkGrnd_File = new File("src/main/resources/png/purpleAndBlue.png");
    final Image blueMtn_bkGrnd = new  Image(blueMtn_bkGrnd_File.toURI().toString());
    final Image purpleAndBlue_bkGrnd = new Image(purple_bkGrnd_File.toURI().toString());
    ArrayList< Image > bkGrndImages = new ArrayList <>();

    public SBMT_Sheet() throws IOException, GeneralSecurityException { populateBkGrndImages_Array(); }

    private static Credential auth() throws IOException, GeneralSecurityException {
        final java.util.logging.Logger buggyLogger = java.util.logging.Logger.getLogger(FileDataStoreFactory.class.getName());
        buggyLogger.setLevel(java.util.logging.Level.SEVERE);
        InputStreamReader in = new InputStreamReader(new FileInputStream("src/main/resources/json/credentials.json"));
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load( JacksonFactory.getDefaultInstance(), in);
        List<String> scopes = Collections.singletonList( SheetsScopes.SPREADSHEETS );
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder( GoogleNetHttpTransport.newTrustedTransport(),
                        JacksonFactory.getDefaultInstance(), clientSecrets, scopes)
                .setDataStoreFactory( new FileDataStoreFactory( new java.io.File("tokens") ) )
                .setAccessType( "offline" )
                .build();
        return new AuthorizationCodeInstalledApp( flow, new LocalServerReceiver()).authorize( "user" );
    }

    public static Sheets getSheetService() throws IOException, GeneralSecurityException {
        Credential credential = auth();
        return new Sheets.Builder( GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
                credential ).setApplicationName( APPLICATION_NAME ).build();
    }

    public List<List<Object>> getSheetData() throws IOException, GeneralSecurityException {
        sheetService = getSheetService(); String range = "sbMileage";
        ValueRange response = sheetService.spreadsheets().values().get( SPREADSHEET_ID, range ).execute();
        return response.getValues();
    }

    private ObservableList<String> getSheetData_AsObservableList( int index ) throws IOException, GeneralSecurityException {
        ObservableList<String> sheetDataAsObservableList = FXCollections.observableArrayList();
        List<List<Object>> sheet = getSheetData();
        for ( List<Object> row : sheet )
        { sheetDataAsObservableList.add( row.get( index ).toString() ); }
        return sheetDataAsObservableList;
    }

    private ObservableList<String> getSheetDataCol1_AsObservableList() throws IOException, GeneralSecurityException
    { return getSheetData_AsObservableList( 0 ); }

    private ObservableList<String> getSheetDataCol2_AsObservableList() throws IOException, GeneralSecurityException
    { return getSheetData_AsObservableList( 1 ); }

    public void addEntryToSheet(String miles) throws IOException, GeneralSecurityException {
        sheetService = getSheetService();
        ValueRange appendBody = new ValueRange()
                .setValues( Collections.singletonList( Arrays.asList( currentDate , miles ) ) );
        @SuppressWarnings( "unused" )
        AppendValuesResponse appendResult = sheetService.spreadsheets().values()
                .append( SPREADSHEET_ID, "sbMileage", appendBody )
                .setValueInputOption( "USER_ENTERED" )
                .setInsertDataOption( "INSERT_ROWS" )
                .setIncludeValuesInResponse( true )
                .execute();
    }

    public void updateSheet(String range, String valueToAdd) throws IOException, GeneralSecurityException {
        sheetService = getSheetService();
        ValueRange body = new ValueRange()
                .setValues( Collections.singletonList( Collections.singletonList( valueToAdd ) ) );
        @SuppressWarnings( "unused" )
        UpdateValuesResponse result = sheetService.spreadsheets().values()
                .update( SPREADSHEET_ID, range, body )
                .setValueInputOption( "RAW" )
                .execute();
    }

    public void resetSheetFromIndex( int rowIndex ) throws IOException, GeneralSecurityException {
        sheetService = getSheetService();
        DeleteDimensionRequest deleteRequest = new DeleteDimensionRequest()
                .setRange(
                        new DimensionRange()
                        .setSheetId( 0 )
                        .setDimension( "ROWS" )
                        .setStartIndex( rowIndex )
                );
        List<Request> requests = new ArrayList <>();
        requests.add( new Request().setDeleteDimension( deleteRequest ) );
        BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests( requests );
        sheetService.spreadsheets().batchUpdate( SPREADSHEET_ID, body ).execute();
    }

    public void removePlaceHolderRows( ObservableList<SheetEntry> sheetEntries )
    { sheetEntries.subList( 0 , 5 ).clear(); }

    public double getLastTenEntries_MileAvg()
    { return findLastTenEntries_MileAvg(); }

    public double get_MileAvg()
    { return findCurrent_MileAVG(); }

    private double findCurrent_MileAVG() {
        double mileCount = 0.0;
        ArrayList < List < Object > > previousEntries = new ArrayList <>( sheet );
        previousEntries.subList( 0, 5 ).clear();
        for ( List<Object> row : previousEntries )
        { mileCount += Double.parseDouble( row.get( 1 ).toString() ); }
        return DoubleRounder.round((mileCount / previousEntries.size()), 2);
    }

    private double findLastTenEntries_MileAvg() {
        double mileCount = 0.0;
        ArrayList<List<Object>> previousEntries = new ArrayList <>();
        for ( List<Object> row : Reversed.reversed( sheet )) {
            previousEntries.add( row );
            if (previousEntries.size() + 1 > 10)
                break; }
        for ( List<Object> row :  previousEntries  )
        { mileCount += Double.parseDouble( row.get( 1 ).toString() ); }
        return DoubleRounder.round((mileCount / 10), 2);
    }

    public double getTotalMileage() {
        double mileageTotal = 0.0;
        if( sheet == null || sheet.isEmpty() )
            { return Double.parseDouble( "No data found." ); }
        else { for (List<Object> row : sheet)
                 mileageTotal += Double.parseDouble( row.get( 1 ).toString() ); }
        return DoubleRounder.round(mileageTotal, 2);
    }

    public ObservableList<String> getEntryDates_AsObservableList() throws IOException, GeneralSecurityException
    { return getSheetDataCol1_AsObservableList(); }

    public String getMileageWarningThreshold() throws IOException, GeneralSecurityException
    { return getSheetDataCol1_AsObservableList().get( 0 ); }

    public String getUserPhoneNum() throws IOException, GeneralSecurityException
    { return getEntryDates_AsObservableList().get( 1 ); }

    public String getUserEmail() throws IOException, GeneralSecurityException
    { return getEntryDates_AsObservableList().get( 2 ); }

    public String getUserCarrier() throws IOException, GeneralSecurityException
    { return getEntryDates_AsObservableList().get( 3 ); }

    public String getStored_MileageWarningThreshold() throws IOException, GeneralSecurityException
    { return getEntryDates_AsObservableList().get( 4 ); }

    public String getBkGrndIndex() throws IOException, GeneralSecurityException
    { return getSheetDataCol2_AsObservableList().subList( 0, 1 ).get( 0 ); }

    public String getStartDate() throws IOException, GeneralSecurityException {
        String startDateAndTime = getEntryDates_AsObservableList().get( 5 );
        return startDateAndTime.substring(0, Math.min(startDateAndTime.length(), 10));
    }

    public void resetSheet() throws IOException, GeneralSecurityException {
        updateSheet( "sbMileage!A1", String.valueOf( 0 ) );
        updateSheet( "sbMileage!A2", "empty" );
        updateSheet( "sbMileage!A3", "empty" );
        updateSheet( "sbMileage!A4", "empty" );
        updateSheet( "sbMileage!A5", String.valueOf( 0 ) );
        updateSheet( "sbMileage!B1", String.valueOf( 0 ) );
        updateSheet( "sbMileage!B2", String.valueOf( 0 ) );
        updateSheet( "sbMileage!B3", String.valueOf( 0 ) );
        updateSheet( "sbMileage!B4", String.valueOf( 0 ) );
        updateSheet( "sbMileage!B5", String.valueOf( 0 ) );
        updateSheet( "sbMileage!C1", String.valueOf( 0 ) );
        if ( canSheetBeReset() )
             { resetSheetFromIndex( 5 ); }
    }

    public boolean isSheetCellEmpty( int index ) throws IOException, GeneralSecurityException {
        return !getEntryDates_AsObservableList().get( index ).equals( "empty" )
                &&  !getEntryDates_AsObservableList().get( index ).equals( String.valueOf( 0 ) );
    }

    public boolean canSheetBeReset() throws IOException, GeneralSecurityException {
        return getSheetData().size() > 5
                || !getMileageWarningThreshold().equals( String.valueOf( 0 ) )
                || !getUserPhoneNum().equals( "empty" )
                || !getUserEmail().equals( "empty" )
                || !getUserCarrier().equals( "empty" )
                || !getStored_MileageWarningThreshold().equals( String.valueOf( 0 ) );
    }

    private void populateBkGrndImages_Array()
    { bkGrndImages.add( blueMtn_bkGrnd ); bkGrndImages.add( purpleAndBlue_bkGrnd ); }

    public void setBkGrnd( ImageView bkGrnd ) throws IOException, GeneralSecurityException
    { bkGrnd.setImage( bkGrndImages.get( Integer.parseInt( getBkGrndIndex() ))); }

    public void verifyBkGrnd( ImageView bkGrnd_ImageView ) throws IOException, GeneralSecurityException {
        String bkGrnd_Index = getBkGrndIndex();
        if ( bkGrnd_Index.equals( "0" ) ) {
            bkGrnd_ImageView.setImage( purpleAndBlue_bkGrnd );
            updateSheet( "sbMileage!B1", "1" ); }
       else if ( bkGrnd_Index.equals( "1" ) ) {
            bkGrnd_ImageView.setImage( blueMtn_bkGrnd );
            updateSheet( "sbMileage!B1", "0" ); }
    }
}