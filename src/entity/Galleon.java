package entity;
import game.Game;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import tiles.Tile;

public class Galleon extends Vessel
{
	public Galleon(double col, double row)
	{
		super(col, row);
		animationImages = new BufferedImage[1];
		try
		{
			animationImages[0] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Entities/galleon0.png"));
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		width = 192;//8
		height = 312;//13
		image = animationImages[0];
		deckWidth = 4;
		deckHeight = 7;
		cargo = new Entity[deckWidth * deckHeight + 2];
		animationSpeed = 10;
		calcFootprint();
	}
	
	public void render(Graphics g)
	{
		super.render(g);
		renderCol = col - Game.getWorld().getCamera().getCol();
		renderRow = row - Game.getWorld().getCamera().getRow();
		
		Graphics2D g2d = (Graphics2D)g;
		AffineTransform bf = g2d.getTransform();
		
//		if(selected)
//		{
//			g.setColor(Color.RED);
//			g2d.setStroke(new BasicStroke(3));
//			g.drawRect((int)(renderCol * Tile.TILE_SIZE) - 60, (int)(renderRow * Tile.TILE_SIZE) - 5, width + 120, height + 10);
//		}
//		
		g2d.rotate(direction + Math.PI / 2, (renderCol * Tile.TILE_SIZE) + (width / 2), (renderRow * Tile.TILE_SIZE) + (height / 2));
//		g.drawImage(image, (int)(renderCol * Tile.TILE_SIZE), (int)(renderRow * Tile.TILE_SIZE), width, height, null);
		g2d.setTransform(bf);
		
//		Graphics2D g2d = (Graphics2D)g;
//		AffineTransform bf = g2d.getTransform();
		
		for(int i = 0; i < cargo.length; i++)
		{
			if(cargo[i] != null)
			{
				g2d.rotate(direction + Math.PI / 2, renderCol * Tile.TILE_SIZE + width / 2, renderRow * Tile.TILE_SIZE + height / 2);
				if(i < 4)
					g.drawImage(cargo[i].getImage(), (int)((renderCol + (i % deckWidth)) * Tile.TILE_SIZE) + 48, (int)((renderRow + (i / deckWidth)) * Tile.TILE_SIZE) + 66, cargo[i].getWidth(),cargo[i].getHeight(), null);
				else if(i < 20)
					g.drawImage(cargo[i].getImage(), (int)((renderCol + (i % deckWidth)) * Tile.TILE_SIZE) + 48, (int)((renderRow + (i / deckWidth)) * Tile.TILE_SIZE) + 90, cargo[i].getWidth(),cargo[i].getHeight(), null);
				else if(i < 28)
					g.drawImage(cargo[i].getImage(), (int)((renderCol + (i % deckWidth)) * Tile.TILE_SIZE) + 48, (int)((renderRow + (i / deckWidth)) * Tile.TILE_SIZE) + 120, cargo[i].getWidth(),cargo[i].getHeight(), null);
				else if(i < 30)
					g.drawImage(cargo[i].getImage(), (int)((renderCol + (i % deckWidth)) * Tile.TILE_SIZE) + 72, (int)((renderRow + (i / deckWidth)) * Tile.TILE_SIZE) - 124, cargo[i].getWidth(),cargo[i].getHeight(), null);
				g2d.setTransform(bf);
			}
		}
//		g.setColor(Color.RED);
//		g.fillRect((int)(centerCol *Tile.TILE_SIZE),(int)( centerRow * Tile.TILE_SIZE), Tile.TILE_SIZE, Tile.TILE_SIZE);
	}
}