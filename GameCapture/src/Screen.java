import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;

import javax.swing.JFrame;

public class Screen 
{
	GraphicsDevice vc;
	
	public Screen()
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		vc = ge.getDefaultScreenDevice();
	}
	
	public void setFullScreen(DisplayMode gm, JFrame win)
	{
		win.setUndecorated(true);
		win.setResizable(false);
		
		vc.setFullScreenWindow(win);
		
		if (gm!=null && vc.isDisplayChangeSupported())
		{
			vc.setDisplayMode(gm);
		}
	}
	
	public void CloseFullScreen()
	{
		Window w = vc.getFullScreenWindow();
		
		if (w != null)
		{
			w.dispose();
		}
		
		vc.setFullScreenWindow(null);
	}
}
