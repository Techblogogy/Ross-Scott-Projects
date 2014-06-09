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
	
	public void setImg() throws Exception
	{
		rob = new Robot();
		dImg = rob.createScreenCapture(new Rectangle(0,0,800,600));
		
		/*mousePos = MouseInfo.getPointerInfo().getLocation();
		System.out.println(mousePos);*/
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		try {
			setImg();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		g.drawImage(dImg, 0, 0, 800, 600, null);
	}
}
