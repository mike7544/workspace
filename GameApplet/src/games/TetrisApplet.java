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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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



@SuppressWarnings("serial")
public class TetrisApplet extends JApplet implements  ActionListener {
	
	
	private Timer appTimer;
	static final int APP_WIDTH = 800;
	static final int APP_HEIGHT = 600;
	private int choice;
	
	private StartMenu startMenu = null;
	private GameScore gameScore = null;
	private Tetris game = null;
	
	public void init() {
		
		
		setSize(APP_WIDTH,APP_HEIGHT);
		appTimer = new Timer(100,this);
		
		game = new Tetris();
		game.setSize(APP_WIDTH,APP_HEIGHT);
		game.setVisible(false);
		add(game);
		this.addKeyListener(game);
		
		gameScore = new GameScore();
		gameScore.setSize(APP_WIDTH,APP_HEIGHT);
		gameScore.setVisible(false);
		add(gameScore);
		
		
		startMenu = new StartMenu();
		startMenu.setVisible(false);
		add(startMenu);
		
		this.setFocusable(true);
		this.requestFocus();
		choice = 1;
		
	}
	
	public void start() {
		appTimer.start();
			choice = 1;
			
			switch(choice) {
			case 1:
				game.repaint();
				game.setVisible(true);
				break;
			case 2:

				gameScore.repaint();
				gameScore.setVisible(true);
				break;
			case 3:
				startMenu.repaint();
				startMenu.setVisible(true);				
				break;
				
			}
			choice = 0;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		choice = startMenu.getChoice();
		startMenu.resetChoice();
		
		if(choice == 1 || choice == 2 ) {
			
			startMenu.setVisible(false);
		}
		
		if(game != null) {
			if(game.quit()) {
				game.setVisible(false);
				game.resetGame();
				choice = 3;
			}
		}
		
		if(gameScore != null) {
			if(gameScore.exit()) {
				gameScore.setVisible(false);
				gameScore.resetExit();
				choice = 3;
			}
		}
	
		this.start();
		
	}

}

class StartMenu extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private JButton btnRed;
	private JButton btnYellow; 
	private BufferedImage backgroundImage;
	
	private int choice;
	
	public StartMenu() {
		this.setLayout(null);
		
		backgroundImage = getImage("background_startmenu.jpg");
		ImageIcon startIcon = getIcon("btn_start.png");
	
		btnRed = new JButton(startIcon);//(getIcon("btn_start.png"));
		btnRed.setOpaque(false);
		btnRed.setContentAreaFilled(false);
		btnRed.setBorderPainted(false);
		btnRed.setFocusPainted(false);
		btnRed.setBounds(500, 80, 270, 100);
		btnRed.addActionListener(this);
		
		btnRed.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent event) {
				btnRed.setIcon(getIcon("btn_start_mouseover.png"));
			}
			public void mouseExited(MouseEvent event) {
				btnRed.setIcon(getIcon("btn_start.png"));
			}
			public void mousePressed(MouseEvent event){
				choice = 1;
			}
		});
		add(btnRed);
		
		btnYellow = new JButton(getIcon("btn_score.png"));
		btnYellow.setOpaque(false);
		btnYellow.setContentAreaFilled(false);
		btnYellow.setBorderPainted(false);
		btnYellow.setFocusPainted(false);
		btnYellow.setBounds(500, 200, 270, 100);
		btnYellow.addActionListener(this);
		btnYellow.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent event) {
				btnYellow.setIcon(getIcon("btn_score_mouseover.png"));
			}
			public void mouseExited(MouseEvent event) {
				btnYellow.setIcon(getIcon("btn_score.png"));
			}
			public void mousePressed(MouseEvent event){
				//btnYellow.setIcon(getIcon("btn_score_mouseover.png"));

				choice = 2;
			}
		});
		
		add(btnYellow);

		choice = 0;
	
	}
	public ImageIcon getIcon(String filename) {
		ImageIcon icon = null;
		
		String filePath = "/images/" + filename;
		
		try {
			 icon = new ImageIcon(getClass().getResource(filePath));
		}
		
		catch (Exception ex) {
			System.out.println("file " + filePath + " not found");
		}
		
		return icon;
	}
	
	public BufferedImage getImage(String filename) {
		BufferedImage tempImage = null;
		
		String filePath = "/images/" + filename;
		
		try {
			tempImage = ImageIO.read(getClass().getResource(filePath));
		}
		
		catch (Exception ex) {
			System.out.println("file " + filePath + " not found");
		}
		
		return tempImage;
	}
	
	public void actionPerformed(ActionEvent e) {
	    if(e.getActionCommand().equals("red")) 
			choice = 1;
		
	    else if (e.getActionCommand().equals("yellow"))
			choice = 2;
	   System.out.println(choice);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(backgroundImage, 0,0,getWidth(),getHeight(),null);
		
		//g.setColor(Color.pink);
		//g.fillRect(0, 0,getWidth(),getHeight());

	}
	public void resetChoice() {
		choice = 0;
	}
	public int getChoice() {
		return choice;
	}
	
	
}
class GameScore1  extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JButton btnCancel = new JButton("exit");
	
	private boolean endProgram;
	
	public GameScore1 () {
		endProgram = false;
		btnCancel.addActionListener(this);
		add(btnCancel);
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		g.setColor(Color.red);
		g.fillRect(0, 0,getWidth(),getHeight());
	}
	
	public boolean exit() {
		return endProgram;
	}
	public void resetExit() {
		endProgram = !endProgram;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("exit")) 
			endProgram = true;
		
	}
	
	
}

