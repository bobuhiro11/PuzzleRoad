package net.bobuhiro11.puzzleroad; 


import java.util.Random;
import java.util.concurrent.TimeUnit;

import net.bobuhiro11.puzzleroadconsole.Puzzle;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
	
	//Timerイベントの感覚
	private long interval = 1;
	private Runnable runnable;
	private Handler handler = new Handler();
	private Context context;
	
	//FPS固定装置
	FPSManager fPSManager;
	
	//パズル本体
	public PlayPuzzle playPuzzle;
	
	//ゲーム中の背景画像
	private Bitmap backGround;
	private Rect backGroundSrc,backGroundDst;
	
	//ゴールとスタートのオブジェクト
	public Person goalObject,startObject;
	
	//ダイアログ
	private Dialog dialog;
	
	//タイトル
	private Bitmap title;
	
	//ゲームのサイズを決定
	private final int n = 4;
	
	//ゲームカウント
	public GameCount gameCount;
	
	
	/**
	 * 現在のゲームの状態
	 */
	public Status status;
	
	public MainView(Context context) {
		super(context);
		
		this.context = context;
		this.gameCount  = new GameCount(context);
		//this.gameCount.read();
		
		fPSManager = new FPSManager(30);
		
		//画面サイズ取得
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		int w = disp.getWidth();
		int h = disp.getHeight();
		this.backGroundSrc = new Rect(0,0,700,1200);
		this.backGroundDst = new Rect(0,0,w,h);
		
        //ダイアログ生成
        dialog = new Dialog(context,this,w,h);
		
        //パズル部の生成
		playPuzzle = new PlayPuzzle(
				context,this,
				new Rect(w/14,h/3,w*13/14,h*5/6),
				n);
		
        //スタートとゴールのオブジェクト生成
        startObject = new Person(context,playPuzzle.puzzle.start,
        		playPuzzle.rect,n,R.drawable.person,this);
        goalObject = new Person(context,playPuzzle.puzzle.goal,
        		playPuzzle.rect,n,R.drawable.flag,this);
        
        //パズル部にスタートとゴールを結びつける
        playPuzzle.startObject = startObject;
        playPuzzle.goalObject = goalObject;
        
        //背景
		Resources r = context.getResources();
        backGround = BitmapFactory.decodeResource(r, R.drawable.background_game);
		
        //タイトル
        title = BitmapFactory.decodeResource(r, R.drawable.title);
        	
        status=Status.title;

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

	//更新処理
	private void update(){
		if(status==Status.playing){
			//パズル中
			playPuzzle.update();
		}else if(status==Status.personMovin){
			//パズル後のアニメーション中
			startObject.update();
		}
	}
	
	//タイマーイベント(intervalごとに呼ばれる．)
	private void TimerEvent() {
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
			update();
			// 描画処理
			Canvas canvas = holder.lockCanvas();
			this.draw(canvas);
			holder.unlockCanvasAndPost(canvas);
			//FPS固定
			try {
				TimeUnit.NANOSECONDS.sleep(fPSManager.state());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// 描画処理
	@Override
	public void draw(Canvas canvas) {
		if(canvas==null){
			return;
		}
		if(status==Status.title){
			canvas.drawBitmap(title, this.backGroundSrc, this.backGroundDst, null);
			return;
		}

		playPuzzle.draw(canvas);
		canvas.drawBitmap(backGround,this.backGroundSrc,this.backGroundDst, null);
		goalObject.draw(canvas);
		startObject.draw(canvas);
		if(status==Status.dialog){
			dialog.draw(canvas);
		}
		gameCount.draw(canvas);
		fPSManager.draw(canvas);
	}

	// タッチイベント
	public boolean onTouchEvent(MotionEvent event) {
		if(status==Status.playing){
			playPuzzle.touch(event);
		}else if(status==Status.dialog){
			dialog.touch(event,n);
		}else if(status==Status.title){
			this.status = Status.playing;
		}
		return true;
	}
}