package entity;
import game.Game;
import game.Utility;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import tiles.Node;
import tiles.Tile;

public class Entity
{
	public static final int	DOWN_RIGHT = 315,
							DOWN = 270,
							DOWN_LEFT = 225,
							LEFT = 180,
							UP_LEFT = 135,
							UP = 90,
							UP_RIGHT = 45,
							RIGHT = 0;
	protected double row, col,
					centerRow, centerCol,
					renderRow, renderCol;
	
	protected int width, height,
				destinationCol, destinationRow,
				footprintWidth, footprintHeight,
				diagFootprintWidth, diagFootprintLength;//for galleon diagWidth = 4, diagLength = 10;
	protected double direction;
	protected float speed = .08f;
	protected PathFinder pathFinder;
	protected ArrayList<Point> footprint;
	protected BufferedImage image;
	protected int animationSpeed;
	public BufferedImage[] animationImages;
	private int animationIndex = 0;
	protected boolean selected = false;
	protected Node destinationNode;
	private ArrayList<Node> path;
	
	public Entity(double col, double row)
	{
		this.row = row;
		this.col = col;
		footprintWidth = (int)Utility.toTiles(width);
		footprintHeight = (int)Utility.toTiles(height);
		pathFinder = new PathFinder();
	}
	
	public boolean update(float delta)
	{
		centerCol = col + Utility.toTiles(width / 2);
		centerRow = row + Utility.toTiles(height / 2);
		updateOccupiedTiles();
		calcFootprint();
		
		if(Game.getWorld().getCamera().getSelectionBox() != null)
		{
			if(shouldBeSelected())
				selected = true;
			else
				selected = false;
		}
		
		if(selected && Game.input.rightMousePressed) //destinations relative to map
		{
			
			new Thread()
			{
				public void run()
				{
					
					destinationCol = (int)(Game.getWorld().getCamera().getCol() + Utility.toTiles(Game.input.getPressX()));
					destinationRow = (int)(Game.getWorld().getCamera().getRow() + Utility.toTiles(Game.input.getPressY()));
					destinationNode = new Node(destinationCol, destinationRow);
					
					long ct = System.currentTimeMillis();
					path = pathFinder.calculatePath(Entity.this);
					long lt = System.currentTimeMillis();
					System.out.println(lt - ct);
					
				}
			}.start();
//			Game.input.rightMousePressed = false;
		}
		
		//move
		if(path != null && path.size() > 0 )//still have a move queue
		{
			direction = Math.atan2(Math.round(path.get(0).getRow()) + Utility.toTiles(Tile.TILE_SIZE / 2) - centerRow, Math.round(path.get(0).getCol()) + Utility.toTiles(Tile.TILE_SIZE / 2) - centerCol);
			direction = Math.toRadians((360 + Math.toDegrees(direction)) % 360);
//			direction = Math.toRadians(Math.round(Math.toDegrees(direction) / 45.0) * 45);
			if(Math.round(path.get(0).getCol()) == Math.round(col) && Math.round(path.get(0).getRow()) == Math.round(row))
				path.remove(0);
			else
			{
				col += Math.cos(direction) * speed * delta * Game.getWorld().getTile(centerCol, centerRow).getSpeedLimiter(this);
				row += Math.sin(direction) * speed * delta * Game.getWorld().getTile(centerCol, centerRow).getSpeedLimiter(this);
				animate();
			}
		}
		else //not moving
		{
			destinationRow = 0;
			destinationCol = 0;
			image = animationImages[0];
		}
		
//		//reached destination
//		if(Math.round(centerRow) == Math.round(destinationRow) && Math.round(centerCol) == Math.round(destinationCol))
//		{
//			destinationRow = 0;
//			destinationCol = 0;
//			image = animationImages[0];
//		}
		return true;
	}
	
	public void render(Graphics g)
	{
		renderCol = col - Game.getWorld().getCamera().getCol();
		renderRow = row - Game.getWorld().getCamera().getRow();
		pathFinder.render(g);
//		if(path != null && path.size() > 0)
//		{
//			for(int i = 0; i < path.size(); i++)
//			{
//				double renderCol2 = path.get(i).getCol() - Game.getWorld().getCamera().getCol();
//				double renderRow2 = path.get(i).getRow() - Game.getWorld().getCamera().getRow();
//				g.setColor(Color.MAGENTA);
//				g.fillRect((int) Utility.toPixels(renderCol2), (int)Utility.toPixels(renderRow2), Tile.TILE_SIZE, Tile.TILE_SIZE);
//			}
//			
//		}
		if(destinationCol != 0 && destinationRow != 0)
		{
			g.setColor(Color.RED);
			g.drawRect((int)Utility.toPixels(destinationCol - Game.getWorld().getCamera().getCol()), (int)Utility.toPixels(destinationRow - Game.getWorld().getCamera().getRow()), Tile.TILE_SIZE, Tile.TILE_SIZE);
		}
				Graphics2D g2d = (Graphics2D)g;
		AffineTransform bf = g2d.getTransform();
		
		if(selected)
		{
			g.setColor(Color.RED);
			g2d.setStroke(new BasicStroke(3));
			g.drawRect((int)Utility.toPixels(renderCol) - 5, (int)Utility.toPixels(renderRow) - 5, width + 10, height + 10);
		}
		
		g2d.rotate(direction + Math.toRadians(90), (renderCol * Tile.TILE_SIZE) + (width / 2), (renderRow * Tile.TILE_SIZE) + (height / 2));
		g.drawImage(image, (int)Utility.toPixels(renderCol), (int)Utility.toPixels(renderRow), width, height, null);
		g2d.setTransform(bf);
		
	}
	
