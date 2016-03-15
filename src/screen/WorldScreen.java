package screen;

import java.awt.Graphics;

import game.Game;
import game.World;

public class WorldScreen extends Screen
{
	private World world;
	
	public WorldScreen(World world)
	{
		key = "WorldScreen";
		this.world = world;
	}
	
	public static boolean justAccessed = false;
	
	public void update(float delta)
	{
		world.update(delta);
		
		if(Game.input.miniMapOpened && !justAccessed)
		{
			MiniMapScreen.justAccessed = true;
			Game.setScreen("MiniMapScreen");
		}
		else if(!Game.input.miniMapOpened)
			justAccessed = false;
		
		if(Game.input.esc)
			Game.setScreen("PauseScreen");
	}
	
	public void render(Graphics g)
	{
		world.render(g);
	}
}
