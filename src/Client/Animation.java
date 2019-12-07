package Client;

import javax.swing.*;
import java.awt.*;

public class Animation extends JPanel {

	private static final long serialVersionUID = 1L;
	int w;
    int h;

    public Animation(int hauteur){
        this.w = 2*hauteur;
        this.h = 2*hauteur;
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillOval(100-w/2, 100-h/2, w,h);
    }
}
