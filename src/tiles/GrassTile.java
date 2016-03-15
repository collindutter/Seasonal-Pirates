package tiles;
import game.Utility;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GrassTile extends Tile
{
	BufferedImage image;
	
	
	public GrassTile()
	{
		try
		{
			tileImages[GRASS_TILE_0] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/GrassTextures/grassTile00.png"));
			tileImages[GRASS_TILE_1] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/GrassTextures/grassTile01.png"));
			tileImages[GRASS_TILE_2] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/GrassTextures/grassTile02.png"));
			tileImages[GRASS_TILE_3] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/GrassTextures/grassTile03.png"));
//			image = ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/GrassTextures/swampyGrass0.png"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		speedLimiter = 1;
		boatSpeedLimiter = 0;
		cost = 80;
		walkable = true;
		sailable = false;
		miniMapTileColor = 0x007F0E;
		
	}
	
	public void render(double col, double row, int ID, Graphics g)
	{
//		if(!Game.getWorld().isTileOccupied((int)(col + Game.getWorld().getCamera().getCol()), (int)(row + Game.getWorld().getCamera().getRow())))
			g.drawImage(tileImages[ID], (int)Utility.toPixels(col), (int)Utility.toPixels(row), Tile.TILE_SIZE, Tile.TILE_SIZE, null);
	}
}