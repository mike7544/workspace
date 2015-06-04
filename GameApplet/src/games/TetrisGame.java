package games;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.Timer;


@SuppressWarnings("serial")
public class TetrisGame extends JApplet {
	
	private GameProgram gameApplet;
	
	public void init() {
		gameApplet = new GameProgram();
		
		this.setSize(700, 600);
		add(gameApplet);
	}
	
}

@SuppressWarnings("serial")
class GameProgram extends JPanel implements KeyListener, ActionListener {
	
	private final static int AMOUNT_OF_BLOCKS = 6;
	private final static int ROTATE_AMOUNT = 3;

	private final static int BLOCK_WIDTH = 4;
	private final static int BLOCK_HEIGHT = 4;
	
	private final static int BLOCK_POS_X = 175;
	private final static int BLOCK_POS_Y = 50;
	private final static int BLOCK_SIZE = 25;
	
	private final static int BOARD_WIDTH = 10;
	private final static int BOARD_HEIGHT = 20;
	private final static int BOARD_POS_X = 125;
	private final static int BOARD_POS_Y = 75;
	
	private final static int NEXT_BLOCK_POS_X = 475;
	private final static int NEXT_BLOCK_POS_Y = 125;
	private final static int MAX_WAIT_TIME = 1;
	
	private int gameSpeed;
	
	private int blockType; 
	private int nextBlockType;
	private int rotateBlock;
	private int blockLocateX;
	private int blockLocateY;

	private int countWaitTime;
	private int totalScore;
	private int gameLevel;
	private int countLines;
	private int totalLines;
	
	
	private Timer gameTimer;
	private boolean pauseGame;
	private boolean endGame;
	
	private GameObject tetrisBlock;
	private GameObject gameBoard;
	private GameObject nextBlock;
	
	private BufferedImage[] blockImage;
	private BufferedImage backgroundImage;

	public GameProgram() {
		tetrisBlock = new GameObject();
		gameBoard = new GameObject();
		nextBlock = new GameObject();
		blockImage = new BufferedImage[7];
		
		
		int[] boardGrid  = new int[BOARD_WIDTH * BOARD_HEIGHT];
		
		gameBoard.setObject(boardGrid,BOARD_WIDTH,BOARD_HEIGHT,BLOCK_SIZE);
		gameBoard.setPosition(BOARD_POS_X, BOARD_POS_Y);
		backgroundImage = getImage("background_01.jpg");
		loadGameImage();
		setNextBlock();
		setTetris();
		
		countWaitTime = 0;
		gameSpeed = 550;
		totalScore = gameLevel = 0;
		
		totalLines = countLines = 0;
		pauseGame = false;
		endGame = false;
		
		gameTimer = new Timer(gameSpeed,this);
		
		this.addKeyListener(this);
		this.setFocusable(true);
		this.requestFocus();
		
	}
	public BufferedImage getImage(String filename) {
		BufferedImage tempImage = null;
		
		String filePath = "/image/" + filename;
		
		try {
			tempImage = ImageIO.read(getClass().getResource(filePath));
		}
		
		catch (Exception ex) {
			System.out.println("file " + filePath + " not found");
		}
		
		return tempImage;
	}
	public void loadGameImage() {
		String[] pic = {"block_cyan.png","block_yellow.png","block_blue.png",
						"block_lightgreen.png","block_darkblue.png","block_green.png",
						"block_red.png"};
		
		for(int i = 0; i < pic.length; i++) 
			blockImage[i] = getImage(pic[i]);
		
	}
	@Override
	public void paintComponent(Graphics g) {
		this.paintComponents(g);
		Graphics2D g2d =  (Graphics2D) g;
		gameTimer.start();
		
		Draw draw = new Draw();
		
		updateTetris();
		setGameStatistics();
		setGameSpeed();
		
		g2d.drawImage(backgroundImage,0,0,getWidth(),getHeight(),null);
		
		g2d.fillRect(425,75, 175,150);
		draw.display(g2d, nextBlock, blockImage);
		
		draw.setBackground(g2d, gameBoard, Color.black);
		//draw.displayCheckerImage(g2d, gameBoard);
		
		draw.display(g2d, gameBoard, blockImage);
		
		draw.display(g2d, tetrisBlock, blockImage);
			
		g2d.fillRect(425,275, 175,300);

		printGameStatistics(g2d);
	
	}
	
