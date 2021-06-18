package net.darekasan.minjtwit.view;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

public class ImageView extends JFrame {

	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private JPanel panel;
	ArrayList imgs=new ArrayList();

	/**
	 * Create the frame.
	 */
	public ImageView(String[] url) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		//panel = new JPanel();
		//tabbedPane.addTab("New tab", null, panel, null);

		try {
			for(int i=0;i<url.length;i++){
				imgs.add(new Img(new URL(url[i])));
				tabbedPane.addTab("img"+i, null, (JPanel)imgs.get(i), null);
			}

		} catch (MalformedURLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

	class Img extends JPanel{
		BufferedImage bi;
		URL url;
		int status=0;//0:loading 1:loaded 2:load failed

		Img(URL u){
			url=u;
			loadImage li =new loadImage();

			li.start();
		}

		class loadImage extends Thread{
			public void run(){
				try {
					status=0;
					Img.this.repaint();
					bi=ImageIO.read(url);
					status=1;
					Img.this.repaint();
				} catch (IOException e) {
					// TODO 自動生成された catch ブロック
					status=2;
					Img.this.repaint();
					e.printStackTrace();
				}

			}
		}

		public void paintComponent(Graphics g){
			Graphics2D g2=(Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	                RenderingHints.VALUE_ANTIALIAS_ON);
			switch (status) {
			case 0:
				g.drawString("loading", 20, 20);
				break;
			case 1:

				double m=(double)this.getHeight()/bi.getHeight();
				if(bi.getWidth()*m>this.getWidth()){
					m=(double)this.getWidth()/bi.getWidth();
					g2.drawImage(bi, 0, 0,this.getWidth(),(int)(bi.getHeight()*m), this);
				}else{
					g2.drawImage(bi, 0, 0,(int)(bi.getWidth()*m),this.getHeight(), this);
				}

				break;
			case 2:
				g.drawString("error", 20, 20);
				break;
			}
		}
	}



}
