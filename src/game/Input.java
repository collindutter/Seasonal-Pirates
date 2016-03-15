package game;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import tiles.Tile;

public class Input extends MouseAdapter implements KeyListener
{
	private boolean[] keys = new boolean[700];
	public boolean up, left, right, down, miniMapOpened, esc, e, q;
	
	Game game;
	
	public boolean mouseClicked;
	public boolean leftMousePressed;
	public boolean rightMousePressed;
	public Point mouseLocation;
	public boolean cursorOnScreen = true;
	public int wheelClicks = Tile.TILE_SIZE;
	
	private double pressX, pressY;
	private double releaseX, releaseY;
	
	
	public Input(Game game)
	{
		this.game = game;
		game.addKeyListener(this);
		game.addMouseListener(this);
		game.addMouseWheelListener(this);
		Point mp = game.getMousePosition();
//		mouseLocation = mp == null ? new Point(Game.WIDTH / 2, Game.HEIGHT / 2) : mp;
	}
	
	public void update()
	{
		updateKeys();
		Point mp = game.getMousePosition();
		mouseLocation = mp == null ? new Point(Game.WIDTH / 2, Game.HEIGHT / 2) : mp;
	}
	
	public void updateKeys()
	{
		left = keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT];
		right = keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT];
		down = keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN];
		up = keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP];
		miniMapOpened = keys[KeyEvent.VK_M];
		esc = keys[KeyEvent.VK_ESCAPE];
		e = keys[KeyEvent.VK_E];
		q = keys[KeyEvent.VK_Q];
	}
	
	public void keyPressed(KeyEvent e)
	{
		keys[e.getKeyCode()] = true;
		
		if(e.getKeyCode() == KeyEvent.VK_EQUALS && wheelClicks <= 98)
			wheelClicks++;
		if(e.getKeyCode() == KeyEvent.VK_MINUS && wheelClicks >= 10)
			wheelClicks--;
		
	}
	
	public void keyReleased(KeyEvent e)
	{
		keys[e.getKeyCode()] = false;
	}
	
	public void keyTyped(KeyEvent e)
	{
	}
	
	public void mouseClicked(MouseEvent e)
	{
		if(e.getClickCount() == 2)
			System.out.println("DOUBLE CLICK :D");
	}
	
	public void mouseEntered(MouseEvent e)
	{
		cursorOnScreen = true;
	}
	
	public void mouseExited(MouseEvent e)
	{
		cursorOnScreen = false;
	}
	
	public void mousePressed(MouseEvent e)
	{	
		pressX = mouseLocation.x;
		pressY = mouseLocation.y;
		
		if(e.getButton() == MouseEvent.BUTTON1)
		{
			leftMousePressed = true;
//			System.out.println("yeah aggin");
			if(miniMapOpened == false)
				Game.getWorld().getCamera().setSelectionBox(new SelectionBox());
		}
		if(e.getButton() == MouseEvent.BUTTON3)
			rightMousePressed = true;
	}
	
	public void mouseReleased(MouseEvent e)
	{
//		mouseClicked = false;
		
		releaseX =  mouseLocation.x;
		releaseY =  mouseLocation.y;
		
		pressX = 0;
		pressY = 0;
		
		if(e.getButton() == MouseEvent.BUTTON1)
		{
			Game.getWorld().getCamera().setSelectionBox(null);
			leftMousePressed = false;
		}
		if(e.getButton() == MouseEvent.BUTTON3)
			rightMousePressed = false;
		
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) 
	{
		if(wheelClicks >= 0) 
			wheelClicks -= e.getWheelRotation();
		if(wheelClicks <= 10)
			wheelClicks = 10;
		if(wheelClicks >= 98)
			wheelClicks = 98; 
	}
	
	//gets and sets
	public double getPressX()
	{
		return pressX;
	}
	public double getPressY()
	{
		return pressY;
	}
	public double getReleaseX()
	{
		return releaseX;
	}
	public double getReleaseY()
	{
		return releaseY;
	}
}
