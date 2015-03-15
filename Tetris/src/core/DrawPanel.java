package core;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;

import menu.MENU_STATE;
import menu.MenuItem;
import menu.MenuSpinner;
import other.Logger;
import config.Settings;


@SuppressWarnings("serial")
public class DrawPanel extends JPanel {
	private int gridSize;
	private int breite;
	private int hoehe;
	
	public boolean rend = false;
	
	private Color bg;
	private Color font;
	
	private ArrayList<Block> bList;
	
	public DrawPanel(int width, int height) {
		super();
		Logger.write("DrawPanel: Init ...");
		breite = width;
		hoehe = height;
		gridSize = (int)(breite / Main.numBlocksPerRow);
		Logger.write("DrawPanel: GridSize: " + (float)(breite/ Main.numBlocksPerRow) + " " + (float)(hoehe/Main.numBlocksPerColoumn), Color.BLUE);
		bList = new ArrayList<Block>();
		bg = new Color(0, 0, 0);
		font = new Color(230,230,230);
		Logger.write("DrawPanel: Finished Init", Color.GREEN);
	}
	public Dimension getPreferredSize() {
		return new Dimension(breite, hoehe);
	}
	
	public void setbList(ArrayList<Block> bList) {
		this.bList = bList;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(!rend)
			return;
		
		g.setColor(bg);
		g.fillRect(0, 0, getWidth(), (int)(gridSize*Main.numBlocksPerColoumn)+1);
		g.setColor(new Color(128,128,128));
		g.drawRect(0, 0, (int)(gridSize*Main.numBlocksPerRow), (int)(gridSize*Main.numBlocksPerColoumn)+1);
		
		//Draw Coloum highlighter
		if(Settings.getItem("FallingHelp").charAt(0) == '1'){
			ArrayList<Point> punktList = Main.game.getCurrentBlock().getNurDiePunkteDieWirBrauchen();
			g.setColor(new Color(255,255,255, 128));
			for (int i = 0; i < punktList.size() && Main.game.getGameState() == GAME_STATE.RUNNING; i++) {
				g.fillRect(punktList.get(i).x * gridSize, 0, gridSize, (int)(gridSize *  Main.numBlocksPerColoumn));
				
			}
		}
		
		//Draw Blocks
		for(int i = 0; i < bList.size(); i++){
			if(bList.get(i).getPosAbs().y >= 0){
				g.setColor(bList.get(i).getFarbe());
				g.fillRect(bList.get(i).getPosAbs().x * gridSize, bList.get(i).getPosAbs().y * gridSize, gridSize, gridSize);
				g.setColor(Color.BLACK);
				g.drawRect(bList.get(i).getPosAbs().x * gridSize, bList.get(i).getPosAbs().y * gridSize, gridSize, gridSize);
			}
		}
		//Draw Level
		g.setColor(font);
		g.setFont(new Font("Arial", 1, 30));
		int fHeight = g.getFontMetrics().getHeight();
		g.drawString("Level:", breite+15, 10+fHeight);
		g.drawString("" + Main.game.getLevel(), breite+15, 20+2*fHeight);
		
		//Draw Score
		g.drawString("Score:", breite+15, 30+3*fHeight);
		g.drawString("" + Main.game.getScore(), breite+15, 40+4*fHeight);
		
		//Draw PrevBlock
		ArrayList<Block> steinList = Main.game.getPrevBlockList();
		int xSub = (int) (Main.numBlocksPerRow/2);
		
		for (int i = 0; i < steinList.size(); i++) {
			g.setColor(steinList.get(i).getFarbe());
			g.fillRect((steinList.get(i).getPosAbs().x-xSub)*25 + breite + 15, (int) ((steinList.get(i).getPosAbs().y+3f)*25 + 50+4*fHeight), 25, 25);
			g.setColor(Color.BLACK);
			g.drawRect((steinList.get(i).getPosAbs().x-xSub)*25 + breite + 15, (int) ((steinList.get(i).getPosAbs().y+3f)*25 + 50+4*fHeight), 25, 25);
		}
		
		g.setColor(Color.GRAY);
		g.drawRect(breite + 15, 50+4*fHeight, 100, 100);
		
		//Draw Clipboard Block
		steinList = Main.game.getClipboardBlockList();
		
		if(steinList != null){
			xSub = Main.game.getClipboardBlock().getXmin();
			int ySub = Main.game.getClipboardBlock().getYmin();
			for (int i = 0; i < steinList.size(); i++) {
				g.setColor(steinList.get(i).getFarbe());
				g.fillRect((steinList.get(i).getPosAbs().x-xSub)*25 + breite + 15, (int) ((steinList.get(i).getPosAbs().y-ySub)*25 + 80+7*fHeight), 25, 25);
				g.setColor(Color.BLACK);
				g.drawRect((steinList.get(i).getPosAbs().x-xSub)*25 + breite + 15, (int) ((steinList.get(i).getPosAbs().y-ySub)*25 + 80+7*fHeight), 25, 25);
			}
			
			
		}
		if(Settings.getItem("Clipboard").charAt(0) == '1'){
			g.setColor(Color.GRAY);
			g.drawRect(breite + 15, 80+7*fHeight, 100, 100);
		}
		
		//Menu Screen?
		if(Main.game.getGameState() != GAME_STATE.RUNNING){
			g.setColor(new Color(0,0,0,200));
			g.fillRect(0, 0, getWidth(), (int)(gridSize*Main.numBlocksPerColoumn));
		
			//Draw all the Menus
			g.setColor(font);
			ArrayList<MenuItem> items = Main.menu.getCurrentMenuItems();

			for (MenuItem it : items) {
				it.draw(g, breite, getHeight());
			}
			
			if(Main.menu.getmState() == MENU_STATE.MS_HIGHSCORE){
				//Draw Highscores
				ArrayList<HScore> tmpList = Main.score.getScoreList( ((MenuSpinner)Main.menu.getCurrentMenuItems().get(1)).cIdx );
				g.setFont(new Font("Arial", 0, 25));
				g.drawString("Level", (int) (0.1f*breite+40),  (int) (0.4f*getHeight()));
				g.drawString("Score", (int) (0.1f*breite+110), (int) (0.4f*getHeight()));
				g.drawString("Name",  (int) (0.1f*breite+190), (int) (0.4f*getHeight()));
				
				for(int i = 0; i < tmpList.size(); i++){
					g.drawString(i+1 + "."                , (int) (0.1f*breite),     (int) (0.4f*getHeight()+(i+1)*(g.getFontMetrics().getHeight() + 10)));
					g.drawString(tmpList.get(i).level + "", (int) (0.1f*breite+40),  (int) (0.4f*getHeight()+(i+1)*(g.getFontMetrics().getHeight() + 10)));
					g.drawString(tmpList.get(i).score + "", (int) (0.1f*breite+110), (int) (0.4f*getHeight()+(i+1)*(g.getFontMetrics().getHeight() + 10)));
					g.drawString(tmpList.get(i).name      , (int) (0.1f*breite+190), (int) (0.4f*getHeight()+(i+1)*(g.getFontMetrics().getHeight() + 10)));
				}
			}

			//Draw Selection
			if(Main.menu.getSelectedIdx()>-1){
				MenuItem sel = items.get(Main.menu.getSelectedIdx());
				String color = Settings.getItem("SelectionColor");
				g.setColor(new Color(Integer.parseInt(color.split("-")[0]), Integer.parseInt(color.split("-")[1]), Integer.parseInt(color.split("-")[2])));
				sel.drawSelection(g, breite, getHeight());
			}

		}
	}	
}
