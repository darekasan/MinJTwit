package net.darekasan.minjtwit.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import net.darekasan.minjtwit.main.TwitCore;
import net.darekasan.minjtwit.model.ListChangeListener;
import net.darekasan.minjtwit.model.MStatus;
import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JPanel panel;
	private TextBox textPane;
	private JButton btnTweet;
	private JScrollPane scrollPane;
	private JPanel panel_1;
	static String toolongtext = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
	private JPanel panel_2;
	private JLabel lblMemory;
	private Timer rTimer;
	private JLabel lblBuffer;
	private JTabbedPane tabbedPane;
	private JPanel panel_3;

	private int oldHeight=0;

	TwitCore core;
	Properties prop;
	private JButton btnConfig;

	public MainWindow(TwitCore c,Properties p) {
		core=c;
		prop=p;
		setTitle("MinJTwit");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 416);
		setMaximumSize(new Dimension(450,-1));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		textPane = new TextBox();
		textPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if((e.getModifiers()==KeyEvent.CTRL_MASK)&&e.getKeyCode()==KeyEvent.VK_ENTER){
					tweet();
				}
			}
		});
		panel.add(textPane);


		btnTweet = new JButton("Tweet");
		btnTweet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tweet();
			}
		});
		panel.add(btnTweet);

		panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		btnConfig = new JButton("Config");
		btnConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConfigWindow cw=new ConfigWindow(core, prop);
				cw.setVisible(true);
			}
		});
		panel_2.add(btnConfig);

		lblMemory = new JLabel("Memory");
		lblMemory
				.setIcon(new ImageIcon(
						MainWindow.class
								.getResource("/javax/swing/plaf/metal/icons/ocean/computer.gif")));
		panel_2.add(lblMemory);

		lblBuffer = new JLabel("Buffer");
		lblBuffer.setIcon(new ImageIcon(MainWindow.class
				.getResource("/javax/swing/plaf/metal/icons/ocean/file.gif")));
		panel_2.add(lblBuffer);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tabbedPane
				.addTab("Home",
						new ImageIcon(
								MainWindow.class
										.getResource("/javax/swing/plaf/metal/icons/ocean/homeFolder.gif")),
						scrollPane, null);

		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		panel_1 = new JPanel();
		panel_1.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				JScrollBar scrollbar=scrollPane.getVerticalScrollBar();
				if(scrollbar.getValue()!=0){
					scrollbar.setValue(scrollbar.getValue()+panel_1.getHeight()-oldHeight);
					oldHeight=panel_1.getHeight();
				}
			}
		});
		panel_1.setBackground(SystemColor.control);
		scrollPane.setViewportView(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));

		panel_3 = new JPanel();
		tabbedPane.addTab("New tab", null, panel_3, null);

		// panel_1.add(new
		// TweetContainer(toolongtext,"darekasan_net","だれかさんねっと","https://pbs.twimg.com/profile_images/580787892663947264/C5Yfkw2T_normal.png"));
		// panel_1.add(new
		// TweetContainer("Tweet","darekasan_net","だれかさんねっと","https://pbs.twimg.com/profile_images/580787892663947264/C5Yfkw2T_normal.png"));
		core.getStatuses().addListener(new statusListener());
		core.getTwitterStream().addListener(new uslistner());
	}

	class TextBox extends JTextPane{
		String rtext="";

		TextBox(){
			super();
			setOpaque(false);
            setBackground(new Color(0,0,0,0));
            addKeyListener(new KeyListener() {

				public void keyTyped(KeyEvent e) {
					rtext=String.valueOf(140-getText().length());
					repaint();
				}

				public void keyReleased(KeyEvent e) {
					// TODO 自動生成されたメソッド・スタブ

				}

				public void keyPressed(KeyEvent e) {
					// TODO 自動生成されたメソッド・スタブ

				}
			});
		}

		public void paintComponent(Graphics g){
			FontMetrics fm=g.getFontMetrics();
			int strw=fm.stringWidth(rtext);
			g.drawString(rtext, this.getWidth()-strw, this.getHeight());
			super.paintComponent(g);
		}



	}

	public void startTimer() {
		rTimer = new Timer(1000, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Runtime r = Runtime.getRuntime();
				lblMemory.setText(r.freeMemory() / 1000000 + "MB / "
						+ r.totalMemory() / 1000000 + "MB");

				for (Component comp : panel_1.getComponents()) {
					try{
						TweetContainer tweet=(TweetContainer)comp;
						tweet.dateRefresh(new Date(System.currentTimeMillis()));
					}catch (ClassCastException ex) {

					}

				}
				lblBuffer.setText(core.getStatuses().size()+"/"+core.getStatusesSize());

			}
		});
		rTimer.start();
	}

	public JPanel getTweetField() {
		return panel_1;
	}

	void tweet(){
		btnTweet.setEnabled(false);
		textPane.setEnabled(false);
		try {
			core.getTwitter().updateStatus(textPane.getText());
			textPane.setText("");
		} catch (TwitterException e) {
			panel_1.add(new NotifyContainer("ツイートに失敗しました", e.getErrorMessage(), NotifyContainer.ERROR), 0);
		}
		btnTweet.setEnabled(true);
		textPane.setEnabled(true);
		textPane.requestFocusInWindow();
	}

	void removePanel(){
		Component[] components = panel_1.getComponents();
		for(int i=0;i<components.length-100;i++){
			panel_1.remove(components.length-1);
		}
	}

	class statusListener implements ListChangeListener{

		public void add(Object o) {
			// TODO 自動生成されたメソッド・スタブ
			if(panel_1!=null){
				MStatus s =(MStatus) o;
				if(s.getIsHome()){
					panel_1.add(new TweetContainer(s.getStatus(),core),0);
					removePanel();
				}

			}
		}

		public void addAll(Collection c) {
			for (Object object : c) {
				MStatus s =(MStatus) object;
				if(s.getIsHome()){
					panel_1.add(new TweetContainer(s.getStatus(),core),0);
				}
			}

		}

	}


	class uslistner implements UserStreamListener {

		public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
			// TODO 自動生成されたメソッド・スタブ

			try {
				MStatus status =core.getMStatusFromId(statusDeletionNotice.getStatusId());
				panel_1.add(new NotifyContainer(status.getStatus().getUser().getScreenName()+" delete tweet", status.getStatus().getText(), NotifyContainer.GENERIC), 0);
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}

		public void onScrubGeo(long userId, long upToStatusId) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onStallWarning(StallWarning warning) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onStatus(Status status) {
			if(status.isRetweetedByMe()){
				for (Component comp : panel_1.getComponents()) {
					try{
						TweetContainer tweet=(TweetContainer)comp;
						if(tweet.getStatus().getId()==status.getRetweetedStatus().getId()){
							tweet.setIsRetweeted(true);
						}

					}catch (ClassCastException ex) {

					}
				}
			}else{
				try {
					Status source =status.getRetweetedStatus();
					if(status.getRetweetedStatus().getUser().getId()==core.getTwitter().verifyCredentials().getId()){
						String text=core.getMStatusFromId(source.getId()).getStatus().getText();
						if(text.length()>30){
							text=text.substring(0, 29)+"...";
						}
						panel_1.add(new NotifyContainer("<span color='#0000cc'>"+status.getUser().getScreenName()+" retweeted</span>", text, NotifyContainer.GENERIC), 0);
					}
				} catch (TwitterException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}

		}

		public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onException(Exception arg0) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onBlock(User source, User blockedUser) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onDeletionNotice(long directMessageId, long userId) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onDirectMessage(DirectMessage directMessage) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onFavorite(User source, User target, Status favoritedStatus) {
			// TODO 自動生成されたメソッド・スタブ
			String text;
			try {
				if(target.getId()==core.getTwitter().verifyCredentials().getId()){
					text=core.getMStatusFromId(favoritedStatus.getId()).getStatus().getText();
					if(text.length()>30){
						text=text.substring(0, 29)+"...";
					}
					panel_1.add(new NotifyContainer("<span color='#ffff00'>"+source.getScreenName()+" favorited</span>", text, NotifyContainer.GENERIC), 0);
				}else{
					for (Component comp : panel_1.getComponents()) {
						try{
							TweetContainer tweet=(TweetContainer)comp;
							if(tweet.getStatus().getId()==favoritedStatus.getId()){
								tweet.setIsFavorited(true);
							}

						}catch (ClassCastException ex) {

						}
					}
				}
			} catch (TwitterException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
		}

		public void onFavoritedRetweet(User source, User target,
				Status favoritedRetweeet) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onFollow(User source, User followedUser) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onFriendList(long[] friendIds) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onQuotedTweet(User source, User target, Status quotingTweet) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onRetweetedRetweet(User source, User target,
				Status retweetedStatus) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onUnblock(User source, User unblockedUser) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onUnfavorite(User source, User target,
				Status unfavoritedStatus) {
			String text;
			try {
				if(target.getId()==core.getTwitter().verifyCredentials().getId()){
					text=core.getMStatusFromId(unfavoritedStatus.getId()).getStatus().getText();
					if(text.length()>30){
						text=text.substring(0, 29)+"...";
					}
					panel_1.add(new NotifyContainer(source.getScreenName()+" unfavorited", text, NotifyContainer.GENERIC), 0);
				}else{
					for (Component comp : panel_1.getComponents()) {
						try{
							TweetContainer tweet=(TweetContainer)comp;
							if(tweet.getStatus().getId()==unfavoritedStatus.getId()){
								tweet.setIsFavorited(false);
							}

						}catch (ClassCastException ex) {

						}
					}
				}
			} catch (TwitterException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}

		}

		public void onUnfollow(User source, User unfollowedUser) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onUserDeletion(long deletedUser) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onUserListCreation(User listOwner, UserList list) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onUserListDeletion(User listOwner, UserList list) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onUserListMemberAddition(User addedMember, User listOwner,
				UserList list) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onUserListMemberDeletion(User deletedMember,
				User listOwner, UserList list) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onUserListSubscription(User subscriber, User listOwner,
				UserList list) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onUserListUnsubscription(User subscriber, User listOwner,
				UserList list) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onUserListUpdate(User listOwner, UserList list) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onUserProfileUpdate(User updatedUser) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onUserSuspension(long suspendedUser) {
			// TODO 自動生成されたメソッド・スタブ

		}

	}

}
