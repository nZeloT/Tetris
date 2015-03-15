package core;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class SteinER {
	private ArrayList<Block> b;
	private int type;
	private int dim;
	private int[][] matrix;
	private Point absMatrixCoord;


	public SteinER(int t){
		type = t;
		b = new ArrayList<Block>();
		int mid = (int)(Main.numBlocksPerRow / 2);
		Color f;
		switch(t){
		//++
		//++
		case 0:
			f = Color.YELLOW;
			b.add(new Block(mid, -1, f));
			b.add(new Block(mid+1, -1, f));
			b.add(new Block(mid, 0, f));
			b.add(new Block(mid+1, 0, f));
			break;
		//+
		//+
		//+
		//+
		case 1:
			f = Color.CYAN;
			b.add(new Block(mid, -3, f));
			b.add(new Block(mid, -2, f));
			b.add(new Block(mid, -1, f));
			b.add(new Block(mid, 0, f));
			break;
		//+
		//++
		// +
		case 2:
			f = Color.GREEN;
			b.add(new Block(mid, -2, f));
			b.add(new Block(mid, -1, f));
			b.add(new Block(mid+1, -1, f));
			b.add(new Block(mid+1, 0, f));
			break;

		// +
		//++
		//+
		case 3:
			f = Color.RED;
			b.add(new Block(mid+1, -2, f));
			b.add(new Block(mid+1, -1, f));
			b.add(new Block(mid, -1, f));
			b.add(new Block(mid, 0, f));
			break;

		//++
		// +
		// +
		case 4:
			f = Color.ORANGE;
			b.add(new Block(mid, -2, f));
			b.add(new Block(mid+1, -2, f));
			b.add(new Block(mid+1, -1, f));
			b.add(new Block(mid+1, 0, f));
			break;
		//++
		//+
		//+
		case 5:
			f = Color.BLUE;
			b.add(new Block(mid+1, -2, f));
			b.add(new Block(mid, -2, f));
			b.add(new Block(mid, -1, f));
			b.add(new Block(mid, 0, f));
			break;

		// +
		//+++
		case 6:
			f = new Color(154,11,213);
			b.add(new Block(mid, 0, f));
			b.add(new Block(mid+1, 0, f));
			b.add(new Block(mid+2, 0, f));
			b.add(new Block(mid+1, -1, f));
			break;			
		//   +
		//  +
		// +
		//+
		case 7:
			f = Color.WHITE;
			b.add(new Block(mid, 0, f));
			b.add(new Block(mid+1, -1, f));
			b.add(new Block(mid+2, -2, f));
			b.add(new Block(mid+3, -3, f));
			break;
		
		//  +
		//  +
		//++
		case 8:
			f = Color.WHITE;
			b.add(new Block(mid, 0, f));
			b.add(new Block(mid+1, 0, f));
			b.add(new Block(mid+2, -1, f));
			b.add(new Block(mid+2, -2, f));
			break;
		
		//+  +
		// ++
		case 9:
			f = Color.WHITE;
			b.add(new Block(mid, -1, f));
			b.add(new Block(mid+1, 0, f));
			b.add(new Block(mid+2, 0, f));
			b.add(new Block(mid+3, -1, f));
			break;
			
		// +
		//+ +
		// +
		case 10:
			f = Color.WHITE;
			b.add(new Block(mid+1, 0, f));
			b.add(new Block(mid, -1, f));
			b.add(new Block(mid+2, -1, f));
			b.add(new Block(mid+1, -2, f));
			break;
			
		}
		absMatrixCoord = new Point(getXmin(), getYmin());

	}

	public void move(int down, int right){
		if(down == 0 && right == 0)
			return;
		for(int i = 0; i < b.size(); i++)
			b.get(i).setPosAbs(new Point(b.get(i).getPosAbs().x + right, b.get(i).getPosAbs().y + down));
		absMatrixCoord.x += right;
		absMatrixCoord.y += down;
	}


	public ArrayList<Point> getNurDiePunkteDieWirBrauchen(){
		ArrayList<Point> p = new ArrayList<Point>();
		ArrayList<Integer> x = new ArrayList<Integer>();


		for (int i = 0; i < b.size(); i++) {
			boolean add = true;
			for (int j = 0; j < x.size(); j++) {
				if(x.get(j) == b.get(i).getPosAbs().x)
					add = false;

			}
			if(add)
				x.add(b.get(i).getPosAbs().x);
		}		

		// y max auf jedes x 
		for (int j = 0; j <x.size(); j++)
		{
			int yMax = 0;
			for (int i = 0 ; i < b.size();i++)
			{
				if(x.get(j)==  b.get(i).getPosAbs().x)
					if (yMax < b.get(i).getPosAbs().y)
						yMax= b.get(i).getPosAbs().y;
			}

			p.add(new Point(x.get(j),yMax));

		}

		return p;
	}

	
	public int [][] getMatrix(){
		dim = getStoneDimension()+1;
	
		matrix = new int[dim][dim];

		for (int i = 0 ;i< b.size();i++) {
			matrix[b.get(i).getPosAbs().y-getYmin()][b.get(i).getPosAbs().x-getXmin()]=i+1;
		}
		return matrix;
	}
	
	public int getDim() {
		return dim;
	}

	public void rotate(){
		if(type == 0)
			return;
		matrix = getRotatedMatrix();
		
		setPositionAbs(absMatrixCoord.x, absMatrixCoord.y);
	}

	public ArrayList<Block> getList(){
		return b;
	}

	public ArrayList<Point> getPointList() {
		ArrayList<Point> ret = new ArrayList<Point>();

		for (Block bl : b) {
			ret.add(bl.getPosAbs());
		}

		return ret;
	}
	
	public void killY(int y){
		int yMin = getYmin();
		int yMax = getYmax();
		
		if(y < yMin || yMin < 0)
			return;
		else if(y <= yMax && y >= yMin){
			//Mer muesse n block kille
			for (int i = 0; i < b.size(); i++) {
				if(b.get(i).getPosAbs().y < y)
					b.get(i).setPosAbs(new Point(b.get(i).getPosAbs().x, b.get(i).getPosAbs().y + 1));
				else if(b.get(i).getPosAbs().y == y){
					b.remove(i);
					i--;
				}
			}
			return;
		}
		else{
			move(1,0);
		}		
		
	}
	
	public void setPositionAbs(int x, int y){
		absMatrixCoord.x = x;
		absMatrixCoord.y = y;
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				int idx = matrix[j][i];
				if(idx != 0){
					b.get(idx-1).setPosAbs(new Point(i+x, j+y));
				}
			}
		}		
		int dis = getRotationsRandAusgleich(matrix);
		if(dis != 0){
			for (int i = 0; i < b.size(); i++) {
				b.get(i).setPosAbs(new Point(b.get(i).getPosAbs().x-dis, b.get(i).getPosAbs().y));
			}
			absMatrixCoord.x -= dis;
		}
	}

	private int getStoneDimension(){
		int xmin=getXmin(), xmax=getXmax() ; 
		int ymin=getYmin() , ymax=getYmax();
		
		if (ymax-ymin > xmax-xmin)
			return ymax - ymin;
		else
			return xmax - xmin;
		
	}
	
	public int getXmin(){
		int xmin = 10000;
		for (Block bl : b) {
			
			if(bl.getPosAbs().x < xmin)
				xmin = bl.getPosAbs().x;
			
		}
		return xmin;
	}
	
	public int getXmax(){
		int xmax=0;
		for (Block bl : b) {
			if(bl.getPosAbs().x > xmax)
				xmax = bl.getPosAbs().x;
		}
		return xmax;
	}

	public int getYmin(){
		int ymin= 10000;
		for (Block bl : b) {
			if(bl.getPosAbs().y <ymin)
				ymin = bl.getPosAbs().y;
		}
		return ymin;
	}
	
	public int getYmax(){
		int ymax=0;
		for (Block bl : b) {			
			if ( bl.getPosAbs().y > ymax)
				ymax = bl.getPosAbs().y;
		}
		return ymax;
	}
	
	public Point getAbsMatrixCoord() {
		return absMatrixCoord;
	}

	public int[][] getRotatedMatrix(){
		int[][] ret = new int[dim][dim];

		for (int y = 0; y < dim; y++) {
			for (int x = 0; x < dim; x++) {
				int nX = dim-y-1;
				int nY = x;
				ret[nY][nX] = matrix[y][x];
			}
		}

		return ret;
	}
	
	public int getRotationsRandAusgleich(int[][] mat){
		int xMin = absMatrixCoord.x;
		
		int distanceR = 0;
		int distanceL = 0;
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				int idx = mat[j][i];
				if(idx != 0){
					if(i+xMin-Main.numBlocksPerRow+1 > distanceR)
						distanceR = (int)(i+xMin-Main.numBlocksPerRow+1);
					if(i+xMin < distanceL)
						distanceL = (int)(i+xMin);
				}
			}
		}
		if(distanceL != 0)
			return distanceL;
		if(distanceR != 0)
			return distanceR;
		return 0;
	}
	
	public int getType() {
		return type;
	}
}
