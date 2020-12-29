import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
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
import com.google.api.services.sheets.v4.model.ValueRange;
import org.decimal4j.util.DoubleRounder;

public class SheetsAndJava {

    private final static String APPLICATION_NAME = "SB Mileage Track";
    private final static String SPREADSHEET_ID= "1IbU92yUWtT9w_kG3iCt8HTw5rmYbwgpwHPE6TUPVXIg";

    public static void main( String[] args ) throws IOException, GeneralSecurityException {
        addAllMileage( readSheetData() );
    }

    private static Credential auth() throws IOException, GeneralSecurityException {
        InputStream in = SheetsAndJava.class.getResourceAsStream( "/json/credentials.json" );
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load( JacksonFactory.getDefaultInstance(),
                new InputStreamReader( in ) );
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

    public static List<List<Object>> readSheetData() throws IOException, GeneralSecurityException {
        Sheets sheetService = getSheetService();
        String range = "sbMileage";
        ValueRange response = sheetService.spreadsheets().values().get( SPREADSHEET_ID, range ).execute();
        return response.getValues();
    }

    public static void print_MileageSpreadsheet(List<Object> row) {
        System.out.format( "\nDate Recorded : %s\nMileage : %s\n", row.get( 0 ), row.get( 1 ) );
    }

    public static void addAllMileage(List<List<Object>> values) {
        if(values == null || values.isEmpty()){
            System.out.println("No data found.");
        } else {
            double mileageTotal = 0.0;
            for (List<Object> row : values) {
                print_MileageSpreadsheet( row );
                mileageTotal += Double.parseDouble( row.get( 1 ).toString() );
            }
            System.out.println("\nTotal Miles : " + DoubleRounder.round(mileageTotal, 2));
        }
    }
}