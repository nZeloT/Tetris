package menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import config.Settings;

public class MenuChBx extends MenuItem {
	public boolean on;
	public MenuChBx(String aInfo) {
		super();
		selectable = true;
		additionalInfo = aInfo;
		on = Settings.getItem(additionalInfo).charAt(0) == '0' ? false : true;
	}

	public MenuChBx(String t, float xK, float yK, Font f, String sKey) {
		super(t, xK, yK, f);
		selectable = true;
		additionalInfo = sKey;
		on = Settings.getItem(additionalInfo).charAt(0) == '0' ? false : true;
	}
	
	@Override
	public void draw(Graphics g, int pWidth, int pHeight) {
		super.draw(g, pWidth, pHeight);
		FontMetrics fm = g.getFontMetrics(font);
		
		int oX = (int) (pWidth - (pWidth * x) - fm.getHeight());
		Color oC = g.getColor();
		g.setColor(on ? Color.GREEN : Color.RED);
		g.fillRect(oX, (int) (y*pHeight), fm.getHeight(), fm.getHeight());
		g.setColor(oC);
	}
	
	@Override
	public void drawSelection(Graphics g, int pWidth, int pHeight) {
		Rectangle2D rect = getSelBounds(g, pWidth, pHeight);
		int oX = (int) (rect.getX());
		int oY = (int) (rect.getY());
		int width = (int) (rect.getWidth());
		int height = (int) (rect.getHeight());
		
		if(on){
			//Links
			g.fillPolygon(new int[]{oX, oX+10, oX+10}, new int[]{(int) (oY+height/2.0f), (int)(oY+height/2.0f+10), (int)(oY+height/2.0f-10)}, 3);
		}else{
			//Rechts
			g.fillPolygon(new int[]{oX+width, oX+width-10, oX+width-10}, new int[]{(int) (oY+height/2.0f), (int) (oY+height/2.0f+10), (int) (oY+height/2.0f-10)}, 3);
		}
	}
	
	@Override
	public int getX(Graphics g, int pWidth) {
		return (int) (x*pWidth);
	}

	@Override
	public Rectangle2D getSelBounds(Graphics g, int pWidth, int pHeight) {
		Rectangle2D rect = getBoundsRect(g, text);
		int oX = (int) (pWidth - (pWidth * x) - g.getFontMetrics(font).getHeight());
		rect.setRect(oX-20, getBoundsY(pHeight)-10, rect.getHeight()+40, rect.getHeight()+20);
		return rect;
	}
	
	@Override
	public void keyIn(KeyEvent key) {

		switch (key.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			on = false;
			break;
		case KeyEvent.VK_RIGHT:
			on = true;
			break;
		default:
			break;
		}
	}

}
