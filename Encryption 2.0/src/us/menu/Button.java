package us.menu;

import java.awt.*;
import java.awt.event.MouseEvent;

//I like to make my own buttons because I don't like the standard JButtons for artistic reasons
public class Button {
	
	protected Shape boundary;
	
	protected Color outlineColor, fillColor, noUseColor, mouseOverColor, clickColor, textColor;
	
	protected String text;
	protected int textSize, selectedTextSize, unSelectedTextSize;
	protected String font;
	protected Point textLocation;
	
	protected boolean clicked, useBoundary;
	
	public Button() {
		boundary = new Rectangle();
		
		outlineColor = Color.black;
		textColor = Color.black;
		noUseColor = Color.white; //fill color when button isn't being used
		mouseOverColor = Color.lightGray; //fill color when mouse is in button but not clicking
		clickColor = Color.gray; //fill color when mouse is clicking
		fillColor = noUseColor; //fillColor is the color being used 
		
		text = "";
		textLocation = new Point(); //text location relative to boundary
		selectedTextSize = 30;
		unSelectedTextSize = 24;
		textSize = unSelectedTextSize;
		font = "Impact";
		
		clicked = false;
		useBoundary = true; //if false, will not draw boundary and will ignore textColor
	}
	
	public void draw(Graphics2D g) {
		if(useBoundary) {
			g.setColor(fillColor);
			g.fill(boundary);
		
			g.setColor(outlineColor);
			g.draw(boundary);
		
			g.setColor(textColor);
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
		} else
			fillColor = mouseOverColor;
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

	//Getters and setters
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isClicked() {
		return clicked;
	}
	public void setBoundary(Shape boundary) {
		this.boundary = boundary;
	}
	public void setOutlineColor(Color outlineColor) {
		this.outlineColor = outlineColor;
	}
	public void setNoUseColor(Color noUseColor) {
		this.noUseColor = noUseColor;
		fillColor = this.noUseColor;
	}
	public void setMouseOverColor(Color mouseOverColor) {
		this.mouseOverColor = mouseOverColor;
	}
	public void setClickColor(Color clickColor) {
		this.clickColor = clickColor;
	}
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}
	public void setUnSelectedTextSize(int textSize) {
		unSelectedTextSize = textSize;
	}
	public void setSelectedTextSize(int newSize) {
		selectedTextSize = newSize;
	}
	public void setFont(String font) {
		this.font = font;
	}
	public void setTextLocation(Point textLocation) {
		this.textLocation = textLocation;
	}
	public void setTextLocation(int x, int y) {
		textLocation.setLocation(x, y);
	}
	public void useBoundary(boolean useBoundary) {
		this.useBoundary = useBoundary;
	}
	public boolean useBoundary() {
		return useBoundary;
	}

}
