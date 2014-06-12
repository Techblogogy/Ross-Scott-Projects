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
import com.sun.jna.Memory;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HBITMAP;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.INT_PTR;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinGDI;
import com.sun.jna.platform.win32.WinGDI.BITMAPINFO;
import com.sun.jna.platform.win32.WinNT.HANDLE;

import jna.extra.GDI32Extra;
import jna.extra.User32Extra;
import jna.extra.WinGDIExtra;

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
	
	public static HWND hWnd;
	
	public static int screenId = 0;
	
	ScreenMag()
	{
		
	}
	
	public static BufferedImage capture(HWND hWnd)
	{
		HDC hdcWindow = User32.INSTANCE.GetDC(hWnd);
        HDC hdcMemDC = GDI32.INSTANCE.CreateCompatibleDC(hdcWindow);

        RECT bounds = new RECT();
        User32Extra.INSTANCE.GetClientRect(hWnd, bounds);

        wWidth = bounds.right - bounds.left;
        wHeight = bounds.bottom - bounds.top;

        HBITMAP hBitmap = GDI32.INSTANCE.CreateCompatibleBitmap(hdcWindow, wWidth, wHeight);

        HANDLE hOld = GDI32.INSTANCE.SelectObject(hdcMemDC, hBitmap);
        GDI32Extra.INSTANCE.BitBlt(hdcMemDC, 0, 0, wWidth, wHeight, hdcWindow, 0, 0, WinGDIExtra.SRCCOPY);

        GDI32.INSTANCE.SelectObject(hdcMemDC, hOld);
        GDI32.INSTANCE.DeleteDC(hdcMemDC);

        BITMAPINFO bmi = new BITMAPINFO();
        bmi.bmiHeader.biWidth = wWidth;
        bmi.bmiHeader.biHeight = -wHeight;
        bmi.bmiHeader.biPlanes = 1;
        bmi.bmiHeader.biBitCount = 32;
        bmi.bmiHeader.biCompression = WinGDI.BI_RGB;

        Memory buffer = new Memory(wWidth * wHeight * 4);
        GDI32.INSTANCE.GetDIBits(hdcWindow, hBitmap, 0, wHeight, buffer, bmi, WinGDI.DIB_RGB_COLORS);

        BufferedImage image = new BufferedImage(wWidth, wHeight, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, wWidth, wHeight, buffer.getIntArray(0, wWidth * wHeight), 0, wWidth);

        GDI32.INSTANCE.DeleteObject(hBitmap);
        User32.INSTANCE.ReleaseDC(hWnd, hdcWindow);

        return image;
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
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gd = ge.getScreenDevices();
		
		if (args[1] != null)
		{		
			screenId = Integer.parseInt(args[1])-1;
			System.out.println(screenId);
			
			sWidth = gd[screenId].getDisplayMode().getWidth();
			sHeight = gd[screenId].getDisplayMode().getHeight();
			
			System.out.println(sWidth);
			System.out.println(sHeight);
			
			win.setLocation( Integer.parseInt(args[2]), Integer.parseInt(args[3]) );
		}
		
		//GraphicsConfiguration gc = gd.getDefaultConfiguration();
		
		//gd.setFullScreenWindow(win);
		
		String windowName = args[0]; //"Grim Fandango";
		int[] rect;
		nWidth = (float)wWidth/(float)wHeight * sHeight;
		nX = (float)sWidth/(float)2 - nWidth/(float)2;
		
		hWnd = User32.INSTANCE.FindWindow(null, windowName);
		if (hWnd == null)
		{
			System.exit(1);
		}
		
		/*User32.INSTANCE.MoveWindow(hWnd, 10, 10, 100, 100, true);
		
		System.exit(1);*/
		
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
				
				hWnd = User32.INSTANCE.FindWindow(null, windowName);
				if (hWnd == null)
					System.exit(0);
				
				dImg = capture(hWnd);
				
				nWidth = (float)wWidth/(float)wHeight * sHeight;
				nX = (float)sWidth/(float)2 - nWidth/(float)2;
				
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
