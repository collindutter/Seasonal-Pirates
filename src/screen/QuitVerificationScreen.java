package screen;

import game.Game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import UIComponent.Button;

public class QuitVerificationScreen extends Screen
{
private BufferedImage play0, play1;
	
	public QuitVerificationScreen()
	{
		key = "QuitVerificationScreen";
		
		try
		{
			play0 = ImageIO.read(ClassLoader.getSystemResourceAsStream("Menus/Buttons/ButtonTest.png"));
			play1 = ImageIO.read(ClassLoader.getSystemResourceAsStream("Menus/Buttons/ButtonTestSelected.png"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		components.add(new Button(300, 400, 128, 64, 40,  play0, play1, "NO", "")
		{
			public void buttonAction()
			{
				Game.setScreen(Game.getPreviousScreen());
			}
		});
		components.add(new Button(500, 400, 128, 64, 40,  play0, play1, "YES", "")
		{
			public void buttonAction()
			{
				System.exit(1);
			}
		});
	}
	
	public void update(float delta)
	{
		Game.screens.get("MovingBackgroundScreen").update(delta);
		super.update(delta);
	}
	
	public void render(Graphics g)
	{
		Game.screens.get("MovingBackgroundScreen").render(g);
		super.render(g);
	}
}
