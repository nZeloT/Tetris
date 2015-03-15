package core;
import java.awt.Color;
import java.io.File;
import javax.sound.midi.*;

import menu.MENU_STATE;
import other.Logger;
import config.Settings;


public class Sound {
	private Sequencer sequenA;
	private Sequencer sequenB;
	private Sequence sequenzA;
	private Sequence sequenzB;
	private Synthesizer synthA;
	private Synthesizer synthB;
	private Receiver recA;
	private Receiver recB;
	
	private float bpm;
	
	private int soundOn;
	
	public Sound() throws Exception{
		Logger.write("Sound: Init ...");
		soundOn = Integer.parseInt(Settings.getItem("SoundOn"));
		bpm = 110;
		
		synthA = MidiSystem.getSynthesizer();
		synthA.open();
		synthB = MidiSystem.getSynthesizer();
		synthB.open();
		
		sequenA = MidiSystem.getSequencer();
		sequenzA = MidiSystem.getSequence(new File(Settings.getItem("SoundA")));
		sequenA.setSequence(sequenzA);
		if (synthA.getDefaultSoundbank() == null) {
            recA = MidiSystem.getReceiver();
		} else {
            recA = synthA.getReceiver();
		}
		sequenA.getTransmitter().setReceiver(recA);
		sequenA.open();
		sequenA.setTempoInBPM(bpm);
		
		sequenB = MidiSystem.getSequencer();
		sequenzB = MidiSystem.getSequence(new File(Settings.getItem("SoundB")));
		sequenB.setSequence(sequenzB);
		if (synthB.getDefaultSoundbank() == null) {
            recB = MidiSystem.getReceiver();
		} else {
            recB = synthB.getReceiver();
		}
		sequenA.getTransmitter().setReceiver(recB);
		sequenB.open();
		sequenB.setTempoInBPM(bpm);
		
		Logger.write("Sound: Finished Init", Color.GREEN);
	}
	
	public void start(boolean runMidiA) throws Exception{
		if(soundOn == 0)
			return;
		if(runMidiA){
			sequenA.setTickPosition(0);
			sequenA.start();
			Logger.write("Sound: Restarted Sound A", Color.GREEN);
		}else{
			sequenB.setTickPosition(0);
			sequenB.start();
			Logger.write("Sound: Restarted Sound B", Color.GREEN);
		}
	}
	
	public void stop(boolean a){
		if(a){
			sequenA.stop();
			Logger.write("Soud: Stopped Sound A", Color.GREEN);
		}else{
			sequenB.stop();
			Logger.write("Sound: Stopped Sound B", Color.GREEN);
		}
	}
	
	public void setNewBPM(float nBPM){
		if(soundOn == 0)
			return;
		bpm = nBPM;
		sequenA.setTempoInBPM(bpm);
		Logger.write("Sound: New BPM for SoundA is " + bpm, Color.BLUE);
	}
	
	public void checkRunningAndRestartWhenNot() throws Exception{
		if(soundOn == 0)
			return;
		if(Main.game.getGameState() == GAME_STATE.RUNNING || Main.menu.getmState() == MENU_STATE.MS_PAUSE){
			if(!sequenA.isRunning())
				start(true);
		}else{
			if(!sequenB.isRunning())
				start(false);
		}
	}
	
	public void setSoundOn(int sO) {
		this.soundOn = sO;
		if(sO == 0){
			stop(true);
			stop(false);
		}
		
	}
}