	public void setGameStatistics() {
		final int POINTS = 375;
		final int MOVE_UP_LEVEL = 20;
		
		while(gameBoard.isRowFull()) {
			gameBoard.invertRow();
			gameBoard.dropDown();
			countLines ++;
		}
		if(countLines > 0) {
			totalScore += countLines * POINTS;
			totalLines += countLines;
			countLines = 0;
		}
		gameLevel = (totalLines / MOVE_UP_LEVEL) + 1;
	}
	
	public void printGameStatistics(Graphics g2d) {
		g2d.setColor(Color.white);
		
		if(endGame) {
			g2d.setFont(new Font("Courier", Font.BOLD,35));
			g2d.drawString("END GAME", 240, 300);

		}
		if(pauseGame) {
			g2d.setFont(new Font("Courier", Font.BOLD,35));
			g2d.drawString("PAUSE...", 240, 300);

		}
		g2d.setFont(new Font("Courier", Font.BOLD,20));

		g2d.drawString("NEXT", 475, 90);
		
		g2d.drawString("LEVEL", 475, 300);
		g2d.drawString(Integer.toString(gameLevel), 475, 325);
		
		g2d.drawString("LINES", 475, 400);
		g2d.drawString(Integer.toString(totalLines), 475, 425);

		g2d.drawString("SCORE", 475, 500);
		g2d.drawString(Integer.toString(totalScore), 475, 525);
		
		/*
		g2d.setFont(new Font("Dialog", Font.BOLD,15));
		g2d.drawString("up arrow   : rotate", 680, 300);
		g2d.drawString("right arrow: move right", 680, 330);
		g2d.drawString("left arrow : move left", 680, 360);
		g2d.drawString("down arrow : drop fast", 680, 390);
		g2d.drawString("spacebar   : instance drop", 680, 420);
		g2d.drawString("<escape> & <p> key: pause ", 680, 450);
		*/

	}
	
	public void addObjectToBackground() {
		gameBoard.addObject(tetrisBlock);
		setTetris();
	}
	public void updateTetris() {
		
		int[] nextGameBlock = GameBlocks.getBlock(blockType, rotateBlock);
		
		tetrisBlock.setObject(nextGameBlock, BLOCK_WIDTH, BLOCK_HEIGHT,BLOCK_SIZE);
		tetrisBlock.setPosition(blockLocateX, blockLocateY);
	}
	
