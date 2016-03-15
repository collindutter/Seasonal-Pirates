package game;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;
import tiles.Tile;
import entity.Entity;
import entity.Galleon;
import entity.Pirate;
import entity.RowBoat;
import game.minimap.MiniMap;

public class World
{
	private BufferedImage worldMap;
	private int worldWidth, worldHeight; //width and height of the world in tiles
	
	private byte[][] tiles;
	private Camera camera;
//	private MiniMap minimap;
	
	private ArrayList<Entity> entityList = new ArrayList<Entity>();
	private Iterator<Entity> entityIterator;
	
	public World()
	{
		camera = new Camera();
		for(int i = 0; i < 5; i++)
		{
			entityList.add(new Pirate(60 + Utility.negToPositive(5), 60 + Utility.negToPositive(5)));
		}
//		entityList.add(new Pirate(630, 200));
//		entityList.add(new Galleon(50, 50));
//		entityList.add(new RowBoat(45, 45));
		
		try
		{
			worldMap = ImageIO.read(ClassLoader.getSystemResourceAsStream("WorldMaps/PiratesMap.png"));
		}catch(IOException e)
		{
			System.err.println("Map name invalid!");
		}
		
		worldWidth = worldMap.getWidth();
		worldHeight = worldMap.getHeight();
		
		tiles = new byte[worldWidth][worldHeight];
	
//		minimap = new MiniMap();
		
//		for(int col = (int) camera.getCol(); col < camera.getCol() + Utility.toTiles(Game.WIDTH); col++)
//		{
//			for(int row = (int) camera.getRow(); row < camera.getRow() + Utility.toTiles(Game.HEIGHT); row++)
//			{	
//				generateTile(col, row);
//			}
//		}
		for(int col = 0; col < worldWidth; col++)
		{
			for(int row = 0; row < worldHeight; row++)
			{	
				generateTile(col, row);
			}
		}
		//printGraph();
	}
	
	public void generateTile(int col, int row)
	{
		int pixelColor;
		
		pixelColor = worldMap.getRGB(col, row); 
		pixelColor = pixelColor & 0xFFFFFF;//removes alpha value
		
		switch(pixelColor)
		{
			case 0xFFD800://YELLOW
				switch((int)(Math.random() * 5))
				{
					case 0:
						tiles[col][row] = Tile.SAND_TILE_0;
						break;
					case 1:
						tiles[col][row] = Tile.SAND_TILE_1;
						break;
					case 2:
						tiles[col][row] = Tile.SAND_TILE_2;
						break;
					case 3:
						tiles[col][row] = Tile.SAND_TILE_3;
						break;
					case 4:
						tiles[col][row] = Tile.SAND_TILE_4;
						break;
				}
				break;
			case 0x0026FF://BLUE
				tiles[col][row] = Tile.OCEAN_TILE;
				break;
			case 0x007F0E://GREEN
				switch((int)Math.floor(Math.random() * 4))
				{
					case 0:
						tiles[col][row] = Tile.GRASS_TILE_0;
						break;
					case 1:
						tiles[col][row] = Tile.GRASS_TILE_1;
						break;
					case 2:
						tiles[col][row] = Tile.GRASS_TILE_2;
						break;
					case 3:
						tiles[col][row] = Tile.GRASS_TILE_3;
						break;
				}
				break;
			case 0x855E42://LIGHT BROWN
				tiles[col][row] = Tile.DIRT_TILE;
				break;
			case 0x463018://DARK BROWN
				tiles[col][row] = Tile.MUD_TILE;
				break;
			case 0x0094FF://LIGHT BLUE
				tiles[col][row] = Tile.SHALLOW_WATER_TILE;
				break;
			case 0xFF0000://LIGHT BLUE
				tiles[col][row] = Tile.X_TILE;
				break;
			case 0xFF6A00://ORANGE
				tiles[col][row] = Tile.LAVA_TILE;
				break;
			case 0x262626://DARK GREY
				tiles[col][row] = Tile.LAVA_ROCK_TILE;
				break;
			case 0xAF7F0E://VOMIT
				tiles[col][row] = Tile.DIRT_PATH_TILE;
				break;
			case 0x404040://GREY
				tiles[col][row] = Tile.ROCK_TILE | (byte)0x80;
				break;
			case 0x00FF21://LIGHT GREEN
				tiles[col][row] = Tile.PALM_TREE_TILE | (byte)0x80;
				break;
			default:
				System.err.println("A pixel from the WorldMap was an invalid color.");
				System.exit(1);
				break;
		}
	}
	
