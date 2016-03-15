package tiles;

import game.Game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LavaTile extends Tile
{
	private BufferedImage[] animationImages= new BufferedImage[6];
	private int animationIndex;
	BufferedImage image;
	
	public LavaTile()
	{
		try
		{
			for(int i = 0; i < animationImages.length; i++)
			{
				animationImages[i] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/LavaTextures/lavaTile" + i + ".png"));
			}
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		
		speedLimiter = 0f;
		boatSpeedLimiter = 1f;
		miniMapTileColor = 0xFF6A00;
		walkable = false;
		sailable = true;
	}
	
	public void update()
	{
		if(Game.ticks % 40 == 0 || Game.ticks == 0)
		{
			if(animationIndex == animationImages.length - 1)
				animationIndex = 0;
			else
				animationIndex++;
		}
	}
	
	public void render(double col, double row, int ID, Graphics g)
	{
//		if(!Game.getWorld().isTileOccupied((int)(col + Game.getWorld().getCamera().getCol()), (int)(row + Game.getWorld().getCamera().getRow())))
			g.drawImage(animationImages[animationIndex], (int)(col * Tile.TILE_SIZE), (int)(row * Tile.TILE_SIZE), Tile.TILE_SIZE, Tile.TILE_SIZE, null);
	}
}
