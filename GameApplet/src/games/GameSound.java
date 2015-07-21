package games;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * 
 * GameSound program get sounds from /sounds/ directory
 *
 */

public class GameSound {

	private boolean soundOn;

	public GameSound() {
	}

	public boolean isSoundOn() {
		return soundOn;
	}

	public void setSoundOn(boolean soundOn) {
		this.soundOn = soundOn;
	}

	public void setSoundOnOff() {
		this.soundOn = !soundOn;
	}

	public void playInstantDrop() {
		playSound(0);
	}

	public void playRemoveBlocks() {
		playSound(1);
	}

	public void playRotateBlock() {
		playSound(2);
	}
	public void playEndGame() {
		playSound(3);
	}
	public void playSound(int index) {
		String[] filename = { "instant_drop.wav", "remove_blocks.wav",
				"rotate.wav", "sound8.wav" };

		if (soundOn)
			loadGameSound(filename[index]);
	}

	public void loadGameSound(String filename) {

		try  {

			Clip clip = AudioSystem.getClip();

			AudioInputStream inputStream = AudioSystem
					.getAudioInputStream(Tetris.class.getResource("/sounds/"
							+ filename));
			clip.open(inputStream);
			clip.start();
				

		} catch (Exception ex) {
			System.out.println("can't find " + filename + " file");
		}

	}

}
