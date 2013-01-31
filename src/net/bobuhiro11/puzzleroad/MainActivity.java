package net.bobuhiro11.puzzleroad;

import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
	
	private MainView mainView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//タイトルバーを消す
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//ステータスバーを消す
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Intent i = getIntent();
		//Activityをシングルトップにする
		//i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		setVolumeControlStream(AudioManager.STREAM_MUSIC); 
		//Viewをセット
		mainView = new MainView(this);
		setContentView(mainView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		//ゲームカウントを読み込む．
		this.mainView.gameCount.read();
		Log.d("", "read");
		super.onResume();
	}

	@Override
	protected void onPause() {
		//ゲームカウントを書き込む．
		this.mainView.gameCount.save();
		Log.d("", "save");

		super.onPause();
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);
	}

	

}
