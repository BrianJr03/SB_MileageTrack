
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
}