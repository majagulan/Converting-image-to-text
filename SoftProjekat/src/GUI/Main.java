package GUI;

import GUI.MyFrame;

/**
 * Klasa <code>Main</code> iz koje se poziva klasa {@link MyFrame}.
 *
 */
public class Main {

	public static void main(String[] args) {
		
		MyFrame mf = MyFrame.getInstance();
		mf.setVisible(true);
	}

}
