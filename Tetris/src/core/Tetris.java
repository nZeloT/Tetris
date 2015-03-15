package core;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import menu.MENU_STATE;

import config.Settings;

import other.Logger;
import other.Statistics;

public class Tetris {
	private ArrayList<SteinER> sList;
	private int aktSteinIdx;
	private boolean[][] feld;
	private int score;
	private int level;
	private int gameMode;
	private SteinER prevBlock;

	private GAME_STATE gState;
	
	private Statistics stat;
	
	private Clipboard board;

	public int TIMED_MOVE;
	public int TIMED_MOVED_MIN;

	public Tetris(){
		Logger.write("Tetris: Init...");
		TIMED_MOVE = (int) (750 / (Main.numBlocksPerColoumn / 20.0f));
		TIMED_MOVED_MIN = (int) (300 / (Main.numBlocksPerColoumn / 20.0f));
		score = 0;
		sList = new ArrayList<SteinER>();
		aktSteinIdx = -1;
		feld = getGlobalePunkteDieWirBrauchen();
		level = (int)(score/25.0f) + 1;
		gameMode = 0;

		gState = GAME_STATE.Menu;
		prevBlock = new SteinER(genType());
		
		stat = new Statistics();
		
		board = new Clipboard();
		
		genNewBlock();
		Logger.write("Tetris: Finished Init", Color.GREEN);
	}

	public boolean keyEvent(KeyEvent key){
		if(gState == GAME_STATE.RUNNING){
			switch(key.getKeyCode()){
	
			case KeyEvent.VK_LEFT:
				if(checkSideHit(false)){
					sList.get(aktSteinIdx).move(0, -1);
				}
				break;
	
			case KeyEvent.VK_RIGHT:
				if(checkSideHit(true)){
					sList.get(aktSteinIdx).move(0, 1);
				}
				break;
	
			case KeyEvent.VK_UP:
				int[][] mat = sList.get(aktSteinIdx).getRotatedMatrix();
				if(checkSteinERPlacemendAllowed(mat, sList.get(aktSteinIdx).getAbsMatrixCoord(), sList.get(aktSteinIdx).getRotationsRandAusgleich(mat)))
					sList.get(aktSteinIdx).rotate();
				break;
	
			case KeyEvent.VK_DOWN:
				if(!hammerWasGetroffe()){
					sList.get(aktSteinIdx).move(1, 0);
				}
				break;
				
			case KeyEvent.VK_SPACE:
				if(!Main.previoulsyPressedKeys[3]){
					while(!hammerWasGetroffe())
						sList.get(aktSteinIdx).move(1, 0);
				}
				Main.previoulsyPressedKeys[3] = true;
				break;
	
			case KeyEvent.VK_P:
				if(!Main.previoulsyPressedKeys[0]){
					Main.menu.setmState(MENU_STATE.MS_PAUSE);
					gState = GAME_STATE.Menu;
					Logger.write("Tetris: Game Paused");
				}
				Main.previoulsyPressedKeys[0] = true;
				break;
				
			case KeyEvent.VK_TAB:
				if(!Main.previoulsyPressedKeys[2] && Settings.getItem("Clipboard").charAt(0) == '1'){
					
					if(board.hastSteinER()){
						if(checkSteinERPlacemendAllowed(board.getStein().getMatrix(), new Point(sList.get(aktSteinIdx).getXmin(), sList.get(aktSteinIdx).getYmin()), 0)){
	
							SteinER swap = board.swapSteinERs(sList.get(aktSteinIdx));
							sList.set(aktSteinIdx, swap);
							Logger.write("Tetris: Swaped 2 SteinER");
						}
					}else{
						board.swapSteinERs(sList.get(aktSteinIdx));
						sList.remove(aktSteinIdx);
						aktSteinIdx--;
						genNewBlock();
						Logger.write("Tetris: Copied SteinER to Clipboard");
					}
				}
				Main.previoulsyPressedKeys[2] = true;
				break;
			default:
				break;
			}
		}else{
			return false;
		}

		Main.gui.draw.setbList(Main.game.getBList());
		Main.gui.draw.repaint();
		return true;
	}

	private boolean checkSteinERPlacemendAllowed(int[][] mat, Point matPos, int randAusgleich) {		
		matPos.x -= randAusgleich;
		
		for (int y = 0; y < mat.length; y++) {
			for (int x = 0; x < mat[0].length; x++) {
				if(mat[y][x] != 0){
					if(matPos.y + y < Main.numBlocksPerColoumn){
						if(matPos.x + x < Main.numBlocksPerRow && matPos.x + x >= 0 && matPos.y + y >= 0)//Rotation & Steintausch auch �ber y > 0
							if(feld[x+matPos.x][y+matPos.y])
								return false;
					}else{
						return false;
					}
				}
			}
		}

		return true;
	}

	private boolean checkSideHit(boolean rechts){
		ArrayList<Point> block = sList.get(aktSteinIdx).getPointList();

		for (Point bs : block) {
			if(rechts){
				if(bs.x < Main.numBlocksPerRow-1){
					if( bs.y >= 0 && feld[bs.x+1][bs.y]){
						return false;
					}
				}
				else 
					return false;
			}else{
				if(bs.x > 0){
					if(bs.y >= 0 && feld[bs.x-1][bs.y]){
						return false;
					}
				}
				else
					return false;
			}
		}
		return true;
	}

	private void checkForFullLine(){
		for (int i = 0; i < feld[0].length; i++) {
			int counter = 0;
			for (int j = 0; j < feld.length; j++) {
				if(feld[j][i])
					counter ++;
			}
			if(counter == feld.length){
				score += 10;
				checkLevelUp();

				for (int j = 0; j < sList.size()-1; j++) {
					sList.get(j).killY(i);
				}
			}
		}
	}

