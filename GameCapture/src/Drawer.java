import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class Drawer extends JPanel{
	private static final long serialVersionUID = 1L;
	
	public Point mousePos;
	
	private BufferedImage dImg = null;
	private Robot rob = null;
	
	private boolean render = false;
	
	public Drawer()
	{
		setOpaque(false);
		
		try {
			rob = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public void setImg() throws Exception
	{
		dImg = rob.createScreenCapture(new Rectangle(0,0,800,600));
		
		/*mousePos = MouseInfo.getPointerInfo().getLocation();
		System.out.println(mousePos);*/
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		/*try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try {
			setImg();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Captured");
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		g.drawImage(dImg, 0, 0, 1024, 768, null);
		System.out.println("Printed");
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		g.clearRect(0, 0, 800, 600);
		System.out.println("Cleared");*/
		
		/*try {
			setImg();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		g.drawImage(dImg, 0, 0, 1024, 768, null);*/
		
		g.drawImage(dImg, 0, 0, 1024, 768, null);
		System.out.println("Draw");
	}
}
