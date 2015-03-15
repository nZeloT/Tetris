package menu;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class MenuSpinner extends MenuItem {
	private ArrayList<String> items;
	public int cIdx;
	private int xSub;

	public MenuSpinner() {
		super();
		selectable = true;
		items = null;
		cIdx = 0;
		xSub = 0;
	}

	public MenuSpinner(String t, float xK, float yK, Font f, String aInfo) {
		super(t, xK, yK, f);
		selectable = true;
		additionalInfo = aInfo;
		items = null;
		cIdx = 0;
		xSub = 0;
	}
	
	@Override
	public void draw(Graphics g, int pWidth, int pHeight) {
		if(!text.equals("-"))
			super.draw(g, pWidth, pHeight);
		else
			g.setFont(font);
		if(items == null)
			calcItems();
		
		if(xSub == 0){
			for (int i = 0; i < items.size(); i++) {
				FontMetrics fm   = g.getFontMetrics(font);
				Rectangle2D rect = fm.getStringBounds(items.get(i), g);
				if(rect.getWidth() > xSub)
					xSub = (int) rect.getWidth();
			}
		}
		
		
		if(!text.equals("-"))//Mit text linksbuendig
			g.drawString(items.get(cIdx), (int) (pWidth - (pWidth * x)-xSub), getFontY(g, pHeight));
		else //Sonst zentriert
			g.drawString(items.get(cIdx), getX(g, pWidth, items.get(cIdx)), getFontY(g, pHeight));		
	}
	
	@Override
	public void drawSelection(Graphics g, int pWidth, int pHeight) {
		Rectangle2D rect = getSelBounds(g, pWidth, pHeight);
		int oX = (int) (rect.getX());
		int oY = (int) (rect.getY());
		int width = (int) (rect.getWidth());
		int height = (int) (rect.getHeight());
		
		boolean left = cIdx > 0;
		boolean right = cIdx < items.size()-1;

		if(left){
			//Links
			g.fillPolygon(new int[]{oX, oX+10, oX+10}, new int[]{(int) (oY+height/2.0f), (int) (oY+height/2.0f+10), (int) (oY+height/2.0f-10)}, 3);
		}
		if(right){
			//Rechts
			g.fillPolygon(new int[]{oX+width, oX+width-10, oX+width-10}, new int[]{(int) (oY+height/2.0f), (int) (oY+height/2.0f+10), (int) (oY+height/2.0f-10)}, 3);
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
	public Rectangle2D getSelBounds(Graphics g, int pWidth, int pHeight) {
		Rectangle2D rect = getBoundsRect(g, text);
		if(!text.equals("-"))
			rect.setRect(pWidth - (pWidth * x)-xSub-20, getBoundsY(pHeight)-10, xSub+40, rect.getHeight()+20);
		else
			rect.setRect(pWidth/2.0f-xSub/2.0f-20, getBoundsY(pHeight)-10, xSub+40, rect.getHeight()+20);
		return rect;
	}
	
	@Override
	public void keyIn(KeyEvent key) {

		switch (key.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			if(cIdx > 0)
				cIdx --;
			break;
		case KeyEvent.VK_RIGHT:
			if(cIdx < items.size()-1)
				cIdx ++;
			break;
		default:
			break;
		}
	}

	public void setTo(String t){
		if(items == null)
			calcItems();
		for (int i = 0; i < items.size(); i++) {
			if(items.get(i).equals(t)){
				cIdx = i;
				return;
			}
		}
	}
	
	public String getSelItem(){
		return items.get(cIdx);
	}
	
	private void calcItems(){
		cIdx = 0;
		items = new ArrayList<String>();
		if(additionalInfo.split("-")[0].equals("txt")){
			for (int i = 1; i < additionalInfo.split("-").length; i++) {
				items.add(additionalInfo.split("-")[i]);
			}
		}else{
			int min = Integer.parseInt(additionalInfo.split("-")[1]);
			int max = Integer.parseInt(additionalInfo.split("-")[2]);
			int ste = Integer.parseInt(additionalInfo.split("-")[3]);
			for (int i = min; i <= max; i+=ste) {
				items.add("" + i);
			}
		}
	}
	
	public void setItems(ArrayList<String> items) {
		this.items = items;
		cIdx = 0;
	}

	@Override
	public String toString() {
		return "MenuSpinner [items=" + items + ", cIdx=" + cIdx + ", xSub="
				+ xSub + ", text=" + text + ", x=" + x + ", y=" + y
				+ ", selectable=" + selectable + ", font=" + font
				+ ", additionalInfo=" + additionalInfo + "]";
	}
}
