package tiles;
import java.io.IOException;
import javax.imageio.ImageIO;

public class XTile extends Tile
{
	public XTile()
	{
		try
		{
			tileImages[X_TILE] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/sandXTile.png"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		speedLimiter = Tile.sandTile.speedLimiter;
		boatSpeedLimiter = Tile.sandTile.boatSpeedLimiter;
		miniMapTileColor = 0xFF0000;
		walkable = true;
		sailable = false;
	}
}