package screen;

import game.Game;
import game.Utility;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import tiles.Tile;

public class MovingBackgroundScreen extends Screen
{
	private BufferedImage backgroundImage, dimmer;

	private double camCol;
	private double camRow;
	private float camColVel = (float)Utility.negToPositive(.02);
	private float camRowVel = (float)Utility.negToPositive(.02);
	private final float TITLE_SCREEN_TILE_SIZE = 1.5f;
	private float sampleRange = 2.5f;
	private int margin = -50;
	byte[][] titleScreenTiles;
	private Color oceanColor = new Color(Tile.oceanTile.miniMapTileColor); 
	
	public MovingBackgroundScreen()
	{
		key = "MovingBackgroundScreen";
		
		try
		{
			backgroundImage = ImageIO.read(ClassLoader.getSystemResourceAsStream("Menus/MainBackgroundMap.png"));
			dimmer = ImageIO.read(ClassLoader.getSystemResourceAsStream("Menus/titleDimmer.png"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		titleScreenTiles = Utility.imageToTiles(backgroundImage);
		
		camCol = 100;
		camRow = 100;
	}


	public void render(Graphics g)
	{
		renderTiles(g);
	}
	
	public void update(float delta)
	{
		camCol += camColVel;
		camRow += camRowVel;
		
		if(camCol < -margin || camCol + Game.WIDTH / TITLE_SCREEN_TILE_SIZE > backgroundImage.getWidth() + margin)
		{
			camColVel = -camColVel;
			camCol += camColVel * 2;
		}
		if(camRow < -margin || camRow + Game.HEIGHT / TITLE_SCREEN_TILE_SIZE > backgroundImage.getHeight() + margin)
		{
			camRowVel = -camRowVel;
			camCol += camRowVel * 2;
		}
	}
	
	public void renderTiles(Graphics g)
	{
		g.setColor(oceanColor);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
		for(double col = camCol; col < camCol + Game.WIDTH / TITLE_SCREEN_TILE_SIZE; col += sampleRange)
		{
			for(double row = camRow; row < camRow + Game.HEIGHT / TITLE_SCREEN_TILE_SIZE; row += sampleRange)
			{
				if(col <= 0 || col >= titleScreenTiles.length - 1)
					continue;
				if(row <= 0 || row >= titleScreenTiles[0].length - 1)
					continue;
				
				byte tileID = titleScreenTiles[(int)col][(int)row];
				
				double renderCol = col - camCol;
				double renderRow = row - camRow;
				
				switch(tileID)
				{
				case Tile.OCEAN_TILE:
					break;
				case Tile.GRASS_TILE_0:
				case Tile.GRASS_TILE_1:
				case Tile.GRASS_TILE_2:
				case Tile.GRASS_TILE_3:
					renderTile(renderCol, renderRow, Tile.grassTile.miniMapTileColor, g);
					break;
				case Tile.SAND_TILE_0:
				case Tile.SAND_TILE_1:
				case Tile.SAND_TILE_2:
				case Tile.SAND_TILE_3:
				case Tile.SAND_TILE_4:
				case Tile.X_TILE:
					renderTile(renderCol, renderRow, Tile.sandTile.miniMapTileColor, g);
					break;
				case Tile.DIRT_TILE:
					renderTile(renderCol, renderRow, Tile.dirtTile.miniMapTileColor, g);
					break;
				case Tile.MUD_TILE:
					renderTile(renderCol, renderRow, Tile.mudTile.miniMapTileColor, g);
					break;
				case Tile.DIRT_PATH_TILE:
					renderTile(renderCol, renderRow, Tile.dirtPathTile.miniMapTileColor, g);
					break;
				case Tile.SHALLOW_WATER_TILE:
					renderTile(renderCol, renderRow, Tile.shallowWaterTile.miniMapTileColor, g);
					break;
				case Tile.LAVA_TILE:
					renderTile(renderCol, renderRow, Tile.lavaTile.miniMapTileColor, g);
					break;
				case Tile.PALM_TREE_TILE:
					renderTile(renderCol, renderRow, Tile.palmTreeTile.miniMapTileColor, g);
					break;
				case Tile.ROCK_TILE:
					renderTile(renderCol, renderRow, Tile.rockTile.miniMapTileColor, g);
					break;
				case Tile.LAVA_ROCK_TILE:
					renderTile(renderCol, renderRow, Tile.lavaRockTile.miniMapTileColor, g);
					break;
				default:
					continue;
				}
			}
		}
		g.drawImage(dimmer, 0, 0, Game.WIDTH, Game.HEIGHT, null);//dims screen
	}
	
	public void renderTile(double renderCol, double renderRow, int tileColor, Graphics g)
	{
		g.setColor(new Color(tileColor));
		g.fillRect((int)(renderCol * TITLE_SCREEN_TILE_SIZE), (int)(renderRow * TITLE_SCREEN_TILE_SIZE), (int)Math.ceil(TITLE_SCREEN_TILE_SIZE * sampleRange), (int)Math.ceil(TITLE_SCREEN_TILE_SIZE * sampleRange));
	}
}
