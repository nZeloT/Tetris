package other;

import java.awt.*;
import java.io.*;
import java.util.*;

class GameStat {
	public int[] gStat;
	public int score;
	public int level;
	
	public GameStat(){
		gStat = new int[7];
		score = 0;
		level = 0;
	}
}

public class Statistics {
	ArrayList<GameStat> statAllGame;
	
	public Statistics() {
		statAllGame = new ArrayList<GameStat>();
		readLongTermStats();
	}
	
	public void increaseType(int type){
		if(type >= statAllGame.get(0).gStat.length)
			return;
		statAllGame.get(0).gStat[type] ++;
	}
	
	public void reset(){
		statAllGame.add(0, new GameStat());
		if(statAllGame.size() > 500)
			statAllGame.remove(statAllGame.size()-1);
	}
	
	public void logStats(int level, int score){
		statAllGame.get(0).level = level;
		statAllGame.get(0).score = score;
		//calc percentages
		float count = 0;
		for (int i = 0; i < statAllGame.get(0).gStat.length; i++) {
			count += statAllGame.get(0).gStat[i];
		}
		
		String w = "Statistics: 0: O | 1: I | 2: S | 3: Z | 4: L | 5: J | 6: T";
		w += "<table><tr><td>Type</td><td>Count</td><td>Percentage</td></tr>";
		for (int i = 0; i < statAllGame.get(0).gStat.length; i++) {
			w += "<tr><td>" + i + "</td><td>" + statAllGame.get(0).gStat[i] + "</td><td>" + ((statAllGame.get(0).gStat[i] / count)*100.0f) + "</td></tr>";
		}
		w += "</table>";
		Logger.write(w, Color.ORANGE);
		writeLongTermStats();
	}
	
	private void readLongTermStats(){
		try {
			BufferedReader br = new BufferedReader(new FileReader("res/stat.csv"));
			String in;
			while((in = br.readLine()) != null){
				GameStat gs = new GameStat();
				gs.level = Integer.parseInt(in.split(";")[0]);
				gs.score = Integer.parseInt(in.split(";")[1]);
				for (int i = 0; i < 7; i++) {
					gs.gStat[i] = Integer.parseInt(in.split(";")[i+2]);
				}
				statAllGame.add(gs);
			}
			br.close();
			Logger.write("Statistics: Successfully Read Statistics.", Color.GREEN);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.write("Statistics: FAILED Reading Long Term Stats!", Color.RED);
		}
	}
	
	private void writeLongTermStats(){
		try {
			FileWriter w = new FileWriter("res/stat.csv");
			for (GameStat gs : statAllGame) {
				w.append(gs.level + ";" + gs.score + ";" + gs.gStat[0] + ";"+ gs.gStat[1] + ";"+ gs.gStat[2] + ";"+ gs.gStat[3] + ";"+ gs.gStat[4] + ";"+ gs.gStat[5] + ";"+ gs.gStat[6] + "\r\n");
			}
			w.close();
			Logger.write("Statistics: Successfully written LTS!", Color.GREEN);
		} catch (Exception e) {
			Logger.write("Statistics: FAiLED Writing Long Term Stats!", Color.RED);
		}
	}

}
