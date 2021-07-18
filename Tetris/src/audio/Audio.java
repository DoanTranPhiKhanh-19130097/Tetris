package audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Audio {
	private Clip clip;

	public Audio(String fileName) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		AudioInputStream audio = AudioSystem.getAudioInputStream(new File(fileName));
		clip = AudioSystem.getClip();
		clip.open(audio);
	}

	// phat nhac
	public void start() {
		if (!clip.isRunning())
			clip.start();
	}

	// dung nhac
	public void stop() {
		if (clip.isRunning())
			clip.stop();
	}

	// vong lap
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	// thay doi am luong
	public void changVolume(int value) {
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue((float) ((value / 100.0f * 80) - 80));
	}
}
