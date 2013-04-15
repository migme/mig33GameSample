package com.mig33.gamesample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.mig33.android.sdk.LoginListener;
import com.mig33.android.sdk.Mig33;
import com.mig33.android.sdk.Session;
import com.projectgoth.BServiceHelper;
import com.projectgoth.b.android.RestClient;
import com.projectgoth.b.data.Profile;
import com.projectgoth.b.exception.RestClientException;
import com.projectgoth.b.exception.RestErrorException;

public class MainActivity extends Activity {
	
	private Button mBtnConnectWithMig33;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Mig33.getInstance().initialize(getApplication());

		mBtnConnectWithMig33 = (Button) findViewById(R.id.btn_connect_with_mig33);
		mBtnConnectWithMig33.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Mig33.getInstance().login(MainActivity.this, new LoginListener() {
					
					@Override
					public void onLoginSuccess() {
						final Session session = Session.getInstance();
						
						Toast.makeText(MainActivity.this, "login ok", Toast.LENGTH_LONG).show();
						
						Thread thread = new Thread(new Runnable() {
							
							@Override
							public void run() {
								RestClient restclient = (RestClient) BServiceHelper.getInstance().getRestClient();
								try {
									final Profile profile = restclient.getProfileByUsername(session.getUsername());
									runOnUiThread(new Runnable() {
										
										@Override
										public void run() {
											((App)getApplication()).profile = profile;
											Intent intent = new Intent(MainActivity.this, GameActivity.class);
											startActivity(intent);
											finish();
										}
									});
								} catch (RestErrorException e) {
									e.printStackTrace();
								} catch (RestClientException e) {
									e.printStackTrace();
								}
							}
						});
						thread.start();
					}
					
					@Override
					public void onLoginError() {
						Toast.makeText(MainActivity.this, "login fail", Toast.LENGTH_LONG).show();
					}
				});
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