	private boolean checkForGameOver(){
		int counter = 0;
		for (int j = 0; j < feld.length; j++) {
			if(feld[j][0])
				counter ++;
		}
		if(counter > 0){
			Logger.write("Tetris: Game Over! in Level: " + level + " and a Score of: " + score, Color.ORANGE);
			gState = GAME_STATE.Menu;
			if(gameMode == 0)
				stat.logStats(level, score);
			if(Main.score.checkScore(score, gameMode) > -1 && Main.menu.getmState() != MENU_STATE.MS_HIGHSCOREIN){
				Main.menu.setHighScorePos(Main.score.checkScore(score, gameMode));
				Main.score.addScore(Main.menu.getHighScorePos(), level, score, "Anonymus", gameMode);
				Main.menu.setHighScoreIn("Anonymus");
				Main.menu.setmState(MENU_STATE.MS_HIGHSCOREIN);
			}
			else
				Main.menu.setmState(MENU_STATE.MS_HIGHSCORE);
			Main.music.stop(true);
			try {
				Main.music.start(false);
			} catch (Exception e) {
				Logger.write("Tetris: FAILED to start SoundB: " + e.getStackTrace().toString(), Color.RED);
			}
			return true;
		}
		return false;
	}

	public void calcStep(){
		if(!hammerWasGetroffe())
			sList.get(aktSteinIdx).move(1, 0);
		else
			genNewBlock();
	}

	private boolean hammerWasGetroffe(){
		ArrayList<Point> block = sList.get(aktSteinIdx).getNurDiePunkteDieWirBrauchen();
		feld = getGlobalePunkteDieWirBrauchen();


		for (Point bs : block) {
			if (bs.y == Main.numBlocksPerColoumn-1){
				feld = getGlobalePunkteDieWirBrauchen();
				return true;
			}else {
				if(feld[bs.x][bs.y+1]){
					feld = getGlobalePunkteDieWirBrauchen();
					return true;
				}
			}
		}
		return false;
	}

	private void genNewBlock() {
		Logger.write("Tetris: Generating a new Stone");
		sList.add(prevBlock);
		prevBlock = new SteinER(genType());
		aktSteinIdx++;
		
		if(gameMode == 0)
			stat.increaseType(prevBlock.getType());
		
		sList.get(aktSteinIdx).move(-1, 0);
		feld = getGlobalePunkteDieWirBrauchen();
		checkForFullLine();
		if(!checkForGameOver()){
			sList.get(aktSteinIdx).move(1, 0);
			sList.get(aktSteinIdx).getMatrix();
			if(sList.get(aktSteinIdx).getType() <= 6)
				score ++;
			else
				score += 3;
			checkLevelUp();
		}
	}

	public ArrayList<Block> getBList() {
		ArrayList<Block> b = new ArrayList<Block>();
		for(int i = 0; i < sList.size(); i++)
			b.addAll(sList.get(i).getList());
		return b;
	}

	public boolean[][] getGlobalePunkteDieWirBrauchen(){
		ArrayList<Block> b = getBList();
		boolean[][] ret = new boolean[(int)Main.numBlocksPerRow][(int)Main.numBlocksPerColoumn];

		for(int i = 0; i < ret.length; i++){
			for(int j = 0; j < ret[0].length; j++)
				ret[i][j] = false;
		}

		for(int i = 0; i < b.size()-4; i++){//+1 :D
			Point p =b.get(i).getPosAbs();
			if(p.y >= 0)
				ret[p.x][p.y] = true;
		}

		return ret;
	}
	
	private void checkLevelUp(){
		if((int)(score/25.0f) + 1 > level){
			Logger.write("Tetris: Level Up! " + level, Color.BLUE);
			level = (int)(score/25.0f) + 1;
			TIMED_MOVE -= (int) (50 * (Main.numBlocksPerColoumn / 20.0f));
			if(TIMED_MOVE < TIMED_MOVED_MIN)
				TIMED_MOVE = TIMED_MOVED_MIN;
			float nBPM;
			if(level < 14)
				nBPM = 110 + (level-1)*4;
			else
				nBPM = 166;
			Main.music.setNewBPM(nBPM);
		}
	}
	
	public void reset(){
		TIMED_MOVE = (int) (750 / (Main.numBlocksPerColoumn / 20.0f));
		score = 0;
		sList = new ArrayList<SteinER>();
		aktSteinIdx = -1;
		feld = getGlobalePunkteDieWirBrauchen();
		prevBlock = new SteinER(genType());
		level = (int)(score/25.0f) + 1;
		board.reset();
		stat.reset();

		genNewBlock();

		gState = GAME_STATE.RUNNING;
	}

	private int genType(){
		int type = (int)(Math.random()* (gameMode == 1 ? 11 : 7));
		Logger.write("Tetris: New Type: " + type, Color.BLUE);
		return type;
	}
	public int getScore() {
		return score;
	}
	public int getLevel() {
		return level;
	}
	
	public int getGameMode(){
		return gameMode;
	}
	public SteinER getCurrentBlock(){
		return sList.get(aktSteinIdx);
	}
	public ArrayList<Block> getPrevBlockList() {
		return prevBlock.getList();
	}
	public SteinER getPrevBlock() {
		return prevBlock;
	}
	public ArrayList<Block> getClipboardBlockList(){
		return board.getBList();
	}
	public SteinER getClipboardBlock(){
		return board.getStein();
	}
	public GAME_STATE getGameState(){
		return gState;
	}
	public void setgState(GAME_STATE gState) {
		this.gState = gState;
	}
	public void setGameMode(int mode) {
		this.gameMode = mode;
	}
}
