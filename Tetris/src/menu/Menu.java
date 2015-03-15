package menu;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import config.Settings;

import other.Logger;
import core.GAME_STATE;
import core.Main;

public class Menu {
	private MENU_STATE mState;
	private HashMap<MENU_STATE, ArrayList<MenuItem>> menus;
	private int selectedIdx;
	private ArrayList<Integer> selableIdx;
	
	private int highScorePos;

	public Menu() {
		mState = MENU_STATE.MS_MAIN;
		menus = new HashMap<MENU_STATE, ArrayList<MenuItem>>();
		selectedIdx = -1;
		selableIdx = new ArrayList<Integer>();
		try {
			readMenuConfig();
			Logger.write("Menu: Successfully red the menu File!", Color.GREEN);
		} catch (Exception e) {
			Logger.write("Menu: FAILED reading Menu File: " + Arrays.toString(e.getStackTrace()), Color.RED);
		}
		
		findSelectableItems();
	}
	
	public void keyEvent(KeyEvent key){
		
		switch(key.getKeyCode()){
		case KeyEvent.VK_ENTER:
			if(!Main.previoulsyPressedKeys[1]){
				if(mState == MENU_STATE.MS_MAIN){
					if(selectedIdx == 2 || selectedIdx == 3){//Start Normal/Hardcore game
						((MenuTField)menus.get(MENU_STATE.MS_HIGHSCOREIN).get(1)).input = "";
						highScorePos = -1;
						if(selectedIdx == 2)
							Main.game.setGameMode(0);
						else
							Main.game.setGameMode(1);
						Main.game.reset();
						Main.music.stop(false);
						try {
							Main.music.start(true);
						} catch (Exception e) {
							Logger.write("Menu: FAILED to start SoundA " + e.getStackTrace(), Color.RED);
						}

						Logger.write("Menu: Game Started");
					}
					else if(selectedIdx == 4){//Showup Highscore
						setmState(MENU_STATE.MS_HIGHSCORE);
					}
					else if(selectedIdx == 5){//Show Options
						setmState(MENU_STATE.MS_OPTIONS);
					}
					else if(selectedIdx == 6){
						System.exit(0);
					}
					else
						Logger.write("Menu: Main Menu: Unknow Index Selected", Color.RED);
				
				}
				else if(mState == MENU_STATE.MS_HIGHSCORE){
					if(selectedIdx == 2){
						setmState(MENU_STATE.MS_MAIN);
					}
				}
				else if(mState == MENU_STATE.MS_PAUSE){
					Main.game.setgState(GAME_STATE.RUNNING);
				}
				else if(mState == MENU_STATE.MS_OPTIONS){
					if(selectedIdx == 6){
						Settings.setItem("SoundOn"        , ((MenuChBx)getCurrentMenuItems().get(1)).on ? "1" : "0" );
						Settings.setItem("ConsoleLog"     , ((MenuChBx)getCurrentMenuItems().get(2)).on ? "1" : "0" );
						Settings.setItem("FallingHelp"    , ((MenuChBx)getCurrentMenuItems().get(3)).on ? "1" : "0" );
						Settings.setItem("Clipboard"      , ((MenuChBx)getCurrentMenuItems().get(4)).on ? "1" : "0" );
						
						Logger.setConsoleOut(Integer.parseInt(Settings.getItem("ConsoleLog")));
						Main.music.setSoundOn(Integer.parseInt(Settings.getItem("SoundOn")));
						
						Settings.writeConfig();
						setmState(MENU_STATE.MS_MAIN);
					}
					else if(selectedIdx == 5){
						setmState(MENU_STATE.MS_OPTIONS_WINDOW);
					}
				}
				else if(mState == MENU_STATE.MS_OPTIONS_WINDOW){
					if(selectedIdx == 4){
						Settings.setItem("AspectRatio" , ((MenuSpinner)getCurrentMenuItems().get(1)).getSelItem());
						Settings.setItem("WindowSize"  , ((MenuSpinner)getCurrentMenuItems().get(2)).getSelItem());
						Settings.setItem("FieldSize"   , ((MenuSpinner)getCurrentMenuItems().get(3)).getSelItem());
						
						Settings.writeConfig();
						setmState(MENU_STATE.MS_OPTIONS);
					}
				}
				else if(mState == MENU_STATE.MS_HIGHSCOREIN){
					if(selectedIdx == 2 && ((MenuTField)getCurrentMenuItems().get(1)).input.length() > 0){
						Main.score.editScore(highScorePos, ((MenuTField)getCurrentMenuItems().get(1)).input, Main.game.getGameMode());
						setmState(MENU_STATE.MS_HIGHSCORE);
					}
				}
			}
			Main.previoulsyPressedKeys[1] = true;
			break;
			
		case KeyEvent.VK_P:
			if(!Main.previoulsyPressedKeys[0] && mState == MENU_STATE.MS_PAUSE){
				Main.game.setgState(GAME_STATE.RUNNING);
			}
			Main.previoulsyPressedKeys[0] = true;
			break;
			
		case KeyEvent.VK_DOWN:
			increaseSelIdx();
			break;
			
		case KeyEvent.VK_UP:
			decreaseSelIdx();
			break;
			
		default:
			getCurrentMenuItems().get(selectedIdx).keyIn(key);
			if(mState == MENU_STATE.MS_OPTIONS_WINDOW && selectedIdx == 1 && (key.getKeyCode() == KeyEvent.VK_LEFT || key.getKeyCode() == KeyEvent.VK_RIGHT))
				recalcWindowPrefValues();
			else if(mState == MENU_STATE.MS_OPTIONS_WINDOW && selectedIdx == 2 && (key.getKeyCode() == KeyEvent.VK_LEFT || key.getKeyCode() == KeyEvent.VK_RIGHT))
				recalcFieldPrefValues();
			break;
		}
	}
	
