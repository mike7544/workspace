package games;


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
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	static final int APP_GAME = 1;
	static final int APP_SCORE = 2;
	static final int APP_MENU = 3;
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
		
		choice = APP_GAME;
		
	}
	
	public void start() {
		appTimer.start();

		switch (choice) {
		case APP_GAME:
			game.repaint();
			game.setVisible(true);
			break;
		case APP_SCORE:
			gameScore.repaint();
			gameScore.setVisible(true);
			break;
		case APP_MENU:
			startMenu.repaint();
			startMenu.setVisible(true);
			break;
		}
	}
	/*
	public void paint(Graphics g) {
		String userName = this.getParameter("user_name");
		g.drawString("Hello " + userName, 300, 100);
	}
	*/
	@Override
	public void actionPerformed(ActionEvent e) {
		choice = startMenu.getChoice();
		startMenu.resetChoice();
		
		if (choice == 1 || choice == 2) {
			startMenu.setVisible(false);
			game.resetGame();
		}
		
		if (game.quit()) {
			game.setVisible(false);
			choice 
			= 3;
		}
		if (gameScore.exit()) {
			gameScore.setVisible(false);
			gameScore.resetExit();
			choice = 3;
		}
		
		this.start();
	}
	

}


class StartMenu extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int BTN_START = 1;
	private static final int BTN_SCORE = 2;

	private JButton btnStart;
	private JButton btnScore;
	private BufferedImage backgroundImage;
	private MyImage image;
	private int choice;

	public StartMenu() {
		
		this.setLayout(null);
		image = new MyImage();
		backgroundImage = image.getImage("background_startmenu.jpg");

		btnStart = new JButton(image.getIcon("btn_start.png"));
		btnStart.setOpaque(false);
		btnStart.setContentAreaFilled(false);
		btnStart.setBorderPainted(false);
		btnStart.setFocusPainted(false);
		btnStart.setBounds(500, 80, 270, 100);

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
		add(btnStart);

		btnScore = new JButton(image.getIcon("btn_score.png"));
		btnScore.setOpaque(false);
		btnScore.setContentAreaFilled(false);
		btnScore.setBorderPainted(false);
		btnScore.setFocusPainted(false);
		btnScore.setBounds(500, 200, 270, 100);
		btnScore.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent event) {
				btnScore.setIcon(image.getIcon("btn_score_mouseover.png"));
			}

			public void mouseExited(MouseEvent event) {
				btnScore.setIcon(image.getIcon("btn_score.png"));
			}

			public void mousePressed(MouseEvent event) {
				choice = BTN_SCORE;
			}
		});

		add(btnScore);

		choice = 0;

	}

	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);


	}

	public void resetChoice() {
		choice = 0;
	}

	public int getChoice() {
		return choice;
	}

}


class GameRecords {
	private String name;
	private int score;
	private int level;
	private Date date;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String toString() {
		return "name = " + name + " score = " + score + " level = " + level
				+ " date = " + date;
	}

}

class GameScore extends JPanel {
	private static final long serialVersionUID = 2L;

	ArrayList<GameRecords> records;
	private boolean endProgram;
	private JButton btnExit;
	private BufferedImage bkgdImage;
	private BufferedImage logoTitleImage;
	private BufferedImage logoMenuImage;
	private MyImage image;
	
	public GameScore() {
		setLayout(null);
		image = new MyImage();
		
		ImageIcon exitIcon = image.getIcon("btn_exit.png");

		btnExit = new JButton(exitIcon);
		btnExit.setOpaque(false);
		btnExit.setContentAreaFilled(false);
		btnExit.setBorderPainted(false);
		btnExit.setFocusPainted(false);
		btnExit.setBounds(300, 525, 200, 50);

		btnExit.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent event) {
				btnExit.setIcon(image.getIcon("btn_exit_mouseover.png"));
				
			}

			public void mouseExited(MouseEvent event) {
				btnExit.setIcon(image.getIcon("btn_exit.png"));
			}

