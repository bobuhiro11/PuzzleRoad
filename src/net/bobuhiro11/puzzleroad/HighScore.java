package net.bobuhiro11.puzzleroad;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * ハイスコア
 *
 */
public class HighScore {
	private int score;
	private Context context;
	
	public HighScore(Context context){
		this.context = context;
		score = 1;
	}
	
	/**
	 * ハイスコアをリセットする．
	 * @return
	 */
	public void reset(){
		this.score = 1;
	}
	
	/**
	 * @return 現在のハイスコア取得
	 */
	public int get(){
		return score;
	}
	/**
	 * ハイスコアをセット
	 */
	public void set(int score){
		this.score = score;
	}
	
	/**
	 * ゲーム数を保存する．
	 */
	public void save(){
        SharedPreferences pref = 
                context.getSharedPreferences( "gc", Context.MODE_PRIVATE );
        Editor editor = pref.edit();
        editor.putInt("score", score);
        editor.commit();
	}
	
	/**
	 * ゲーム数を読み込む．
	 */
	public void read(){
        SharedPreferences pref = 
                context.getSharedPreferences( "gc", Context.MODE_PRIVATE );
        score = pref.getInt("score", 1);
	}
}