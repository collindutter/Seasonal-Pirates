package screen;

import game.Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import UIComponent.Button;

public class LoadingScreen extends Screen
{
	private BufferedImage backgroundImage, dimmer;
	
	public LoadingScreen()
	{
		key = "LoadingScreen";
		try
		{
			backgroundImage = ImageIO.read(ClassLoader.getSystemResourceAsStream("Menus/MainMenuBackground.png"));
			dimmer = ImageIO.read(ClassLoader.getSystemResourceAsStream("Menus/titleDimmer.png"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void render(Graphics g)
	{
		g.drawImage(backgroundImage, 0, 0, Game.WIDTH, Game.HEIGHT, null);
		g.drawImage(dimmer, 0, 0, Game.WIDTH, Game.HEIGHT, null);
		g.setColor(new Color(0xC60000));
		g.setFont(new Font("mael", Font.BOLD, 100));
		g.drawString("LOADING", 75, 175);
		g.setFont(new Font("mael", Font.BOLD, 50));
		g.drawString("YOU SUCK", 100, 300);
//		g.drawString("8=======D", 100, 400);
		super.render(g);
	}
}