	public void update(float delta)
	{
		double width = Utility.toTiles(Game.WIDTH);
		double height = Utility.toTiles(Game.HEIGHT);
		
		Tile.TILE_SIZE = (byte)Game.input.wheelClicks;
		
		double width2 = Utility.toTiles(Game.WIDTH);
		double height2 = Utility.toTiles(Game.HEIGHT);
		
		camera.col -= (width2 - width) / 2;
		camera.row -= (height2 - height) / 2;
		
		camera.update(delta);
		
//		for(int col = (int) camera.getCol(); col < camera.getCol() + Utility.toTiles(Game.WIDTH); col++)
//		{
//			for(int row = (int) camera.getRow(); row < camera.getRow() + Utility.toTiles(Game.HEIGHT); row++)
//			{	
//				if(col <= 0 || col >= worldWidth)
//					continue;
//				if(row <= 0 || row >= worldHeight)
//					continue;
//				
//				if((tiles[col][row] & 0x7F) == Tile.NULL_TILE)
//					generateTile(col, row);
//			}
//		}
		
		for(int i = 0; i < entityList.size(); i++)
		{
			Entity e = entityList.get(i);
			if(!e.update(delta))
				entityList.remove(e);
		}
		
		Game.input.rightMousePressed = false;
		Tile.oceanTile.update();
		Tile.shallowWaterTile.update();
		Tile.lavaTile.update();
	}
	
	public void render(Graphics g)
	{
		for(int col = (int) camera.getCol() - 1; col < camera.getCol() + Utility.toTiles(Game.WIDTH); col++)
		{
			for(int row = (int) camera.getRow() - 1; row < camera.getRow() + Utility.toTiles(Game.HEIGHT); row++)
			{

				double renderCol = col - camera.getCol();
				double renderRow = row - camera.getRow();
				
				if(col < 0 || col >= worldWidth)
				{
					Tile.oceanTile.render(renderCol, renderRow, 0, g);
					continue;
				}
				if(row < 0 || row >= worldHeight)
				{
					Tile.oceanTile.render(renderCol, renderRow, 0, g);
					continue;
				}
				
				byte id = (byte) (tiles[col][row] & 0x7F);
				switch(id)
				{
				case Tile.SAND_TILE_0:
				case Tile.SAND_TILE_1:
				case Tile.SAND_TILE_2:
				case Tile.SAND_TILE_3:
				case Tile.SAND_TILE_4:
					Tile.sandTile.render(renderCol, renderRow, id, g);
					break;
				case Tile.GRASS_TILE_0:
				case Tile.GRASS_TILE_1:
				case Tile.GRASS_TILE_2:
				case Tile.GRASS_TILE_3:
					Tile.grassTile.render(renderCol, renderRow, id, g);
					break;
				case Tile.DIRT_TILE:
					Tile.dirtTile.render(renderCol, renderRow, id, g);
					break;
				case Tile.MUD_TILE:
					Tile.mudTile.render(renderCol, renderRow, id, g);
					break;
				case Tile.DIRT_PATH_TILE:
					Tile.dirtPathTile.render(renderCol, renderRow, id, g);
					break;
				case Tile.SHALLOW_WATER_TILE:
					Tile.shallowWaterTile.render(renderCol, renderRow, id, g);
					break;
				case Tile.OCEAN_TILE:
					Tile.oceanTile.render(renderCol, renderRow, id, g);
					break;
				case Tile.LAVA_TILE:
					Tile.lavaTile.render(renderCol, renderRow, id, g);
					break;
				case Tile.X_TILE:
					Tile.xTile.render(renderCol, renderRow, id, g);
					break;
				case Tile.PALM_TREE_TILE:
					Tile.palmTreeTile.render(renderCol, renderRow, id, g);
					break;
				case Tile.ROCK_TILE:
					Tile.rockTile.render(renderCol, renderRow, id, g);
					break;
				case Tile.LAVA_ROCK_TILE:
					Tile.lavaRockTile.render(renderCol, renderRow, id, g);
					break;
				}
			}
		}


		for(Entity e : entityList)
		{
			e.render(g);
		}

		camera.render(g);
	}
	