	public void setNextBlock() {
		
		Random randomizer = new Random();
		nextBlockType = randomizer.nextInt(AMOUNT_OF_BLOCKS + 1);
		
		int[] nextGameBlock = GameBlocks.getBlock(blockType, 0);
		
		nextBlock.setObject(nextGameBlock,BLOCK_WIDTH, BLOCK_HEIGHT,BLOCK_SIZE);
		nextBlock.setPosition(NEXT_BLOCK_POS_X ,NEXT_BLOCK_POS_Y);
	}
	public void setTetris() {
		rotateBlock = 0;
		blockLocateX = BLOCK_POS_X;
		blockLocateY = BLOCK_POS_Y;
		
		blockType = nextBlockType;
		setNextBlock();

	}
	@Override
	public void keyPressed(KeyEvent e) {
		
		if(endGame) return;
		
		int tempRotateBlock = rotateBlock;
		int tempBlockLocateX = blockLocateX;
		int tempBlockLocateY = blockLocateY;
		
		int keyPress = e.getKeyCode();
		
		switch(keyPress) {
		
		case KeyEvent.VK_UP:
			if(rotateBlock < ROTATE_AMOUNT)
				rotateBlock ++;
			else
				rotateBlock = 0;
			break;
			
		case KeyEvent.VK_DOWN:
			blockLocateY += BLOCK_SIZE;
			break;
			
		case KeyEvent.VK_LEFT:
			blockLocateX += -BLOCK_SIZE;
			break;
			
		case KeyEvent.VK_RIGHT:
			blockLocateX += BLOCK_SIZE;
			break;
			
		case KeyEvent.VK_SPACE:
			if(pauseGame) break;
			while(gameBoard.inBound(tetrisBlock) && !gameBoard.hasObject(tetrisBlock)) {
				tempBlockLocateY = blockLocateY;
				blockLocateY += BLOCK_SIZE;
				updateTetris();
				
			}
			blockLocateY = tempBlockLocateY;
			updateTetris();
			addObjectToBackground();
			break;
		case KeyEvent.VK_P:
		case KeyEvent.VK_ESCAPE:
			pauseGame = !pauseGame;
			break;
		default:
			return;
		}
		
		if(pauseGame) {
			rotateBlock = tempRotateBlock;
			blockLocateX = tempBlockLocateX;
			blockLocateY = tempBlockLocateY;
			return;
		}
		
		updateTetris();
		if(gameBoard.inBound(tetrisBlock) && !gameBoard.hasObject(tetrisBlock)) {
			this.repaint();
		}
		else {
			rotateBlock = tempRotateBlock;
			blockLocateX = tempBlockLocateX;
			blockLocateY = tempBlockLocateY;
		}
		updateTetris();

	}
	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(endGame) return;
		
		if(pauseGame) {
			repaint();
			return;
		}
		
		int tempBlockLocateY = blockLocateY;
		
		blockLocateY += BLOCK_SIZE;
		
		updateTetris();
		
		//System.out.println("Y = " + blockLocateY + " X = " + blockLocateX);
		
		if(gameBoard.inBound(tetrisBlock) && !gameBoard.hasObject(tetrisBlock)) 
			repaint();
		
		else {
				
			blockLocateY = tempBlockLocateY;
			countWaitTime ++;
			
			if(countWaitTime >= MAX_WAIT_TIME) {
				updateTetris();
				if(gameBoard.hasObject(tetrisBlock)) { 
					endGame = true;
					repaint();
				}
				else {
					addObjectToBackground();
					countWaitTime = 0;
				}
			}
		}// end if statement
		
	}
	
	public void setGameSpeed() {
		switch(gameLevel) {
		case 2:
			gameSpeed = 500;
			break;
		case 3:
			gameSpeed = 450;
			break;
		case 4:
			gameSpeed = 400;
			break;
		case 5:
			gameSpeed = 350;
			break;
		case 6:
			gameSpeed = 300;
			break;
		case 7:
			gameSpeed = 250;
			break;
		case 8:
			gameSpeed = 200;
			break;
		case 9:
			gameSpeed = 150;
			break;
		case 10:
			gameSpeed = 100;
			break;	
		}
		gameTimer.setDelay(gameSpeed);
	}
}

class Draw {
	private boolean bGridOn;
	
	Draw() {
		bGridOn = false;
		
	}
	
