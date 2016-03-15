package game;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

import tiles.Tile;

public class Utility
{
	public static BufferedImage makeColorTransparent(BufferedImage im, final Color color)
	{
    	ImageFilter filter = new RGBImageFilter()
	    	{
	    		public int markerRGB = color.getRGB() | 0xFF000000;
	    		public final int filterRGB(int x, int y, int rgb)
	    		{
	    			if ((rgb | 0xFF000000) == markerRGB)
	    				return 0x00FFFFFF & rgb;
	    			else
	    				return rgb;
	    		}
	    	};
    	ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
    	Image i = Toolkit.getDefaultToolkit().createImage(ip);
    	im.getGraphics().drawImage(i, 0, 0, null);
    	return im;
    }
	
	public static BufferedImage extractSprite(BufferedImage sheet, int spriteNumber)
	{
		return makeColorTransparent(sheet.getSubimage(24 * spriteNumber, 0, 24, 24), new Color(0,0,0));
	}
	
	public static byte[][] imageToTiles(BufferedImage image)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		
		byte[][] tiles = new byte[width][height];
		
		for(int col = 0; col < width; col++)
		{
			for(int row = 0; row < height; row++)
			{
				int pixelColor;
				
				pixelColor = image.getRGB(col, row); 
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
						tiles[col][row] = Tile.ROCK_TILE;
						break;
					case 0x00FF21://LIGHT GREEN
						tiles[col][row] = Tile.PALM_TREE_TILE;
						break;
					default:
						System.err.println("A pixel from the WorldMap was an invalid color.");
						System.exit(1);
						break;
				}
			}	
		}
		
		return tiles;
	}
	
	public static double distSquared(double x1,double x2,double y1,double y2)
	{
		return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
	}
	
	public static double negToPositive(double d) 
	{
	    return (Math.random() * 2 * d) - d;
	}
	
	public static double toTiles(double pixels)
	{
		return pixels / Tile.TILE_SIZE;
	}
	
	public static double toPixels(double tiles)
	{
		return tiles * Tile.TILE_SIZE;
	}
	
	public static float round(double numberToRound, int roundPlace)
	{
		return Math.round(numberToRound * roundPlace) / roundPlace;
	}
}