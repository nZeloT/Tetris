package core;
import java.awt.Color;
import java.util.ArrayList;

import other.Logger;

public class Clipboard {
	private SteinER stein;

	public SteinER swapSteinERs(SteinER st){
		if(stein == null){
			stein = st;
			return null;
		}
		stein.setPositionAbs(st.getXmin(), st.getYmin());
		
		SteinER tmp = stein;
		stein = st;
		Logger.write("Clipboard: Swap Completed", Color.GREEN);
		return tmp;
	}
	
	public ArrayList<Block> getBList(){
		if(stein != null)
			return stein.getList();
		return null;
	}
	
	public SteinER getStein(){
		return stein;
	}
	
	public boolean hastSteinER(){
		if(stein == null)
			return false;
		else
			return true;
	}
	
	public void reset(){
		stein = null;
	}

}
