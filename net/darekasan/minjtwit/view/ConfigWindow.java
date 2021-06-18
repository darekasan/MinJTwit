package net.darekasan.minjtwit.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import net.darekasan.minjtwit.main.TwitCore;

public class ConfigWindow extends JFrame {

	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private JPanel panel;
	private JList list;
	private JPanel panel_1;
	private JLabel lblNewLabel;
	private JButton button;
	TwitCore core;
	Properties prop;


	public ConfigWindow(TwitCore c,Properties p) {
		core=c;prop=p;
		setTitle("Config");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 401, 366);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		panel = new JPanel();
		tabbedPane.addTab("General", null, panel, null);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		panel_1 = new JPanel();
		panel.add(panel_1);

		lblNewLabel = new JLabel("アカウント:");
		panel_1.add(lblNewLabel);

		button = new JButton("認証情報の削除");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//prop.remove("accessToken");
				
				//prop.remove("accessTokenSecret");
				prop.clear();
				try {
					prop.store(new FileOutputStream("minjtwit.properties"), "MinJTwitConf");
				} catch (FileNotFoundException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				}
				//JOptionPane.showMessageDialog(null, "変更は再起動後に適用されます");
			}
		});
		panel_1.add(button);

		list = new JList();
		panel.add(list);
	}

}
