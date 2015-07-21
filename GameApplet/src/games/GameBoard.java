package games;

public class GameBoard extends GameObject {
	private static final int BOARD_WIDTH = 10;
	private static final int BOARD_HEIGHT = 20;
	private static final int BOARD_POS_X = 125;
	private static final int BOARD_POS_Y = 75;
	private static final int BLOCK_SIZE = 25;

	public GameBoard() {
		setBoard();
	}

	public void setBoard() {
		int[] boardGrid = new int[BOARD_WIDTH * BOARD_HEIGHT];
		setObject(boardGrid, BOARD_WIDTH, BOARD_HEIGHT, BLOCK_SIZE);
		setBlockPosition(BOARD_POS_X, BOARD_POS_Y);
	}
	
	public boolean isBlockInsideBoard(GameBlock gameBlock) {
		return (inBound(gameBlock) && !hasObject(gameBlock));
	}
	
	public void superDrop(GameBlock gameBlock) {
		int tempY = 0;

		while (isBlockInsideBoard(gameBlock)) {
			tempY = gameBlock.getBlockPosY();
			gameBlock.moveBlockDown();
		}
		gameBlock.setBlockPosY(tempY);
		gameBlock.setBlockPos();
	}
	
}