			public void mousePressed(MouseEvent event) {
				endProgram = true;
			}
		});
		add(btnExit);

		endProgram = false;
		records = new ArrayList<>();
		bkgdImage = image.getImage("bkgd_green_grid.jpg");
		logoTitleImage = image.getImage("logo_score_title.png");
		logoMenuImage = image.getImage("logo_score_menu.png");
	}

	public void getTopScore() {
		try {
			getRecords();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public void getRecords() throws ClassNotFoundException, SQLException {
		String queryStatement = "select name, score, level, date "
				+ "from records order by score desc";
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection(
				"jdbc:mysql://localhost/MikeGames", "root", "pumkin");

		PreparedStatement statement = connection
				.prepareStatement(queryStatement);
		ResultSet result = statement.executeQuery();

		int index = 0;
		System.out.println("get records!!!");
		while (result.next()) {
			GameRecords tempRecords = new GameRecords();

			tempRecords.setName(result.getString(1));
			tempRecords.setScore(result.getInt(2));
			tempRecords.setLevel(result.getInt(3));
			tempRecords.setDate(result.getDate(4));

			records.add(index, tempRecords);
			index++;
		}

		if (connection != null)
			connection.close();
		if (statement != null)
			statement.close();
		if (result != null)
			result.close();

	}

	public void displayTopScore(Graphics2D g2d) {
		int x = 0;
		int y = 100;
		int index = 1;

		g2d.drawImage(logoTitleImage, 275, 25, null);
		g2d.drawImage(logoMenuImage, 150, 75, null);
		g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 24));

		if (records.size() > 0)
			for (int i = 0; i < 10; i++) {

				y += 40;
				x = 150;

				g2d.drawString(index + "", x - 50, y);

				g2d.drawString(records.get(i).getName(), x, y);
				x += 150;

				g2d.drawString(records.get(i).getScore() + "", x, y);
				x += 170;

				g2d.drawString(records.get(i).getLevel() + "", x, y);
				x += 90;
				g2d.drawString(records.get(i).getDate() + "", x, y);

				if (index > records.size() - 1)
					break;
				else
					index++;

			}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		Graphics2D g2d = (Graphics2D) g;

		if (records.size() == 0) {
			getTopScore();
		}
		g2d.drawImage(bkgdImage, 0, 0, getWidth(), getHeight(), null);

		g2d.setColor(Color.white);
		setRenderingFonts(g2d);

		displayTopScore(g2d);
	}

	private void setRenderingFonts(Graphics2D g2d) {
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		rh.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		g2d.setRenderingHints(rh);
	}

	public boolean exit() {
		return endProgram;
	}

	public void resetExit() {
		endProgram = !endProgram;
		records.clear();
	}
}

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
	private boolean escKeyPress;

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
	private BufferedImage messageImage;

	private BufferedImage logoTextLevel;
	private BufferedImage logoTextLines;
	private BufferedImage logoTextNext;
	private BufferedImage logoTextScore;

	private int animateTime;
	private int blockTheme;
	private int backgroundTheme;
	private MyImage image;
	
	public Tetris() {
		
		gameBlock = new GameObject();
		gameBoard = new GameObject();
		nextBlock = new GameObject();
		blockImage = new BufferedImage[BLOCK_THEMES_TOTAL][7];
		backgroundImage = new BufferedImage[BKGD_TOTAL];
		soundOn = true;

		image = new MyImage();
		loadGameImage();
		setNextBlock();
		setGame();

		gameTimer = new Timer(gameSpeed, this);
		this.addMouseListener(this);
		this.addKeyListener(this);
		this.setFocusable(true);
		this.requestFocus();

	}
	
	public void setGame() {

		setBlocks();

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
		escKeyPress = false;

		turnOnGrid = false;
		justStarted = true;
		quitGame = false;

		overrideCommand = false;

		blockTheme = BLOCK_THEMES_TOTAL;
		backgroundTheme = 0;

		setBlocks();

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
		
		for (int i = 0; i < BKGD_TOTAL; i++)
			backgroundImage[i] = image.getImage(bkgdImage[i]);

		endGameImage = image.getImage("logo_endgame.png");
		messageImage = image.getImage("logo_press_esc.png");
		controlKeyImage = image.getImage("logo_control_keys.png");
		soundOnImage = image.getImage("logo_sound_on.png");
		soundOffImage = image.getImage("logo_sound_off.png");

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
				//"sound56.wav"};
		
		if(soundOn)
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
			System.out.println("can't find " + filename + " file");
		}
		
	}
	public void loadGameSound2(String filename) {
	
	
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
		
		if(countLines > 0) {
			totalScore += countLines * GAME_POINTS;
			totalLines += countLines;
			countLines = 0;
		}
		if(!overrideCommand)
		gameLevel = (totalLines / GAME_MOVE_UP_LEVEL) + 1;
	}
	@Override
	public void paintComponent(Graphics g) {
		this.paintComponents(g);
		Graphics2D g2d =  (Graphics2D) g;
		
		
		updateTetris();
		int tempLevel = gameLevel;
		
		
		gameTimer.start();
		checkGameBoardRow();
	
		if(animateTime == 0) 
			gameBoard.dropBlock();
		else
			animateTime --;

		if(animateTime == 4)
			playSound(1);
		
		setGameStatistics();
		setGameSpeed();
		drawGameSprites(g2d);
		if(tempLevel != gameLevel) 
			changeBkgdTheme();
		

	}
	
	public void pressAnyKey(Graphics g2d) {
		g2d.drawString("Press any key to continue...", 100, 70);

	}
	public void drawGameMessage(Graphics g2d) {
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
		draw.showAllImage(isTimeAnimated());
		draw.setCutOff(75);
		
		if(blockTheme == BLOCK_THEMES_TOTAL) {
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
			draw.display(g2d, nextBlock, blockImage[blockTheme]);
			
			if(turnOnGrid)
				draw.displayCheckerImage(g2d, gameBoard);
			
			else
				draw.setBackground(g2d, gameBoard,Color.BLACK);
			draw.display(g2d, gameBoard, blockImage[blockTheme]);
			draw.display(g2d, gameBlock, blockImage[blockTheme]);
			g2d.fillRoundRect(425,275, 175,300,30,30);

		}
		
		drawGameStatistics(g2d);
		drawGameMessage(g2d);
		
		
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
		boolean bRotate = false;
		
		if(endGame) {
			keyPress = 0;
			quitGame = false;
		}
		
		if (justStarted) {
			keyPress = 0;
			justStarted = false;

		} else if (quitGame) {
			keyPress = 0;
			endGame = true;
			savePlayerScore();
			System.out.println("save records !!!");
		}

		switch (keyPress) {

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

			while (gameBoard.inBound(gameBlock)
					&& !gameBoard.hasObject(gameBlock)) {
				tempBlockLocateY = blockLocateY;
				blockLocateY += BLOCK_SIZE;
				updateTetris();
			}

			blockLocateY = tempBlockLocateY;
			updateTetris();
			addObjectToBackground();
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
		case KeyEvent.VK_L:
			gameLevel++;
			overrideCommand = true;
			break;
		case KeyEvent.VK_ESCAPE:
		case KeyEvent.VK_P:
			escKeyPress = !escKeyPress;
			pauseGame = escKeyPress;// !pauseGame;
			break;
		}

		if (pauseGame) {
			rotateBlock = tempRotateBlock;
			blockLocateX = tempBlockLocateX;
			blockLocateY = tempBlockLocateY;
		}

		else {
			updateTetris();
			if (gameBoard.inBound(gameBlock) && !gameBoard.hasObject(gameBlock)) {
				if(bRotate)
					playSound(2);

				this.repaint();
			}
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
		
		if (pauseGame || animateTime > 0 || justStarted)
			repaint();
		else if (!quitGame)
			dropBlocks();
		

	}

	public void dropBlocks() {
		int tempBlockLocateY = blockLocateY;

		blockLocateY += BLOCK_SIZE;
		updateTetris();

		if (gameBoard.inBound(gameBlock) && !gameBoard.hasObject(gameBlock))
			repaint();

		else {
			blockLocateY = tempBlockLocateY;
			updateTetris();

			if (gameBoard.hasObject(gameBlock)) {
				quitGame = true;
				playSound(3);

			}
			else
				addObjectToBackground();
			repaint();
		}
	}
	
	public void changeGameTheme() {
		changeBkgdTheme();
		changeBlockTheme();
	}

	public void changeBkgdTheme() {
		if (backgroundTheme >= backgroundImage.length - 1)
			backgroundTheme = 0;
		else
			backgroundTheme++;
	}

	public void changeBlockTheme() {

		if (blockTheme >= BLOCK_THEMES_TOTAL)
			blockTheme = 0;
		else
			blockTheme++;
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
		
			
		if (animateTime > 0)
			gameTimer.setDelay(50);

		else
			gameTimer.setDelay(gameSpeed);
	}

	public static java.sql.Date getDate() {
		long currentDate = System.currentTimeMillis();
		return new java.sql.Date(currentDate);
	}
	public void savePlayerScore() {
		if (totalScore > 0)
			try {
				recordScore();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	public void recordScore() throws SQLException, ClassNotFoundException {
		String playerName = "unknown";
		String queryStatement = "insert into records (name, score, level, date)"
				+ "value (?,?,?,?)";

		//Class.forName("com.mysql.jdbc.Driver");
		Connection connect = DriverManager.getConnection(
				"jdbc:mysql://localhost/MikeGames", "root", "pumkin");
		PreparedStatement statement = connect.prepareStatement(queryStatement);

		statement.setString(1, playerName);
		statement.setInt(2, totalScore);
		statement.setInt(3, gameLevel);
		statement.setDate(4, getDate());
		statement.execute();

		if (connect != null)
			connect.close();
		if (statement != null)
			statement.close();

	}

	@Override
	public void mouseClicked(MouseEvent e) {

		boolean bHar = e.getX() > 475 && e.getX() < 499;
		boolean bVer = e.getY() > 525 && e.getY() < 560;

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
				
				if(bImage && blockIndex < 0)
						blockIndex *= -1;
				if(blockIndex > 0)
					if(locate_Y >= cutOff) ///
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
					if(locate_Y >= cutOff) //
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

