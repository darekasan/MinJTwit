package net.darekasan.minjtwit.main;
import java.awt.EventQueue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import net.darekasan.minjtwit.view.AuthWindow;
import net.darekasan.minjtwit.view.MainWindow;
import twitter4j.TwitterException;

public class Main {

	private MainWindow mainwindow;
	TwitCore core;
	Properties properties;

	public Main(String[] args) {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("minjtwit.properties"));
		} catch (FileNotFoundException e1) {
			try {
				System.out.println("config file is not found!");
				properties.store(new FileOutputStream("minjtwit.properties"), "MinJTwitConf");
			} catch (FileNotFoundException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
			if(!properties.containsKey("accessToken")){
				AuthWindow awin = new AuthWindow(properties);
				awin.show();
				try {
					properties.store(new FileOutputStream("minjtwit.properties"), "MinJTwitConf");
				} catch (FileNotFoundException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				}
			}

			core=new TwitCore(properties);
			showMainWindow();
			try {
				core.connectStream();
				core.waitInput();

			} catch (TwitterException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
	}

	void showMainWindow(){
		EventQueue.invokeLater(new Runnable() {


			public void run() {
				try {
					mainwindow = new MainWindow(core,properties);
					mainwindow.setVisible(true);
					mainwindow.startTimer();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO 自動生成されたメソッド・スタブ
		Main m = new Main(args);

	}

}
