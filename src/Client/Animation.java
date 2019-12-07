package Client;

import javax.swing.*;
import java.awt.*;

public class Animation extends JPanel {

    int w;
    int h;

    public Animation(int hauteur){
        this.w = 2*hauteur;
        this.h = 2*hauteur;
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillOval(0, 0, w,h);
    }
}
