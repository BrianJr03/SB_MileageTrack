
import googleSheet.SendWarning;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class GenericTestClass {

    @Test
    public void testReplace() {
        ObservableList<String> a = FXCollections.observableArrayList();
        ObservableList<String> b = FXCollections.observableArrayList();
        a.add( "@" );
        for ( String entry : a )
             { b.add( entry.replace( "@", "||" ) ); }
        Assertions.assertEquals( b.get( 0 ), "||" );
        System.out.println("\nOriginal: " + a.get( 0 ));
        System.out.println("After Replacement: " + b.get( 0 ));
    }

    @Test
    public void sendWheelWarning() throws IOException, GeneralSecurityException, MessagingException {
        SendWarning sendWarning = new SendWarning(); sendWarning.writeWheelNotification();
        sendWarning.sendNotificationAsEmail( "bkwalker@bsu.edu" );
        sendWarning.sendNotificationAsTextMSG( "219-359-6331", "Sprint" );
    }

    @Test
    public void getLastElement() {
        ObservableList<String> listOfStrings = FXCollections.observableArrayList();
        int count = 0;
        System.out.print("\n");
        for ( int i = 0; i < 5; i++ )
        { count++;listOfStrings.add( "Element " + ( i+1 )); }
        String lastEntry = listOfStrings.get( listOfStrings.size() - 1 );
        for ( String element : listOfStrings )
        { System.out.println( element ); }
        System.out.println("\nLast Element: " + lastEntry);
        Assertions.assertEquals( "Element " + count, lastEntry );
    }
}