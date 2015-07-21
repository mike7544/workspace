package games;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JPanel;

////////////////////////////////////////////////////////////////
//
//Start Menu main program
//
////////////////////////////////////////////////////////////////

public class StartMenu extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int BTN_START = 1;

	private JButton btnStart;
	private BufferedImage backgroundImage;
	private LoadImage image;
	private int choice;

	public StartMenu() {

		this.setLayout(null);
		image = new LoadImage();

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
