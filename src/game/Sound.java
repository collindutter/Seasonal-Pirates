package game;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

public enum Sound
{
	YARG("yarg.wav");
	
	private Clip clip;

	Sound(String soundFileName)
	{
		try
		{
			URL url = this.getClass().getClassLoader().getResource("Sounds/" + soundFileName);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
		}catch(UnsupportedAudioFileException e)
		{
			e.printStackTrace();
		}catch(IOException e)
		{
			e.printStackTrace();
		}catch(LineUnavailableException e)
		{
			e.printStackTrace();
		}
	}
	
	public void play()
	{
		if (clip.isRunning())
			clip.stop();
		clip.setFramePosition(0);
		clip.start();
		
	}

	// Optional method to pre-load all the sound files.
	public static void init()
	{
		values();
	}
}