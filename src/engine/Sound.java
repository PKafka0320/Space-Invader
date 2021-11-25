package engine;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

public class Sound {
    // constructor
    public static final String MAIN = "src/Sound/maintheme.wav";
    public static final String STAGE2 = "src/Sound/stage2.wav";
    public static final String STAGE3 = "src/Sound/stage3.wav";
    public static final String GAMEOVER = "src/Sound/sounds_effects_gameover.wav";
    public static final String ENEMY_DEAD = "src/Sound/sounds_effects_enemydead.wav";
    public static final String GOT_ITEM = "src/Sound/sounds_effects_gotitem.wav";
    public static final String BOSS = "src/Sound/boss.wav";

    private Clip clip;
    private static Map<String, Clip> soundMap = new HashMap<>();
    private static String[] str = new String[7];

    static{
        str[0] = MAIN;
        str[1] = STAGE2;
        str[2] = STAGE3;
        str[3] = GAMEOVER;
        str[4] = ENEMY_DEAD;
        str[5] = GOT_ITEM;
        str[6] = BOSS;
        for(String s : str) {
            try{
                Clip soundClip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(s));
                soundClip.open(inputStream);
                soundMap.put(s,soundClip);

            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            }
        }

    }

    public Sound(String sound) {
        clip = soundMap.get(sound);
    }

    public void playMusic(Boolean loop) {
        if (clip.isRunning())
            clip.stop();
        clip.setFramePosition(0);
        clip.start(); // actually start playing here
        if (loop)
            clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
        clip.setFramePosition(0);
    }

    public float getVolume() {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    public void setVolume(float volume) {
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }

    public Clip getClip() {
        return clip;
    }


}
