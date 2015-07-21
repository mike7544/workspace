package games;

public class Game {

	public static GameBlock block() {
		return new GameBlock();
	}

	public static GameBoard board() {
		return new GameBoard();
	}

	public static GameControls controls() {
		return new GameControls();
	}

	public static GameImage image() {
		return new GameImage();
	}

	public static GameSound sound() {
		return new GameSound();
	}

	public static GameStats stats() {
		return new GameStats();
	}

}
