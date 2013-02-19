package net.bobuhiro11.puzzleroad;

import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private MainView mainView;
	private final int MENU_SELECT_RESET=1,MENU_SELECT_TWEET=2;

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
		// getMenuInflater().inflate(R.menu.activity_main, menu);
		menu.add(0, MENU_SELECT_RESET, 0, "リセット");
		menu.add(0, MENU_SELECT_TWEET, 0, "つぶやく");
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
		//これがなくなるとポーズでは消えなくなる．
		//finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case MENU_SELECT_RESET:
	    	this.mainView.gameCount.reset();
	    	Toast.makeText(this, "リセットされました．",Toast.LENGTH_SHORT).show();
	        return true;
	    case MENU_SELECT_TWEET:
	    	Intent intent = new Intent(android.content.Intent.ACTION_SEND);
	        intent.setType("text/plain");
	        String content = "ぼくのぱずうぇいはもう「"
	        		+String.valueOf(this.mainView.gameCount.get())
	        		+"ゲーム目」だぜ！！！！ #pazway https://play.google.com/store/apps/developer?id=bobuhiro11";
	        intent.putExtra(Intent.EXTRA_TEXT, content);
	        startActivity(Intent.createChooser(
	                intent, "Twitterを選択してください．"));
	    }
	    return false;
	}	

}
