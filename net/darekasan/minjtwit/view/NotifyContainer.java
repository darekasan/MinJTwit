package net.darekasan.minjtwit.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class NotifyContainer extends JPanel {
	private JLabel lblContent;

	public static final int GENERIC=0;
	public static final int ERROR=1;
	/**
	 * Create the panel.
	 */
	public NotifyContainer(String title,String content,int type) {


		if(type==ERROR){
			setBorder(new LineBorder(new Color(255, 0, 0)));
			setBackground(new Color(255, 80, 80));
		}else{
			setBorder(new LineBorder(new Color(0, 0, 0)));
			setBackground(new Color(255, 255, 255));
		}

		setLayout(new BorderLayout(0, 0));

		lblContent = new JLabel();
		lblContent.setVerticalAlignment(SwingConstants.TOP);

		add(lblContent, BorderLayout.CENTER);
setContent("<font size='-1'>"+title+"</font>"+"<p>"+content+"</p>");
		int contentheight = (int) lblContent.getPreferredSize().getHeight();
		setMaximumSize(new Dimension(400,
					(int) lblContent.getPreferredSize().height));
		java.awt.Toolkit.getDefaultToolkit().beep();
	}


	void setContent(String content) {
		lblContent.setText("<html><body style='width:250px'>"+content + "</body></html>");
	}


}
