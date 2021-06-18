package net.darekasan.minjtwit.main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import net.darekasan.minjtwit.model.ExtendedList;
import net.darekasan.minjtwit.model.ListChangeListener;
import net.darekasan.minjtwit.model.MStatus;
import twitter4j.DirectMessage;
import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;
import twitter4j.auth.AccessToken;

public class TwitCore {
	List<Status> homeStatuses;
	ExtendedList statuses;
	HashMap<String, BufferedImage> images;

	Twitter twitter;
	TwitterStream tstream;
	int statusessize=150;
	int imagessize=50;

	TwitCore(Properties p) {
		p.getProperty("accessKey");
		homeStatuses=new ArrayList<Status>();
		statuses=new ExtendedList();
		images=new HashMap<String, BufferedImage>();
		twitter = new TwitterFactory().getInstance();
		twitter.setOAuthAccessToken(new AccessToken(p.getProperty("accessToken"), p.getProperty("accessTokenSecret")));
		tstream = new TwitterStreamFactory().getInstance();
		tstream.setOAuthAccessToken(new AccessToken(p.getProperty("accessToken"), p.getProperty("accessTokenSecret")));
		tstream.addListener(new uslistner());
		statuses.addListener(new statusListener());
		User myUser;
		try {
			myUser = twitter.verifyCredentials();
			printInfo("Connected:" + myUser.getName() + " (@"
					+ myUser.getScreenName() + ") UID:" + myUser.getId());
			printInfo("Your lastest tweet:" + myUser.getStatus().getText());

		} catch (TwitterException e) {
			printError("アカウント情報の取得に失敗しました。");
			e.printStackTrace();
		}
	}


	public ExtendedList getStatuses(){
		return statuses;
	}

