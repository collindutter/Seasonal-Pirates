package entity;
import game.Game;
import game.Utility;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import tiles.Tile;



public class Pirate extends Entity
{
	protected Vessel vessel;
	
	public Pirate(double col, double row)
	{
		super(col, row);
		try
		{
			animationImages = new BufferedImage[4];
			animationImages[0] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Entities/pirate1.png"));
			animationImages[1] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Entities/pirate0.png"));
			animationImages[2] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Entities/pirate1.png"));
			animationImages[3] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Entities/pirate2.png"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		width = 24;
		height = 24;
		animationSpeed = 15;
		speed = .04f;
		image = animationImages[0];
		calcFootprint();
	}
	
	public boolean update(float delta)
	{
		super.update(delta);
		width = Tile.TILE_SIZE;
		height = Tile.TILE_SIZE;
		//boards the pirate on a vessel
		for(Entity e : Game.getWorld().getEntityList())
		{
			double distanceSquared = Utility.distSquared(centerRow, e.getCenterRow(), centerCol, e.getCenterCol());
			if(e instanceof Vessel)
			{
				if(distanceSquared < Utility.toTiles(e.getWidth() / 2) * Utility.toTiles(e.getWidth() / 2))
				{
					if(((Vessel)e).board(this))
					{
						Game.getWorld().setTileOccupied((int)Math.round(centerCol), (int)Math.round(centerRow), false);
						return false;
					}
				}
			}
			
			//pirates push eachother aside
			if(e instanceof Pirate && e != this)
			{
				if((destinationRow != 0) && (destinationCol != 0))
				{
					double angle = Math.atan2(e.getCenterRow() - centerRow, e.getCenterCol() - centerCol);
					if(distanceSquared < Utility.toTiles(getWidth() / 2) * Utility.toTiles(getWidth() / 2) + .25)
					{
						col += Math.cos(angle + Math.PI) * speed * Game.getWorld().getTile(centerCol, centerRow).getSpeedLimiter(this) * delta;
						row += Math.sin(angle + Math.PI) * speed * Game.getWorld().getTile(centerCol, centerRow).getSpeedLimiter(this) * delta;
					}
				}
			}
		}

		return true;
	}
	
	public void render(Graphics g)
	{
		if(vessel == null)
			super.render(g);
	}
}