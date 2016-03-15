package tiles;

import java.io.IOException;

import javax.imageio.ImageIO;

public class LavaRockTile extends Tile
{
	public LavaRockTile() 
	{
		try
		{
			tileImages[LAVA_ROCK_TILE] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/lavaRockTile.png"));

		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		speedLimiter = 1;
		boatSpeedLimiter = 0;
		miniMapTileColor = 0x262626;
		walkable = false;
		sailable = false;
	}
}
