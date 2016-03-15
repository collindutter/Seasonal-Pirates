package UIComponent;

import game.Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public abstract class UIComponent
{
	protected int width, height;
	protected int xCoord, yCoord;
	protected Color textColor;
	
	public boolean containsMouse = false;
	
	
	public UIComponent(int xCoord, int yCoord, int width, int height)
	{
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.width = width;
		this.height = height;
	}
	
	public void update()
	{
		containsMouse = containsMouse();
		if(containsMouse)
			textColor = Color.WHITE;
		else
			textColor = new Color(0xC60000);
	}
	
	public abstract void render(Graphics g);
	
	public boolean containsMouse()
	{
		Point mL = Game.input.mouseLocation;
		int xM = mL.x;
		int yM = mL.y;
		
		if(xM > xCoord && xM < xCoord + width && yM > yCoord && yM < yCoord + height)
			return true;
		return false;
	}
}

