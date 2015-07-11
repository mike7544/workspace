package games;

/**
 A Tetris game that allows the player to change the look or 
 theme of the game while playing. Instead of looking at the 
 same type of bricks and background throughout the game, 
 the player has the ability to change the brick type and 
 background image whenever their game play becomes stale, 
 thus insuring a longer and more enjoyable game play. 

 Author: Michael Rose date created: 07/10/2015

 */

import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;



////////////////////////////////////////////////////////////////
//
// Main Tetris Applet
//
////////////////////////////////////////////////////////////////
@SuppressWarnings("serial")
public class TetrisApplet extends JApplet implements  ActionListener {

	
	private Timer appTimer;
	static final int APP_WIDTH = 800;
	static final int APP_HEIGHT = 600;
	static final int APP_GAME = 1;
	static final int APP_MENU = 3;
	private int choice;
	
	private StartMenu startMenu = null;
	private Tetris game = null;
	
	
	
	public void init() {
		
	
		setSize(APP_WIDTH,APP_HEIGHT);		// set main applet window size
		appTimer = new Timer(100,this);		// timer for main program
		
		game = new Tetris();				// create tetris video game
		game.setSize(APP_WIDTH,APP_HEIGHT);	// set tetris game window size
		game.setVisible(false);
		add(game);
		this.addKeyListener(game);
		
		startMenu = new StartMenu();		// create start menu
		startMenu.setVisible(false);
		add(startMenu);
		
		this.setFocusable(true);
		this.requestFocus();
		
		// As soon as the program loads
		// display the tetris video game first.
		choice = APP_GAME;					
		
		
	}
	
	public void start() {
		appTimer.start();					// start main applet timer
		
		switch (choice) {					
		case APP_GAME:						// make tetris video game visible
			game.repaint();
			game.setVisible(true);
			break;
			
		case APP_MENU:						// make start menu visible
			startMenu.repaint();
			startMenu.setVisible(true);
			break;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		choice = startMenu.getChoice();		// get choice from start menu
		startMenu.resetChoice();			// reset start menu
		
		if (choice == APP_GAME) {			// make tetris game visible
			startMenu.setVisible(false);	// and make start menu invisible
			game.resetGame();				// reset all game variables
		}
		
		if (game.quit()) {					// quit tetris game
			game.setVisible(false);				
			choice = APP_MENU;				// and go back to start menu
		}
		
		this.start();
	}
	

}
////////////////////////////////////////////////////////////////
//
// Start Menu main program
//
////////////////////////////////////////////////////////////////

class StartMenu extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int BTN_START = 1;

	private JButton btnStart;
	private BufferedImage backgroundImage;
	private MyImage image;
	private int choice;

	public StartMenu() {
		
		this.setLayout(null);
		image = new MyImage();
		
		//
		// start menu background
		//
		backgroundImage = image.getImage("background_startmenu.jpg");
		
		//
		// create the start menu button
		//
		btnStart = new JButton(image.getIcon("btn_start.png"));
		btnStart.setOpaque(false);
		btnStart.setContentAreaFilled(false);
		btnStart.setBorderPainted(false);
		btnStart.setFocusPainted(false);
		btnStart.setBounds(500, 80, 270, 100);
		
		//
		// add button to action lististener
		//
		btnStart.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent event) {
				btnStart.setIcon(image.getIcon("btn_start_mouseover.png"));
			}

			public void mouseExited(MouseEvent event) {
				btnStart.setIcon(image.getIcon("btn_start.png"));
			}

			public void mousePressed(MouseEvent event) {
				choice = BTN_START;
			}
		});
		
		//
		// add button to start menu
		//
		add(btnStart);
		
		//
		// start menu off with no choice
		//
		choice = 0;

	}

	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		Graphics2D g2d = (Graphics2D) g;
		
		//
		// display the start menu wallpaper
		//
		g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);


	}
	
	public void resetChoice() {
		choice = 0;
	}

	public int getChoice() {
		return choice;
	}

}


////////////////////////////////////////////////////////////////
//
// MyImage program get images from /image/ directory
//
////////////////////////////////////////////////////////////////

class MyImage {

	public ImageIcon getIcon(String filename) {
		return new ImageIcon(getImage(filename));

	}

	public BufferedImage getImage(String filename) {
		BufferedImage tempImage = null;

		String filePath = "/images/" + filename;

		try {
			tempImage = ImageIO.read(getClass().getResourceAsStream(filePath));
		}

		catch (Exception ex) {
			System.out.println("file " + filePath + " not found");
		}

		return tempImage;
	}
}

