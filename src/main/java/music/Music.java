package music;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

@SuppressWarnings( "unused" )
public class Music {

    private final Clip clip;
    public String clipStatus;

    public Music(String songPath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                new File( songPath ).getAbsoluteFile() );
        clip = AudioSystem.getClip();
        clip.open( audioInputStream );
        FloatControl control = ( FloatControl ) clip.getControl( FloatControl.Type.MASTER_GAIN );
        control.setValue( -20.0f );
    }

    public void play() {
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        this.clipStatus = "playing";
    }
}
