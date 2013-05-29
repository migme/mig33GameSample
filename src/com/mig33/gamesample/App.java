package com.mig33.gamesample;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.mig33.android.sdk.Mig33;
import com.mig33.android.sdk.common.Tools;
import com.mig33.android.sdk.model.UserInfo;

public class App extends Application {
	
	private static final String	TAG				= "App";
	
	// private static final String CONSUMER_KEY =
	// "<Put your consumer key here>";
	//
	// private static final String CONSUMER_SECRET =
	// "<Put your consumer secret here>";
	//
	// private static final String APP_NAME = "<Put your App Name Here>";
	
	private static final String	CONSUMER_KEY	= "4e9618d89bcc5714b47705a85fe7b2f005191f8d5";
	
	private static final String	CONSUMER_SECRET	= "17f3d4b255a7acd4a87ddf364b94ccf9";
	
	private static final String	APP_NAME		= "sampleapp";
	
	private static final String	PREFS			= "SampleGamePrefs";
	
	private static final String	GOLD_COINS		= "GOLD_COINS";
	
	private SharedPreferences	preferences;
	
	private UserInfo			myInfo;
	
	public void onCreate() {
		super.onCreate();
		preferences = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		Mig33.getInstance().initialize(this);
		Mig33.getInstance().setConsumerDetails(CONSUMER_KEY, CONSUMER_SECRET, APP_NAME);
	}
	
	/**
	 * @return the myInfo
	 */
	public UserInfo getMyInfo() {
		return myInfo;
	}
	
	/**
	 * @param myInfo
	 *            the myInfo to set
	 */
	public void setMyInfo(UserInfo myInfo) {
		this.myInfo = myInfo;
	}
	
	/**
	 * @return the goldCoins
	 */
	public int getGoldCoins() {
		return preferences.getInt(GOLD_COINS, 100);
	}
	
	/**
	 * @param goldCoins
	 *            the goldCoins to set
	 */
	public void setGoldCoins(int goldCoins) {
		Tools.log(TAG, "setting Gold Coins");
		if (goldCoins >= 0) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putInt(GOLD_COINS, goldCoins);
			editor.commit();
			Tools.log(TAG, "Gold Coins set to " + goldCoins);
		}
	}
	
	/**
	 * @param goldCoins
	 *            the goldCoins to set
	 */
	public void addGoldCoins(int goldCoins) {
		Tools.log(TAG, "adding Gold Coins : " + goldCoins);
		if (goldCoins > 0) {
			int coins = goldCoins + getGoldCoins();
			setGoldCoins(coins);
			Tools.log(TAG, "your Gold Coins are now " + coins);
		}
	}
	
	public String getGoldCoinStr() {
		int coins = getGoldCoins();
		return coins + " Gold Coins";
	}
}
