package games;

import java.util.Random;

public class GameBlock extends GameObject {
	private static final int AMOUNT_OF_BLOCKS = 6;
	private static final int ROTATE_AMOUNT = 3;

	private static final int BLOCK_WIDTH = 4;
	private static final int BLOCK_HEIGHT = 4;

	private static final int BLOCK_SIZE = 25;

	private int blockType;
	private int blockRotate;
	private int blockStartPosX;
	private int blockStartPosY;

	public GameBlock() {

		setBlockDimension();
		setBlockLocation();
		setBlockIndex(BlockData.getBlock(blockType, blockRotate));

	}

	public int getBlockType() {
		return blockType;
	}

	public void getBlockType(int blockType) {
		this.blockType = blockType;
		getNextBlock();

	}

	public int getBlockRotate() {
		return blockRotate;
	}

	public void setBlockRotate(int blockRotate) {
		this.blockRotate = blockRotate;
		getNextBlock();
	}

	public void setNextBlock() {
		Random randomizer = new Random();
		blockType = randomizer.nextInt(AMOUNT_OF_BLOCKS + 1);
		blockRotate = 0;
	}

	public void getNextBlock() {

		setBlockIndex(BlockData.getBlock(blockType, blockRotate));
		setBlockPos();
	}

	public void resetBlock() {
		setBlockPosX(blockStartPosX);
		setBlockPosY(blockStartPosY);
		blockRotate = 0;
	}

	public void rotateBlock() {
		blockRotate = (blockRotate < ROTATE_AMOUNT) ? ++blockRotate : 0;
		getNextBlock();
	}

	public void moveBlockRight() {
		setBlockPosX(getBlockPosX() + BLOCK_SIZE);
		setBlockPos();

	}

	public void moveBlockLeft() {
		setBlockPosX(getBlockPosX() - BLOCK_SIZE);
		setBlockPos();

	}

	public void moveBlockDown() {
		setBlockPosY(getBlockPosY() + BLOCK_SIZE);
		setBlockPos();

	}

	public void setBlockPosition(int x, int y) {
		blockStartPosX = x;
		blockStartPosY = y;
		setBlockPosX(blockStartPosX);
		setBlockPosY(blockStartPosY);
	}

	public void setBlockDimension() {
		setBlockHeight(BLOCK_HEIGHT);
		setBlockWidth(BLOCK_WIDTH);
		setBlockSize(BLOCK_SIZE);

	}
}
