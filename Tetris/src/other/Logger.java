package other;
import java.awt.Color;
import java.io.FileWriter;


public class Logger {
	private static FileWriter w;
	private static int consoleOut;
	public static void initLogFile(String title, String file){
		if(w != null)
			endLogFile();
		consoleOut = 1;
		try {
			w = new FileWriter(file);
			w.write("<html><head><body><h3>" + title + "</h3><hr><ul>");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void endLogFile(){
		try {
			w.append("</ul></body></html>");
			w.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void write(String str, Color c){
		try {
			if(consoleOut == 1)
				System.out.println(str);
			w.append("<li style=\"color: rgba(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + "," + c.getTransparency() + ");\">" + str + "</li>\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void write(String str){
		try {
			if(consoleOut == 1)
				System.out.println(str);
			w.append("<li>" + str + "</li>\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void write(int[][] mat, String str, Color c){
		String out = "<li style=\"color: rgba(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + "," + c.getTransparency() + ");\">" + str + "<table>";
		for (int i = 0; i < mat.length; i++) {
			out += "<tr>";
			for (int j = 0; j < mat[0].length; j++) {
				out += "<td>" + mat[i][j] + "</td>";
			}
			out += "</tr>";
		}
		out += "</table></li>";
		try {
			if(consoleOut == 1)
				System.out.println(str);
			w.append(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void write(int[][] mat, String str){
		String out = "<li>" + str + "</table>";
		for (int i = 0; i < mat.length; i++) {
			out += "<tr>";
			for (int j = 0; j < mat[0].length; j++) {
				out += "<td>" + mat[i][j] + "</td>";
			}
			out += "</tr>";
		}
		out += "</table></li>";
		try {
			if(consoleOut == 1)
				System.out.println(str);
			w.append(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setConsoleOut(int consoleOut) {
		if(consoleOut == 0){
			write("Logger: ConsoleOut switched OFF. NO further Console Out!", Color.RED);
		}
		Logger.consoleOut = consoleOut;
	}

}
