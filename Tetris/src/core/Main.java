package core;

import java.awt.Color;

import menu.Menu;

import other.Logger;
import other.MainGUI;


import config.Settings;


public class Main {
	public static MainGUI gui;
	public static Tetris game;
	public static Menu menu;
	public static Sound music;
	public static Highscore score;
	
	public static float numBlocksPerRow;
	public static float numBlocksPerColoumn;
	public static boolean[] previoulsyPressedKeys;
	
	public static boolean run = true;
	
	
	public static void main(String[] args) {
		Logger.initLogFile("Tetris Logfile", "res/tetris-log.html");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				run = false;
				music.stop(false);
				music.stop(true);
				Logger.endLogFile();
			}
		});
		Logger.write("Main: Init Game ...");
		Settings.readConfig();
		Logger.setConsoleOut(Integer.parseInt(Settings.getItem("ConsoleLog")));
		numBlocksPerRow = Integer.parseInt(Settings.getItem("FieldSize").split("x")[0]);
		numBlocksPerColoumn = Integer.parseInt(Settings.getItem("FieldSize").split("x")[1]);
		previoulsyPressedKeys = new boolean[4]; // 0 = Enter | 1 = P | 2 = Tab | Space
		for(int i = 0; i < previoulsyPressedKeys.length; i++)
			previoulsyPressedKeys[i] = false;
		
		
		try {
			music = new Sound();
			music.start(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		gui = new MainGUI();
		gui.setVisible(true);
		
		menu = new Menu();
		
		game = new Tetris();
		score = new Highscore();
		
		gui.draw.rend = true;
		gui.draw.repaint();
		
		Logger.write("Main: Finished Init Game", Color.GREEN);
		Logger.write("Main: Going to Start Main loop", Color.RED);
		
		float FPS = Float.parseFloat(Settings.getItem("FPS"));
		long lastTime, lastFPS = 0;
        lastTime = lastFPS = System.nanoTime();
        
        float timeBuffer = 0.0f;
		
		while(run){
			long deltaTime = System.nanoTime() - lastTime;
            lastTime += deltaTime;
            
            timeBuffer += deltaTime/1000000.0f;
            
            if(timeBuffer > game.TIMED_MOVE && game.getGameState() == GAME_STATE.RUNNING){
            	long t1 = System.nanoTime();
            	game.calcStep();
            	gui.draw.setbList(Main.game.getBList());
            	gui.draw.repaint();
            	try {
					music.checkRunningAndRestartWhenNot();
				} catch (Exception e) {
					Logger.write("Main: FAILED to checkAnd Resart the Sound", Color.RED);
					Logger.write("Main: " + e.getStackTrace().toString());
				}
            	timeBuffer = 0.0f;
            	Logger.write("Main: Calculation took: " + ((System.nanoTime() - t1)/1000000.0f) + " ms");
            }
            else if(game.getGameState() == GAME_STATE.Menu){
            	gui.draw.repaint();
            	try {
					music.checkRunningAndRestartWhenNot();
				} catch (Exception e) {
					Logger.write("Main: FAILED to checkAnd Resart the Sound", Color.RED);
					Logger.write("Main: " + e.getStackTrace().toString());
				}
            }
            

            //calculate the FPS
            if(System.nanoTime() - lastFPS >= 1e9) {
                lastFPS += 1e9;
            }
            
            //Gets the remaining number of milliseconds left in the frame
            long sleepTime = Math.round((1e9/FPS - (System.nanoTime() - lastTime))/1e6);
            if(sleepTime <= 0)
                continue;
            
            //this sleeping method uses Thread.sleep(1) for the first 4/5 of the sleep loop, and Thread.yield() for the rest. This gives me an accuracy of about 3-4 microseconds
            long prev = System.nanoTime(), diff;
            while((diff = System.nanoTime() - prev) < sleepTime) {
                if(diff < sleepTime * 0.8)
                    try { Thread.sleep(1); } catch(Exception exc) {}
                else
                    Thread.yield();
            }
        }
		
	}
}
