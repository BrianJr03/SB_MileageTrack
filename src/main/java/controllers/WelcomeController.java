package controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import music.Music;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {

    public AnchorPane rootPane;
    Music bgMusic = new Music( "src/main/resources/music/The Deli.wav" );

    public WelcomeController() throws UnsupportedAudioFileException, IOException, LineUnavailableException {}

    @Override
    public void initialize( URL location , ResourceBundle resources ) {
        bgMusic.play();
        new WelcomeScreen().start();
    }

    class WelcomeScreen extends Thread {
        @Override
        public void run() { try {
                Thread.sleep( 905 );
                Platform.runLater( () -> {
                    Parent root = null;
                    try { root = FXMLLoader.load(getClass().getResource("/ui/main.fxml"));
                    } catch ( IOException exception )
                    { exception.printStackTrace(); }
                    rootPane.getChildren().setAll( root ); } );
            } catch ( InterruptedException e )
            { e.printStackTrace(); }
        }
    }
}
