import java.awt.*;  
import java.awt.geom.AffineTransform;  
import java.awt.image.BufferedImage;  
import javax.swing.*;  
import javax.swing.event.*;  
   
public class Magnifier extends JPanel {  
    BufferedImage image;  
    double scale = 50.0;  
   
    protected void paintComponent(Graphics g) {  
        super.paintComponent(g);  
        if(image == null) {  
            initImage();  
        }  
        double x = (getWidth() - scale*image.getWidth())/2.0;  
        double y = (getHeight() - scale*image.getHeight())/2.0;  
        AffineTransform at = AffineTransform.getTranslateInstance(x,y);  
        at.scale(scale, scale);  
        ((Graphics2D)g).drawRenderedImage(image, at);  
    }  
   
    private void initImage() {  
        int w = getWidth();  
        int h = getHeight();  
        int type = BufferedImage.TYPE_INT_RGB;  
        image = new BufferedImage(w, h, type);  
        Graphics2D g2 = image.createGraphics();  
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  
                            RenderingHints.VALUE_ANTIALIAS_ON);  
        g2.setBackground(getBackground());  
        g2.clearRect(0,0,w,h);  
        g2.setPaint(Color.blue);  
        g2.drawLine(w/4, h*3/4, w*3/4, h/4);  
        g2.dispose();  
    }  
   
    private JSlider getSlider() {  
        JSlider slider = new JSlider(10, 2000, 500);  
        slider.addChangeListener(new ChangeListener() {  
            public void stateChanged(ChangeEvent e) {  
                int value = ((JSlider)e.getSource()).getValue();  
                scale = value/10.0;  
                //System.out.printf("scale = %.1f%n", scale);  
                repaint();  
            }  
        });  
        return slider;  
    }  
   
    public static void main(String[] args) {  
    	Magnifier test = new Magnifier();  
        JFrame f = new JFrame();  
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        f.add(test);  
        f.add(test.getSlider(), "Last");  
        f.setSize(400,400);  
        f.setLocation(200,200);  
        f.setVisible(true);  
    }
}