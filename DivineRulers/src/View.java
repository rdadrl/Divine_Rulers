import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class View {
	JFrame frame;
	public View() {
		frame = new JFrame("Titanic Space Odyssey");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Canvas cvs = new Canvas();
        
        frame.add(cvs);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null); // Place the window in the middle of the screen
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
