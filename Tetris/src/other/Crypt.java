package other;

public class Crypt {

	public static String crypt(String cry, int d){
		String out = "";
		for (int i = 0; i < cry.length(); i++) {
			char ch = cry.charAt(i);
			ch += d;
			ch %= 255;
			out += ch;
		}
		return out;
	}
	
	public static String encrypt(String enc, int d){
		String out = "";
		for (int i = 0; i < enc.length(); i++) {
			char ch = enc.charAt(i);
			ch -= d;
			if(ch < 0)
				ch = (char) (255-ch);
			out += ch;
		}
		return out;
	}

}
