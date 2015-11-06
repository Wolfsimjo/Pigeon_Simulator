package View;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import Controler.GameControler;
import Model.Nouriture;
import Model.Pigeon;

public class WindowGame extends BasicGame {
	
	private GameControler gc;
	private GameContainer container;

	public static SpriteSheet spriteN;
	public static SpriteSheet spriteP;
	public static SpriteSheet spriteC;
	public static Animation[] animations = new Animation[8];
	public static Animation[] animationsC = new Animation[8];

	public WindowGame(String title) throws SlickException {
		super(title);
		gc = new GameControler();
	}

	public static void main(String[] args) throws SlickException {
		new AppGameContainer(new WindowGame("Pigeons"), 640, 480, false).start();
	}
	
	private Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
	    Animation animation = new Animation();
	    for (int x = startX; x < endX; x++) {
	        animation.addFrame(spriteSheet.getSprite(x, y), 100);
	    }
	    return animation;
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		this.container = container;
		spriteN = new SpriteSheet("Pigeon_Simulator/sprite/nouriture.png", 64, 64);
		spriteP = new SpriteSheet("Pigeon_Simulator/sprite/pigeon3.png", 64, 64);
		spriteC = new SpriteSheet("Pigeon_Simulator/sprite/child.png", 32, 32);
		this.animations[0] = loadAnimation(spriteP, 0, 4, 0);
	    this.animations[1] = loadAnimation(spriteP, 0, 4, 1);
	    this.animations[2] = loadAnimation(spriteP, 0, 4, 2);
	    this.animations[3] = loadAnimation(spriteP, 0, 4, 3);
		this.animationsC[0] = loadAnimation(spriteC, 0, 3, 0);
	    this.animationsC[1] = loadAnimation(spriteC, 0, 3, 1);
	    this.animationsC[2] = loadAnimation(spriteC, 0, 3, 2);
	    this.animationsC[3] = loadAnimation(spriteC, 0, 3, 3);
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		g.clear();
		g.setBackground(Color.lightGray);
		gc.render(container, g);
	}

	@Override
	public void update(GameContainer arg0, int arg1) throws SlickException {
	}
	
	@Override
    public void keyReleased(int key, char c) {
        if (Input.KEY_ESCAPE == key) {
            container.exit();
        }
    }
	
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		switch (button) {
		case 0:
			try {
				gc.addNouriture(new Nouriture(x, y, gc));
			} catch (SlickException e) {
				e.printStackTrace();
			}
			break;
		case 1:
			try {
				Pigeon p = new Pigeon(x, y, gc);
				gc.addPigeon(p);
				gc.addObserver(p);
			} catch (SlickException e) {
				e.printStackTrace();
			}			
			break;

		default:
			break;
		}
	}
	

}
