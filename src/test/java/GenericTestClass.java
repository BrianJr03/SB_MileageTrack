
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

public class GenericTestClass {

    public GenericTestClass() {}

    @Test
    public void testReplace() {
        ObservableList<String> a = FXCollections.observableArrayList();
        ObservableList<String> b = FXCollections.observableArrayList();
        for ( int i = 0; i < 10; i++ )
        { a.add( "@" ); }
        for ( String entry : a )
        { b.add( entry.replace( "@", "||" ) ); }
        System.out.println(a);
        System.out.println(b);
    }
}
