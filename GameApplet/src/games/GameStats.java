package games;

public class GameStats {
	private static final int GAME_POINTS = 250;
	private static final int GAME_MOVE_UP_LEVEL = 20;

	private int totalScore;
	private int countLines;
	private int totalLines;
	private int gameLevel;

	public GameStats() {
		setStats();
	}

	public int getTotalScore() {
		return totalScore;
	}
	public void addLines() {
		countLines ++;
	}
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
	
	public int getCountLines() {
		return countLines;
	}

	public void setCountLines(int countLines) {
		this.countLines = countLines;
	}

	public int getTotalLines() {
		return totalLines;
	}

	public void setTotalLines(int totalLines) {
		this.totalLines = totalLines;
	}
	public void addGameLevel() {
		gameLevel ++;
	}
	public int getGameLevel() {
		return gameLevel;
	}

	public void setGameLevel(int gameLevel) {
		this.gameLevel = gameLevel;
	}
	public void setStats() {
		totalScore = 0;
		totalLines = 0;
		countLines = 0;
		gameLevel = 1;
	}
	public void setGamePoints() {
		if (countLines > 0) {
			totalScore += countLines * GAME_POINTS;
			totalLines += countLines;
			countLines = 0;
		}
		changeLevel();
	}
	public void changeLevel() {
		gameLevel = (totalLines / GAME_MOVE_UP_LEVEL) + 1;
	}
}
