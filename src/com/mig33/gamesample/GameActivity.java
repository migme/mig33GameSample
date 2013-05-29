package com.mig33.gamesample;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mig33.android.sdk.Mig33;
import com.mig33.android.sdk.api.ActivityAPI;
import com.mig33.android.sdk.api.ActivityAPI.ActivityListener;
import com.mig33.android.sdk.api.Payment;
import com.mig33.android.sdk.api.Payment.PaymentListener;
import com.mig33.android.sdk.api.People;
import com.mig33.android.sdk.api.People.PeopleListener;
import com.mig33.android.sdk.common.Tools;
import com.mig33.android.sdk.model.UserInfo;
import com.mig33.gamesample.model.GoldItem;

public class GameActivity extends Activity {
	
	private static final String			TAG			= "GameActivity";
	
	// private static final int[] IMAGES = new int[] { R.drawable.game1,
	// R.drawable.game2,
	// R.drawable.game3, R.drawable.game4, R.drawable.game5, R.drawable.game6,
	// R.drawable.game7, R.drawable.game8, R.drawable.game9 };
	
	private TextView					mTextName;
	private TextView					mTextCountry;
	private TextView					mTextStatus;
	private TextView					mTextCoins;
	private TextView					mTextGoldItem;
	private ImageView					mImageCard;
	private Button						mButtonSpin;
	private Button						mButtonShare;
	private Button						mButtonBuy;
	
	ProgressDialog						dialog;
	
	private int							idx			= 0;
	
	private static final GoldItem[]		GOLD_ITEMS	= { new GoldItem(1, 100), new GoldItem(2, 250),
			new GoldItem(3, 400)					};
	
	private HashMap<String, GoldItem>	goldItemRef;
	
	private App							app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		app = (App) getApplication();
		mTextName = (TextView) findViewById(R.id.text_name);
		mTextCoins = (TextView) findViewById(R.id.text_coins);
		mTextCountry = (TextView) findViewById(R.id.text_country);
		mTextStatus = (TextView) findViewById(R.id.text_status);
		mTextGoldItem = (TextView) findViewById(R.id.gold_item_value);
		mTextCoins.setTextColor(0xFFFFD700);
		
		goldItemRef = new HashMap<String, GoldItem>();
		
		People.getInstance().addListener(new PeopleListener() {
			public void onNewDataAvailable(String userId, String groupId) {
				Tools.log(TAG, "onNewDataAvailable: " + userId + " " + groupId);
				updateData();
			}
		});
		
		Payment.getInstance().addListener(new PaymentListener() {
			public void onPaymentSucceeded(String reference) {
				Tools.log(TAG, "onPaymentSucceeded: " + reference);
				GoldItem item = goldItemRef.get(reference);
				Mig33.getInstance().showOkDialog(
						reference + " - payment received for: " + item.getDescription(),
						GameActivity.this);
				app.addGoldCoins(item.getAmount());
				mTextCoins.setText(app.getGoldCoinStr());
			}
			
			public void onPaymentError(String reference) {
				Tools.log(TAG, "onPaymentError: " + reference);
				Mig33.getInstance().showOkDialog("payment error for: " + reference,
						GameActivity.this);
			}
			
			public void onPaymentCanceled(String reference) {
				Tools.log(TAG, "onPaymentCanceled: " + reference);
				Mig33.getInstance().showOkDialog("payment canceled for: " + reference,
						GameActivity.this);
			}
		});
		
		ActivityAPI.getInstance().addListener(new ActivityListener() {
			public void onPostSent(String id) {
				Mig33.getInstance().showOkDialog("post sent: " + id, GameActivity.this);
			}
			
			public void onPostFailed(String id) {
				Mig33.getInstance().showOkDialog("post failed: " + id, GameActivity.this);
			}
		});
		
		mImageCard = (ImageView) findViewById(R.id.image_card);
		mImageCard.setImageResource(R.drawable.chest_of_gold);
		mTextGoldItem.setText(GOLD_ITEMS[idx].getDescription());
		// mImageCard.setImageResource(IMAGES[idx]);
		
		mButtonSpin = (Button) findViewById(R.id.btn_spin);
		mButtonSpin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				spin();
			}
		});
		
		mButtonShare = (Button) findViewById(R.id.btn_share);
		mButtonShare.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				share();
			}
		});
		
		mButtonBuy = (Button) findViewById(R.id.btn_buy);
		mButtonBuy.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				buy();
			}
		});
		
		updateData();
	}
	
	private void updateData() {
		UserInfo info = People.getInstance().getMyInfo();
		app.setMyInfo(info);
		if (info != null) {
			Tools.log(TAG, "updateData: " + info.toString());
			mTextName.setText("Hi " + info.getDisplayName() + " (" + info.getUsername() + ") id: "
					+ info.getId());
			mTextCountry.setText("Country: " + info.getCountry());
			mTextStatus.setText("Status Message (" + info.getStatusMessage() + ")");
		}
		mTextCoins.setText(app.getGoldCoinStr());
	}
	
	private void buy() {
		Mig33.getInstance().showToast(this, "Payment not yet available");
		String reference = Payment.getInstance().buy(this, GOLD_ITEMS[idx].getDescription(),
				GOLD_ITEMS[idx].getPrice());
		Tools.log(TAG, "buy: " + reference);
		
		goldItemRef.put(reference, GOLD_ITEMS[idx]);
	}
	
	private void spin() {
		People.getInstance().getMyInfo();
		People.getInstance().getMyFriendsInfo();
		
		idx++;
		if (idx >= GOLD_ITEMS.length)
			idx = 0;
		mTextGoldItem.setText(GOLD_ITEMS[idx].getDescription());
		// mImageCard.setImageResource(IMAGES[idx]);
	}
	
	private void share() {
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Share");
		alert.setMessage("Enter the message you want to share.");
		
		final EditText input = new EditText(this);
		alert.setView(input);
		
		alert.setPositiveButton("Post", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Editable value = input.getText();
				ActivityAPI.getInstance().post(value.toString(), null);
			}
		});
		
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		try {
			alert.show();
		} catch (Exception e) {
			Tools.log(e);
		}
		
	}
}
