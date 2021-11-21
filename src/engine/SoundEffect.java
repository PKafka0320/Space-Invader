package engine;

import javax.sound.sampled.*;

public enum SoundEffect {
    MAIN("/src/Sound/maintheme.wav"),
    STAGE2("/src/Sound/stage2.wav"),
    STAGE3("/src/Sound/stage3.wav"),
    ENEMY_DEAD("/src/Sound/sounds_effects_enemydead.wav"),
    GOT_ITEM("/src/Sound/sounds_effects_gotitem.wav"),
    BOSS("/src/Sound/boss.wav");

    private Clip clip;

    SoundEffect(String filename) {
        try {
            URL url = this.getClass.getResource(filename);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play(Boolean loop) {
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
}
