package tiles;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SandTile extends Tile
{
	public SandTile()
	{
		try
		{
			tileImages[SAND_TILE_0] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/SandTextures/sandTile000.png"));
			tileImages[SAND_TILE_1] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/SandTextures/sandTile000.png"));
			tileImages[SAND_TILE_2] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/SandTextures/sandTile000.png"));
			tileImages[SAND_TILE_3] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/SandTextures/sandTile000.png"));
			tileImages[SAND_TILE_4] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/SandTextures/sandTile000.png"));
//			tileImages[SAND_TILE_0] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/SandTextures/sandTile000.png"));
//			tileImages[SAND_TILE_1] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/SandTextures/sandTile01.png"));
//			tileImages[SAND_TILE_2] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/SandTextures/sandTile02.png"));
//			tileImages[SAND_TILE_3] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/SandTextures/sandTile03.png"));
//			tileImages[SAND_TILE_4] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Textures/SandTextures/sandTile04.png"));
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		miniMapTileColor = 0xFFD800;
		speedLimiter = .9f;
		boatSpeedLimiter = 1f;
		cost = 100;
		walkable = true;
		sailable = false;
	}
}