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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;

import javax.swing.Timer;

////////////////////////////////////////////////////////////////
//
// Main Tetris Applet
//
////////////////////////////////////////////////////////////////
@SuppressWarnings("serial")
public class TetrisApplet extends JApplet implements ActionListener {

	private Timer appTimer;
	static final int APP_WIDTH = 800;
	static final int APP_HEIGHT = 600;
	static final int APP_GAME = 1;
	static final int APP_MENU = 3;
	private int choice;

	private StartMenu startMenu = null;
	private Tetris game = null;

	public void init() {

		setSize(APP_WIDTH, APP_HEIGHT); // set main applet window size
		appTimer = new Timer(100, this); // timer for main program

		game = new Tetris(); // create tetris video game
		game.setSize(APP_WIDTH, APP_HEIGHT); // set tetris game window size
		game.setVisible(false);
		add(game);
		this.addKeyListener(game);

		startMenu = new StartMenu(); // create start menu
		startMenu.setVisible(false);
		add(startMenu);

		this.setFocusable(true);
		this.requestFocus();

		// As soon as the program loads
		// display the tetris video game first.
		choice = APP_GAME;

	}

	public void start() {
		appTimer.start(); // start main applet timer

		switch (choice) {
		case APP_GAME: // make tetris video game visible
			game.repaint();
			game.setVisible(true);
			break;

		case APP_MENU: // make start menu visible
			startMenu.repaint();
			startMenu.setVisible(true);
			break;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		choice = startMenu.getChoice(); // get choice from start menu
		startMenu.resetChoice(); // reset start menu

		if (choice == APP_GAME) { // make tetris game visible
			startMenu.setVisible(false); // and make start menu invisible
			game.resetGame(); // reset all game variables
		}

		if (game.quit()) { // quit tetris game
			game.setVisible(false);
			choice = APP_MENU; // and go back to start menu
		}

		this.start();
	}

}
