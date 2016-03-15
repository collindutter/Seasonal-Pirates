package tiles;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PalmTreeTile extends Tile
{
	public PalmTreeTile()
	{
		try
		{
			tileImages[Tile.PALM_TREE_TILE] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/palmTreeTile0.png"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		speedLimiter = 1;
		boatSpeedLimiter = 1;
		miniMapTileColor = 0x267F00;
		walkable = false;
		sailable = false;
	}
}