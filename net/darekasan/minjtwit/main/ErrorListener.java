package net.darekasan.minjtwit.main;

import java.awt.event.ActionListener;

import twitter4j.TwitterException;

public interface ErrorListener extends ActionListener {
	public void favoriteFailed(long id,TwitterException e);
	public void unFavoriteFailed(long id,TwitterException e);
	public void retweetFailed(long id,TwitterException e);
	public void tweetFailed(String text,TwitterException e);
	public void deleteTweetFailed(long id,TwitterException e);

}
