package UIComponent;

import game.Game;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Button extends UIComponent
{
	private BufferedImage image, imageSelected;
	private String text;
	private String pointer;
	
	int xString, yString, fontSize;
	
	public Button(int xCoord, int yCoord, int width, int height, int fontSize, BufferedImage image, BufferedImage imageSelected, String text, String pointer)
	{
		super(xCoord, yCoord, width, height);
		this.image = image;
		this.imageSelected = imageSelected;
		this.text = text;
		this.fontSize = fontSize;
		this.pointer = pointer;
	}

	public void buttonAction()
	{
		Game.setScreen(pointer);
	}
	
	public void update()
	{
		super.update();
		if(containsMouse && Game.input.leftMousePressed)
		{
			buttonAction();
			Game.input.leftMousePressed = false;
		}
	}
	
	public void render(Graphics g)
	{
//		g.setColor(backgroundColor);
//		g.fillRect(xCoord, yCoord, width, height);
//		g.setColor(edgeColor);
		if(containsMouse)
			g.drawImage(imageSelected, xCoord, yCoord, width, height, null);
		else
			g.drawImage(image, xCoord, yCoord, width, height, null);
		g.setColor(textColor);
//		g.setFont(new Font("pr celtic narrow", Font.BOLD, fontSize));
		g.setFont(new Font("mael", Font.BOLD, fontSize));
//		g.setFont(new Font("candles", Font.BOLD, fontSize));
		int textWidth = g.getFontMetrics().stringWidth(text);
		int textHeight = g.getFontMetrics().getHeight();
		g.drawString(text, xCoord + width / 2 - textWidth / 2, yCoord + height / 2 + textHeight / 4);
	}
}
