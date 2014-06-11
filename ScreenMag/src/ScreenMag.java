import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.Arrays;
import com.sun.jna.*;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.*;

public class ScreenMag {
	public static Robot rob;
	public static BufferedImage dImg;
	
	public static int wX;
	public static int wY;
	public static int wWidth;
	public static int wHeight;
	
	public static int sWidth;
	public static int sHeight;
	
	public static float nX;
	public static float nWidth;
	
	public static int topB = 30;
	public static int sideB = 8;
	
	ScreenMag()
	{
		
	}
	
	public interface User32 extends StdCallLibrary 
	{
		User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);
		HWND FindWindow(String lpClassName, String lpWindowName);
		
		int GetWindowRect(HWND handle, int[] rect);
	}
	
	public static int[] getRect(String windowName) throws WindowNotFoundException, GetWindowRectException 
	{
		HWND hwnd = User32.INSTANCE.FindWindow(null, windowName);
		
		if (hwnd == null) 
		{
			throw new WindowNotFoundException("", windowName);
		}
		
		int[] rect = {0, 0, 0, 0};
		int result = User32.INSTANCE.GetWindowRect(hwnd, rect);
		
		if (result == 0)
		{
			throw new GetWindowRectException(windowName);
		}
		
		return rect;
	}
	
	@SuppressWarnings("serial")
	public static class WindowNotFoundException extends Exception 
	{
		public WindowNotFoundException(String className, String windowName) 
		{
			super(String.format("Window null for className: %s; windowName: %s", className, windowName));
		}
	}
	
	@SuppressWarnings("serial")
	public static class GetWindowRectException extends Exception {
		public GetWindowRectException(String windowName) {
			super("Window Rect not found for " + windowName);
		}
	}
	
	public static void main(String[] args) 
	{
		//Create Main Window
		JFrame win = new JFrame();
		win.setIgnoreRepaint(true);
		
		win.setResizable(false);
		win.setUndecorated(true);
		win.setAlwaysOnTop(true);
		win.setState(JFrame.MAXIMIZED_BOTH);
		win.setLocation(0,0);
		
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		sWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		sHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		/*GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		
		gd.setFullScreenWindow(win);*/
		
		String windowName = args[0]; //"Grim Fandango";
		int[] rect;
		
		try 
		{
			rect = getRect(windowName);
			
			wX = rect[0]+sideB; //1928;
			wY = rect[1]+topB; //30;
			wWidth = (rect[2] - wX)-sideB; //640;
			wHeight = (rect[3] - wY)-sideB; //478;
			
			//System.out.println(wX+" "+wY+" "+(wWidth-wX+" "+(wHeight-wY));
		} catch (WindowNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (GetWindowRectException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		nWidth = (float)wWidth/(float)wHeight * sHeight;
		nX = (float)sWidth/(float)2 - nWidth/(float)2;
		
		//Create Canvas
		Canvas cnv = new Canvas();
		cnv.setIgnoreRepaint(true);
		cnv.setSize(sWidth,sHeight);
		
		win.add(cnv);
		win.pack();
		win.setVisible(true); 
		
		//Create BackBuffer
		cnv.createBufferStrategy(2);
		BufferStrategy buffer = cnv.getBufferStrategy();
		
		Graphics graphics = null;
		
		try {
			rob = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		while (true)
		{
			try
			{
				graphics = buffer.getDrawGraphics();
				
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, sWidth, sHeight);
				
				try 
				{
					rect = getRect(windowName);
					
					wX = rect[0]+sideB; //1928;
					wY = rect[1]+topB; //30;
					wWidth = (rect[2] - wX)-sideB; //640;
					wHeight = (rect[3] - wY)-sideB; //478;
					
					//System.out.println(wX+" "+wY+" "+(wWidth-wX+" "+(wHeight-wY));
				} catch (WindowNotFoundException e) {
					e.printStackTrace();
					System.exit(1);
				} catch (GetWindowRectException e) {
					e.printStackTrace();
					System.exit(1);
				}
				
				//TODO DrawStuff
				dImg = rob.createScreenCapture(new Rectangle(wX,wY,wWidth,wHeight));
				
				graphics.drawImage(dImg, (int)nX, 0, (int)nWidth, sHeight,null);
				
				if (!buffer.contentsLost())
					buffer.show();
				
				Thread.yield();
			}
			finally
			{
				if (graphics != null)
					graphics.dispose();
			}
		}
	}
}
