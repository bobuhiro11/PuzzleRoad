package net.bobuhiro11.puzzleroad; 


import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class MainView extends SurfaceView implements
SurfaceHolder.Callback, Runnable {

	private SurfaceHolder holder;
	private Thread thread;
	private long interval = 1;
	private Runnable runnable;
	private Handler handler = new Handler();
	
	//パズル本体
	private PlayPuzzle playPuzzle;
	
	//背景画像
	private Bitmap backGround;
	
	//ゴールとスタートのオブジェクト
	private Person goalObject,startObject;
	
	public MainView(Context context) {
		super(context);
		
		//画面サイズ取得
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		int w = disp.getWidth();
		int h = disp.getHeight();
		
		//はじめのゲームのサイズを決定
		int n = 4;
		
        //パズル部の生成
		playPuzzle = new PlayPuzzle(
				context,
				new Rect(w/14,h/3,w*13/14,h*5/6),
				n);
		
        //スタートとゴールのオブジェクト生成
        startObject = new Person(context,playPuzzle.puzzle.start,
        		playPuzzle.rect,n,R.drawable.person);
        goalObject = new Person(context,playPuzzle.puzzle.goal,
        		playPuzzle.rect,n,R.drawable.flag);
        
        //パズル部にスタートとゴールを結びつける
        playPuzzle.startObject = startObject;
        playPuzzle.goalObject = goalObject;
        
		
		Resources r = context.getResources();
        backGround = BitmapFactory.decodeResource(r, R.drawable.background_game);

		// getHolder()メソッドでSurfaceHolderを取得。さらにコールバックを登録
		getHolder().addCallback(this);
		// タイマー処理を開始
		runnable = new Runnable() {
			public void run() {
				TimerEvent();
				handler.postDelayed(this, interval);
			}
		};
		handler.postDelayed(runnable, interval);
	}

	//タイマーイベント(intervalごとに呼ばれる．)
	private void TimerEvent() {
		playPuzzle.timer();
		startObject.timer();
	}

	// SurfaceView生成時に呼び出される
	public void surfaceCreated(SurfaceHolder holder) {
		this.holder = holder;
		thread = new Thread(this);
	}

	// SurfaceView変更時に呼び出される
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// スレッドスタート
		if (thread != null) {
			thread.start();
		}
	}

	// SurfaceView破棄時に呼び出される
	public void surfaceDestroyed(SurfaceHolder holder) {
		thread = null;
	}

	// スレッドによるSurfaceView更新処理
	public void run() {
		while (thread != null) {
			// 更新処理
			playPuzzle.update();
			startObject.update();
			// 描画処理
			Canvas canvas = holder.lockCanvas();
			this.draw(canvas);
			holder.unlockCanvasAndPost(canvas);

		}
	}

	// 描画処理
	@Override
	public void draw(Canvas canvas) {
		if(canvas==null){
			return;
		}
		playPuzzle.draw(canvas);
		canvas.drawBitmap(backGround, new Rect(0,0,700,1200), new Rect(0,0,canvas.getWidth(),canvas.getHeight()), null);
		goalObject.draw(canvas);
		startObject.draw(canvas);
	}

	// タッチイベント
	public boolean onTouchEvent(MotionEvent event) {
		playPuzzle.touch(event);
		return true;
	}
}