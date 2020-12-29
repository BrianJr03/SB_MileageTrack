package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
import org.decimal4j.util.DoubleRounder;
import org.joda.time.DateTime;

@SuppressWarnings( "unused" )
public class SheetsAndJava {

    private Sheets sheetService;
    public final String currentDate = DateTime.now().toLocalDate().toString();
    private final static String APPLICATION_NAME = "SB Mileage Track";
    private final static String SPREADSHEET_ID= "1IbU92yUWtT9w_kG3iCt8HTw5rmYbwgpwHPE6TUPVXIg";

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
        return new AuthorizationCodeInstalledApp( flow, new LocalServerReceiver() ).authorize( "user" );
    }

    public static Sheets getSheetService() throws IOException, GeneralSecurityException {
        Credential credential = auth();
        return new Sheets.Builder( GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
                credential ).setApplicationName( APPLICATION_NAME ).build();
    }

    public List<List<Object>> getSheetData() throws IOException, GeneralSecurityException {
        sheetService = getSheetService();
        String range = "sbMileage";
        ValueRange response = sheetService.spreadsheets().values().get( SPREADSHEET_ID, range ).execute();
        return response.getValues();
    }

    private ObservableList<String> getSheetDataAsObservableList(int columnIndex) throws IOException, GeneralSecurityException {
        ObservableList<String> sheetDataAsObservableList = FXCollections.observableArrayList();
        List<List<Object>> sheet = getSheetData();
        for ( List<Object> row : sheet ) {
            sheetDataAsObservableList.add( row.get( columnIndex ).toString() );
        }
        return sheetDataAsObservableList; }

    public ObservableList<String> getEntryDates_AsObservableList() throws IOException, GeneralSecurityException {
       return getSheetDataAsObservableList( 0 );
    }

    public ObservableList<String> getEntryMiles_AsObservableList() throws IOException, GeneralSecurityException {
        return getSheetDataAsObservableList( 1 );
    }

    public void addEntryToSheet( String miles ) throws IOException, GeneralSecurityException {
        sheetService = getSheetService();
        ValueRange appendBody = new ValueRange()
                .setValues( Collections.singletonList( Arrays.asList( currentDate , miles ) ) );
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
        UpdateValuesResponse result = sheetService.spreadsheets().values()
                .update( SPREADSHEET_ID, range, body )
                .setValueInputOption( "RAW" )
                .execute();
    }

    public void printSheet() throws IOException, GeneralSecurityException {
        int entryCount = 0;
        List<List<Object>> sheet = getSheetData();
        for (List<Object> row : sheet) {
            entryCount++;
            System.out.format( "\n%d. Date Recorded : %s\nMileage : %s\n", entryCount, row.get( 0 ), row.get( 1 ) );
        }
    }

    public double findLastTenEntries_MileAvg() throws IOException, GeneralSecurityException {
        double mileCount = 0.0;
        List<List<Object>> sheet = getSheetData();
        ArrayList<List<Object>> previousEntries = new ArrayList <>();
        for ( List<Object> row : Reversed.reversed( getSheetData() )) {
            previousEntries.add( row );
            if (previousEntries.size() + 1 > 10)
                break; }
        for ( List<Object> row : previousEntries )
        { mileCount += Double.parseDouble( row.get( 1 ).toString() ); }
        return DoubleRounder.round((mileCount / 10), 2);
    }

    public double getLastTenEntries_MileAvg() throws IOException, GeneralSecurityException
    { return findLastTenEntries_MileAvg(); }

    public double findTotalMileage( List<List<Object>> values) {
        double mileageTotal = 0.0;
        if(values == null || values.isEmpty())
            { System.out.println("No data found."); }
        else { for (List<Object> row : values)
            { mileageTotal += Double.parseDouble( row.get( 1 ).toString() ); }
        }
        return DoubleRounder.round(mileageTotal, 2);
    }
}