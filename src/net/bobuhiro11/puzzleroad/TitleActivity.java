package net.bobuhiro11.puzzleroad;

import net.bobuhiro11.puzzleroad.R.id;
import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class TitleActivity extends Activity implements OnClickListener {
	
	private ImageView imageView;

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
		setContentView(R.layout.activity_title);
	
		imageView  = (ImageView)findViewById(id.imageView1);
		imageView.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_title, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		Toast.makeText(this, "ようこそ",Toast.LENGTH_SHORT).show();
		
		//セレクターへ画面遷移
		Intent intent = new Intent(TitleActivity.this,MainActivity.class);
		intent.setAction(Intent.ACTION_VIEW);
		startActivity(intent);
	}

}
