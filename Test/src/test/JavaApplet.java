package test;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JavaApplet extends JApplet{
	
	private static final long serialVersionUID = 1L;
	private JLabel label = new JLabel("This is a Java applet");
	 
	    public void init() {
	        setLayout(new FlowLayout());
	        label.setFont(new Font("Arial", Font.BOLD, 18));
	        label.setForeground(Color.RED);
	        add(label);
	    }
	 
	    public void drawText(String text) {
	        label.setText(text);
	    }
	    public int draw() {
	    	new GameTable();
	    	return 7544;
	    }

}

class GameTable extends JPanel {
	private static final long serialVersionUID = 1L;
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		Graphics2D g2d = (Graphics2D) g;
		
		g.fillOval(0, 0, 200, 200);
	}
}
