package game;
import java.awt.Graphics;
import java.awt.Point;

public class Camera
{
	protected double col = 0;
	protected double row = 0;
	protected float cameraVel = .2f;
	protected SelectionBox selectionBox;
	protected int margin = 4;
	
	public Camera()
	{
		
	}
	
	public boolean update(float delta)
	{
		if(Game.input.up)// && row > cameraVel + 4)
			row -= cameraVel * delta;
		if(Game.input.down)// && row + Utility.toTiles(Game.HEIGHT) < Game.getWorld().getTiles().length - (cameraVel + 5))
			row += cameraVel * delta;
		if(Game.input.right)// && col + Utility.toTiles(Game.WIDTH) < Game.getWorld().getTiles()[0].length - (cameraVel + 5))
			col += cameraVel * delta;
		if(Game.input.left)// && col > cameraVel + 4)
			col -= cameraVel * delta;
		
		//Movement with mouse
		Point mouseLocation = Game.input.mouseLocation;
		

//		if(mouseLocation.x < Game.WIDTH * 1 / 8 || mouseLocation.x > Game.WIDTH * 7 / 8 ||
//				mouseLocation.y < Game.HEIGHT * 1 / 8 || mouseLocation.y > Game.HEIGHT * 7 / 8)
//		{
//			double angleToMouse = Math.atan2(Game.input.mouseLocation.y - Game.HEIGHT / 2, Game.input.mouseLocation.x - Game.WIDTH / 2);
//			
//			col += Math.cos(angleToMouse) * cameraVel * delta;
//			row += Math.sin(angleToMouse) * cameraVel * delta;
//			
////				if(col < margin || col + Utility.toTiles(Game.WIDTH) > Game.getWorld().getWorldWidth() - margin)
////					col += Math.cos(angleToMouse + Math.PI) * cameraVel * delta;
////				if(row < margin || row  + Utility.toTiles(Game.HEIGHT) > Game.getWorld().getWorldHeight() - margin)
////					row += Math.sin(angleToMouse + Math.PI) * cameraVel * delta;
//			
//		}
		
		
		if(selectionBox != null && Game.input.cursorOnScreen)
			selectionBox.update(delta);
		if(!Game.input.cursorOnScreen)
			selectionBox = null;
			
		return true;
	}
	
	public void render(Graphics g)
	{
		if(selectionBox != null && Game.input.cursorOnScreen)
			selectionBox.render(g);
	}
	
	//gets and sets
	public SelectionBox getSelectionBox()
	{
		return selectionBox;
	}
	
	public void setSelectionBox(SelectionBox selectionBox)
	{
		this.selectionBox = selectionBox;
	}
	
	public double getVel()
	{
		return cameraVel;
	}
	
	
	public double getCol()
	{
		return col;
	}

	public void setCol(int x)
	{
		this.col = x;
	}

	public double getRow()
	{
		return row;
	}

	public void setRow(int y)
	{
		this.row = y;
	}
}