	public void keyTypedEvent(KeyEvent key){
		if(mState == MENU_STATE.MS_HIGHSCOREIN){		
			if(selectedIdx == 1)
				getCurrentMenuItems().get(1).keyIn(key);
		}
	}
	
	private void readMenuConfig() throws Exception{
		BufferedReader br = new BufferedReader(new FileReader("res/menu"));
		String in;
		int k = -1;
		ArrayList<ArrayList<MenuItem>> list = new ArrayList<ArrayList<MenuItem>>();
		for (int i = 0; i < 6; i++) {
			list.add(new ArrayList<MenuItem>());
		}
		
		while((in = br.readLine()) != null && (k++) < 6){
			//1 Zeile = 1 Menu
			for (int i = 0; i < in.split("#").length; i++) {
				//Items getrennt durch #
				String sp = in.split("#")[i];
				String fo = sp.split("::")[4];
				MenuItem it;
				if(sp.split("::")[0].equals("btn"))
					it = new MenuButton();
				else if(sp.split("::")[0].equals("chb"))
					it = new MenuChBx(sp.split("::")[5]);
				else if(sp.split("::")[0].equals("spn"))
					it = new MenuSpinner();
				else if(sp.split("::")[0].equals("tfl"))
					it = new MenuTField(sp.split("::")[5]);
				else
					it = new MenuText();
				
				it.text = sp.split("::")[1];
				it.x    = Float.parseFloat(sp.split("::")[2]);
				it.y    = Float.parseFloat(sp.split("::")[3]);
				it.font = new Font(fo.split("-")[0], 0, Integer.parseInt(fo.split("-")[1]));
				if(sp.split("::")[0].equals("spn"))
					it.additionalInfo = sp.split("::")[5];
				list.get(k).add(it);
			}
		}
		br.close();
		menus.put(MENU_STATE.MS_MAIN, list.get(0));
		menus.put(MENU_STATE.MS_PAUSE, list.get(1));
		menus.put(MENU_STATE.MS_HIGHSCORE, list.get(2));
		menus.put(MENU_STATE.MS_HIGHSCOREIN, list.get(3));
		menus.put(MENU_STATE.MS_OPTIONS, list.get(4));
		menus.put(MENU_STATE.MS_OPTIONS_WINDOW,  list.get(5));
		Logger.write("Menu: Got the following Menus: " + menus);
		
		//Setting up Options start Values
		((MenuChBx)getMenuItems(MENU_STATE.MS_OPTIONS).get(1)).on = Settings.getItem("SoundOn"        ).charAt(0)== '1' ? true : false;
		((MenuChBx)getMenuItems(MENU_STATE.MS_OPTIONS).get(2)).on = Settings.getItem("ConsoleLog"     ).charAt(0)== '1' ? true : false;
		((MenuChBx)getMenuItems(MENU_STATE.MS_OPTIONS).get(3)).on = Settings.getItem("FallingHelp"    ).charAt(0)== '1' ? true : false;
		((MenuChBx)getMenuItems(MENU_STATE.MS_OPTIONS).get(4)).on = Settings.getItem("Clipboard"      ).charAt(0)== '1' ? true : false;

		((MenuSpinner)getMenuItems(MENU_STATE.MS_OPTIONS_WINDOW).get(1)).setTo( Settings.getItem("AspectRatio") );
		recalcWindowPrefValues();
		((MenuSpinner)getMenuItems(MENU_STATE.MS_OPTIONS_WINDOW).get(2)).setTo( Settings.getItem("WindowSize") );
		((MenuSpinner)getMenuItems(MENU_STATE.MS_OPTIONS_WINDOW).get(3)).setTo( Settings.getItem("FieldSize") );
		
	}
	private void findSelectableItems(){
		ArrayList<MenuItem> cList = getCurrentMenuItems();
		selableIdx = new ArrayList<Integer>();
		for (int i = 0; i < cList.size(); i++) {
			if(cList.get(i).selectable){
				selableIdx.add(i);
			}
		}
		
		if(selableIdx.size() > 0)
			selectedIdx = selableIdx.get(0);
		else
			selectedIdx = -1;
		
	}
	
