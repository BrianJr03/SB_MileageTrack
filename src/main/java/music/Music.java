package music;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Music {

    Clip clip;
    public String clipStatus;

    public Music(String songPath) throws UnsupportedAudioFileException, IOException,
            LineUnavailableException {
        AudioInputStream audioInputStream =
                AudioSystem.getAudioInputStream( new File( songPath ).getAbsoluteFile() );
        clip = AudioSystem.getClip();
        clip.open( audioInputStream );
    }

    public void play(){
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        this.clipStatus = "playing";
    }
}
