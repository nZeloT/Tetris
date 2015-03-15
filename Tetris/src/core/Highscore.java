package core;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

import other.Crypt;
import other.Logger;
import config.Settings;

class HScore {
	public int score = 0;
	public int level = 0;
	public String name = "";

	
	public HScore(int l, int s, String n) {
		score = s;
		level = l;
		name = n;
	}

	@Override
	public String toString() {
		return "HScore [score=" + score + ", level=" + level + ", name=" + name
				+ "]";
	}
}
public class Highscore {
	private ArrayList<ArrayList<HScore>> scoreList;
	public Highscore() {
		Logger.write("Highscore: Init ...");
		try{
			readScores();
		}catch(Exception e){
			Logger.write("Highscore: FAILED reading Highscores! " + Arrays.toString(e.getStackTrace()), Color.RED);
		}
		Logger.write("Highscore: Finished Init", Color.GREEN);
	}
	
	public int checkScore(int score, int list){
		int ret = -1;
		for (int i = 0; i < scoreList.get(list).size(); i++) {
			if(scoreList.get(list).get(i).score < score){
				ret = i;
				break;
			}
		}
		return ret;
	}
	
	public void addScore(int pos, int level, int score, String name, int list){
		if(pos < 0 || scoreList.get(list).size() <= pos)
			return;
		
		scoreList.get(list).add(pos, new HScore(level, score, checkForIllegalcharacters(name)));
		scoreList.get(list).remove(scoreList.get(list).size()-1);
		Logger.write("Higscore: Added new Highscore: Position: " + pos + " Level: " + level + " Score: " + score + " Name: " + checkForIllegalcharacters(name), Color.GREEN);
		try {
			writeScores();
		} catch (Exception e) {
			Logger.write("Highscore: FAILED writing Highscore!", Color.RED);
		}
	}
	
	public void editScore(int pos, String name, int list){
		if(pos < 0 || scoreList.get(list).size() <= pos)
				return;
		
		scoreList.get(list).get(pos).name = checkForIllegalcharacters(name);
		Logger.write("Higscore: Changing Name ofHighscore: Position: " + pos + " Name: " + checkForIllegalcharacters(name), Color.GREEN);
		try {
			writeScores();
		} catch (Exception e) {
			Logger.write("Highscore: FAILED writing Highscore!", Color.RED);
		}
	}

	private void readScores() throws Exception {
		String fileNames = Settings.getItem("HighscoreFiles");
		scoreList = new ArrayList<ArrayList<HScore>>();
		for (int i = 0; i < fileNames.split("::").length; i++) {
			scoreList.add(new ArrayList<HScore>());
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileNames.split("::")[i]), "ISO-8859-1"));
			String in;
			int j = 0;
			while((in = br.readLine()) != null && (j++) < 5){
				in = Crypt.encrypt(in, genD());
				Logger.write("Highscore: Got Highscore From File: " + in, Color.BLUE);
				scoreList.get(i).add(new HScore(Integer.parseInt(in.split("#")[0]), Integer.parseInt(in.split("#")[1]), in.split("#")[2]));
			}
			br.close();
			Logger.write("Highscore: Finished File! Go to the next!", Color.GREEN);
		}
		Logger.write("Highscore: Finished Reading Highscores", Color.GREEN);
	}
	
	private void writeScores() throws Exception{
		String fileNames = Settings.getItem("HighscoreFiles");
		for (int i = 0; i < scoreList.size(); i++) {
			OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(fileNames.split("::")[i]), "ISO-8859-1");
		
			for (HScore hs : scoreList.get(i)) {
				w.write(Crypt.crypt(hs.level + "#" + hs.score + "#" + hs.name + "", genD()) + "\r\n");
			}
			w.close();
		}
		
		Logger.write("Highscore: Finished Rewriting of Higscores", Color.GREEN);
	}
	
	private int genD() {
		float zwv = 42.0f;
		zwv = 5 << 8 - 889/254 + 0xACDC << 0xCC - (10^0xA - 10^0xC);
		zwv %= 255;
		return (int) zwv;
	}

	private String checkForIllegalcharacters(String in){
		String out = "";
		for (int i = 0; i < in.length(); i++) {
			char ch = in.charAt(i);
			if(!Character.isLetterOrDigit(ch))
				ch = 'F';
			else if(ch == '\u00E4')
				ch = 'a';
			else if(ch == '\u00C4')
				ch = 'A';
			else if(ch == '\u00F6')
				ch = 'o';
			else if(ch == '\u00D6')
				ch = 'O';
			else if(ch == '\u00DC')
				ch = 'U';
			else if(ch == '\u00FC')
				ch = 'u';
			else if(ch == '\u00DF')
				ch = 's';
			out += ch;
		}
		return out;
	}
	
	public ArrayList<HScore> getScoreList(int list) {
		return scoreList.get(list);
	}

}
