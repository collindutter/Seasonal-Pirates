package tiles;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DirtPathTile extends Tile
{
	public DirtPathTile()
	{
		try
		{
			tileImages[DIRT_PATH_TILE] =  ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/dirtPathTile.png"));
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		
		speedLimiter = 1;
		boatSpeedLimiter = 0;
		cost = 40;
		walkable = true;
		sailable = false;
		miniMapTileColor = 0xAF7F0E;
	}
}
