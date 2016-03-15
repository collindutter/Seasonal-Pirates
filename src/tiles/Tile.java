package tiles;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import entity.Entity;
import entity.Vessel;
import game.Utility;

public class Tile
{
	private double x, y;
	public static byte TILE_SIZE = 32;
	public static final byte //64 MAX
							NULL_TILE = 0,
							GRASS_TILE_0 = 01,
							GRASS_TILE_1 = 02,
							GRASS_TILE_2 = 03,
							GRASS_TILE_3 = 04,
							
							DIRT_TILE = 05,
							MUD_TILE = 06,
							DIRT_PATH_TILE = 07,
							
							SAND_TILE_0 = 10,
							SAND_TILE_1 = 11,
							SAND_TILE_2 = 12,
							SAND_TILE_3 = 13,
							SAND_TILE_4 = 14,
							
							SHALLOW_WATER_TILE = 20,
							OCEAN_TILE = 21,
							LAVA_TILE = 22,
	
							ROCK_TILE = 30,
							LAVA_ROCK_TILE = 31,
							
							PALM_TREE_TILE = 40,
							
							X_TILE = 50;
	
	public static SandTile sandTile = new SandTile();
	public static DirtTile dirtTile = new DirtTile();
	public static MudTile mudTile = new MudTile();
	public static DirtPathTile dirtPathTile = new DirtPathTile();
	public static OceanTile oceanTile = new OceanTile();
	public static GrassTile grassTile = new GrassTile();
	public static ShallowWaterTile shallowWaterTile = new ShallowWaterTile();
	public static RockTile rockTile = new RockTile();
	public static LavaRockTile lavaRockTile = new LavaRockTile();
	public static XTile xTile = new XTile();
	public static PalmTreeTile palmTreeTile = new PalmTreeTile();
	public static LavaTile lavaTile = new LavaTile();
	
	public int cost = 1;
	
	protected BufferedImage[] tileImages = new BufferedImage[200];
//	protected BufferedImage sheet
	
	public float speedLimiter = 1;
	public float boatSpeedLimiter = 0;
	
	protected boolean walkable;
	protected boolean sailable;

	public static final int waterAnimationSpeed = 30;
	
	public int miniMapTileColor; 
	
	public void render(double col, double row, int ID, Graphics g)
	{
//		if(!Game.getWorld().isTileOccupied((int)(col + Game.getWorld().getCamera().getCol()), (int)(row + Game.getWorld().getCamera().getRow())))
			g.drawImage(tileImages[ID], (int)Utility.toPixels(col), (int)Utility.toPixels(row), Tile.TILE_SIZE, Tile.TILE_SIZE, null);
	}
	
	public float getSpeedLimiter(Entity e)
	{
		if(e instanceof Vessel)
		{
			return boatSpeedLimiter;
		}
		return speedLimiter;
	}
	
	//gets and sets
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public int getCost()
	{
		return cost;
	}
	
	public boolean isWalkable()
	{
		return walkable;
	}
	
	public boolean isSailable()
	{
		return sailable;
	}
}