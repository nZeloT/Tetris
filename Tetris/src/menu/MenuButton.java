package menu;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public class MenuButton extends MenuItem {

	public MenuButton() {
		super();
		selectable = true;
	}

	public MenuButton(String t, float xK, float yK, Font f) {
		super(t, xK, yK, f);
		selectable = true;
	}
	
	@Override
	public void drawSelection(Graphics g, int pWidth, int pHeight) {
		Rectangle2D rect = getSelBounds(g, pWidth, pHeight);
		int oX = (int) (rect.getX());
		int oY = (int) (rect.getY());
		int width = (int) (rect.getWidth());
		int height = (int) (rect.getHeight());

		//Sekrecht
		g.drawLine(oX, oY, oX, oY+height);
		g.drawLine(oX+width, oY, oX+width, oY+height);

		//Waagrecht oben
		g.drawLine(oX, oY, oX+15, oY);
		g.drawLine(oX+width-15, oY, oX+width, oY);

		//Waagrecht unten
		g.drawLine(oX, oY+height, oX+15, oY+height);
		g.drawLine(oX+width-15, oY+height, oX+width, oY+height);
	}
}
