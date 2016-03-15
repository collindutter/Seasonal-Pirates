package screen;

import game.Game;
import game.Utility;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import tiles.Tile;

import UIComponent.Button;

public class TitleScreen extends Screen
{
	private BufferedImage logo, backgroundImage, play0, play1;
	
	private String title = "Pirates";
	
	
	public TitleScreen()
	{
		key = "TitleScreen";
		
		try
		{
			logo = ImageIO.read(ClassLoader.getSystemResourceAsStream("Menus/PiratesLogoTemplate.png"));
			backgroundImage = ImageIO.read(ClassLoader.getSystemResourceAsStream("Menus/MainBackgroundMap.png"));
			play0 = ImageIO.read(ClassLoader.getSystemResourceAsStream("Menus/Buttons/ButtonTest.png"));
			play1 = ImageIO.read(ClassLoader.getSystemResourceAsStream("Menus/Buttons/ButtonTestSelected.png"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		components.add(new Button(200, 350, 128, 64, 40,  play0, play1, "PLAY", "WorldScreen"));
		components.add(new Button(200, 450, 195, 64, 40,  play0, play1, "OPTIONS", "OptionScreen"));
		
		components.add(new Button(200, 550, 128, 64, 40,  play0, play1, "QUIT", "QuitVerificationScreen"));
	}
	
	public void update(float delta)
	{
		Game.screens.get("MovingBackgroundScreen").update(delta);
		super.update(delta);
	}
	
	public void render(Graphics g)
	{
		Game.screens.get("MovingBackgroundScreen").render(g);
		g.drawImage(logo, 0, 10, logo.getWidth() * 2, logo.getHeight() * 2, null);
		g.setColor(new Color(0xC60000));
		g.setFont(new Font("mael", Font.BOLD, 160));
		g.drawString(title, 75, 175);
		super.render(g);
	}
	
	
}