class GameScore  extends JPanel implements ActionListener {
	private static final long serialVersionUID = 2L;
	private JButton btnCancel = new JButton("exit");
	
	private boolean endProgram;
	
	public GameScore () {
		endProgram = false;
		btnCancel.addActionListener(this);
		add(btnCancel);
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		g.setColor(Color.yellow);
		g.fillRect(0, 0,getWidth(),getHeight());
	}
	
	public boolean exit() {
		return endProgram;
	}
	public void resetExit() {
		endProgram = !endProgram;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("exit")) 
			endProgram = true;
		
	}
	
	
}
@SuppressWarnings("serial")
class Tetris extends JPanel implements KeyListener, ActionListener {
	
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
	private final static int THEMES_TOTAL = 11;
	private final static int BKGD_TOTAL = 12;
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
	private boolean escKeyPress;
	private boolean soundOn;
	private boolean justStarted;
	private boolean quitGame;
	
	private GameObject gameBlock;
	private GameObject gameBoard;
	private GameObject nextBlock;
	
	private BufferedImage[][] blockImage;
	private BufferedImage[] backgroundImage;
	private BufferedImage endGameImage;
	private BufferedImage soundOnImage;
	private BufferedImage soundOffImage;
	private BufferedImage controlKeyImage;
	//private BufferedImage pauseGameImage;
	private BufferedImage messageImage;
	
	private BufferedImage logoTextLevel;
	private BufferedImage logoTextLines;
	private BufferedImage logoTextNext;
	private BufferedImage logoTextScore;
	
	private int animateTime;
	private int gameTheme;
	private int backgroundTheme;
	
	
	public Tetris() {
		
		gameBlock = new GameObject();
		gameBoard = new GameObject();
		nextBlock = new GameObject();
		blockImage = new BufferedImage[THEMES_TOTAL][7];
		backgroundImage = new BufferedImage[BKGD_TOTAL];
		
		int[] boardGrid  = new int[BOARD_WIDTH * BOARD_HEIGHT];

		gameBoard.setObject(boardGrid,BOARD_WIDTH,BOARD_HEIGHT,BLOCK_SIZE);
		gameBoard.setPosition(BOARD_POS_X, BOARD_POS_Y);

		loadGameImage();
		setNextBlock();
		setBlocks();
		
		gameSpeed = 550;
		totalScore = 0;
		gameLevel = 0;
		
		totalLines = 0;
		countLines = 0;
		
		pauseGame = false;
		endGame = false;
		escKeyPress = false;
		turnOnGrid = false;
		soundOn = false;
		justStarted = true;
		quitGame = false;
		
		gameTheme = THEMES_TOTAL;
		backgroundTheme = 0;
		
		gameTimer = new Timer(gameSpeed,this);
		
		this.addKeyListener(this);
		this.setFocusable(true);
		this.requestFocus();
		
	}
	
	

