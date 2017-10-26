package us.menu;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public class ToggleButton extends Button {
	
	public ToggleButton() {
		super();
	}
	
	public void draw(Graphics2D g) {
		if(useBoundary) {		
			g.setColor(outlineColor);
			g.setStroke(new BasicStroke(2));
			g.draw(boundary);
			g.setStroke(new BasicStroke(1));
		
			g.setColor(fillColor);
			g.setFont(new Font(font, 12, textSize));
			int x = boundary.getBounds().x + textLocation.x;
			int y = boundary.getBounds().y + textLocation.y;
			if(textSize == selectedTextSize) {
				x -= (selectedTextSize - unSelectedTextSize) * 1.3; //approximately centers the text if the difference is less than 10
				y += (selectedTextSize - unSelectedTextSize) * 0.4;
			}
			g.drawString(text, x, y);
		} else {
			g.setColor(fillColor);
			g.setFont(new Font(font, 12, textSize));
			int x = boundary.getBounds().x + textLocation.x;
			int y = boundary.getBounds().y + textLocation.y;
			if(textSize == selectedTextSize) {
				x -= (selectedTextSize - unSelectedTextSize) * 1.3; //approximately centers the text if the difference is less than 10
				y += (selectedTextSize - unSelectedTextSize) * 0.4;
			}
			g.drawString(text, x, y);
		}
	}
	
	public void mousePressed(MouseEvent k) {
		if(boundary.contains(k.getPoint())) 
			fillColor = clickColor;
	}
	public void mouseClicked(MouseEvent k) {
		clicked = false; 
	}
	public void mouseReleased(MouseEvent k) {
		clicked = boundary.contains(k.getPoint());;
		
		if(!clicked) {
			textSize = unSelectedTextSize;
			fillColor = noUseColor;
		} else {
			fillColor = mouseOverColor;
			useBoundary = !useBoundary;
		}
	}
	public void mouseMoved(MouseEvent k) {
		if(boundary.contains(k.getPoint())) {
			fillColor = mouseOverColor;
			textSize = selectedTextSize;
		}
		else {
			fillColor = noUseColor;
			textSize = unSelectedTextSize;
		}
	}
	
	public void setUseBoundary(boolean use) {
		useBoundary = use;
	}
	

}
