package net.bobuhiro11.puzzleroad;

import java.util.concurrent.TimeUnit;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * FPSの表示・調整するクラス
 */
public class FPSManager {
	// 1秒（ナノ秒単位）
	private static long ONE_SEC_TO_NANO = TimeUnit.SECONDS.toNanos(1L); 

	// 1ミリ秒（ナノ秒単位）
	private static long ONE_MILLI_TO_NANO = TimeUnit.MILLISECONDS.toNanos(1L); 

	private int maxFps;
	private int[] fpsBuffer;
	private int fpsCnt;
	private long startTime;
	private long elapsedTime;
	private long sleepTime;
	private long oneCycle; 
	
	private final int FONT_SIZE = 20;
	private Paint paint;

	/**
	 * コンストラクタ
	 * @param fps 動作FPS
	 */
	public FPSManager(int fps) {
		maxFps = fps;
		fpsBuffer = new int[maxFps];
		fpsCnt = 0;
		startTime = System.nanoTime();
		oneCycle = (long)(Math.floor((double)ONE_SEC_TO_NANO / maxFps));
		
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(FONT_SIZE);
	} 

	/**
	 * 状態更新
	 * @return sleepする時間（ナノ秒）
	 */
	public long state() {
		fpsCnt++;
		if (maxFps <= fpsCnt) {
			fpsCnt = 0;
		} 

		elapsedTime = System.nanoTime() - startTime;
		sleepTime = oneCycle - elapsedTime; 

		// 余裕が無い場合でも1ミリ秒はsleepさせる
		if (sleepTime < ONE_MILLI_TO_NANO) {
			sleepTime = ONE_MILLI_TO_NANO;
		} 

		int fps = (int)(ONE_SEC_TO_NANO / (elapsedTime + sleepTime));
		fpsBuffer[fpsCnt] = fps; 

		startTime = System.nanoTime() + sleepTime; 

		return sleepTime;
	} 

	/**
	 * FPSの取得
	 * @return 現在のFPS
	 */
	public double getFps() {
		int allFps = 0;
		for (int i = 0; i < maxFps; i++) {
			allFps += fpsBuffer[i];
		} 

		return allFps / (double)maxFps;
	}
	
	public void draw(Canvas canvas){
            canvas.drawText(String.format("%.1f", getFps()), 0, FONT_SIZE-2, paint);
	}
}