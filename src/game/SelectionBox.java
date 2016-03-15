package game;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import entity.Entity;
import tiles.Tile;

public class SelectionBox
{
	private double startCol, startRow, currentCol, currentRow;
	public SelectionBox()
	{
//		startCol = 	(int) (Game.input.getPressX() / Tile.TILE_SIZE);
//		startRow =  (int) (Game.input.getPressY() / Tile.TILE_SIZE);
		startCol =  (Game.input.getPressX() / Tile.TILE_SIZE) + Game.getWorld().getCamera().getCol();
		startRow =  (Game.input.getPressY() / Tile.TILE_SIZE) + Game.getWorld().getCamera().getRow();

		for(Entity e : Game.getWorld().getEntityList())
			e.setSelected(false);
	}
	
	public void update(float delta)
	{
		currentCol = Utility.toTiles(Game.input.mouseLocation.x) + Game.getWorld().getCamera().getCol();
		currentRow = Utility.toTiles(Game.input.mouseLocation.y) + Game.getWorld().getCamera().getRow();
	}
	
	public void render(Graphics g)
	{
		if(Game.input.mouseLocation != null)
		{
			double renderStartCol = startCol - Game.getWorld().getCamera().getCol();
			double renderStartRow = startRow - Game.getWorld().getCamera().getRow();
			
			Graphics2D g2d = (Graphics2D)g;
			g2d.setStroke(new BasicStroke(3));
			g.setColor(Color.RED);
			
			g.drawLine((int)(Utility.toPixels(renderStartCol)), (int)Utility.toPixels(renderStartRow), Game.input.mouseLocation.x, (int)(renderStartRow * Tile.TILE_SIZE));
			g.drawLine((int)Utility.toPixels(renderStartCol), (int)Utility.toPixels(renderStartRow ), (int)Utility.toPixels(renderStartCol), Game.input.mouseLocation.y);
			g.drawLine(Game.input.mouseLocation.x, (int)Utility.toPixels(renderStartRow), Game.input.mouseLocation.x, Game.input.mouseLocation.y);
			g.drawLine((int)Utility.toPixels(renderStartCol), Game.input.mouseLocation.y, Game.input.mouseLocation.x, Game.input.mouseLocation.y);
		}
	}
	
	public boolean containsEntity(Entity e)
	{
		if(e.getCol() <= startCol && e.getCol() >= currentCol && e.getRow() >= startRow && e.getRow() < currentRow)
			return true;
		if(e.getCol() >= startCol && e.getCol() <= currentCol && e.getRow() >= startRow && e.getRow() < currentRow)
			return true;
		if(e.getCol() <= startCol && e.getCol() >= currentCol && e.getRow() <= startRow && e.getRow() > currentRow)
			return true;
		if(e.getCol() >= startCol && e.getCol() <= currentCol && e.getRow() <= startRow && e.getRow() > currentRow)
			return true;
		return false;
	}
}

