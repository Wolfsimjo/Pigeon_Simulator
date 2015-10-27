package View;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Model.Nouriture;
import Model.Pigeon;

public class ScreenMap extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	private static JFrame mainWindow;
	private LevelMap level;

	public static void main(String[] args) {
		ScreenMap manager = new ScreenMap();
		manager.setSize(800, 600);
		mainWindow = new JFrame();

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(manager);

		mainWindow.setContentPane(panel);
		mainWindow.pack();
		mainWindow.setResizable(false);
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);
		mainWindow.setTitle("Pigeons");
		
		manager.start();
	}

	public void start() {
		level = new LevelMap();
		Pigeon p = new Pigeon(50, 50);
		level.addCharacter(p);
		
		Nouriture n = new Nouriture(100, 100, null);
		level.addCharacter(n);
		
		Thread thread = new Thread(this);
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}

	@Override
	public void run() {
		while(true) {
			Render(this.getGraphics());
		}
	}

	private void Render(Graphics g) {
		g.setColor(Color.WHITE);
		//g.fillRect(0, 0, this.getWidth(), this.getHeight());
		level.render(g);
	}

}
