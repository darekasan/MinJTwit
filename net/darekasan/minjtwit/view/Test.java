package net.darekasan.minjtwit.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.peer.ScrollbarPeer;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Test extends JFrame {

	JButton button1;
	JScrollPane scrollpane;
	JPanel panel;
	BufferedImage bi;

	public Test() throws IOException{
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		Insets insets = this.getInsets();
		this.setSize(600,400);
		this.setBackground(new Color(255,255,255));
		this.setLocationRelativeTo(null);

		JPanel p =new JPanel();


		panel=new imagePanel();
		scrollpane=new JScrollPane(panel);

		this.add(scrollpane);
		//p.add(scrollpane);
		this.setVisible(true);
		


	}

	public class imagePanel extends JPanel{
		public void paintComponent(Graphics g){
			//super.paintComponent(g);
			System.out.println("てす");
			
			try {
				bi = ImageIO.read(new File("C:\\Users\\darekasan\\Desktop\\test.png"));
				panel.setPreferredSize(new Dimension(bi.getWidth(),bi.getHeight()));
				Rectangle r = scrollpane.getVisibleRect();
				
				Graphics2D g2=(Graphics2D)g;
				g2.drawImage(bi,0,0,panel);

			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			panel.revalidate();

		}
	}


	public class aListener implements ActionListener{
		  public void actionPerformed(ActionEvent e){
		    if(e.getActionCommand().equals("テスト")){
		    	System.out.println("押された");
		    }
		  }
	}


}
