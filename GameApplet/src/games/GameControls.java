package games;



public class GameControls {
	
	private static final int BLOCK_SPEED = 550;

	private boolean pauseGame;
	private boolean turnOnGrid;
	private boolean endGame;
	private boolean controlKeyBox;
	private boolean justStarted;
	private boolean quitGame;
	private boolean overrideCommand;
	private int gameSpeed;
	
	
	public GameControls() {
	}

	public void setControls() {
		gameSpeed = BLOCK_SPEED;
		pauseGame = false;
		endGame = false;
		controlKeyBox = false;

		turnOnGrid = false;
		justStarted = true;
		quitGame = false;

		overrideCommand = false;
	}
	public boolean isPauseGame() {
		return pauseGame;
	}

	public void setPauseGame(boolean pauseGame) {
		this.pauseGame = pauseGame;
	}
	public boolean flipOnGrid() {
		return turnOnGrid = !turnOnGrid;
	}
	public boolean isTurnOnGrid() {
		return turnOnGrid;
	}

	public void setTurnOnGrid(boolean turnOnGrid) {
		this.turnOnGrid = turnOnGrid;
	}

	public boolean isEndGame() {
		return endGame;
	}

	public void setEndGame(boolean endGame) {
		this.endGame = endGame;
	}

	public boolean flipControlKeyBox() {
		return controlKeyBox = !controlKeyBox;
	}
	public boolean isControlKeyBox() {
		return controlKeyBox;
	}

	public void setControlKeyBox(boolean controlKeyBox) {
		this.controlKeyBox = controlKeyBox;
	}

	public boolean isJustStarted() {
		return justStarted;
	}

	public void setJustStarted(boolean justStarted) {
		this.justStarted = justStarted;
	}

	public boolean isQuitGame() {
		return quitGame;
	}

	public void setQuitGame(boolean quitGame) {
		this.quitGame = quitGame;
	}

	public boolean isOverrideCommand() {
		return overrideCommand;
	}

	public void setOverrideCommand(boolean overrideCommand) {
		this.overrideCommand = overrideCommand;
	}


	public int getGameSpeed() {
		return gameSpeed;
	}

	public void setGameSpeed(int gameSpeed) {
		this.gameSpeed = gameSpeed;
	}

	
	
}