	public boolean quit() {
		return false;//endGame;
	}
	public void resetGame() {
		gameSpeed = 550;
		totalScore = 0;
		gameLevel = 0;
		
		totalLines = 0;
		countLines = 0;
		
		pauseGame = true;
		endGame = false;
		escKeyPress = false;
		turnOnGrid = false;
		soundOn = false;
		
		gameTheme = THEMES_TOTAL;
		backgroundTheme = 0;
		gameBoard = null;
		
		setNextBlock();
		setBlocks();
		
		int[] boardGrid  = new int[BOARD_WIDTH * BOARD_HEIGHT];
		gameBoard = new GameObject();

		gameBoard.setObject(boardGrid,BOARD_WIDTH,BOARD_HEIGHT,BLOCK_SIZE);
		gameBoard.setPosition(BOARD_POS_X, BOARD_POS_Y);
	}
	public BufferedImage getImage(String filename) {
		BufferedImage tempImage = null;
		
		String filePath = "/images/" + filename;
		
		try {
			tempImage = ImageIO.read(getClass().getResource(filePath));
		}
		
		catch (Exception ex) {
			System.out.println("file " + filePath + " not found");
		}
		
		return tempImage;
	}
	public void loadGameImage() {
		String[][] pic = 
			{
			{"block_cyan.png","block_yellow.png","block_blue.png",
			 "block_lightgreen.png","block_darkblue.png","block_green.png",
			 "block_red.png"}, 
			 
			{"block_pushbutton_blue.png","block_pushbutton_green.png","block_pushbutton_red.png",
			 "block_pushbutton_purple.png","block_pushbutton_orange.png","block_pushbutton_yellow.png",
			 "block_pushbutton_cyan.png"},
			 
			{"iron_blue_block.png","iron_darkblue_block.png","iron_green_block.png",
			 "iron_orange_block.png","iron_purple_block.png","iron_red_block.png",
			 "iron_yellow_block.png"},
			 
			{"block_metal_blue.png","block_metal_green.png","block_metal_red.png",
			  "block_metal_purple.png","block_metal_orange.png","block_metal_yellow.png",
			  "block_metal_cyan.png"},
			  
		    {"block_button_blue.png","block_button_green.png","block_button_red.png",
			 "block_button_purple.png","block_button_orange.png","block_button_yellow.png",
			 "block_button_cyan.png"},
			 
		    {"emoji_blue.png","emoji_darkblue.png","emoji_green.png",
			 "emoji_pink.png","emoji_purple.png","emoji_red.png",
			 "emoji_yellow.png"},
				 
		   {"block_gel_blue.png","block_gel_green.png","block_gel_red.png",
			"block_gel_purple.png","block_gel_orange.png","block_gel_yellow.png",
			"block_gel_cyan.png"},
			 
			{"tube_blue_block.png","tube_cyran_block.png","tube_green_block.png",
			 "tube_orange_block.png","tube_purple_block.png","tube_red_block.png",
			 "tube_yellow_block.png"},
			 
			{"block_ball_baseball.png","block_ball_baskitball.png","block_ball_beach.png",
			 "block_ball_football.png","block_ball_soccer.png","block_ball_volley.png",
			 "block_ball_yellow_soccer.png"},
				 
			{"block_mushroom_blue02.png","block_mushroom_brown.png","block_mushroom_green.png",
			 "block_mushroom_orange.png","block_mushroom_purple.png","block_mushroom_red02.png",
			 "block_mushroom_lightblue.png"},
			 
		   {"block_dragonball_01.png","block_dragonball_02.png","block_dragonball_03.png",
			"block_dragonball_04.png","block_dragonball_05.png","block_dragonball_06.png",
			"block_dragonball_07.png"}
			 
			};
		
		
		for(int k = 0; k < THEMES_TOTAL; k++)
			for(int i = 0; i < 7; i++) 
				blockImage[k][i] = getImage( pic[k][i]);
		
		String[] bkgdImage = {
			"bkgd_building.jpg",
			"bkgd_darkred_bricks.jpg",
			"bkgd_gravel.jpg",
			"bkgd_playgame.jpg",
			"bkgd_black_bricks.jpg",
			"bkgd_live.jpg",
			"bkgd_grey_bricks.jpg",		
			"bkgd_jungle.jpg",		
			"bkgd_mario.jpg",			
			"bkgd_rockandsky.jpg",	
			"bkgd_tetris.jpg",
			"bkgd_crack.jpg",
		
			};
		
		for(int i = 0; i < BKGD_TOTAL; i++)
			backgroundImage[i] = getImage(bkgdImage[i]);
		
		//pauseGameImage = getImage("pause_logo.png");
		endGameImage = getImage("logo_endgame.png");
		messageImage = getImage("logo_press_esc.png");
		controlKeyImage = getImage("logo_control_keys.png");
		soundOnImage = getImage("logo_sound_on.png");
		soundOffImage = getImage("logo_sound_off.png");
		
		logoTextLevel = getImage("logo_text_level.png");
		logoTextLines = getImage("logo_text_lines.png");
		logoTextNext = getImage("logo_text_next.png");
		logoTextScore = getImage("logo_text_score.png");

		
	}
	public void playSound(int index) {
		String[] filename = {"instant_drop.wav", "remove.wav"};
		
		loadGameSound(filename[index]);
	}
	
