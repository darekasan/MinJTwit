package net.darekasan.minjtwit.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import net.darekasan.minjtwit.main.TwitCore;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.URLEntity;
import twitter4j.User;

public class TweetContainer extends JPanel {
	private JLabel lblIcon;
	private JLabel lblContent;

	private Timer rTimer;
	private String tweet, screenname, name, iconUrl;
	String additionalInfo="";
	private Date date;
	boolean isFavorited=false;
	boolean isRetweeted=false;
	private JButton btnReply;
	private JButton btnRt;
	private JButton btnFav;
	private JButton btnDetail;
	Status status;
	User user;
	TwitCore core;
	JPopupMenu detailMenu;

	/**
	 * Create the panel.
	 */
	public TweetContainer(Status s,TwitCore c) {
		user=s.getUser();
		core=c;
		detailMenu=new JPopupMenu();

		if(s.isRetweet()){

			status=s.getRetweetedStatus();
			User sourceuser=status.getUser();
			tweet=core.encodeHtml(status);
			addUrl(status);
			screenname=sourceuser.getScreenName();
			name=sourceuser.getName();
			iconUrl=sourceuser.getProfileImageURL();
			date=status.getCreatedAt();
			additionalInfo="<small color='#00cc00'>@"+core.htmlEscape(user.getScreenName())+" retweeted</small><br>";
			isFavorited=status.isFavorited();
			isRetweeted=status.isRetweetedByMe();
			user=sourceuser;
		}else{
			status=s;
			tweet = core.encodeHtml(status);
			addUrl(status);
			screenname = user.getScreenName();
			name = user.getName();
			iconUrl = user.getProfileImageURL();
			date = s.getCreatedAt();
			isFavorited=status.isFavorited();
			isRetweeted=status.isRetweeted();

		}


		setBackground(new Color(255, 255, 255));

		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new BorderLayout(0, 0));

		lblContent = new JLabel();
		lblContent.setVerticalAlignment(SwingConstants.TOP);
		setContent(name, screenname, tweet, "now");
		add(lblContent, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		 btnReply = new JButton("Reply");
		btnReply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		btnReply.setFont(btnReply.getFont().deriveFont(
				btnReply.getFont().getSize() - 2f));
		panel.add(btnReply);

		 btnRt = new JButton("RT");
		btnRt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					core.getTwitter().retweetStatus(status.getId());
				} catch (TwitterException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				}
			}
		});
		if(isRetweeted){
			btnRt.setBackground(new Color(0, 204, 0));
		}else{
			btnRt.setBackground(null);
		}

		btnRt.setFont(btnRt.getFont()
				.deriveFont(btnRt.getFont().getSize() - 2f));
		panel.add(btnRt);

		 btnFav = new JButton("Fav");
		btnFav.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		if(isFavorited){
			btnFav.setBackground(new Color(255, 255, 0));
		}else{
			btnFav.setBackground(null);
		}
		btnFav.setFont(btnFav.getFont().deriveFont(
				btnFav.getFont().getSize() - 2f));
		panel.add(btnFav);

		 btnDetail = new JButton("...");
		btnDetail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				detailMenu.show(btnDetail, 20, 20);
			}
		});
		btnDetail.setFont(btnDetail.getFont().deriveFont(
				btnDetail.getFont().getSize() - 2f));
		panel.add(btnDetail);

		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		lblIcon = new JLabel();
		lblIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UserWindow uwin= new UserWindow(user, core);
				uwin.setVisible(true);
			}
		});

		panel_1.add(lblIcon);

		BufferedImage icon=core.getImage(iconUrl);
		if(icon!=null){
			lblIcon.setIcon(new ImageIcon(icon));
		}else{
			lblIcon.setIcon(new ImageIcon(TweetContainer.class
					.getResource("/javax/swing/plaf/metal/icons/ocean/error.png")));
		}

		int buttonh=panel.getPreferredSize().height;
		// setPreferredSize(new Dimension(400,200));
		int contentheight = (int) lblContent.getPreferredSize().getHeight();
		if (contentheight > 48) {
			setMaximumSize(new Dimension(400,
					(int) lblContent.getPreferredSize().height + panel.getPreferredSize().height+2));
		} else {
			setMaximumSize(new Dimension(400, 50+panel.getPreferredSize().height));
		}

		addMenuItem("", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO 自動生成されたメソッド・スタブ
				System.out.println("Fire!");
			}
		});

	}

	public Status getStatus(){
		return status;
	}

	public void setIsFavorited(boolean value){
		isFavorited=value;
		if(isFavorited){
			btnFav.setBackground(new Color(255, 255, 0));
		}else{
			btnFav.setBackground(null);
		}
	}

	public void setIsRetweeted(boolean value){
		isFavorited=value;
		if(isRetweeted){
			btnRt.setBackground(new Color(0, 204, 0));
		}else{
			btnRt.setBackground(null);
		}
	}

	public void setContent(String name, String sn, String tw, String date) {
		lblContent.setText("<html><body style='width:250px'>"+additionalInfo+"<b>"
				+ TwitCore.htmlEscape(name) + "</b><small>@" + sn + "</small></div>  "
				+ "<small color='#aaaaaa'>" + date + "</small><br>"
				+ tw+ "</body></html>");
	}

	public void dateRefresh(Date d) {
		long diff = d.getTime() - date.getTime();
		String str = "";
		if (diff < 10000) {
			str = "now";
		} else if (diff < 60000) {
			str = diff / 1000 + "s";
		} else if (diff < 3600000) {
			str = diff / 60000 + "m";
		} else if (diff < 86400000) {
			str = diff / 360000 + "h";
		} else {
			str = date.toString();
		}
		setContent(name, screenname, tweet, str);
	}

	void addUrl(Status s) {
		URLEntity[] entities = s.getURLEntities();
		for (URLEntity entity : entities) {
			final String url = entity.getExpandedURL();
			addMenuItem(url, new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					// TODO 自動生成されたメソッド・スタブ
					try {
						Desktop.getDesktop().browse(new URI(url));
					} catch (IOException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					}
				}
			});
		}
		MediaEntity[] mEntities = s.getExtendedMediaEntities();
		final String[] urls=new String[mEntities.length];
		for (int i=0;i<mEntities.length;i++) {
			MediaEntity mEntity=mEntities[i];
			urls[i]=mEntities[i].getMediaURL();
		}
		addMenuItem(mEntities.length+" Media(s)..."+mEntities[0].getType(), new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ImageView iv=new ImageView(urls);
				iv.setVisible(true);
			}
		});
	}

	void addMenuItem(String s,ActionListener a){
		JMenuItem item=new JMenuItem(s);
		detailMenu.add(item);
		item.addActionListener(a);
	}
}
