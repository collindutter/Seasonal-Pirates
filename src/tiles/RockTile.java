package tiles;
import java.io.IOException;
import javax.imageio.ImageIO;

public class RockTile extends Tile
{
	public RockTile()
	{
		try
		{
			tileImages[Tile.ROCK_TILE] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/rockTile.png"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	
		speedLimiter = 1;
		boatSpeedLimiter = 0;
		cost = 1000;
		walkable = false;
		sailable = false;
		miniMapTileColor = 0x404040;
	}	
}