	public void loadGameSound(String filename) {
		
		try {
			
			Clip clip = AudioSystem.getClip();
			
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(
					Tetris.class.getResourceAsStream("/sounds/" + filename));
			
			clip.open(inputStream);
			clip.start();
			
		} catch(Exception ex) {
			System.out.println("can't find wav file");
		}
	}
	
	
	
	public boolean isTimeAnimated() {
		if (animateTime % 2 > 0) 
			return true;
		
		return false;
		
	}
	public void checkGameBoardRow() {
		while(gameBoard.isRowFull()) {
			gameBoard.invertRow();
			countLines ++;
		}
		if(countLines > 0)
			animateTime = 5;
			
	}
	public void setGameStatistics() {
		final int POINTS = 375;
		final int MOVE_UP_LEVEL = 20;
		
		
		if(countLines > 0) {
			totalScore += countLines * POINTS;
			totalLines += countLines;
			countLines = 0;
		}
		gameLevel = (totalLines / MOVE_UP_LEVEL) + 1;
	}
	@Override
	public void paintComponent(Graphics g) {
		this.paintComponents(g);
		
		gameTimer.start();
		
		Graphics2D g2d =  (Graphics2D) g;
		
		//updateTetris();
		checkGameBoardRow();
			
		if(animateTime == 0)
			gameBoard.dropBlock();
		
		else
			animateTime --;

		if(animateTime == 4 && soundOn)
			playSound(1);
		
		setGameStatistics();
		setGameSpeed();
		drawGameSprites(g2d);
		
		
	}
	public void pressAnyKey(Graphics g2d) {
		g2d.drawString("Press any key to continue...", 100, 70);

	}
	public void displayGameMessage(Graphics g2d) {
		if(quitGame) {
			pressAnyKey(g2d);
			g2d.drawImage(endGameImage,175,200,null);
		}
		else if(justStarted) {
			pressAnyKey(g2d);
			g2d.drawImage(controlKeyImage,100,100,null);

		}
		else if(escKeyPress)
			g2d.drawImage(controlKeyImage,100,100,null);
		
		
		if(soundOn) 
			g2d.drawImage(soundOnImage,475,525,35,35,null);
		else
			g2d.drawImage(soundOffImage,475,525,35,35,null);
		
		
		
		g2d.drawImage(messageImage,100,0,null);
	}
	
	
	
