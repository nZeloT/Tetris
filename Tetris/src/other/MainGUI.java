package other;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import config.Settings;
import core.DrawPanel;
import core.Main;



@SuppressWarnings("serial")
public class MainGUI extends JFrame implements KeyListener{

	private JPanel contentPane;
	public DrawPanel draw;
	


	public MainGUI() {
		Logger.write("GUI: Init ...");
		int fw = Integer.parseInt(Settings.getItem("WindowSize").split("x")[0]);
		int fh = (int) Float.parseFloat(Settings.getItem("WindowSize").split("x")[1]);
		
		setFocusTraversalKeysEnabled(false);
		addKeyListener(this);
		setTitle("Tetris");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, fw+141, fh+38);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		draw = new DrawPanel(fw,fh);
		draw.setBounds(0, 0, fw+125, fh);
		contentPane.add(draw);
		Logger.write("GUI: Finished Init", Color.GREEN);
	}

	

	@Override
	public void keyPressed(KeyEvent key) {		
		if(!Main.game.keyEvent(key))
			Main.menu.keyEvent(key);
	}
	
	
	@Override
	public void keyReleased(KeyEvent key) {
		switch(key.getKeyCode()){
		case KeyEvent.VK_P:
			Main.previoulsyPressedKeys[0] = false;
			break;
		case KeyEvent.VK_ENTER:
			Main.previoulsyPressedKeys[1] = false;
			break;
		case KeyEvent.VK_TAB:
			Main.previoulsyPressedKeys[2] = false;
			break;
		case KeyEvent.VK_SPACE:
			Main.previoulsyPressedKeys[3] = false;
			break;
		default:
				break;
		}
		
	}
	
	@Override
	public void keyTyped(KeyEvent key) {
		Main.menu.keyTypedEvent(key);	
	}
}
