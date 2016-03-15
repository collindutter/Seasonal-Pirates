package tiles;
import game.Game;
import game.Utility;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ShallowWaterTile extends Tile
{
	private BufferedImage[] animationImages= new BufferedImage[24];
	private int animationIndex;
	
	public ShallowWaterTile()
	{
		try
		{
			for(int i = 0; i < animationImages.length; i++)
			{
				animationImages[i] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/WaterTextures/ShallowWater/shallowWaterTile" + i + ".png"));
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		speedLimiter = .4f;
		boatSpeedLimiter = 1f;
		cost = 180;
		walkable = true;
		sailable = true;
		miniMapTileColor = 0x0094FF;
	}
	
	public void update()
	{
		if(Game.ticks % waterAnimationSpeed == 0 || Game.ticks == 0)
		{
			if(animationIndex == animationImages.length - 1)
				animationIndex = 0;
			else
				animationIndex++;
		}
	}
	
	public void render(double col, double row, int ID, Graphics g)
	{
		g.drawImage(animationImages[animationIndex], (int)Utility.toPixels(col), (int)Utility.toPixels(row), Tile.TILE_SIZE, Tile.TILE_SIZE, null);
	}
}