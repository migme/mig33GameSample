package com.mig33.gamesample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mig33.android.sdk.LoginListener;
import com.mig33.android.sdk.Mig33;
import com.mig33.android.sdk.api.People;
import com.mig33.android.sdk.common.Tools;
import com.mig33.android.sdk.model.UserInfo;

public class MainActivity extends Activity {
	
	private static final String	TAG		= "MainActivity";
	
	private Button				mBtnConnectWithMig33;
	
	private App					app;
	
	private Mig33				mig33	= Mig33.getInstance();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		app = (App) getApplication();
		
		mBtnConnectWithMig33 = (Button) findViewById(R.id.btn_connect_with_mig33);
		mig33.setMainActivity(MainActivity.class);
	}
	
	public void onResume() {
		super.onResume();
		
		if (mig33.isAuthorized()) {
			mBtnConnectWithMig33.setText("Start");
			if (People.getInstance().getMyInfo() != null) {
				UserInfo myInfo = People.getInstance().getMyInfo();
				app.setMyInfo(myInfo);
			}
		} else {
			mBtnConnectWithMig33.setText("login with mig33");
		}
		
		mBtnConnectWithMig33 = (Button) findViewById(R.id.btn_connect_with_mig33);
		mBtnConnectWithMig33.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mig33.isAuthorized()) {
					startGameActivity();
				} else {
					startLoginProcess();
				}
			}
		});
		
		mig33.setLoginListener(new LoginListener() {
			public void onLoginSuccess() {
				Tools.log(TAG, "onLoginSuccess");
				startGameActivity();
				mig33.showToast(MainActivity.this, "Login Successful");
			}
			
			public void onLoginError() {
				Tools.log(TAG, "onLoginError");
				mig33.showToast(MainActivity.this, "Login Failed");
			}
		});
	}
	
	private void startGameActivity() {
		Tools.log(TAG, "Starting Game Activity");
		Intent intent = new Intent(this, GameActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}
	
	private void startLoginProcess() {
		Tools.log(TAG, "Starting Login process");
		mig33.login(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