	public void displayGrid(boolean bGridOn) {
		this.bGridOn = bGridOn;
	}
	public void displayCheckerImage(Graphics2D g, GameObject gameObject) {
		int locate_X = gameObject.getPosX();
		int locate_Y = gameObject.getPosY();
		
		int verticalBlockAmount = gameObject.getVerticalBlockAmount();
		int horizontalBlockAmount = gameObject.getHorizontalBlockAmount();
		
		int blockSize = gameObject.getBlockSize();
		boolean bflip = true;
		Color blockHue1 = new Color(22,22,22);
		Color blockHue2 = new Color(37,37,37);
		Color blockLineHue = new Color(0,3,0);

		
		for(int y = 0; y < verticalBlockAmount; y++ ) {
			for(int x = 0; x < horizontalBlockAmount; x++) {
				
				if(bflip) 
					g.setColor(blockHue1);
				else
					g.setColor(blockHue2);
				

				g.fillRect(locate_X,locate_Y, blockSize, blockSize);
				g.setColor(blockLineHue);
				g.drawRect(locate_X,locate_Y, blockSize, blockSize);
			
				
				locate_X += blockSize;
				bflip = !bflip;
			}
			bflip = !bflip;
			locate_X = gameObject.getPosX();
			locate_Y += blockSize;
		}
	}
	public void display(Graphics2D g, GameObject gameObject) {
		int locate_X = gameObject.getPosX();
		int locate_Y = gameObject.getPosY();
		
		int verticalBlockAmount = gameObject.getVerticalBlockAmount();
		int horizontalBlockAmount = gameObject.getHorizontalBlockAmount();
		
		int blockSize = gameObject.getBlockSize();
		int blockIndex = 0;
		int index = 0;
		
		for(int y = 0; y < verticalBlockAmount; y++ ) {
			for(int x = 0; x < horizontalBlockAmount; x++) {
				blockIndex = gameObject.getBlockIndex(index);
				if(bGridOn)
					g.drawRect(locate_X,locate_Y, blockSize, blockSize);
					
				if(blockIndex > 0)
					displayImage(g,locate_X,locate_Y,blockSize,blockIndex);
				
				locate_X += blockSize;
				index ++;
			}
			locate_X = gameObject.getPosX();
			locate_Y += blockSize;
		}
	}
	public void setBackground(Graphics2D g, GameObject gameObject, Color hue) {
		int x = gameObject.getOutlineX();
		int y = gameObject.getOutlineY();
		
		int horizontalBlockAmount = gameObject.getOutlineWidth();
		int verticalBlockAmount = gameObject.getOutlineHeight();
		g.setColor(hue);
		g.fillRect(x, y, horizontalBlockAmount, verticalBlockAmount);
	}
	public void displayOutline(Graphics2D g, GameObject gameObject) {
		
		int x = gameObject.getOutlineX();
		int y = gameObject.getOutlineY();
		
		int horizontalBlockAmount = gameObject.getOutlineWidth();
		int verticalBlockAmount = gameObject.getOutlineHeight();

		g.drawRect(x, y, horizontalBlockAmount, verticalBlockAmount);
		
	}
	private void displayImage(Graphics2D g, int locate_X, int locate_Y, int size,int index) {
		
		switch(index) {
		case 1:
			g.setColor(Color.CYAN);
			break;
		case 2:
			g.setColor(Color.YELLOW);
			break;	
		case 3:
			g.setColor(Color.BLUE);
			break;
		case 4:
			g.setColor(Color.ORANGE);
			break;
		case 5:
			g.setColor(Color.MAGENTA);
			break;
		case 6:
			g.setColor(Color.GREEN);
			break;	
		case 7:
			g.setColor(Color.RED);
			break;
		}
		
		g.fillRect(locate_X, locate_Y, size, size);
		g.setColor(Color.BLACK);
		g.drawRect(locate_X, locate_Y, size, size);
		
	}
	
	public void display(Graphics2D g, GameObject gameObject, BufferedImage[] image) {
		int locate_X = gameObject.getPosX();
		int locate_Y = gameObject.getPosY();
		
		int verticalBlockAmount = gameObject.getVerticalBlockAmount();
		int horizontalBlockAmount = gameObject.getHorizontalBlockAmount();
		
		int blockSize = gameObject.getBlockSize();
		int blockIndex = 0;
		int index = 0;
		
		for(int y = 0; y < verticalBlockAmount; y++ ) {
			for(int x = 0; x < horizontalBlockAmount; x++) {
				blockIndex = gameObject.getBlockIndex(index);
				//if(bGridOn)
				//	g.drawRect(locate_X,locate_Y, blockSize, blockSize);
				
				
				if(blockIndex > 0)
					g.drawImage(image[blockIndex - 1], locate_X, locate_Y, blockSize, blockSize, null);

				
				locate_X += blockSize;
				index ++;
			}
			locate_X = gameObject.getPosX();
			locate_Y += blockSize;
		}
	}
	
	
}

