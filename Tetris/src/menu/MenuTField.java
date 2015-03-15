package menu;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

public class MenuTField extends MenuItem {
	public String input = "";
	private int mLength;
	private int xSub;

	public MenuTField(String aInfo) {
		super();
		selectable = true;
		additionalInfo = aInfo;
		mLength = Integer.parseInt(additionalInfo);
		xSub = 0;
	}

	public MenuTField(String t, float xK, float yK, Font f, String aInfo) {
		super(t, xK, yK, f);
		selectable = true;
		additionalInfo = aInfo;
		mLength = Integer.parseInt(additionalInfo);
		xSub = 0;
	}
	
	@Override
	public void draw(Graphics g, int pWidth, int pHeight) {		
		if(!text.equals("-"))
			super.draw(g, pWidth, pHeight);
		else
			g.setFont(font);
		
		
		if(xSub == 0){
			String t = "";
			for (int i = 0; i < mLength; i++) {
				t += "W";
			}
			xSub = (int) g.getFontMetrics().getStringBounds(t, g).getWidth();
		}
		
		
		if(!text.equals("-")){//Mit text linksbuendig
			g.drawString(input, (int) (pWidth - (pWidth * x)-xSub), getFontY(g, pHeight));
		}else{ //Sonst zentriert
			g.drawString(input, getX(g, pWidth, input), getFontY(g, pHeight));
		}
	}
	
	@Override
	public int getX(Graphics g, int pWidth) {
		return (int) (x*pWidth);
	}
	
	public int getX(Graphics g, int pWidth, String t){		
		Rectangle2D rect = getBoundsRect(g, t);

		int textWidth  = (int)(rect.getWidth());
		int panelWidth = (int)(pWidth);

		int x = (panelWidth  - textWidth)  / 2;
		return x;
	}
	
	@Override
	public void drawSelection(Graphics g, int pWidth, int pHeight) {
		Rectangle2D rect = getSelBounds(g, pWidth, pHeight);
		int oX = (int) (rect.getX());
		int oY = (int) (rect.getY());
		int width = (int) (rect.getWidth());
		int height = (int) (rect.getHeight());
		
		//Sekrecht
		g.drawLine(oX, (int)(oY+height*0.9f), oX, oY+height);
		g.drawLine(oX+width, (int)(oY+height*0.9f), oX+width, oY+height);

		//Waagrecht unten
		g.drawLine(oX, oY+height, oX+width, oY+height);
	}
	
	@Override
	public Rectangle2D getSelBounds(Graphics g, int pWidth, int pHeight) {
		Rectangle2D rect = getBoundsRect(g, input);
		int oX = (int) (pWidth - (pWidth * x) - xSub);
		rect.setRect(oX-5, getBoundsY(pHeight)-5, xSub+10, rect.getHeight()+10);
		return rect;
	}
	
	@Override
	public void keyIn(KeyEvent key) {
		if(key.getID() != 400)
			return;
		if(Character.isLetterOrDigit(key.getKeyChar()) && input.length() < mLength)
			input += key.getKeyChar();
		if(key.getKeyChar() == (char)KeyEvent.VK_BACK_SPACE && input.length() > 0)
			input = input.substring(0, input.length()-1);
	}

}
