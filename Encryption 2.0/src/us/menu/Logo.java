package us.menu;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import us.main.Constants;

public class Logo {
	
	public static void draw(Graphics2D g) {
		//Outline
		g.setStroke(new BasicStroke(3));
		g.setColor(Color.black);
		g.draw(new RoundRectangle2D.Float(6, 24, Constants.WIDTH - 12, Constants.HEIGHT - 30, 29, 29));
		g.setStroke(new BasicStroke(1));
		
		//Title
		g.setColor(Color.white);
		g.fillRect(Constants.WIDTH / 2 - 40, 0, 80, 30);
		
		g.setColor(Color.black);
		g.setFont(new Font("Impact", 12, 30));
		g.drawString("CYBER", Constants.WIDTH / 2 - 38, 28);
		g.drawString("STRONG", Constants.WIDTH / 2 - 47, 55);
	}

}
