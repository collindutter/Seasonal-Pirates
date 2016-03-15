package tiles;
import game.Game;
import game.Utility;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class OceanTile extends Tile
{
	private BufferedImage[] animationImages= new BufferedImage[24];
	private int animationIndex;
	
	public OceanTile()
	{
		try
		{
			for(int i = 0; i < animationImages.length; i++)
			{
				animationImages[i] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/WaterTextures/Ocean/oceanTile" + i + ".png"));
			}
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		
		speedLimiter = 0f;
		boatSpeedLimiter = 1f;
		miniMapTileColor = 0x0026FF;
		walkable = false;
		sailable = true;
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