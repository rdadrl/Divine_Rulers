package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class View {
	JFrame frame;
	Canvas cvs;
	ControlPanel cp;
	public View() throws IOException {
		frame = new JFrame("Titanic Space Odyssey");
		frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cvs = new Canvas();
        cp = new ControlPanel();
        frame.add(cvs, BorderLayout.CENTER);
        frame.add(cp, BorderLayout.EAST);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null); // Place the window in the middle of the screen
	}


}
