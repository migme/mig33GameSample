package com.mig33.gamesample;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mig33.android.sdk.api.Message;
import com.projectgoth.b.exception.RestClientException;
import com.projectgoth.b.exception.RestErrorException;

public class GameActivity extends Activity {

	private static final int[] IMAGES = new int[] {
		R.drawable.game1,
		R.drawable.game2,
		R.drawable.game3,
		R.drawable.game4,
		R.drawable.game5,
		R.drawable.game6,
		R.drawable.game7,
		R.drawable.game8,
		R.drawable.game9,
	};
	
	private TextView mTextName;
	private ImageView mImageCard;
	private Button mButtonSpin;
	private Button mButtonShare;
	ProgressDialog dialog;
	
	private int idx = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		App app = (App) getApplication();
		mTextName = (TextView) findViewById(R.id.text_name);
		mTextName.setText("Hi " + app.profile.getUsername() + " (" + app.profile.getName() + ")");
		
		mImageCard = (ImageView) findViewById(R.id.image_card);
		mImageCard.setImageResource(IMAGES[idx]);
		
		mButtonSpin = (Button) findViewById(R.id.btn_spin);
		mButtonSpin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				spin();
			}
		});
		
		mButtonShare = (Button) findViewById(R.id.btn_share);
		mButtonShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				share();
			}
		});
	}

	private void spin() {
		idx++;
		if (idx >= IMAGES.length) idx = 0;
		mImageCard.setImageResource(IMAGES[idx]);
	}
	
	private void share() {
		dialog = new ProgressDialog(this);
		dialog.setMessage(getResources().getText(R.string.please_wait));
		dialog.show();
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				boolean error = true;
				try {
					Message.createPost("I love this game!", getBinaryData(IMAGES[idx]), "image/jpeg", System.currentTimeMillis() + ".jpg", false, false);
					
					error = false;
				} catch (RestErrorException e) {
					e.printStackTrace();
				} catch (RestClientException e) {
					e.printStackTrace();
				}
				
				final boolean isError = error;
				runOnUiThread(new Runnable() {
					public void run() {
						dialog.dismiss();
						if (isError) {
							Toast.makeText(GameActivity.this, "Unable to post!", Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(GameActivity.this, "Post success!", Toast.LENGTH_LONG).show();
						}
					}
				});
			}
		});
		thread.start();
	}
	
	private byte[] getBinaryData(int resId) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
		return stream.toByteArray();
	}
}
