package game.minimap;

import game.Game;
import game.Utility;

import java.awt.Color;
import java.awt.Graphics;

import screen.MiniMapScreen;

public class WayPoint
{
	private double col, row, renderCol, renderRow;
	private MiniMap miniMap;
	
	public WayPoint(MiniMap map, double col, double row)
	{
		this.col = col;
		this.row = row;
		
		MiniMapScreen m = (MiniMapScreen)Game.screens.get("MiniMapScreen");
		miniMap = map;
	}
	
	public void update(float delta)
	{
		renderCol = col - miniMap.getMiniMapCamera().getCol();
		renderRow = row - miniMap.getMiniMapCamera().getRow();
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.RED);
		g.fillOval((int)(renderCol * miniMap.getMiniMapTileSize()), (int)(renderRow * miniMap.getMiniMapTileSize()), (int)(20 / miniMap.getMiniMapTileSize()), (int)(20 / miniMap.getMiniMapTileSize()));
	}
}
