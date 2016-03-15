package entity;
import game.Game;
import game.Utility;
import tiles.Tile;

public class Vessel extends Entity
{
	protected Entity[] cargo;
	protected int deckWidth, deckHeight;
	
	public Vessel(double col, double row)
	{
		super(col,row);
	}
	
	public boolean board(Entity boardEntity)
	{
		if (cargo[cargo.length - 1] != null)
			return false;
		boardEntity.setImage(boardEntity.getAnimationImages()[0]);
		for(int i = cargo.length - 1; i > 0; i--)
		{
			cargo[i] = cargo[i-1];
		}
		cargo[0] = boardEntity;
		return true;
	}
	
	public void unboard()
	{
		for(int i = 0; i < cargo.length; i++)
		{
			if(cargo[i] != null)
			{
				cargo[i].setRow(centerRow + Utility.negToPositive(height / Tile.TILE_SIZE));
				cargo[i].setCol(centerCol + Utility.negToPositive(width / Tile.TILE_SIZE));
				cargo[i].setDestinationRow(0);
				cargo[i].setDestinationCol(0);
				cargo[i].setSelected(false);
				Game.getWorld().getEntityList().add(cargo[i]);
				cargo[i] = null;
			}
		}
		destinationRow = 0;
		destinationCol = 0;		
	}
	
	public boolean update(float delta)
	{
		super.update(delta);
		
		//push boats apart
		for(Entity e : Game.getWorld().getEntityList())
		{
			if(e instanceof Vessel && e != this)
			{
				double distanceSquared = Utility.distSquared(e.centerCol, centerCol, e.centerRow, centerRow);
				double angle = Math.atan2(e.centerRow - centerRow, e.centerCol - centerCol);
				
				if(distanceSquared < 5 * 5)
				{
					col += Math.cos(angle + Math.PI) * speed * Game.getWorld().getTile(centerCol, centerRow).getSpeedLimiter(this) * delta;
					row += Math.sin(angle + Math.PI) * speed * Game.getWorld().getTile(centerCol, centerRow).getSpeedLimiter(this) * delta;
				}
			}
		}
		
		//unboards on sand
		if(Game.getWorld().getTile(centerCol, centerRow) != null && Game.getWorld().getTile(centerCol, centerRow).equals(Tile.sandTile))
		{
			for(Entity e : cargo)//check to see if it needs to un-board
			{
				if(e != null)
				{
					unboard();
					return true;
				}
			}
			destinationCol = 0;
			destinationRow = 0;
		}
		
		return true;
	}
	
	//get and setter
	public Entity[] getCargo()
	{
		return cargo;
	}
}