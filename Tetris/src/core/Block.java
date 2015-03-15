package core;
import java.awt.Color;
import java.awt.Point;

public class Block {
	private Point posAbs;
	private Color farbe;
	
	public Block(Point pAbs, Color f){
		farbe = f;
		posAbs = pAbs;
	}
	public Block(int x, int y, Color f){
		farbe = f;
		posAbs = new Point(x, y);
	}

	public void setPosAbs(Point pos) {
		this.posAbs = pos;
	}
	public void setFarbe(Color farbe) {
		this.farbe = farbe;
	}
	public Color getFarbe() {
		return farbe;
	}
	public Point getPosAbs() {
		return posAbs;
	}

}