class GameBlocks {
	private static final int[][] brick00 = new int[][] {
			{ 0, 1, 0, 0, 
			  0, 1, 0, 0, 
			  0, 1, 0, 0, 
			  0, 1, 0, 0 },
				
			{ 0, 0, 0, 0, 
			  1, 1, 1, 1, 
			  0, 0, 0, 0, 
			  0, 0, 0, 0 },
					
			{ 0, 0, 1, 0, 
			  0, 0, 1, 0,
			  0, 0, 1, 0, 
			  0, 0, 1, 0 },
			  
			{ 0, 0, 0, 0, 
			  0, 0, 0, 0, 
			  1, 1, 1, 1, 
			  0, 0, 0, 0 }, 
			};
	
	private static final int[][] brick01 = new int[][] {
			{ 
			  0, 0, 0, 0,
			  2, 2, 0, 0,
			  2, 2, 0, 0,
			  0, 0, 0, 0 },
				
			{  
			  0, 0, 0, 0,
			  2, 2, 0, 0, 
			  2, 2, 0, 0,
			  0, 0, 0, 0 },
					
			{ 
			  0, 0, 0, 0,
			  2, 2, 0, 0,
			  2, 2, 0, 0,
			  0, 0, 0, 0 },
			{  
			  0, 0, 0, 0,
			  2, 2, 0, 0,
			  2, 2, 0, 0,
			  0, 0, 0, 0 },
			 };
	private static final int[][] brick02 = new int[][] 
			{ 
			{  
			  0, 3, 0, 0,
			  0, 3, 0, 0,
			  0, 3, 3, 0,
			  0, 0, 0, 0 }, 
			  
			{ 
			  0, 0, 0, 0,
			  3, 3, 3, 0,
			  3, 0, 0, 0,
			  0, 0, 0, 0 },
			  
			{  
			  3, 3, 0, 0,
			  0, 3, 0, 0,
			  0, 3, 0, 0,
			  0, 0, 0, 0 },
			  
			{  
			  0, 0, 3, 0,
			  3, 3, 3, 0,
			  0, 0, 0, 0,
			  0, 0, 0, 0 },
			};
	
	private static final int[][] brick03 = new int[][] 
			{ 
			{ 
			  0, 4, 0, 0, 
			  0, 4, 0, 0,
			  4, 4, 0, 0,
			  0, 0, 0, 0 },
			   
			  
			{ 
			  4, 0, 0, 0,
			  4, 4, 4, 0,
			  0, 0, 0, 0,
			  0, 0, 0, 0 },
			  
			{  
			  0, 4, 4, 0,
			  0, 4, 0, 0,
			  0, 4, 0, 0,
			  0, 0, 0, 0 },
			  
			{ 
			  0, 0, 0, 0,
			  4, 4, 4, 0,
			  0, 0, 4, 0,
			  0, 0, 0, 0 },
			};
	
	
	private static final int[][] brick04 = new int[][] 
			{ 
			{ 
			  0, 5, 0, 0, 
			  5, 5, 5, 0,
			  0, 0, 0, 0,
			  0, 0, 0, 0 }, 
			  
			{ 
			  0, 5, 0, 0, 
			  0, 5, 5, 0,
			  0, 5, 0, 0,
			  0, 0, 0, 0 },
			  
			{ 
			  0, 0, 0, 0, 
			  5, 5, 5, 0,
			  0, 5, 0, 0,
			  0, 0, 0, 0 },
			  
			{ 
			  0, 5, 0, 0, 
			  5, 5, 0, 0,
			  0, 5, 0, 0,
			  0, 0, 0, 0 },
			};
	
