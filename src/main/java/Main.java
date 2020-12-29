import util.SheetsAndJava;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class Main {

    static SheetsAndJava sheetsAndJava = new SheetsAndJava();

    public static void main( String[] args ) throws IOException, GeneralSecurityException {
        List<List<Object>> sheet = sheetsAndJava.getSheetData();
        sheetsAndJava.printSheet(sheet);
        System.out.print("\nMile average for the last 10 entries: " +
                sheetsAndJava.findTotalMileage(sheet) + "\n");
    }
}
