import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Capper {
	private JPanel jp = new Drawer();
	private Timer t;
	private boolean isRunning;
	private JFrame frame;
	private DisplayMode dm;
	
	public int screenH;
	public int screenW;
	
	public Capper()
	{
		screenH = Toolkit.getDefaultToolkit().getScreenSize().height;
		screenW = (screenH * 800) / 600;
		
		dm = new DisplayMode(800, 600, 16, DisplayMode.REFRESH_RATE_UNKNOWN);
		
		frame = new JFrame("Region");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.getContentPane().add(jp);
		
		frame.setResizable(false);
		frame.setUndecorated(true);
		frame.setPreferredSize(new Dimension(800,600));
		//frame.setLocation(8, 30);
		frame.setAlwaysOnTop(true);
		
		frame.setBackground(new Color(0,0,0,0));
		
		/*Screen sc = new Screen();
		sc.setFullScreen(dm, frame);*/
		
		frame.pack();
		frame.setVisible(true);
		
		isRunning = true;
		
		jp.addMouseListener(new MouseAdapter ()
		{
			public void mouseClicked(MouseEvent evt)
			{
				Robot bot = null;
				
				try {
					bot = new Robot();
				} catch (AWTException e) {
					e.printStackTrace();
				}
				
				Point prevPos = MouseInfo.getPointerInfo().getLocation();
				
				bot.mouseMove(evt.getX(), evt.getY());
				
				bot.mousePress(InputEvent.BUTTON1_MASK);
				bot.mouseRelease(InputEvent.BUTTON1_MASK);
				
				bot.mouseMove(prevPos.x, prevPos.y);
			}
		});
		
		t = new Timer();
		t.schedule(new GLoop(), 0, 1000/60);
	}
	
	private class GLoop extends TimerTask
	{
		public void run()
		{	
			jp.repaint();
			
			if (!isRunning)
			{
				t.cancel();
			}
		}
	}
	
	public static void main (String[] args)
	{		
		Capper cp = new Capper();
	}
}