	public void displayGameStatistics(Graphics g2d) {
		g2d.setColor(Color.white);
		
		g2d.setFont(new Font("Courier", Font.BOLD,20));

		g2d.drawImage(logoTextNext, 475, 90, null);
		//g2d.drawString("NEXT", 475, 90);
		
		g2d.drawImage(logoTextLevel, 475, 300, null);
		//g2d.drawString("LEVEL", 475, 300);
		g2d.drawString(Integer.toString(gameLevel), 475, 350);
		
		
		g2d.drawImage(logoTextLines, 475, 375, null);
		//g2d.drawString("LINES", 475, 375);
		g2d.drawString(Integer.toString(totalLines), 475, 425);
		
		g2d.drawImage(logoTextScore, 475, 450, null);
		//g2d.drawString("SCORE", 475, 450);
		g2d.drawString(Integer.toString(totalScore), 475, 500);
		
		

	}
	public void drawGameSprites(Graphics2D g2d) {
		Draw draw = new Draw();
		draw.showAllImage(isTimeAnimated());
		

		if(gameTheme == THEMES_TOTAL) {
			g2d.drawImage(backgroundImage[backgroundTheme],0,0,getWidth(),getHeight(),null);

			//g2d.fillRect(425,75, 175,150);
			draw.display(g2d, nextBlock);
			if(turnOnGrid)
				draw.displayCheckerImage(g2d, gameBoard);
			
			else
				draw.setBackground(g2d, gameBoard, Color.BLACK);
			draw.display(g2d, gameBoard);
			draw.display(g2d, gameBlock);
			g2d.fillRoundRect(425,275, 175,300,30,30);

			//g2d.fillRect(getImage("background_crack.jpg",0,0,175,300,null));
			//g2d.drawImage(backgroundImage[9], 425, 275, 175, 300, null);
		}
		else {

			g2d.drawImage(backgroundImage[backgroundTheme],0,0,getWidth(),getHeight(),null);
			draw.display(g2d, nextBlock, blockImage[gameTheme]);
			
			if(turnOnGrid)
				draw.displayCheckerImage(g2d, gameBoard);
			
			else
				draw.setBackground(g2d, gameBoard,Color.BLACK);
			
			draw.display(g2d, gameBoard, blockImage[gameTheme]);
			draw.display(g2d, gameBlock, blockImage[gameTheme]);
			g2d.fillRoundRect(425,275, 175,300,30,30);

		}
		
		displayGameStatistics(g2d);
		displayGameMessage(g2d);
		
		
	}
	public void addObjectToBackground() {
		gameBoard.addObject(gameBlock);
		setBlocks();
	}
	public void updateTetris() {
		
		int[] nextGameBlock = GameBlocks.getBlock(blockType, rotateBlock);
		
		gameBlock.setObject(nextGameBlock, BLOCK_WIDTH, BLOCK_HEIGHT,BLOCK_SIZE);
		gameBlock.setPosition(blockLocateX, blockLocateY);
	}
	
	public void setNextBlock() {
		
		Random randomizer = new Random();
		nextBlockType = randomizer.nextInt(AMOUNT_OF_BLOCKS + 1);
		
		int[] nextGameBlock = GameBlocks.getBlock(nextBlockType, 0);
		
		nextBlock.setObject(nextGameBlock,BLOCK_WIDTH, BLOCK_HEIGHT,BLOCK_SIZE);
		nextBlock.setPosition(NEXT_BLOCK_POS_X ,NEXT_BLOCK_POS_Y);
	}
	public void setBlocks() {
		rotateBlock = 0;
		blockLocateX = BLOCK_POS_X;
		blockLocateY = BLOCK_POS_Y;
		
		blockType = nextBlockType;
		setNextBlock();

	}
	@Override
	public void keyPressed(KeyEvent e) {
		
		int tempRotateBlock = rotateBlock;
		int tempBlockLocateX = blockLocateX;
		int tempBlockLocateY = blockLocateY;
		
		int keyPress = e.getKeyCode();
		
		if(justStarted || quitGame) {
			keyPress = 0;
			justStarted = false;
			endGame = true;
		
		}
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
			if(pauseGame) 
				break;
			
			while(gameBoard.inBound(gameBlock) && !gameBoard.hasObject(gameBlock)) {
				tempBlockLocateY = blockLocateY;
				blockLocateY += BLOCK_SIZE;
				updateTetris();
			}
			
			blockLocateY = tempBlockLocateY;
			updateTetris();
			addObjectToBackground();
			
			if(soundOn)
				playSound(0);
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
		case KeyEvent.VK_ESCAPE:
		case KeyEvent.VK_P:
			escKeyPress = !escKeyPress;
			pauseGame = escKeyPress;//!pauseGame;
			break;
		}
		
		if(pauseGame ) {
			rotateBlock = tempRotateBlock;
			blockLocateX = tempBlockLocateX;
			blockLocateY = tempBlockLocateY;
		}
		
