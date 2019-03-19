import static javax.swing.SwingUtilities.invokeLater;

import javax.swing.JFrame;


public class Main {
	
    public static void main(String[] args) {
        invokeLater(new Runnable() {
            public void run() {
               
            	new View();
            	
            	
            }
        });
    }
}
