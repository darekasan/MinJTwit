package net.darekasan.minjtwit.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import net.darekasan.minjtwit.main.TwitCore;
import twitter4j.User;

public class UserWindow extends JFrame {

	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private JPanel pnlTweets;
	private JPanel panel;
	private JPanel panel_2;
	private JButton btnFollow;
	private JButton btnMore;
	private JPanel panel_3;
	private JLabel lblIcon;
	private JLabel lblContent;

	User user;
	TwitCore core;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public UserWindow(User u,TwitCore c) {
		user=u;
		core=c;

		String iconUrl=u.getBiggerProfileImageURL();


		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 416);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		pnlTweets = new JPanel();
		tabbedPane.addTab("Tweets", null, pnlTweets, null);

		panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		panel.add(panel_2, BorderLayout.SOUTH);

		btnMore = new JButton("...");
		panel_2.add(btnMore);

		btnFollow = new JButton("Follow");
		panel_2.add(btnFollow);

		panel_3 = new JPanel();
		panel.add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.X_AXIS));

		lblIcon = new JLabel("Icon");
		lblIcon.setPreferredSize(new Dimension(73, 73));
		try {
			lblIcon.setIcon(new ImageIcon(ImageIO.read(new URL(iconUrl))));
		} catch (MalformedURLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		panel_3.add(lblIcon);

		lblContent = new JLabel("Content");
		lblContent.setText("<html><body><big>"+user.getName()+"</big> @"+user.getScreenName()+"<p>"+user.getDescription()+"</p>"+"</body><html>");
		panel_3.add(lblContent);
	}
}
