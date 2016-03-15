package screen;

import game.Game;
import game.minimap.MiniMap;
import game.minimap.WayPoint;

import java.awt.Graphics;

public class MiniMapScreen extends Screen
{
	private MiniMap miniMap;
	WayPoint p;
	
	public MiniMapScreen()
	{
		key = "MiniMapScreen";
		
		miniMap = new MiniMap();
		p = new WayPoint(miniMap, 500, 500);
	}
	
	public static boolean justAccessed = false;
	
	public void update(float delta)
	{
		
		if(Game.input.miniMapOpened && !justAccessed)
		{
			WorldScreen.justAccessed = true;
			Game.setScreen("WorldScreen");
		} else if(!Game.input.miniMapOpened)
			justAccessed = false;
		p.update(delta);
		Game.screens.get("WorldScreen").update(delta);
		miniMap.update(delta);
		super.update(delta);
	}
	
	public void render(Graphics g)
	{
		miniMap.renderTiles(g);
		p.render(g);
		super.render(g);
	}
	
	public MiniMap getMiniMap()
	{
		return miniMap;
	}
}