	private static final int[][] brick05 = new int[][] 
			{ 
			{ 
			  
			  0, 6, 6, 0, 
			  6, 6, 0, 0,
			  0, 0, 0, 0,
			  0, 0, 0, 0 }, 
			  
			{ 
			  0, 6, 0, 0,
			  0, 6, 6, 0, 
			  0, 0, 6, 0, 
			  0, 0, 0, 0 },
			  
			{ 
			  0, 0, 0, 0,
			  0, 6, 6, 0, 
			  6, 6, 0, 0,
			  0, 0, 0, 0 },
			  
			{ 
			  6, 0, 0, 0, 
			  6, 6, 0, 0,
			  0, 6, 0, 0,
			  0, 0, 0, 0 },
			};
	private static final int[][] brick06 = new int[][] 
			{ 
			{ 
			  7, 7, 0, 0,
			  0, 7, 7, 0,
			  0, 0, 0, 0,
			  0, 0, 0, 0 }, 
			  
			{ 
			  0, 0, 7, 0, 
			  0, 7, 7, 0, 
			  0, 7, 0, 0, 
			  0, 0, 0, 0  },
			  
			{ 
			  0, 0, 0, 0,
			  7, 7, 0, 0,
			  0, 7, 7, 0,
			  0, 0, 0, 0 },
			  
			{ 
			  0, 7, 0, 0,
			  7, 7, 0, 0,
			  7, 0, 0, 0,
			  0, 0, 0, 0 },
			};

	public static int[] getBlock(int blockType, int rotate) {
		
		if (rotate > 6) rotate = 0;
		
		switch (blockType) {
		case 0:	return brick00[rotate]; 
		case 1:	return brick01[rotate];
		case 2:	return brick02[rotate];
		case 3:	return brick03[rotate];
		case 4:	return brick04[rotate];
		case 5:	return brick05[rotate];
		case 6: return brick06[rotate];
		}
		return null;
	}
	
}


class GameObject {
	private int[] blockIndex;
	private int horizontalBlockAmount;
	private int verticalBlockAmount;
	private int posX;
	private int posY;
	private int blockSize;
	
	private int outlineX;
	private int outlineY;
	private int outlineWidth;
	private int outlineHeight;
	
	private ArrayList<Point> blockPosition;
	
