package View;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import Model.Character;


public class LevelMap {
	
	private List<Character> characters;
	
	public LevelMap() {
		characters = new ArrayList<Character>();
	}
	
	public void render(Graphics g)
	{
		for(int i = 0; i < characters.size(); i++)
		{
			characters.get(i).render(g);
		}
	}
	
	public void addCharacter(Character c) {
		if(c != null)
			characters.add(c);
	}
	
}
