package game;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JFrame;

import screen.LoadingScreen;
import screen.MiniMapScreen;
import screen.MovingBackgroundScreen;
import screen.OptionScreen;
import screen.PauseScreen;
import screen.QuitVerificationScreen;
import screen.Screen;
import screen.TitleScreen;
import screen.WorldScreen;

public class Game extends Canvas implements Runnable
{
	private static final long serialVersionUID = 1L;
	private static boolean running;
	public  static int WIDTH = 960;
	public  static int HEIGHT = WIDTH * 3 / 4;
	private Thread gameThread;
	private static World world;
	public static Input input;
	public static double loadPercent;
	public static int ticks;
	private static Screen currentScreen;
	private static Screen previousScreen;//keeps track of the last screen you were on
	public static HashMap<String, Screen> screens = new HashMap<String, Screen>();
	
	public Game()
	{
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		setIgnoreRepaint(true);
		requestFocus();	
	}
	
	public static void main(String[] args)
	{
		Game game = new Game();
		JFrame frame = new JFrame("Seasonal Pirates");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(game);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		game.startGame();
//		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//		String [] fonts = ge.getAvailableFontFamilyNames();
//		for(String s : fonts)
//			System.out.println(s);
	}
	
	public void startGame()
	{
		running = true;
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void stopGame()
	{
		running = false;
		try
		{
			gameThread.join();
		}catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	private void init()
	{
		screens.put("LoadingScreen", new LoadingScreen());
		screens.put("OptionScreen", new OptionScreen());
		screens.put("PauseScreen", new PauseScreen());
		screens.put("MovingBackgroundScreen", new MovingBackgroundScreen());
		screens.put("QuitVerificationScreen", new QuitVerificationScreen());
		currentScreen = screens.get("LoadingScreen");
	
		
		input = new Input(this);
		System.out.println("Class Path " + System.getProperty("java.class.path"));
		new Thread()//loads world
		{
			public void run()
			{
				long startLoadTime = System.currentTimeMillis();
				world = new World();
				System.out.println("WORLD LOAD TIME: " + (double)((System.currentTimeMillis() - startLoadTime)) / 1000 + " seconds");
				screens.put("WorldScreen", new WorldScreen(world));
				screens.put("MiniMapScreen", new MiniMapScreen());
				screens.put("TitleScreen", new TitleScreen());
				currentScreen = screens.get("WorldScreen");
			}
		}.start();
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font mael = null;
		try
		{
			mael = Font.createFont(Font.TRUETYPE_FONT, new BufferedInputStream(Game.class.getClassLoader().getResourceAsStream("mael.ttf")));
		} 
		catch (FontFormatException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		ge.registerFont(mael);
		
	}
	
	public void run()
	{
		init();
		
		long lastTime = System.nanoTime();
		float delta = 0.0f;
		double nsPerTick = 1000000000.0 / 60.0;
		long startTimer = System.currentTimeMillis();
		int frames = 0;
		int infoTicks = 0;
		
		while(running)
		{
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			
			while(delta >= 1)
			{
				update(delta);
				ticks++;
				infoTicks++;
				delta--;
			}
			render();
			frames++;
			try
			{
				Thread.sleep(2);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			if(System.currentTimeMillis() - startTimer > 1000)
			{
//				System.out.println("FPS: " + frames + " Updates: " + infoTicks);
				startTimer += 1000;
				infoTicks = 0;
				frames = 0;
			}
		}
		stopGame();
	}
	
	public void render()
	{
		BufferStrategy bs = getBufferStrategy();
		if(bs == null)
		{
			createBufferStrategy(2);
			requestFocus();
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		if(currentScreen != null)
			currentScreen.render(g);
		else
		{
			g.setColor(Color.BLACK);
			g.drawString("LOADING... " + loadPercent + "%", (Game.WIDTH / 2) - 15, Game.HEIGHT / 2);
		}
		bs.show();
		g.dispose();
	}
	
	public void update(float delta)
	{
		input.update();
		if(currentScreen != null)
			currentScreen.update(delta);
	}
	
	public static World getWorld()
	{
		return world;
	}
	
	public static void setScreen(String key)
	{
		previousScreen = currentScreen;
		currentScreen = screens.get(key);
	}
	
	public static String getPreviousScreen()
	{
		return previousScreen.getKey();
	}
}