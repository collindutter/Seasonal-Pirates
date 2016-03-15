package tiles;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MudTile extends Tile
{
	public MudTile()
	{
		try
		{
			tileImages[Tile.MUD_TILE] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/mudTile.png"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		speedLimiter = .6f;
		boatSpeedLimiter = 0f;
		cost = 140;
		walkable = true;
		sailable = false;
		miniMapTileColor = 0x463018;
	}
}
