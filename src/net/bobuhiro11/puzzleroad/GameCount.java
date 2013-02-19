package net.bobuhiro11.puzzleroad;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
	private Context context;
	private HighScore highScore;
	
	public GameCount(Context context){
		this.context = context;
		this.highScore = new HighScore(context);
		count = 1;
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(50);
	}
	
	/**
	 * ゲーム数をリセットする．
	 * @return
	 */
	public void reset(){
		this.count = 1;
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
		//ハイスコア更新
		if(this.count > this.highScore.get())
			this.highScore.set(count);
	}
	
	/**
	 * ゲーム数を保存する．
	 */
	public void save(){
        SharedPreferences pref = 
                context.getSharedPreferences( "gc", Context.MODE_PRIVATE );
        Editor editor = pref.edit();
        editor.putInt("gc", count);
        editor.commit();
        
        
        //ハイスコアも保存
        this.highScore.save();
	}
	
	/**
	 * ゲーム数を読み込む．
	 */
	public void read(){
        SharedPreferences pref = 
                context.getSharedPreferences( "gc", Context.MODE_PRIVATE );
        count = pref.getInt("gc", 1);
        
        //ハイスコアも読み込む．
        this.highScore.read();
	}
	
	/**
	 * ゲーム数描画
	 * @param canvas
	 */
	public void draw(Canvas canvas){
		canvas.drawText(
				String.valueOf(count)+"ゲーム目(ハイスコア : "+highScore.get()+")"
				,0, 100, paint);
	}

}
