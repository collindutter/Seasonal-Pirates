package game;

import java.awt.Point;

public class MiniMapCamera extends Camera
{
	private MiniMap miniMap;
	
	private int viewWidth = (int) (Game.WIDTH / MiniMap.MINIMAP_TILE_SIZE); //in minimap tile size
	private int viewHeight = (int) (Game.HEIGHT / MiniMap.MINIMAP_TILE_SIZE);
	
	public MiniMapCamera(MiniMap miniMap)
	{
		this.miniMap = miniMap; 
		col = -550;
		row = -350;
		cameraVel = 2f;
		margin = -100;
	}
	
	public boolean update(float delta)
	{
		if(Game.input.up)
			row -= cameraVel * delta;
		if(Game.input.down)
			row += cameraVel * delta;
		if(Game.input.right)
			col += cameraVel * delta;
		if(Game.input.left)
			col -= cameraVel * delta;
		if(Game.input.mouseLocation != null)
		{
			//Movement with mouse
			Point mouseLocation = Game.input.mouseLocation;
			
			double angleToMouse = Math.atan2(Game.input.mouseLocation.y - Game.HEIGHT / 2, Game.input.mouseLocation.x - Game.WIDTH / 2);
	
			if(mouseLocation.x < Game.WIDTH * 1 / 8 || mouseLocation.x > Game.WIDTH * 7 / 8 ||
					mouseLocation.y < Game.HEIGHT * 1 / 8 || mouseLocation.y > Game.HEIGHT * 7 / 8)
			{
				col += Math.cos(angleToMouse) * cameraVel * delta;
				row += Math.sin(angleToMouse) * cameraVel * delta;
				
//				if(col < margin || col + (Game.WIDTH / MiniMap.MINIMAP_TILE_SIZE) > Game.getWorld().getWorldWidth() - margin)
//					col += Math.cos(angleToMouse + Math.PI) * cameraVel * delta;
//				if(row < margin || row  + (Game.HEIGHT / MiniMap.MINIMAP_TILE_SIZE) > Game.getWorld().getWorldHeight() - margin)
//					row += Math.sin(angleToMouse + Math.PI) * cameraVel * delta;
				
			}
		}
		
		return true;
	}

	public int getViewWidth()
	{
		return viewWidth;
	}

	public void setViewWidth(int viewWidth)
	{
		this.viewWidth = viewWidth;
	}

	public int getViewHeight()
	{
		return viewHeight;
	}

	public void setViewHeight(int viewHeight)
	{
		this.viewHeight = viewHeight;
	}
}
