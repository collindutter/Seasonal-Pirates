package game.minimap;

import game.Camera;
import game.Game;

import java.awt.Point;

public class MiniMapCamera extends Camera
{
	private MiniMap miniMap;
	
	private double viewWidth; //in minimap tile size
	private double viewHeight;
	
	private float zoom;
	
	public MiniMapCamera(MiniMap miniMap)
	{
		this.miniMap = miniMap; 
		col = -550;
		row = -350;
		cameraVel = 2f;
		margin = -100;
		zoom = miniMap.getMiniMapTileSize();
	}
	
	public boolean update(float delta)
	{
		viewWidth = Game.WIDTH / miniMap.getMiniMapTileSize();
		viewHeight = Game.HEIGHT / miniMap.getMiniMapTileSize();
		
		if(Game.input.e)
			zoom += .05f;
		if(Game.input.q)
			zoom -= .05f;
		
		if(zoom < .75f)
			zoom = .75f;
		if(zoom > 10)
			zoom = 10;
		
		miniMap.setMiniMapTileSize(zoom);
		
		double newWidth = Game.WIDTH / miniMap.getMiniMapTileSize();
		double newHeight = Game.HEIGHT / miniMap.getMiniMapTileSize();
		
		col -= (newWidth - viewWidth) / 2; //zooming right here up in this bitch
		row -= (newHeight - viewHeight) / 2;
		
		viewWidth = newWidth;
		viewHeight = newHeight;
		
		miniMap.setSampleRate((5 / zoom) > 1.25f? (float)(5 / zoom) : 1.25f);
//		miniMap.setSampleRate((5 / zoom));
		
		//as zoom increases, sampleRate decreases
		
//		System.out.println(zoom);
		
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
			
	
			if(mouseLocation.x < Game.WIDTH * 1 / 8 || mouseLocation.x > Game.WIDTH * 7 / 8 ||
					mouseLocation.y < Game.HEIGHT * 1 / 8 || mouseLocation.y > Game.HEIGHT * 7 / 8)
			{
				double angleToMouse = Math.atan2(Game.input.mouseLocation.y - Game.HEIGHT / 2, Game.input.mouseLocation.x - Game.WIDTH / 2);
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

	public double getViewWidth()
	{
		return viewWidth;
	}

	public void setViewWidth(double viewWidth)
	{
		this.viewWidth = viewWidth;
	}

	public double getViewHeight()
	{
		return viewHeight;
	}

	public void setViewHeight(double viewHeight)
	{
		this.viewHeight = viewHeight;
	}

	public float getZoom()
	{
		return zoom;
	}

	public void setZoom(float zoom)
	{
		this.zoom = zoom;
	}
}
