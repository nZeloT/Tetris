package config;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import other.Logger;



public class Settings {

	private static HashMap<String, String> map;

	public static void readConfig(){
		Logger.write("Settings: reading Config ...");
		try{
			map = new HashMap<String, String>();
			BufferedReader br = new BufferedReader(new FileReader("res/config"));
			String in;
			while((in = br.readLine()) != null){
				map.put(in.split("#")[0], in.split("#")[1]);
				Logger.write("Settings: Read Key: " + in.split("#")[0] + " Value: " + in.split("#")[1], Color.BLUE);
			}
			br.close();
			Logger.write("Settings: Finished reading Config", Color.GREEN);
		}catch(Exception e){
			Logger.write("Settings: FAILED reading Config", Color.RED);
		}
	}
	
	public static void writeConfig(){
		Logger.write("Settings: writing Config ...");
		try{
			if(map == null)
				return;
			FileWriter w = new FileWriter("res/config");
			for(String key : map.keySet()){
				w.write(key + "#" + map.get(key) + "\n");
			}
			w.close();
			Logger.write("Settings: Finished writing Config", Color.GREEN);
		}catch(Exception e){
			Logger.write("Settings: FAILED writing Config", Color.RED);
		}
	}
	
	public static String getItem(String key){
		if(map == null)
			readConfig();
		return map.get(key);
	}
	
	public static void setItem(String key, String val){
		if(map == null)
			readConfig();
		Logger.write("Settings: Setting Key: " + key + " to Value: " + val, Color.BLUE);
		map.put(key, val);
	}
}
