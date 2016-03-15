package screen;

import game.Game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import UIComponent.Button;

public class PauseScreen extends Screen
{
	private BufferedImage image0, image1, dimmer;
	
	public PauseScreen()
	{
		key = "PauseScreen";
		try
		{
//			backgroundImage = ImageIO.read(ClassLoader.getSystemResourceAsStream("Menus/MainMenuBackground.png"));
			image0 = ImageIO.read(ClassLoader.getSystemResourceAsStream("Menus/Buttons/ButtonTest.png"));
			image1 = ImageIO.read(ClassLoader.getSystemResourceAsStream("Menus/Buttons/ButtonTestSelected.png"));
			dimmer = ImageIO.read(ClassLoader.getSystemResourceAsStream("Menus/dimmer.png"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		components.add(new Button(380, 200, 190, 64, 40,  image0, image1, "RETURN", "")
		{
			public void buttonAction()
			{
				Game.setScreen(Game.getPreviousScreen());
			}
		});
		components.add(new Button(330, 300, 300, 64, 40,  image0, image1, "TITLESCREEN", "TitleScreen"));
		components.add(new Button(380, 400, 200, 64, 40,  image0, image1, "OPTIONS", "OptionScreen"));
		components.add(new Button(410, 500, 128, 64, 40,  image0, image1, "QUIT", "QuitVerificationScreen"));
	}
	
	public void update(float delta)
	{
		super.update(delta);
	}
	
	public void render(Graphics g)
	{
		Game.screens.get(Game.getPreviousScreen()).render(g);
		g.drawImage(dimmer, 0, 0, Game.WIDTH, Game.HEIGHT, null);//dims screen
		super.render(g);
	}
}
