package screen;

import java.awt.Graphics;
import java.util.ArrayList;
import UIComponent.UIComponent;

public abstract class Screen
{
	protected ArrayList<UIComponent> components = new ArrayList<UIComponent>();
	
	public String key;
	
	public Screen()
	{
		
	}
	
	public void update(float delta)
	{
		for(UIComponent c : components)
			c.update();
	}
	
	public void render(Graphics g)
	{
		for(UIComponent c : components)
			c.render(g);
	}
	
	public String getKey()
	{
		return key;
	}
}