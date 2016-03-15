package tiles;
import java.io.IOException;
import javax.imageio.ImageIO;

public class DirtTile extends Tile
{
	public DirtTile()
	{
		try
		{
			tileImages[Tile.DIRT_TILE] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/dirtTile.png"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		speedLimiter = 1;
		boatSpeedLimiter = 0;
		cost = 80;
		walkable = true;
		sailable = false;
		miniMapTileColor = 0x855E42;
	}
}