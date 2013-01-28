package net.bobuhiro11.puzzleroad; 


import java.util.Random;

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
	//SurfaceViewのfps
	static final long FPS = 20;
	static final long FRAME_TIME = 1000 / FPS;
	
	//Timerイベントの感覚
	private long interval = 1;
	private Runnable runnable;
	private Handler handler = new Handler();
	private Context context;
	
	//FPSカウンター
	Fps fps;
	
	//パズル本体
	public PlayPuzzle playPuzzle;
	
	//背景画像
	private Bitmap backGround;
	private Rect backGroundSrc,backGroundDst;
	
	//ゴールとスタートのオブジェクト
	public Person goalObject,startObject;
	
	//ゲームのサイズを決定
	private int n = 4;
	
	/**
	 * 現在のゲームの状態
	 */
	public Status status;
	
	public MainView(Context context) {
		super(context);
		
		this.context = context;
		
		fps = new Fps();
		
		//画面サイズ取得
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		int w = disp.getWidth();
		int h = disp.getHeight();
		this.backGroundSrc = new Rect(0,0,700,1200);
		this.backGroundDst = new Rect(0,0,w,h);
		
		
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
        
		
		Resources r = context.getResources();
        backGround = BitmapFactory.decodeResource(r, R.drawable.background_game);
        
        status=Status.playing;

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
		fps.update();
		if(status==Status.playing){
			playPuzzle.update();
		}else if(status==Status.personMovin){
			startObject.update();
		}
	}
	
	//タイマーイベント(intervalごとに呼ばれる．)
	private void TimerEvent() {
		if(status==Status.dialog){
			status = Status.playing;
			new AlertDialog.Builder(context)
			.setTitle("Complete!!")
			.setMessage("")
			.setPositiveButton("OK", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//パズルを初期化
					playPuzzle.puzzle = new Puzzle(n+2,1);
					//スタート，ゴールオブジェクト更新
					startObject.setPoint(playPuzzle.puzzle.start);
					goalObject.setPoint(playPuzzle.puzzle.goal);
				}
			})
			.show();
		}
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
		long loopCount = 0;
		long waitTime = 0;
		long startTime = System.currentTimeMillis();

		while (thread != null) {
			try{
				// 更新処理
				update();
				// 描画処理
				Canvas canvas = holder.lockCanvas();
				this.draw(canvas);
				holder.unlockCanvasAndPost(canvas);

				// FPSを制限する．
				// http://android.keicode.com/basics/surfaceview-1.phpより
				loopCount++;
				waitTime = (loopCount * FRAME_TIME) 
						- System.currentTimeMillis() - startTime;
				if( waitTime > 0 ){
					Thread.sleep(waitTime);
				}	
			}catch(Exception e){}

		}

	}

	// 描画処理
	@Override
	public void draw(Canvas canvas) {
		if(canvas==null){
			return;
		}
		playPuzzle.draw(canvas);
		canvas.drawBitmap(backGround,this.backGroundSrc,this.backGroundDst, null);
		goalObject.draw(canvas);
		startObject.draw(canvas);
		fps.onDraw(canvas);
	}

	// タッチイベント
	public boolean onTouchEvent(MotionEvent event) {
		if(status==Status.playing){
			playPuzzle.touch(event);
		}
		return true;
	}
}