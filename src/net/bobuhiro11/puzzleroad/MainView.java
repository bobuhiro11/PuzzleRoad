package net.bobuhiro11.puzzleroad; 


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainView extends SurfaceView implements
SurfaceHolder.Callback, Runnable {

	private SurfaceHolder holder;
	private Thread thread;
	private long interval = 100;
	private Runnable runnable;
	private Handler handler = new Handler();

	private Paint paint;
	private Context context;
	
	public MainView(Context context) {
		super(context);
		
		this.context = context;
		
		//リソースの準備
		paint = new Paint();
		paint.setColor(Color.WHITE);



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

		}
	}
	
	// 更新処理
	private void update(){
		
	}

	// 描画処理
	@Override
	public void draw(Canvas canvas) {
		if(canvas==null){
			return;
		}
		//描画処理
		canvas.drawText("Hello Everyone!", 100, 100, paint);
	}

	// タッチイベント
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}
}