////////////////////////////////////////////////////////////////
//
// Tetris Video Game main program
//
////////////////////////////////////////////////////////////////
@SuppressWarnings("serial")
class Tetris extends JPanel implements KeyListener, ActionListener,
		MouseListener {

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
	private final static int BLOCK_THEMES_TOTAL = 10;
	private final static int BKGD_TOTAL = 15;
	private final static int BLOCK_SPEED = 550;

	private final static int GAME_POINTS = 250;
	private final static int GAME_MOVE_UP_LEVEL = 20;
	
	private int gameSpeed;
	private Timer gameTimer;

	private int blockType;
	private int nextBlockType;
	private int rotateBlock;
	private int blockLocateX;
	private int blockLocateY;

	private int totalScore;
	private int gameLevel;
	private int countLines;
	private int totalLines;

	
	private boolean pauseGame;
	private boolean turnOnGrid;
	private boolean endGame;
	private boolean controlKeyBox;

	private boolean soundOn;
	private boolean justStarted;
	private boolean quitGame;

	private boolean overrideCommand;

	private GameObject gameBlock;
	private GameObject gameBoard;
	private GameObject nextBlock;

	private BufferedImage[][] blockImage;
	private BufferedImage[] backgroundImage;
	private BufferedImage endGameImage;
	private BufferedImage soundOnImage;
	private BufferedImage soundOffImage;
	private BufferedImage controlKeyImage;
	private BufferedImage escKeyImage;

	private BufferedImage logoTextLevel;
	private BufferedImage logoTextLines;
	private BufferedImage logoTextNext;
	private BufferedImage logoTextScore;

	private int animateTime;
	private int blockTheme;
	private int backgroundTheme;
	private MyImage image;
	
	public Tetris() {
		
		gameBlock = new GameObject(); 				// create game blocks
		gameBoard = new GameObject(); 				// create game board
		nextBlock = new GameObject(); 				// create next block

		blockImage = new BufferedImage[BLOCK_THEMES_TOTAL][7]; 	// set space for
																// block images
		backgroundImage = new BufferedImage[BKGD_TOTAL];		// set space for
																// background image
		soundOn = true; 							// turn sound on

		image = new MyImage(); 						// initiate class to load image
		loadGameImage(); 							// load all image for tetris
		setNextBlock();								// set the type of next brick
		setGame();									// initiate more values

		gameTimer = new Timer(gameSpeed, this);		// create timer for tetris
		
		//
		// add mouse and key listener
		//
		this.addMouseListener(this);					
		this.addKeyListener(this);
		this.setFocusable(true);
		this.requestFocus();

	}
	
	public void setGame() {

		setBlocks();			// initiate values for tetris blocks

		//
		// set game board size and location
		//
		int[] boardGrid = new int[BOARD_WIDTH * BOARD_HEIGHT];	
		gameBoard.setObject(boardGrid, BOARD_WIDTH, BOARD_HEIGHT, BLOCK_SIZE);
		gameBoard.setPosition(BOARD_POS_X, BOARD_POS_Y);

		gameSpeed = BLOCK_SPEED;
		totalScore = 0;
		gameLevel = 1;

		totalLines = 0;
		countLines = 0;

		pauseGame = false;
		endGame = false;
		controlKeyBox = false;

		turnOnGrid = false;
		justStarted = true;
		quitGame = false;

		overrideCommand = false;

		blockTheme = BLOCK_THEMES_TOTAL;
		backgroundTheme = 0;

		setBlocks();			// initiate new blocks

	}

	public boolean quit() {
		return endGame;
	}

	public void resetGame() {
		setGame();
	}

	public BufferedImage getImage(String filename) {
		BufferedImage tempImage = null;

		String filePath = "/images/" + filename;
		try {
			
			tempImage = ImageIO.read(getClass().getResourceAsStream(filePath));
			
		}

		catch (Exception ex) {
			System.out.println("file " + filePath + " not found");
		}

		return tempImage;
	}

	public void loadGameImage() {
		String[][] pic = {

				{ "block_bold_cyan.png", "block_bold_yellow.png",
						"block_bold_blue.png", "block_bold_lightgreen.png",
						"block_bold_darkblue.png", "block_bold_green.png",
						"block_bold_red.png" },

				{ "block_pushbutton_blue.png", "block_pushbutton_green.png",
						"block_pushbutton_red.png",
						"block_pushbutton_purple.png",
						"block_pushbutton_orange.png",
						"block_pushbutton_yellow.png",
						"block_pushbutton_cyan.png" },

				{ "iron_blue_block.png", "iron_darkblue_block.png",
						"iron_green_block.png", "iron_orange_block.png",
						"iron_purple_block.png", "iron_red_block.png",
						"iron_yellow_block.png" },

				{ "block_metal_blue.png", "block_metal_green.png",
						"block_metal_red.png", "block_metal_purple.png",
						"block_metal_orange.png", "block_metal_yellow.png",
						"block_metal_cyan.png" },

				{ "block_gel_blue.png", "block_gel_green.png",
						"block_gel_red.png", "block_gel_purple.png",
						"block_gel_orange.png", "block_gel_yellow.png",
						"block_gel_cyan.png" },

				{ "emoji_blue.png", "emoji_darkblue.png", "emoji_green.png",
						"emoji_pink.png", "emoji_purple.png", "emoji_red.png",
						"emoji_yellow.png" },

				{ "block_mushroom_blue02.png", "block_mushroom_brown.png",
						"block_mushroom_green.png",
						"block_mushroom_orange.png",
						"block_mushroom_purple.png",
						"block_mushroom_red02.png",
						"block_mushroom_lightblue.png" },

				{ "block_button_blue.png", "block_button_green.png",
						"block_button_red.png", "block_button_purple.png",
						"block_button_orange.png", "block_button_yellow.png",
						"block_button_cyan.png" },

				{ "block_dragonball_01.png", "block_dragonball_02.png",
						"block_dragonball_03.png", "block_dragonball_04.png",
						"block_dragonball_05.png", "block_dragonball_06.png",
						"block_dragonball_07.png" },

				{ "block_ball_baseball.png", "block_ball_baskitball.png",
						"block_ball_beach.png", "block_ball_football.png",
						"block_ball_soccer.png", "block_ball_volley.png",
						"block_ball_yellow_soccer.png" }
		};
		//
		// load brick images
		//
		for(int k = 0; k < BLOCK_THEMES_TOTAL; k++)
			for(int i = 0; i < 7; i++) 
				blockImage[k][i] = getImage( pic[k][i]);
		
		String[] bkgdImage = {
			"bkgd_building.jpg",
			"bkgd_gravel.jpg",
			"bkgd_food.jpg",
			"bkgd_live.jpg",
			"bkgd_wave.jpg",
			"bkgd_playgame.jpg",
			"bkgd_crack.jpg",
			"bkgd_mario.jpg",			
			"bkgd_colors.jpg",
			"bkgd_rockandsky.jpg",
			"bkgd_grey_bricks.jpg",		
			"bkgd_earth.jpg",
			"bkgd_darkred_bricks.jpg",
			"bkgd_jungle.jpg",
			"bkgd_black_bricks.jpg"};
		
		//
		// load background wallpaper
		//
		for (int i = 0; i < BKGD_TOTAL; i++)
			backgroundImage[i] = image.getImage(bkgdImage[i]);
		//
		// load message images
		//
		endGameImage = image.getImage("logo_endgame.png");
		escKeyImage = image.getImage("logo_press_esc.png");
		controlKeyImage = image.getImage("logo_control_keys.png");
		soundOnImage = image.getImage("logo_sound_on.png");
		soundOffImage = image.getImage("logo_sound_off.png");

		//
		// load logo images
		//
		logoTextLevel = image.getImage("logo_text_level.png");
		logoTextLines = image.getImage("logo_text_lines.png");
		logoTextNext = image.getImage("logo_text_next.png");
		logoTextScore = image.getImage("logo_text_score.png");

	}
	
	public void playSound(int index) {
		String[] filename = { "instant_drop.wav", 
				"remove_blocks.wav",
				"rotate.wav",
				"sound8.wav"};
		
		if(soundOn)
			loadGameSound(filename[index]);
	}
	
	public void loadGameSound(String filename) {

		try {

			Clip clip = AudioSystem.getClip();

			AudioInputStream inputStream = AudioSystem.getAudioInputStream
					(Tetris.class.getResource("/sounds/" + filename));
			clip.open(inputStream);
			clip.start();

		} catch (Exception ex) {
			System.out.println("can't find " + filename + " file");
		}

	}
	
	public boolean isTimeAnimated() {
		
		if (animateTime % 2 > 0)		// check if animation is on
			return true;
		return false;
		
	}
	public void checkGameBoardRow() {
		while(gameBoard.isRowFull()) {	// check if brick row is fill
			gameBoard.invertRow();
			countLines ++;
		}
		
		if(countLines > 0)				// if row is fill start animation
			animateTime = 5;
			
	}
	
	public void setGameStatistics() {
		
		//
		// calculate game points for each row that is fill
		//
		if(countLines > 0) {
			totalScore += countLines * GAME_POINTS;
			totalLines += countLines;
			countLines = 0;
		}
		
		if(!overrideCommand)		// cheat code to move up a level
			gameLevel = (totalLines / GAME_MOVE_UP_LEVEL) + 1;	// change game level
	}
	
	private void setRenderingFonts(Graphics2D g2d) {
		
		//
		// add anti aliasing to
		// system fonts
		//
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		renderHints.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		g2d.setRenderingHints(renderHints);
	}
	@Override
	public void paintComponent(Graphics g) {
		this.paintComponents(g);
		Graphics2D g2d =  (Graphics2D) g;
		
		setRenderingFonts(g2d);			// add anti aliasing to fonts
		updateTetris();					// update all brick values
		int tempLevel = gameLevel;		
		
		
		gameTimer.start();				// start game timer
		checkGameBoardRow();			// check if row is fill with bricks
	
		//
		// if row is fill with bricks
		// then remove bricks and
		// start animateTime
		//
		if(animateTime == 0) 			
			gameBoard.dropBlock();
		else
			animateTime --;		
		
		if(animateTime == 4)			// if brick is remove play sound
			playSound(1);
		
		setGameStatistics();			// get game points, level and line count
		setGameSpeed();					// as level increase so does speed
		drawGameSprites(g2d);			// draw all image
		
		//
		// if player change level
		// then change background image
		//
		if(tempLevel != gameLevel) 		
			changeBkgdTheme();
		

	}
	
	public void pressAnyKey(Graphics g2d) {
		g2d.drawString("Press any key to continue...", 100, 70);

	}
	public void drawGameMessage(Graphics g2d) {
		
		if(quitGame) {							// display quit message
			pressAnyKey(g2d);
			g2d.drawImage(endGameImage,175,200,null);
		}
		else if(justStarted) {					// display just started message
			pressAnyKey(g2d);
			g2d.drawImage(controlKeyImage,100,100,null);

		}
		else if(controlKeyBox)					// display instruction box
			g2d.drawImage(controlKeyImage,100,100,null);
		
		
		if(soundOn) 							// sound on or off image
			g2d.drawImage(soundOnImage,475,525,35,35,null);
		else
			g2d.drawImage(soundOffImage,475,525,35,35,null);
		
		g2d.drawImage(escKeyImage,100,0,null);	// display message to pause game
	}
	
	public void drawGameStatistics(Graphics g2d) {
		g2d.setColor(Color.white);
		
		g2d.setFont(new Font("Courier", Font.BOLD,20));
		
		g2d.drawImage(logoTextNext, 475, 90, null);
		
		g2d.drawImage(logoTextLevel, 475, 300, null);
		g2d.drawString(Integer.toString(gameLevel), 475, 350);
		
		
		g2d.drawImage(logoTextLines, 475, 375, null);
		g2d.drawString(Integer.toString(totalLines), 475, 425);
		
		g2d.drawImage(logoTextScore, 475, 450, null);
		g2d.drawString(Integer.toString(totalScore), 475, 500);
		
	}
	public void drawGameSprites(Graphics2D g2d) {
		
		Draw draw = new Draw();
		
		//
		// display image if isTimeAnimated is true
		//
		draw.showAllImage(isTimeAnimated());
		
		//
		// tetris brick image start at the
		// top of the game board a bit outside 
		// the bound, remove part of the brick image that is
		// outside the bound of the game board
		//
		draw.setCutOff(75);						
		
		if(blockTheme == BLOCK_THEMES_TOTAL) {
			
			//
			// display background wallpaper
			//
			g2d.drawImage(backgroundImage[backgroundTheme],0,0,getWidth(),getHeight(),null);

			
			draw.display(g2d, nextBlock);						// show next tetris brick
			
			if(turnOnGrid)										// display game grid
				draw.displayCheckerImage(g2d, gameBoard);
			
			else
				draw.setBackground(g2d, gameBoard, Color.BLACK);
			
			draw.display(g2d, gameBoard);						// draw game board
			draw.display(g2d, gameBlock);						// draw tetris brick
			
			//g2d.fillRoundRect(425,275, 175,300,30,30);			
			//g2d.fillRect(getImage("background_crack.jpg",0,0,175,300,null));
			//g2d.drawImage(backgroundImage[9], 425, 275, 175, 300, null);
		}
		else {
			//
			// display background wallpaper
			//
			g2d.drawImage(backgroundImage[backgroundTheme],0,0,getWidth(),getHeight(),null);
			draw.display(g2d, nextBlock, blockImage[blockTheme]);	// show next tetris brick
			
			if(turnOnGrid)											// display game grid
				draw.displayCheckerImage(g2d, gameBoard);
			
			else
				draw.setBackground(g2d, gameBoard,Color.BLACK);
			
			draw.display(g2d, gameBoard, blockImage[blockTheme]);	// draw game board
			draw.display(g2d, gameBlock, blockImage[blockTheme]);	// draw tetris brick
			
		

		}
		
		//
		// background for statistics, score, levels, and sound image
		//
		g2d.fillRoundRect(425,275, 175,300,30,30);					

		//
		// display game statistics
		// and message to player
		//
		drawGameStatistics(g2d);			
		drawGameMessage(g2d);
		
		
	}
	public void addObjectToBackground() {
		
		gameBoard.addObject(gameBlock);		// add tetris brick to game board
		setBlocks();						// update tetris brick value
	}
	public void updateTetris() {
		
		//
		// set new tetris block type, size,
		// and location
		//
		int[] nextGameBlock = GameBlocks.getBlock(blockType, rotateBlock);
		gameBlock.setObject(nextGameBlock, BLOCK_WIDTH, BLOCK_HEIGHT,BLOCK_SIZE);
		gameBlock.setPosition(blockLocateX, blockLocateY);
	}
	
	public void setNextBlock() {
		
		
		//
		// randomly get new tetris brick
		//
		Random randomizer = new Random();
		nextBlockType = randomizer.nextInt(AMOUNT_OF_BLOCKS + 1);
		
		//
		// set tetris brick size
		//
		int[] nextGameBlock = GameBlocks.getBlock(nextBlockType, 0);
		nextBlock.setObject(nextGameBlock,BLOCK_WIDTH, BLOCK_HEIGHT,BLOCK_SIZE);
		
		//
		// set tetris brick location
		//
		nextBlock.setPosition(NEXT_BLOCK_POS_X ,NEXT_BLOCK_POS_Y);
	}

	public void setBlocks() {
		//
		// set the rotate, location,
		// and block type for tetris brick
		//
		rotateBlock = 0;
		blockLocateX = BLOCK_POS_X;
		blockLocateY = BLOCK_POS_Y;

		blockType = nextBlockType;
		setNextBlock();				// set new values for the next tetris brick
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		//
		// temporally hold tetris brick value
		//
		int tempRotateBlock = rotateBlock;
		int tempBlockLocateX = blockLocateX;
		int tempBlockLocateY = blockLocateY;
		
		
		int keyPress = e.getKeyCode();			// get key player press
		boolean bRotate = false;
		
		//
		// if player died
		// or choose to quit
		//
		if(endGame) {								
			keyPress = 0;
			quitGame = false;
		}
		
		//
		// pause game so player
		// can read instructions
		// before game start
		//
		if (justStarted) {							
			keyPress = 0;
			justStarted = false;

		} else if (quitGame) {					// if player quit game
			keyPress = 0;
			endGame = true;
			
		}

		switch (keyPress) {						// read which key is press

		case KeyEvent.VK_UP:
			if (rotateBlock < ROTATE_AMOUNT) 
				rotateBlock++;
			else
				rotateBlock = 0;
			bRotate = true;
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
			if (pauseGame)
				break;
			
			//
			// instantly drop brick and
			// check if brick is in bound
			//
			while (gameBoard.inBound(gameBlock) && !gameBoard.hasObject(gameBlock)) {
				tempBlockLocateY = blockLocateY;
				blockLocateY += BLOCK_SIZE;
				
				//
				// update tetris values
				// while brick is being 
				// pull down
				//
				updateTetris();
			}

			blockLocateY = tempBlockLocateY;		// set brick new location
			updateTetris();							// update tetris values
			addObjectToBackground();				// add brick to game board
			playSound(0);							// drop tetris brick sound
			
			break;
		case KeyEvent.VK_S:
			soundOn = !soundOn;
			break;
		case KeyEvent.VK_T:
			changeGameTheme();
			break;
		case KeyEvent.VK_G:
			turnOnGrid = !turnOnGrid;
			break;
		case KeyEvent.VK_Q:
			quitGame = true;
			break;
		case KeyEvent.VK_B:
			changeBkgdTheme();
			break;
		case KeyEvent.VK_L:
			gameLevel++;
			overrideCommand = true;
			break;
		case KeyEvent.VK_ESCAPE:
		case KeyEvent.VK_P:
			
			//
			// pause game and show 
			// control dialog box
			//
			controlKeyBox = !controlKeyBox;
			pauseGame = controlKeyBox;
			break;
		}

		//
		// update tetris brick with
		// new values
		//
		if (pauseGame) {
			rotateBlock = tempRotateBlock;
			blockLocateX = tempBlockLocateX;
			blockLocateY = tempBlockLocateY;
		}

		else {
			updateTetris();						// update tetris values
			
			//
			// if tetris brick is inside game board
			// and does not overlap another brick
			//
			if (gameBoard.inBound(gameBlock) && !gameBoard.hasObject(gameBlock)) {
				if(bRotate)
					playSound(2);				// rotate tetris brick sound

				this.repaint();
			}
			
			//
			// update tetris brick with
			// new values
			//
			else {
				rotateBlock = tempRotateBlock;
				blockLocateX = tempBlockLocateX;
				blockLocateY = tempBlockLocateY;
				updateTetris();
			}
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		//
		// pause game for certain action
		// for example player pause game,
		// just started, or brick animation
		//
		
		if (pauseGame || animateTime > 0 || justStarted)
			repaint();
		else if (!quitGame && !endGame)
			dropBlocks();					// pull tetris brick down
		

	}

	public void dropBlocks() {
		
		int tempBlockLocateY = blockLocateY;

		blockLocateY += BLOCK_SIZE;
		updateTetris();						// update tetris brick values

		//
		// if brick is inside game board
		// and not have the same brick 
		// in the same location
		//
		if (gameBoard.inBound(gameBlock) && !gameBoard.hasObject(gameBlock))
			repaint();

		else {
			blockLocateY = tempBlockLocateY;
			updateTetris();
			
			//
			// if no more tetris brick
			// can fit on board
			// then end game
			//
			if (gameBoard.hasObject(gameBlock)) {
				quitGame = true;
				playSound(3);

			}
			else
				addObjectToBackground();		// add tetris brick to game board
			repaint();
		}
	}
	
	public void changeGameTheme() {
		changeBkgdTheme();						// change background image
		changeBlockTheme();						// change block image
	}

	public void changeBkgdTheme() {
		
		if (backgroundTheme >= backgroundImage.length - 1)
			backgroundTheme = 0;				// first background image
		else
			backgroundTheme++;					// next background image
	}

	public void changeBlockTheme() {

		if (blockTheme >= BLOCK_THEMES_TOTAL)
			blockTheme = 0;						// first block theme
		else
			blockTheme++;						// next block theme
	}
	public void setGameSpeed() {

		//
		// as player advance through
		// the levels,
		// game speed increase 
		//
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
		
		//
		// change game timer speed
		// to display brick animation
		//
		if (animateTime > 0)
			gameTimer.setDelay(50);

		else
			gameTimer.setDelay(gameSpeed);		// return to normal timer speed
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		//
		// if mouse is pointing at 
		// sound image
		//
		boolean bHar = e.getX() > 475 && e.getX() < 499;
		boolean bVer = e.getY() > 525 && e.getY() < 560;
		
		//
		// click sound image to turn on/off
		//
		if (bHar && bVer)
			soundOn = !soundOn;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
	

}

////////////////////////////////////////////////////////////////
//
//		Draw
//		draws object for example: tetris bricks and game board
//
////////////////////////////////////////////////////////////////
class Draw {
	
	private boolean bImage;
	private int cutOff;
	
	Draw() {}
	
	public void setCutOff(int harizontal_start) {
		cutOff = harizontal_start;
	}
	
	public void showAllImage(boolean bImage) {
		this.bImage = bImage;
	}
	
	public void setBackground(Graphics2D g, GameObject gameObject, Color hue) {
		int x = gameObject.getOutlineX();
		int y = gameObject.getOutlineY();
		
		int horBlockAmnt = gameObject.getOutlineWidth();
		int verBlockAmnt = gameObject.getOutlineHeight();
		
		g.setColor(hue);
		g.fillRoundRect(x, y, horBlockAmnt, verBlockAmnt,30,30);
	}
	
	public void displayCheckerImage(Graphics2D g, GameObject gameObject) {
		int locate_X = gameObject.getPosX();
		int locate_Y = gameObject.getPosY();
		
		int verBlockAmnt = gameObject.getVerBlockAmnt();
		int horBlockAmnt = gameObject.getHorBlockAmnt();
		
		int blockSize = gameObject.getBlockSize();
		boolean bflip = true;
		
		Color blockHue1 = new Color(22,22,22);
		Color blockHue2 = new Color(37,37,37);
		Color blockLineHue = new Color(0,3,0);

		
		for(int y = 0; y < verBlockAmnt; y++ ) {
			for(int x = 0; x < horBlockAmnt; x++) {
				
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
		
		int verBlockAmnt = gameObject.getVerBlockAmnt();
		int horBlockAmnt = gameObject.getHorBlockAmnt();
		
		int blockSize = gameObject.getBlockSize();
		int blockIndex = 0;
		int index = 0;

		for(int y = 0; y < verBlockAmnt; y++ ) {
			for(int x = 0; x < horBlockAmnt; x++) {
				
				blockIndex = gameObject.getBlockIndex(index); 		// get tetris brick index
				
				//
				// if tetris brick index is 
				// a negative value change
				// it to positive value
				//
				if(bImage && blockIndex < 0)
						blockIndex *= -1;
				
				if(blockIndex > 0)
					
					//
					// if parts of tetris brick is
					// off the game board then
					// do not draw it
					//
					if(locate_Y >= cutOff) 
						
						//
						// draw tetris brick piece
						//
						displayImage(g,locate_X,locate_Y,blockSize,blockIndex);
				
				locate_X += blockSize;
				index ++;						// next tetris brick piece
			}
			
			locate_X = gameObject.getPosX();
			locate_Y += blockSize;
		}
	}
	public void display(Graphics2D g, GameObject gameObject, BufferedImage[] image) {
		int locate_X = gameObject.getPosX();
		int locate_Y = gameObject.getPosY();
		
		int verBlockAmnt = gameObject.getVerBlockAmnt();
		int horBlockAmnt = gameObject.getHorBlockAmnt();
		
		int blockSize = gameObject.getBlockSize();
		int blockIndex = 0;
		int index = 0;
		
		for(int y = 0; y < verBlockAmnt; y++ ) {
			for(int x = 0; x < horBlockAmnt; x++) {
				blockIndex = gameObject.getBlockIndex(index);		// get tetris brick index
				
				//
				// if tetris brick index is 
				// a negative value change
				// it to positive value
				//
				if(bImage && blockIndex < 0)
						blockIndex *= -1;
				
				if(blockIndex > 0)
					
					//
					// if parts of tetris brick is
					// off the game board then
					// do not draw it
					//
					if(locate_Y >= cutOff) 
						
						//
						// draw tetris brick piece
						//
						g.drawImage(image[blockIndex - 1], locate_X, locate_Y, blockSize, blockSize, null);

				
				locate_X += blockSize;
				index ++;
			}
			locate_X = gameObject.getPosX();
			locate_Y += blockSize;
		}
	}
	
	public void displayOutline(Graphics2D g, GameObject gameObject) {
		
		int x = gameObject.getOutlineX();
		int y = gameObject.getOutlineY();
		
		int horBlockAmnt = gameObject.getOutlineWidth();
		int verBlockAmnt = gameObject.getOutlineHeight();

		g.drawRect(x, y, horBlockAmnt, verBlockAmnt);
		
	}
	private void displayImage(Graphics2D g, int locate_X, int locate_Y, int size,int index) {
		
		switch(index) {
		case 1:
			g.setColor(new Color(31,143,225));
			break;
		case 2:
			g.setColor(new Color(252,191,16));
			break;	
		case 3:
			g.setColor(new Color(48,47,193));;
			break;
		case 4:
			g.setColor(new Color(247,105,31));
			break;
		case 5:
			g.setColor(new Color(144,49,147));
			break;
		case 6:
			g.setColor(new Color(42,166,59));
			break;	
		case 7:
			g.setColor(new Color(230,0,28));
			break;
		}
		
		g.fillRoundRect(locate_X, locate_Y, size, size,10,10);
		g.setColor(Color.BLACK);
		g.drawRoundRect(locate_X, locate_Y, size, size,10,10);
		
	}
	
}

////////////////////////////////////////////////////////////////
//
//		GameBlocks
//		data to create tetris brick
//
////////////////////////////////////////////////////////////////
 class  GameBlocks {
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
		final int MAX_ROTATION = 6;
	
		if (rotate > MAX_ROTATION) rotate = 0;
		
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

////////////////////////////////////////////////////////////////
//
//	Create Game Objects 
//  examples: tetris bricks and game board
//
////////////////////////////////////////////////////////////////
class GameObject {
	private int[] blockIndex;
	private int horBlockAmnt;
	private int verBlockAmnt;
	private int posX;
	private int posY;
	private int blockSize;
	
	private int outlineX;
	private int outlineY;
	private int outlineWidth;
	private int outlineHeight;
	
	private ArrayList<Point> blockPos;
	
	public GameObject() {};
	public GameObject(int [] blockIndex,int horBlockAmnt, int verBlockAmnt,int size) {
		
		//
		// initiate object
		//
		this.setObject(blockIndex,horBlockAmnt,verBlockAmnt,size);
	}
	
	
	public void setObject(int [] blockIndex,int horBlockAmnt, int verBlockAmnt,int size) {
		
		//
		// set all values
		//
		this.horBlockAmnt = horBlockAmnt;
		this.verBlockAmnt = verBlockAmnt;
		posX = 0;
		posY = 0;
		blockSize = size;
		blockPos = new ArrayList<>();
		setObject(blockIndex);
	}
	public void setOutline() {
		 outlineX = posX;
		 outlineY = posY;
		 outlineWidth = blockSize * horBlockAmnt;
		 outlineHeight = blockSize * verBlockAmnt;
		
	}
	public void setBlockPos() {
		int locate_X = posX;
		int locate_Y = posY;
		int index = 0;
		blockPos.clear();
	
		
		for (int y = 0; y < verBlockAmnt; y++) {
			for (int x = 0; x < horBlockAmnt; x++) {
				
				//
				// add new block position
				//
				if (blockIndex[index] > 0)  
					blockPos.add(new Point(locate_X,locate_Y));

				locate_X += blockSize;
				index++;
			}
			locate_X = posX;
			locate_Y += blockSize;
		}
	}
	public int getBlockPosSize() {
		return blockPos.size();
	}
	public int getBlockPosX(int index) {
		return blockPos.get(index).x;
	}
	public int getBlockPosY(int index) {
		return blockPos.get(index).y;
	}
	public int getBlockIndexValue() {
		for(int i = 0; i < blockIndex.length; i++)
			if(blockIndex[i] > 0) 
				return blockIndex[i];
		return 0;
	}
	public ArrayList<Point> getBlockPos() {
		return blockPos;
	}
	public Point getBlockPos(int index) {
		return blockPos.get(index);
	}
	public int getBlockIndex(int num) {
		return blockIndex[num];
	}
	
	public void dropBlock() {
		int size = blockIndex.length;
		int count = size - 1;
		int[] newBlocks = new int[size];
		
		for(int i = size - 1; i >= 0; i--) {
			
			//
			// pull tetris brick down
			// one row at a time
			//
			if(blockIndex[i] >= 0) {
				newBlocks[count] = blockIndex[i];
				count --;
			}
		}
		
		blockIndex = newBlocks;
		this.setBlockPos();
	}
	public boolean hasObject(GameObject gameObject) {
		
		for (int i = 0; i < gameObject.getBlockPosSize(); i++) {
			
			//
			// if tetris pieces do not overlapped
			// other pieces
			//
			if(blockPos.contains(gameObject.getBlockPos(i)))
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
		
		for(int y = 0; y < this.verBlockAmnt; y++ ) {
			for(int x = 0; x < this.horBlockAmnt; x++) {
				
				//
				// if tetris pieces do not overlapped
				// other pieces
				//
				value = gameObject.blockPos.contains(new Point(locate_X,locate_Y));
				
				//
				// add tetris pieces to board
				//
				if(value)
					blockIndex[index] = gameObject.getBlockIndexValue();

				locate_X += blockSize;
				index ++;
			}
			locate_X = posX;
			locate_Y += blockSize;
		}
		setBlockPos();				// update block position
		
	}
	public int getFullRow() {
		int index = blockIndex.length - 1;
		int count = 0;
		
		for (int y = verBlockAmnt; y > 0; y--) {
			for (int x = horBlockAmnt; x > 0; x--) {
				
				//
				// count brick pieces that
				// are in a row
				//
				if(blockIndex[index] > 0)		
					count ++;
				
				index --;
			}
			
			//
			// if brick pieces equal block amount
			// then return row number
			//
			if(count == horBlockAmnt)			
				return y;
			count = 0;
			
		}
		return -1;
	}
	public boolean isRowFull() {
		
		boolean bFlag = false;
		
		//
		// if tetris brick pieces are in a row
		//
		if(getFullRow() > 0) bFlag = true;
		
		return bFlag;
	}
	public void invertRow() {
		
		int row = getFullRow();
		int index = blockIndex.length - 1;
		
		for (int y = verBlockAmnt; y > 0; y--) {
			for (int x = horBlockAmnt; x > 0; x--) {
				
				//
				// invert row of tetris brick pieces
				//
				if(row == y)
					blockIndex[index] = blockIndex[index] * -1;
				
				index --;
			}
		}
	}
	
	public boolean inBound(GameObject gameObject) {
		int locate_X = 0;
		int locate_Y = 0;
		int horBlockAmnt = outlineX + outlineWidth;
		int verBlockAmnt = outlineY + outlineHeight;
		
		for(int i = 0; i < gameObject.getBlockPosSize(); i++) {
			locate_X = gameObject.getBlockPosX(i);
			locate_Y = gameObject.getBlockPosY(i);

			//
			// if tetris brick is in bound
			//
			if(locate_X < outlineX || locate_X >= horBlockAmnt || locate_Y >= verBlockAmnt)
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
		this.setBlockPos();
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
	public int getHorBlockAmnt() {
		return horBlockAmnt;
	}
	public int getVerBlockAmnt() {
		return verBlockAmnt;
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
		return blockPos.toString();
	}
	
}

////////////////////////////////////////////////////////////////
//
//					End Program
//
////////////////////////////////////////////////////////////////

