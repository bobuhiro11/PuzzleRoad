package net.bobuhiro11.puzzleroad;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * 何ゲーム目か
 *
 */
public class GameCount {
	private int count;
	private Paint paint;
	
	public GameCount(){
		count = 1;
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(50);
	}
	
	/**
	 * @return 現在のゲーム数取得
	 */
	public int get(){
		return count;
	}
	/**
	 * ゲーム数をカウントアップ
	 */
	public void up(){
		this.count++;
	}
	
	/**
	 * ゲーム数描画
	 * @param canvas
	 */
	public void draw(Canvas canvas){
		canvas.drawText(String.valueOf(count)+"ゲーム目",0, 100, paint);
	}

}
