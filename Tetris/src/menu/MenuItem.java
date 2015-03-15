package menu;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

public abstract class MenuItem{
	public String text;
	public float x;
	public float y;
	public boolean selectable;
	public Font font;
	public String additionalInfo;
	
	public MenuItem() {
	}
	
	public MenuItem(String t, float xK, float yK, Font f) {
		text = t;
		x = xK;
		y = yK;
		font = f;
	}
	
	public int getX(Graphics g, int pWidth){		
		Rectangle2D rect = getBoundsRect(g, text);

		int textWidth  = (int)(rect.getWidth());
		int panelWidth = (int)(pWidth);

		int x = (panelWidth  - textWidth)  / 2;
		return x;
	}
	
	public int getBoundsY(int pHeight){
		return (int)(y * pHeight);
	}
	
	public int getFontY(Graphics g, int pHeight){
		FontMetrics fm   = g.getFontMetrics(font);
		return (int) (pHeight*y + fm.getAscent());
	}
	
	public Rectangle2D getBoundsRect(Graphics g, String t){
		FontMetrics fm   = g.getFontMetrics(font);
		Rectangle2D rect = fm.getStringBounds(t, g);
		return rect;
	}
	
	public void draw(Graphics g, int pWidth, int pHeight){
		g.setFont(font);
		g.drawString(text, getX(g, pWidth), getFontY(g, pHeight));
	}
	
	public void drawSelection(Graphics g, int pWidth, int pHeight){
		return;
	}
	
	public Rectangle2D getSelBounds(Graphics g, int pWidth, int pHeight) {
		Rectangle2D rect = (Rectangle2D) getBoundsRect(g, text).clone();
		rect.setRect(getX(g, pWidth)-10, getBoundsY(pHeight)-10, rect.getWidth()+20, rect.getHeight()+20);
		return rect;
	}
	
	public void keyIn(KeyEvent key){
		return;
	}

	@Override
	public String toString() {
		return "MenuItem [text=" + text + ", x=" + x + ", y=" + y
				+ ", selectable=" + selectable + ", font=" + font + "]";
	}
	
	
}