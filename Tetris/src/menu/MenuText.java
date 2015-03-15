package menu;

import java.awt.Font;

public class MenuText extends MenuItem {

	public MenuText() {
		selectable = false;
	}

	public MenuText(String t, float xK, float yK, Font f) {
		super(t, xK, yK, f);
		selectable = false;
	}
}
