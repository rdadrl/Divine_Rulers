package xperiment.yipping;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class ControlPanel extends JPanel{
	public static final int WIDTH = 200;
	JButton bLaunch;
	JSpinner sSpeed1;
	JSpinner sSpeed2;
	JSpinner sSpeed3;
	public ControlPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		panel.add(panelSpeed());
		panel.add(panelSelectMode());
		panel.add(panelLaunch());
		this.add(panel, BorderLayout.LINE_START);
		
	}
	
	public JPanel panelAngle() {
		
		return null;
	}

	public JPanel panelSpeed() {
		sSpeed1 = createSpinner(0, -1e6, 1e6, 1);
		sSpeed2 = createSpinner(0, -1e6, 1e6, 1);
		sSpeed3 = createSpinner(0, -1e6, 1e6, 1);
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		JLabel lSpeedX = new JLabel("X-axis velocity: ");
		JLabel lSpeedY = new JLabel("Y-axis velocity: ");
		JLabel lSpeedZ = new JLabel("Z-axis velocity: ");
		JLabel lUnit1 =  new JLabel("km/s");
		JLabel lUnit2 =  new JLabel("km/s");
		JLabel lUnit3 =  new JLabel("km/s");
		gc.gridy = 1;
		panel.add(lSpeedX, gc);
		panel.add(sSpeed1, gc);
		panel.add(lUnit1, gc);
		gc.gridy = 2;
		panel.add(lSpeedY,gc);
		panel.add(sSpeed2, gc);
		panel.add(lUnit2, gc);
		gc.gridy = 3;
		panel.add(lSpeedZ, gc);
		panel.add(sSpeed3, gc);
		panel.add(lUnit3, gc);
		this.add(panel);
		return panel;
	}
	
	public JPanel panelSelectMode() {
		JPanel p = new JPanel();
		String[] strs = { "RK4", "euler" };

		JComboBox listB = new JComboBox(strs);
		listB.setSelectedIndex(0);
		listB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Constant.mode = listB.getSelectedItem().toString();
			}
			
		});
		
		p.add(listB);
		return p;

	}
	
	public JPanel panelLaunch() {
		JPanel p = new JPanel();
		bLaunch = new JButton("launch");
		p.add(bLaunch);
		return p;
	}
	
	public JSpinner createSpinner(double current, double min, double max, int step) {
    	JSpinner spinner = new JSpinner();
    	spinner.setModel(new SpinnerNumberModel(current, min, max, step));
    	return spinner;
    }
}