	//getters and setters
	public void setTileOccupied(double col, double row, boolean occupied)
	{
		if(occupied)
			tiles[(int)Math.round(col)][(int)Math.round(row)] = (byte)(tiles[(int)Math.round(col)][(int)Math.round(row)] | 0x80);
		else
			tiles[(int)Math.round(col)][(int)Math.round(row)] = (byte)(tiles[(int)Math.round(col)][(int)Math.round(row)] & 0x7F);
	}
	
	public void setTileDiscovered(double col, double row, boolean discovered)
	{
		if(discovered)
			tiles[(int)Math.round(col)][(int)Math.round(row)] = (byte)(tiles[(int)Math.round(col)][(int)Math.round(row)] | 0x40);
		else
			tiles[(int)Math.round(col)][(int)Math.round(row)] = (byte)(tiles[(int)Math.round(col)][(int)Math.round(row)] & 0xBF);
	}
	
	public byte[][] getTiles()
	{
		return tiles;
	}
	
	public Tile getTile(double col, double row)
	{
		return getTile(tiles[(int)Math.round(col)][(int)Math.round(row)]);
	}
	
	public Tile getTile(byte tileID)
	{
		switch(tileID & 0x7F)
		{
			case Tile.SAND_TILE_0:
			case Tile.SAND_TILE_1:
			case Tile.SAND_TILE_2:
			case Tile.SAND_TILE_3:
			case Tile.SAND_TILE_4:
				return Tile.sandTile;
			case Tile.GRASS_TILE_0:
			case Tile.GRASS_TILE_1:
			case Tile.GRASS_TILE_2:
			case Tile.GRASS_TILE_3:
				return Tile.grassTile;
			case Tile.DIRT_TILE:
				return Tile.dirtTile;
			case Tile.MUD_TILE:
				return Tile.mudTile;
			case Tile.DIRT_PATH_TILE:
				return Tile.dirtPathTile;
			case Tile.SHALLOW_WATER_TILE:
				return Tile.shallowWaterTile;
			case Tile.OCEAN_TILE:
				return Tile.oceanTile;
			case Tile.LAVA_TILE:
				return Tile.lavaTile;
			case Tile.X_TILE:
				return Tile.xTile;
			case Tile.PALM_TREE_TILE:
				return Tile.palmTreeTile;
			case Tile.ROCK_TILE:
				return Tile.rockTile;
			case Tile.LAVA_ROCK_TILE:
				return Tile.lavaRockTile;
		}
		return null;
	}
	
	public byte getTileID(double col, double row)
	{
		return tiles[(int)Math.round(col)][(int)Math.round(row)];
	}
	
	public boolean isTileOccupied(double col, double row)
	{
		return (getTileID(col, row) & 0x80) == 0x80;
	}
	
	public boolean isTileDiscovered(double col, double row)
	{
		return ((getTileID(col, row) >> 6) & 1) == 1;
	}
	
	public ArrayList<Entity> getEntityList()
	{
		return entityList;
	}
	
	public Iterator<Entity> getEntityIterator()
	{
		return entityIterator;
	}

	public Camera getCamera()
	{
		return camera;
	}

	public int getWorldWidth()
	{
		return worldWidth;
	}
	
	public void printGraph()
	{
		for(int k=0; k<tiles.length; k++)
		{
			System.out.println();
			for(int o=0; o<tiles[0].length; o++)
			{
				if(getTile(k,o)==null)
					System.out.print(".");
				else
					System.out.print("O");
				System.out.print(" ");
			}
		}
	}

	public int getWorldHeight()
	{
		return worldHeight;
	}
}
/*Colors for tiles on worldMap
 * 
 * OceanTile = Dark blue = 0026FF
 * ShallowWaterTile = Light blue = 0094FF
 * Sand = FFD800
 * Green = 007F0E
 */