	private void recalcWindowPrefValues(){
		String tmp = ((MenuSpinner)getMenuItems(MENU_STATE.MS_OPTIONS_WINDOW).get(1)).getSelItem();
		int wF = Integer.parseInt(tmp.charAt(0) + "");
		int hF = Integer.parseInt(tmp.charAt(2) + "");
		
		ArrayList<String> windowValues = new ArrayList<String>();
		for (int s = (wF == 3 || hF == 3 ? 150 : 300); s <= (wF == 3 || hF == 3 ? 350 : 700); s+= (wF == 3 || hF == 3 ? 5 : 10)) {
			boolean add = false;
			for (int f = 10; f <= 20; f++) {
				if((wF*s)%(wF*f) == 0){
					add = true;
					break;
				}
			}
			if(add)
				windowValues.add("" + (wF*s) + "x" + (hF*s));
		}
		
		((MenuSpinner)getMenuItems(MENU_STATE.MS_OPTIONS_WINDOW).get(2)).setItems(windowValues);
		recalcFieldPrefValues();
	}
	
	private void recalcFieldPrefValues(){
		String tmp  = ((MenuSpinner)getMenuItems(MENU_STATE.MS_OPTIONS_WINDOW).get(1)).getSelItem();
		String tmp2 = ((MenuSpinner)getMenuItems(MENU_STATE.MS_OPTIONS_WINDOW).get(2)).getSelItem();
		int wF = Integer.parseInt(tmp.charAt(0) + "");
		int hF = Integer.parseInt(tmp.charAt(2) + "");
		
		float wW = Integer.parseInt(tmp2.split("x")[0]);
		
		ArrayList<String> fieldValues = new ArrayList<String>();
		//2. Feld Werte
		for (int f = 10; f <= 20; f++) {
			if(wW%(wF*f) == 0)
				fieldValues.add("" + (wF*f) + "x" + (hF*f));
		}
		((MenuSpinner)getMenuItems(MENU_STATE.MS_OPTIONS_WINDOW).get(3)).setItems(fieldValues);
	}
	
	public int getSelectedIdx() {
		return selectedIdx;
	}
	public void decreaseSelIdx(){
		int sListIdx = -1;
		for (int i = 0; i < selableIdx.size(); i++) {
			if(selableIdx.get(i) == selectedIdx){
				sListIdx = i;
				break;
			}
				
		}
		if(sListIdx <= 0)
				return;
		selectedIdx = selableIdx.get(sListIdx-1);
	}
	public void increaseSelIdx(){
		int sListIdx = -1;
		
		for (int i = 0; i < selableIdx.size(); i++) {
			if(selableIdx.get(i) == selectedIdx){
				sListIdx = i;
				break;
			}
				
		}
		if(sListIdx >= selableIdx.size()-1)
				return;
		selectedIdx = selableIdx.get(sListIdx+1);
	}
	public void setSelectedIdx(int sIdx) {
		if(selableIdx.contains(sIdx))
			this.selectedIdx = sIdx;
	}
	public MENU_STATE getmState() {
		return mState;
	}
	public void setmState(MENU_STATE mState) {
		Logger.write("Menu: New Menu State: " + mState, Color.BLUE);
		this.mState = mState;
		findSelectableItems();
	}
	public ArrayList<MenuItem> getMenuItems(MENU_STATE ms){
		return menus.get(ms);
	}
	public ArrayList<MenuItem> getCurrentMenuItems(){
		return menus.get(mState);
	}
	public void setHighScoreIn(String highScoreIn) {
		((MenuTField)menus.get(MENU_STATE.MS_HIGHSCOREIN).get(1)).input = highScoreIn;
	}
	public void setHighScorePos(int highScorePos) {
		this.highScorePos = highScorePos;
	}
	public String getHighScoreIn() {
		return ((MenuTField)menus.get(MENU_STATE.MS_HIGHSCOREIN).get(1)).input;
	}
	public int getHighScorePos() {
		return highScorePos;
	}
}