		else {
			updateTetris();
			if(gameBoard.inBound(gameBlock) && !gameBoard.hasObject(gameBlock)) 
				this.repaint();
			
			else {
				rotateBlock = tempRotateBlock;
				blockLocateX = tempBlockLocateX;
				blockLocateY = tempBlockLocateY;
				updateTetris();	
			}
		}


	}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (pauseGame || animateTime > 0 || justStarted) 
				repaint();
		else if (!quitGame) 
			dropBlocks();
		
	}
	public void dropBlocks() {
		int tempBlockLocateY = blockLocateY;
		
		blockLocateY += BLOCK_SIZE;
		updateTetris();
		
		if(gameBoard.inBound(gameBlock) && !gameBoard.hasObject(gameBlock)) 
			repaint();
		
		else {
			blockLocateY = tempBlockLocateY;
			updateTetris();
			
			if(gameBoard.hasObject(gameBlock)) 
				quitGame = true;
			else 
				addObjectToBackground();
			repaint();
		}
	}
	public void changeBkgdTheme() {
		if(backgroundTheme >= backgroundImage.length - 1)
			backgroundTheme = 0;
		else
			backgroundTheme ++;
	}
	public void changeGameTheme() {

		if(gameTheme >= THEMES_TOTAL)
			gameTheme = 0;
		else
			gameTheme ++;
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
		if(animateTime > 0)
			gameTimer.setDelay(50);
		else
		gameTimer.setDelay(gameSpeed);
	}
}

class Draw {
	//private boolean bGridOn;
	private boolean bImage;
	Draw() {
	//	bGridOn = false;
		
	}
	/*
	public void displayGrid(boolean bGridOn) {
		this.bGridOn = bGridOn;
	}
	*/
	public void showAllImage(boolean bImage) {
		this.bImage = bImage;
	}
	public void setBackground(Graphics2D g, GameObject gameObject, Color hue) {
		int x = gameObject.getOutlineX();
		int y = gameObject.getOutlineY();
		
		int horizontalBlockAmount = gameObject.getOutlineWidth();
		int verticalBlockAmount = gameObject.getOutlineHeight();
		g.setColor(hue);
		g.fillRoundRect(x, y, horizontalBlockAmount, verticalBlockAmount,30,30);
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
				//if(bGridOn)
					//g.drawRect(locate_X,locate_Y, blockSize, blockSize);
				
				if(bImage)
					if(blockIndex < 0)
						blockIndex *= -1;
				if(blockIndex > 0)
					
					displayImage(g,locate_X,locate_Y,blockSize,blockIndex);
				
				locate_X += blockSize;
				index ++;
			}
			locate_X = gameObject.getPosX();
			locate_Y += blockSize;
		}
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
				
				
				if(bImage)
					if(blockIndex < 0)
						blockIndex *= -1;
				
				if(blockIndex > 0)
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
		
		int horizontalBlockAmount = gameObject.getOutlineWidth();
		int verticalBlockAmount = gameObject.getOutlineHeight();

		g.drawRect(x, y, horizontalBlockAmount, verticalBlockAmount);
		
	}
	private void displayImage(Graphics2D g, int locate_X, int locate_Y, int size,int index) {
		
		switch(index) {
		case 1:
			g.setColor(new Color(31,143,225));//Color.CYAN);
			break;
		case 2:
			g.setColor(new Color(252,191,16));//Color.YELLOW);
			break;	
		case 3:
			g.setColor(new Color(48,47,193));//Color.BLUE);
			break;
		case 4:
			g.setColor(new Color(247,105,31));//Color.ORANGE);
			break;
		case 5:
			g.setColor(new Color(144,49,147));//Color.MAGENTA);
			break;
		case 6:
			g.setColor(new Color(42,166,59));//Color.GREEN);
			break;	
		case 7:
			g.setColor(new Color(230,0,28));//Color.RED);
			break;
		}
		
		g.fillRoundRect(locate_X, locate_Y, size, size,10,10);
		g.setColor(Color.BLACK);
		g.drawRoundRect(locate_X, locate_Y, size, size,10,10);
		
	}
	
	
	
	
}
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
	public void dropBlock() {
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