	public BufferedImage getImage(String url){
		BufferedImage bi = images.get(url);
		if(bi==null){
			try {
				bi=ImageIO.read(new URL(url));
				if(images.size()<imagessize){
					images.clear();
				}
			} catch (MalformedURLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		return bi;
	}

	void waitInput() throws TwitterException{
		Scanner scan = new Scanner(System.in);
		while(true){
			System.out.print("\n> ");
			String str=scan.nextLine().trim();
			if(str.equals("")){
				System.out.println("コマンドを入力してください");
			}else if(str.equals("printhome")){
				System.out.println("Statuses:"+statuses.size());
				for (Object o : statuses) {
					MStatus s = (MStatus)o;
					User u = s.getStatus().getUser();

					System.out.println("-----------------------------");

					System.out.print("From:");
					if(s.getIsHome())System.out.print("Home ");
					if(s.getIsOther())System.out.print("Other");
					System.out.println();

					System.out.println(u.getName()+" (@"+u.getScreenName()+")");
					System.out.println(expandUrl(s.getStatus()));
					System.out.println(s.getStatus().getCreatedAt());

				}
				System.out.println("-------------END-------------");
			}else if(str.equals("fetchhome")){
				fetchHome(10);
			}else if(str.equals("connect")){
				connectStream();
			}else if(str.equals("disconnect")){
				disconnectStream();
			}else if(str.equals("flushhome")){
				statuses.clear();
			}else if(str.equals("connectsample")){
				tstream.sample();
			}else if(str.equals("fetchbyid")){
				System.out.print("status_idを指定> ");
				String sid=scan.nextLine().trim();
				statuses.add(new MStatus(twitter.showStatus(Long.parseLong(sid.trim())), true, false));
			}
			else{
				System.out.println("不明なコマンド");
			}

		}
	}

	void printInfo(String s) {
		System.out.println("[INFO]" + s);
	}

	void printError(String s) {
		System.out.println("[ERROR]" + s);
	}

	void fetchHome(int count) throws TwitterException {
		List<Status> rl = twitter.getHomeTimeline(new Paging(1));
		Collections.reverse(rl);
		for (Status status : rl) {
			statuses.add(new MStatus(status, true, false));
		}
	}

	public static String expandUrl(Status s) {
		String text = s.getText();
		URLEntity[] entities = s.getURLEntities();
		for (URLEntity entity : entities) {
			String plainUrl = entity.getURL();
			String exxUrl = entity.getExpandedURL();
			text = Pattern.compile(plainUrl).matcher(text).replaceAll(exxUrl);
		}
		MediaEntity[] mEntities = s.getMediaEntities();
		for (MediaEntity mEntity : mEntities) {
			String plainUrl = mEntity.getURL();
			String exxUrl = mEntity.getExpandedURL();
			text = Pattern.compile(plainUrl).matcher(text).replaceAll(exxUrl);
		}
		return text;
	}

	public static String encodeHtml(Status s) {
		String text = htmlEscape(s.getText());
		URLEntity[] entities = s.getURLEntities();
		for (URLEntity entity : entities) {
			String plainUrl = entity.getURL();
			String exxUrl = entity.getExpandedURL();
			text = Pattern.compile(plainUrl).matcher(text).replaceAll(exxUrl);
		}
		MediaEntity[] mEntities = s.getMediaEntities();
		for (int i=0;i<mEntities.length;i++) {
			MediaEntity mEntity=mEntities[i];
			String plainUrl = mEntity.getURL();
			String exxUrl = "<span color='blue'>"+mEntities[i].getType()+i+"</span>";
			text = Pattern.compile(plainUrl).matcher(text).replaceAll(exxUrl);
		}
		return text;
	}

	public static String htmlEscape(String s) {
		return s.replace("&", "&amp;").replace("\"", "&quot;")
				.replace("<", "&lt;").replace(">", "&gt;")
				.replace("'", "&#39;").replace("\n", "<br>");
	}

	void connectStream() {
		tstream.user();
	}

	void disconnectStream() {
		tstream.shutdown();
	}

	public TwitterStream getTwitterStream(){
		return tstream;
	}

	public Twitter getTwitter(){
		return twitter;
	}

	public int getStatusesSize(){
		return statusessize;
	}

	public void setStatusesSize(int size){
		statusessize=size;
	}



	public MStatus getMStatusFromId(long id) throws TwitterException{
		for (Object o : statuses) {
			MStatus ms = (MStatus)o;

			if(ms.getId()==id)return ms;

		}
		MStatus s=new MStatus(twitter.showStatus(id),false, true);
		statuses.add(s);
		return s;
	}

	public void retweet(long statusId,ErrorListener el){
		try {
			twitter.retweetStatus(statusId);
		} catch (TwitterException e1) {
			el.retweetFailed(statusId, e1);
		}
	}

	public void favorite(long statusId,ErrorListener el){
		try {
			twitter.createFavorite(statusId);
		} catch (TwitterException e1) {
			el.favoriteFailed(statusId, e1);
		}
	}

	public void unFavorite(long statusId,ErrorListener el){
		try {
			twitter.destroyFavorite(statusId);
		} catch (TwitterException e1) {
			el.unFavoriteFailed(statusId, e1);
		}
	}

	public void deleteTweet(long statusId,ErrorListener el){
		try {
			twitter.destroyStatus(statusId);
		} catch (TwitterException e1) {
			el.deleteTweetFailed(statusId, e1);
		}
	}

	class statusListener implements ListChangeListener{

		public void add(Object o) {
			// TODO 自動生成されたメソッド・スタブ
			listLimit();
		}

		public void addAll(Collection c) {
			// TODO 自動生成されたメソッド・スタブ
			listLimit();
		}

	}

	void listLimit(){
		int size=statuses.size();
		for(int i=0;i<size-statusessize;i++){
			statuses.remove(0);
		}
	}

	class uslistner implements UserStreamListener {

		public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onScrubGeo(long userId, long upToStatusId) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onStallWarning(StallWarning warning) {
			// TODO 自動生成されたメソッド・スタブ

		}

		public void onStatus(Status status) {
			// TODO 自動生成されたメソッド・スタブ
			statuses.add(new MStatus(status,true,false));

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
			// TODO 自動生成されたメソッド・スタブ

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
