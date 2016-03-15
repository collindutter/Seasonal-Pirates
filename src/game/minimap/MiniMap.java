package game.minimap;

import game.Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import tiles.Tile;

public class MiniMap
{
	public MiniMapCamera miniMapCamera;

	private float miniMapTileSize = .75f;
	public static final int MINIMAP_WIDTH = Game.WIDTH; //in pixels
	public static final int MINIMAP_HEIGHT = Game.HEIGHT;
	
	private float sampleRate = 4f;
	
	public boolean hasFocus = false;
	private BufferedImage compass;
	
	public MiniMap()
	{
		miniMapCamera = new MiniMapCamera(this);
		
		try
		{
			compass = ImageIO.read(ClassLoader.getSystemResourceAsStream("compass.png"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void update(float delta)
	{
		miniMapCamera.update(delta);
	}
	
	private Color oceanColor = new Color(Tile.oceanTile.miniMapTileColor); 
	
	public void renderTiles(Graphics g)
	{
		g.setColor(oceanColor);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);	
		
		for(double col = miniMapCamera.getCol() - 1; col < miniMapCamera.getCol() + miniMapCamera.getViewWidth(); col += sampleRate)
		{
			for(double row = miniMapCamera.getRow() - 1; row < miniMapCamera.getRow() + miniMapCamera.getViewHeight(); row += sampleRate)
			{
				double renderCol = col - miniMapCamera.getCol();
				double renderRow = row - miniMapCamera.getRow();
				
				if(col <= 0 || col >= Game.getWorld().getWorldWidth() - 1)
					continue;
				if(row <= 0 || row >= Game.getWorld().getWorldHeight() - 1)
					continue;
				if(Game.getWorld().getTile(col, row) == null)
					continue;
				
				byte tileID = (byte) (Game.getWorld().getTiles()[(int)col][(int)row] & 0x7F);//& 0x3F ignores first two bits
				
				switch(tileID)
				{
					case Tile.OCEAN_TILE:
						continue;
					case Tile.GRASS_TILE_0:
					case Tile.GRASS_TILE_1:
					case Tile.GRASS_TILE_2:
					case Tile.GRASS_TILE_3:
						renderMinimapTile(renderCol, renderRow, Tile.grassTile.miniMapTileColor, g);
						break;
					case Tile.SAND_TILE_0:
					case Tile.SAND_TILE_1:
					case Tile.SAND_TILE_2:
					case Tile.SAND_TILE_3:
					case Tile.SAND_TILE_4:
						renderMinimapTile(renderCol, renderRow, Tile.sandTile.miniMapTileColor, g);
						break;
					case Tile.DIRT_TILE:
						renderMinimapTile(renderCol, renderRow, Tile.dirtTile.miniMapTileColor, g);
						break;
					case Tile.MUD_TILE:
						renderMinimapTile(renderCol, renderRow, Tile.mudTile.miniMapTileColor, g);
						break;
					case Tile.DIRT_PATH_TILE:
						renderMinimapTile(renderCol, renderRow, Tile.dirtPathTile.miniMapTileColor, g);
						break;
					case Tile.SHALLOW_WATER_TILE:
						renderMinimapTile(renderCol, renderRow, Tile.shallowWaterTile.miniMapTileColor, g);
						break;
					case Tile.LAVA_TILE:
						renderMinimapTile(renderCol, renderRow, Tile.lavaTile.miniMapTileColor, g);
						break;
					case Tile.X_TILE:
						renderMinimapTile(renderCol, renderRow, Tile.xTile.miniMapTileColor, g);
						break;
					case Tile.PALM_TREE_TILE:
						renderMinimapTile(renderCol, renderRow, Tile.palmTreeTile.miniMapTileColor, g);
						break;
					case Tile.ROCK_TILE:
						renderMinimapTile(renderCol, renderRow, Tile.rockTile.miniMapTileColor, g);
						break;
					case Tile.LAVA_ROCK_TILE:
						renderMinimapTile(renderCol, renderRow, Tile.lavaRockTile.miniMapTileColor, g);
						break;
					default:
						continue;
				}
			}
		}
		g.drawImage(compass, 20, 500, null);
	}
	
	public void renderMinimapTile(double renderCol, double renderRow, int tileColor, Graphics g)
	{
		g.setColor(new Color(tileColor));
		g.fillRect((int)(renderCol * miniMapTileSize), (int)(renderRow * miniMapTileSize), (int)Math.ceil(miniMapTileSize * sampleRate), (int)Math.ceil(miniMapTileSize * sampleRate));
	}

	public float getMiniMapTileSize()
	{
		return miniMapTileSize;
	}

	public void setMiniMapTileSize(float miniMapTileSize)
	{
		this.miniMapTileSize = miniMapTileSize;
	}

	public float getSampleRate()
	{
		return sampleRate;
	}

	public void setSampleRate(float sampleRate)
	{
		this.sampleRate = sampleRate;
	}
	
	public MiniMapCamera getMiniMapCamera()
	{
		return miniMapCamera;
	}
}
