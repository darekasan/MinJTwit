package net.darekasan.minjtwit.view;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class AuthWindow extends JDialog {

	private JPanel contentPane;
	private JLabel lbltwitterweb;
	private JLabel lblNewLabel;
	private JTextField textField;
	private JButton btnNewButton_1;
	private JTextField textField_1;
	private JButton button;
	String authUrl;
	RequestToken requestToken;
	AuthWindow aw =this;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public AuthWindow(final Properties p) {
		super((JFrame)null, true);
		final Twitter twitter = TwitterFactory.getSingleton();
		try {
			requestToken = twitter.getOAuthRequestToken();
			authUrl=requestToken.getAuthorizationURL();
		} catch (TwitterException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}

		setBounds(100, 100, 395, 156);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[172px,grow][91px,grow][97px]", "[21px][][][]"));

		lbltwitterweb = new JLabel("TwitterのWebサイトで認証する");
		contentPane.add(lbltwitterweb, "cell 0 0,alignx right,aligny center");

		button = new JButton("開く");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(new URI(authUrl));
				} catch (IOException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(button, "cell 1 0");

		lblNewLabel = new JLabel("PINコードを入力する");
		contentPane.add(lblNewLabel, "cell 0 1,alignx trailing,aligny center");

		textField = new JTextField();
		contentPane.add(textField, "cell 1 1,growx");
		textField.setColumns(10);

		textField_1 = new JTextField();
		contentPane.add(textField_1, "cell 0 3,growx");
		textField_1.setColumns(10);

		btnNewButton_1 = new JButton("完了");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AccessToken accessToken;
				try {
					accessToken = twitter.getOAuthAccessToken(requestToken, textField.getText());
					twitter.setOAuthAccessToken(accessToken);
					p.setProperty("accessTokenSecret", accessToken.getTokenSecret());
					p.setProperty("accessToken", accessToken.getToken());

					aw.dispose();
				} catch (TwitterException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				}

			}
		});
		contentPane.add(btnNewButton_1, "cell 2 3,alignx right");
	}

}