	protected void animate()
	{
		if(Game.ticks % animationSpeed == 0 || Game.ticks == 0)
		{
			if(animationIndex == animationImages.length - 1)
				animationIndex = 0;
			else
				animationIndex++;
			image = animationImages[animationIndex];
		}
	}
	
	protected void calcFootprint()
	{
		footprint = new ArrayList<Point>();
		//make square footprint
		if(direction % 90 == 0)
		{
			for(int i = 0; i < (width / Tile.TILE_SIZE) * (height / Tile.TILE_SIZE); i++)
				footprint.add(new Point((int)Math.round(col + (i % (width / Tile.TILE_SIZE))), (int)Math.round(row + (i / (width / Tile.TILE_SIZE)))));
		}
		//make diagonal footprint
		else
		{
			//do the long diagonal rows 4 times
			for(int t = 0; t < diagFootprintWidth; t++)
			{
				//the long diagonal rows are 10 long
				for(int j = 0; j < diagFootprintLength; j++)
					footprint.add(new Point((int)Math.round(centerCol) - (diagFootprintLength / 2 + 1) +  t + j, (int)Math.round(centerRow) + diagFootprintLength - (diagFootprintLength / 2 + 1) + t - j));
				//do the short diagonal rows 3 times
				if(t != 0)
					//the short diagonal rows are 9 long
					for(int k = 0; k < diagFootprintLength - 1; k++)
						footprint.add(new Point((int)Math.round(centerCol) - (diagFootprintLength / 2 + 1) + t + k, (int)Math.round(centerRow) + diagFootprintLength - 1 - (diagFootprintLength / 2 + 1) + t - k ));
			}
		}
	}
	
	
	protected void updateOccupiedTiles()
	{
//		if(col < 1 || col > Game.getWorld().getTiles().length - 1)
//			return;
//		if(row < 1 || row > Game.getWorld().getTiles()[0].length - 1)
//			return;
		for(double col = this.col - 4; col < this.col + width / Tile.TILE_SIZE + 4; col++)
		{
			for(double row = this.row - 4; row < this.row + height / Tile.TILE_SIZE + 4; row++)
			{
				if(col < 0 || col > Game.getWorld().getWorldWidth())
					continue;
				if(row < 0 || row > Game.getWorld().getWorldHeight())
					continue;
				if(tileShouldBeOccupied(col, row))
					Game.getWorld().setTileOccupied(col, row, true);
				else//!tileShouldBeOccupied
					Game.getWorld().setTileOccupied(col, row, false);
			}
		}
	}
	
	private boolean tileShouldBeOccupied(double col, double row)
	{
		//check for impassable tiles (tree, rock)
		if((Game.getWorld().getTileID(col, row) & 0x7F) == Tile.PALM_TREE_TILE || (Game.getWorld().getTileID(col, row) & 0x7F) == Tile.ROCK_TILE)//make rocks and trees occupied
			return true;
			
		//check for other entities occupying
		for(Entity e : Game.getWorld().getEntityList())
		{
			if (Utility.distSquared(centerCol,  e.getCenterCol(), centerRow, e.getCenterRow()) > (width / Tile.TILE_SIZE + e.getWidth() /  Tile.TILE_SIZE) * (width / Tile.TILE_SIZE + e.getWidth()/ Tile.TILE_SIZE) + 2)
				continue;
			
			for(Point p : e.getFootprint())
			{
				if((int)Math.round(p.getX()) == (int)Math.round(col) && (int)Math.round(p.getY()) == (int)Math.round(row))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	protected boolean shouldBeSelected()
	{
		if(Game.input.leftMousePressed)
		{
			//For dragging
			if(Game.getWorld().getCamera().getSelectionBox() != null && Game.getWorld().getCamera().getSelectionBox().containsEntity(this))
				return true;
			//For clicking
			if(Game.input.mouseLocation.x > Utility.toPixels(renderCol) - 10)
				if(Game.input.mouseLocation.x  < Utility.toPixels(renderCol) + width + 10)
					if(Game.input.mouseLocation.y > Utility.toPixels(renderRow) - 10)
						if(Game.input.mouseLocation.y  < Utility.toPixels(renderRow) + height + 10)
							return true;	
		}
		return false;
	}
	
	
	//getters and setters
	public ArrayList<Point> getFootprint()
	{
		return footprint;
	}
	
	public double getRenderRow()
	{
		return renderRow;
	}
	
	public double getRenderCol()
	{
		return renderCol;
	}
	
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}
	
	public double getRow()
	{
		return row;
	}
	
	public double getCol()
	{
		return col;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public double getCenterRow()
	{
		return centerRow;
	}
	
	public double getCenterCol()
	{
		return centerCol;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public BufferedImage getImage()
	{
		return image;
	}
	
	public void setImage(BufferedImage i)
	{
		image = i;
	}
	
	public void setRow(double r)
	{
		row = r;
	}
	
	public void setCol(double c)
	{
		col = c;
	}
	
	public void setDestinationRow(int r)
	{
		destinationRow = r;
	}
	
	public void setDestinationCol(int c)
	{
		destinationCol = c;
	}
	
	public BufferedImage[] getAnimationImages()
	{
		return animationImages;
	}
	
	public int getDestinationCol()
	{
		return destinationCol;
	}
	
	public int getDestinationRow()
	{
		return destinationRow;
	}
}
				