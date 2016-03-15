package entity;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import tiles.Tile;

public class RowBoat extends Vessel
{
	public RowBoat(double col, double row)
	{
		super(col, row);
		animationImages = new BufferedImage[4];
		try
		{
			animationImages[0] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Entities/rowBoat0.png"));
			animationImages[1] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Entities/rowBoat1.png"));
			animationImages[2] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Entities/rowBoat2.png"));
			animationImages[3] = ImageIO.read(ClassLoader.getSystemResourceAsStream("Entities/rowBoat1.png"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		width = 48;
		height = 48;
		image = animationImages[0];
		deckWidth = 1;
		deckHeight = 2;
		cargo = new Entity[deckWidth * deckHeight];
		animationSpeed = 10;
		calcFootprint();
	}

	public void render(Graphics g)
	{
		super.render(g);
		Graphics2D g2d = (Graphics2D)g;
		AffineTransform bf = g2d.getTransform();
		
		for(int i = 0; i < cargo.length; i++)
		{
			if(cargo[i] != null)
			{
				g2d.rotate(direction + Math.PI / 2, renderCol * Tile.TILE_SIZE + width / 2, renderRow * Tile.TILE_SIZE + height / 2);				
				g.drawImage(cargo[i].getImage(), (int)((renderCol + (i % deckWidth)) * Tile.TILE_SIZE) + 12, (int)((renderRow + (i / deckWidth)) * Tile.TILE_SIZE), cargo[i].getWidth(),cargo[i].getHeight(), null);
				g2d.setTransform(bf);
			}
		}
	}

	
}