	public GameObject() {};
	public GameObject(int [] blockIndex,int horizontalBlockAmount, int verticalBlockAmount,int size) {
		this.setObject(blockIndex,horizontalBlockAmount,verticalBlockAmount,size);
	}
	public void setObject(int [] blockIndex,int horizontalBlockAmount, int verticalBlockAmount,int size) {
		this.horizontalBlockAmount = horizontalBlockAmount;
		this.verticalBlockAmount = verticalBlockAmount;
		posX = 0;
		posY = 0;
		blockSize = size;
		blockPosition = new ArrayList<>();
		setObject(blockIndex);
	}
	public void setOutline() {
		 outlineX = posX;
		 outlineY = posY;
		 outlineWidth = blockSize * horizontalBlockAmount;
		 outlineHeight = blockSize * verticalBlockAmount;
		
	}
	public void setBlockPosition() {
		int locate_X = posX;
		int locate_Y = posY;
		int index = 0;
		blockPosition.clear();
	
		
		for (int y = 0; y < verticalBlockAmount; y++) {
			for (int x = 0; x < horizontalBlockAmount; x++) {
				if (blockIndex[index] > 0)  
					blockPosition.add(new Point(locate_X,locate_Y));

				locate_X += blockSize;
				index++;
			}
			locate_X = posX;
			locate_Y += blockSize;
		}
	}
	public int getBlockPositionSize() {
		return blockPosition.size();
	}
	public int getBlockPositionX(int index) {
		return blockPosition.get(index).x;
	}
	public int getBlockPositionY(int index) {
		return blockPosition.get(index).y;
	}
	public int getBlockIndexValue() {
		for(int i = 0; i < blockIndex.length; i++)
			if(blockIndex[i] > 0) 
				return blockIndex[i];
		return 0;
	}
	public ArrayList<Point> getBlockPosition() {
		return blockPosition;
	}
	public Point getBlockPosition(int index) {
		return blockPosition.get(index);
	}
	public int getBlockIndex(int num) {
		return blockIndex[num];
	}
	public void dropDown() {
		int size = blockIndex.length;
		int count = size - 1;
		int[] newBlocks = new int[size];
		
		for(int i = size - 1; i >= 0; i--) {
			
			if(blockIndex[i] >= 0) {
				newBlocks[count] = blockIndex[i];
				count --;
			}
		}
		
		blockIndex = newBlocks;
		this.setBlockPosition();
	}
	public boolean hasObject(GameObject gameObject) {
		
		for (int i = 0; i < gameObject.getBlockPositionSize(); i++) {
			if(blockPosition.contains(gameObject.getBlockPosition(i)))

				return true;
		}
		return false;
	}
	public void addObject(GameObject gameObject) {
		int index = 0;
		int locate_X = posX;
		int locate_Y = posY;
		boolean value = false;
		int blockSize = gameObject.getBlockSize();
		
		for(int y = 0; y < this.verticalBlockAmount; y++ ) {
			for(int x = 0; x < this.horizontalBlockAmount; x++) {
				
				value = gameObject.blockPosition.contains(new Point(locate_X,locate_Y));
				if(value)
					blockIndex[index] = gameObject.getBlockIndexValue();

				locate_X += blockSize;
				index ++;
			}
			locate_X = posX;
			locate_Y += blockSize;
		}
		setBlockPosition();
		
	}
	public int getFullRow() {
		int index = blockIndex.length - 1;
		int count = 0;
		
		for (int y = verticalBlockAmount; y > 0; y--) {
			for (int x = horizontalBlockAmount; x > 0; x--) {
				if(blockIndex[index] > 0)
					count ++;
				index --;
			}
			if(count == horizontalBlockAmount)
				return y;
			count = 0;
			
		}
		return -1;
	}
	public boolean isRowFull() {
		if(getFullRow() > 0) {
			return true;
		}
		return false;
	}
	public void invertRow() {
		
		int row = getFullRow();
		int index = blockIndex.length - 1;
		
		for (int y = verticalBlockAmount; y > 0; y--) {
			for (int x = horizontalBlockAmount; x > 0; x--) {
				if(row == y)
					blockIndex[index] = blockIndex[index] * -1;
				index --;
			}
		}
	}
	
	public boolean inBound(GameObject gameObject) {
		int locate_X = 0;
		int locate_Y = 0;
		int horizontalBlockAmount = outlineX + outlineWidth;
		int verticalBlockAmount = outlineY + outlineHeight;
		
		for(int i = 0; i < gameObject.getBlockPositionSize(); i++) {
			locate_X = gameObject.getBlockPositionX(i);
			locate_Y = gameObject.getBlockPositionY(i);

			if(locate_X < outlineX || locate_X >= horizontalBlockAmount 
					|| locate_Y >= verticalBlockAmount)
				return false;
		}
		return true;
	}
	public void setObject(int [] blockIndex) {
		this.blockIndex = blockIndex;
	}
	public void setPosition (int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
		this.setBlockPosition();
		this.setOutline();
	}
	public void setPosX(int posX) {
		this.posX = posX;
	}
	public int getPosX() {
		return posX;
	}
	public void setPosY(int posY) {
		this.posY = posY;
	}
	public int getPosY() {
		return posY;
	}
	public int getHorizontalBlockAmount() {
		return horizontalBlockAmount;
	}
	public int getVerticalBlockAmount() {
		return verticalBlockAmount;
	}
	public int getOutlineX() {
		return outlineX;
	}
	public int getOutlineY() {
		return outlineY;
	}
	public int getOutlineWidth() {
		return outlineWidth;
	}
	public int getOutlineHeight() {
		return outlineHeight;
	}
	public int getBlockSize() {
		return blockSize;
	}
	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}
	public String toString() {
		return blockPosition.toString();
	}
	
	
}
/*
 * End Program
 */

