package ca.noxid.lab.entity;


import ca.noxid.uiComponents.UpdateTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CreateEntityDialog extends JDialog {
	private static final long serialVersionUID = -4046455183490545635L;
	private JTextField short1f = new UpdateTextField("NEW");
	private JTextField short2f = new UpdateTextField("NPC");
	private JTextField longnamef = new UpdateTextField("New Entity");
	private JTextPane descf = new JTextPane();
	
	public EntityData ent;

	CreateEntityDialog(Frame aFrame) {
		super(aFrame, true);
		this.setTitle("New Entity Metadata");
		JPanel pane = new JPanel();
		JScrollPane jsp;
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		JPanel namePane = new JPanel();
		namePane.setLayout(new BoxLayout(namePane, BoxLayout.Y_AXIS));
		namePane.setBorder(BorderFactory.createTitledBorder("Name"));
		namePane.add(longnamef);
		pane.add(namePane);
		namePane = new JPanel();
		namePane.setLayout(new BoxLayout(namePane, BoxLayout.Y_AXIS));
		namePane.setBorder(BorderFactory.createTitledBorder("Short Name"));
		namePane.add(short1f);
		namePane.add(short2f);
		pane.add(namePane);
		namePane = new JPanel();
		namePane.setLayout(new BoxLayout(namePane, BoxLayout.Y_AXIS));
		namePane.setBorder(BorderFactory.createTitledBorder("Description"));
		jsp = new JScrollPane(descf);
		descf.setPreferredSize(new Dimension(200, 140));
		descf.setText("This entity has not yet been defined");
		namePane.add(jsp);
		pane.add(namePane);
		JPanel actionsPane = new JPanel();
		JButton button;
		button = new JButton(new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				ent = new EntityData(-1);
				ent.setName(longnamef.getText());
				ent.setShort1(short1f.getText());
				ent.setShort2(short2f.getText());
				ent.setDesc(descf.getText());
				dispose();
			}			
		});
		button.setText("ACCEPT");
		actionsPane.add(button);
		button = new JButton(new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				ent = null;
				dispose();
			}			
		});
		button.setText("CANCEL");
		actionsPane.add(button);
		pane.add(actionsPane);
		
		this.setContentPane(pane);
		
		Point ep = aFrame.getLocationOnScreen();
		ep.x += aFrame.getWidth()/2;
		ep.y += aFrame.getHeight()/2;
		this.pack();
		ep.x -= this.getWidth()/2;
		ep.y -= this.getHeight()/2;
		this.setLocation(ep);
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
}
