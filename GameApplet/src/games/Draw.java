package games;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

////////////////////////////////////////////////////////////////
//
//Draw
//draws object for example: tetris bricks and game board
//
////////////////////////////////////////////////////////////////
public class Draw {

	private boolean bImage;
	private int cutOff;
	private Graphics2D g2d;
	
	Draw() {
	}
	public Draw(Graphics2D g2d) {
		this.g2d = g2d;
	}

	public void setCutOff(int harizontal_start) {
		cutOff = harizontal_start;
	}

	public void showAllImage(boolean bImage) {
		this.bImage = bImage;
	}

	public void setBackground(GameObject gameObject, Color hue) {
		int x = gameObject.getOutlineX();
		int y = gameObject.getOutlineY();

		int blockWidth = gameObject.getOutlineWidth();
		int blockHeight = gameObject.getOutlineHeight();

		g2d.setColor(hue);
		g2d.fillRoundRect(x, y, blockWidth, blockHeight, 30, 30);
	}

	public void displayCheckerImage(GameObject gameObject) {
		int locate_X = gameObject.getBlockPosX();
		int locate_Y = gameObject.getBlockPosY();

		int blockHeight = gameObject.getBlockHeight();
		int blockWidth = gameObject.getBlockWidth();

		int blockSize = gameObject.getBlockSize();
		boolean bflip = true;

		Color blockHue1 = new Color(22, 22, 22);
		Color blockHue2 = new Color(37, 37, 37);
		Color blockLineHue = new Color(0, 3, 0);

		for (int y = 0; y < blockHeight; y++) {
			for (int x = 0; x < blockWidth; x++) {

				if (bflip)
					g2d.setColor(blockHue1);
				else
					g2d.setColor(blockHue2);

				g2d.fillRect(locate_X, locate_Y, blockSize, blockSize);
				g2d.setColor(blockLineHue);
				g2d.drawRect(locate_X, locate_Y, blockSize, blockSize);

				locate_X += blockSize;
				bflip = !bflip;
			}
			bflip = !bflip;
			locate_X = gameObject.getBlockPosX();
			locate_Y += blockSize;
		}
	}

	public void display(GameObject gameObject) {
		int locate_X = gameObject.getBlockPosX();
		int locate_Y = gameObject.getBlockPosY();

		int blockHeight = gameObject.getBlockHeight();
		int blockWidth = gameObject.getBlockWidth();

		int blockSize = gameObject.getBlockSize();
		int blockIndex = 0;
		int index = 0;

		for (int y = 0; y < blockHeight; y++) {
			for (int x = 0; x < blockWidth; x++) {

				blockIndex = gameObject.getBlockIndex(index); // get tetris
																// brick index

				//
				// if tetris brick index is
				// a negative value change
				// it to positive value
				//
				if (bImage && blockIndex < 0)
					blockIndex *= -1;

				if (blockIndex > 0)

					//
					// if parts of tetris brick is
					// off the game board then
					// do not draw it
					//
					if (locate_Y >= cutOff)

						//
						// draw tetris brick piece
						//
						displayImage(locate_X, locate_Y, blockSize,
								blockIndex);

				locate_X += blockSize;
				index++; // next tetris brick piece
			}

			locate_X = gameObject.getBlockPosX();
			locate_Y += blockSize;
		}
	}

	public void display(GameObject gameObject,
			BufferedImage[] image) {

		int locate_X = gameObject.getBlockPosX();
		int locate_Y = gameObject.getBlockPosY();

		int blockHeight = gameObject.getBlockHeight();
		int blockWidth = gameObject.getBlockWidth();

		int blockSize = gameObject.getBlockSize();
		int blockIndex = 0;
		int index = 0;

		for (int y = 0; y < blockHeight; y++) {
			for (int x = 0; x < blockWidth; x++) {
				blockIndex = gameObject.getBlockIndex(index); // get tetris
																// brick index

				//
				// if tetris brick index is
				// a negative value change
				// it to positive value
				//
				if (bImage && blockIndex < 0)
					blockIndex *= -1;

				if (blockIndex > 0)

					//
					// if parts of tetris brick is
					// off the game board then
					// do not draw it
					//
					if (locate_Y >= cutOff)

						//
						// draw tetris brick piece
						//
						g2d.drawImage(image[blockIndex - 1], locate_X, locate_Y,
								blockSize, blockSize, null);

				locate_X += blockSize;
				index++;
			}
			locate_X = gameObject.getBlockPosX();
			locate_Y += blockSize;
		}
	}
	
	public void displayOutline(GameObject gameObject) {

		int x = gameObject.getOutlineX();
		int y = gameObject.getOutlineY();

		int blockWidth = gameObject.getOutlineWidth();
		int blockHeight = gameObject.getOutlineHeight();

		g2d.drawRect(x, y, blockWidth, blockHeight);

	}

	private void displayImage(int locate_X, int locate_Y,
			int size, int index) {

		switch (index) {
		case 1:
			g2d.setColor(new Color(31, 143, 225));
			break;
		case 2:
			g2d.setColor(new Color(252, 191, 16));
			break;
		case 3:
			g2d.setColor(new Color(48, 47, 193));
			;
			break;
		case 4:
			g2d.setColor(new Color(247, 105, 31));
			break;
		case 5:
			g2d.setColor(new Color(144, 49, 147));
			break;
		case 6:
			g2d.setColor(new Color(42, 166, 59));
			break;
		case 7:
			g2d.setColor(new Color(230, 0, 28));
			break;
		}

		g2d.fillRoundRect(locate_X, locate_Y, size, size, 10, 10);
		g2d.setColor(Color.BLACK);
		g2d.drawRoundRect(locate_X, locate_Y, size, size, 10, 10);

	}

}
