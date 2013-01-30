package net.bobuhiro11.puzzleroad;

import net.bobuhiro11.puzzleroadconsole.Puzzle;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

/*
 * パズル完成時のメッセージです．
 */
public class Dialog {
	Rect src,dst;
	Bitmap bmp;
	MainView mainView;
	
	public Dialog(Context context,MainView mainView,int w,int h){
		this.mainView = mainView;
		this.src = new Rect(0,0,400,200);
		this.dst = new Rect(w/8,h/3,w*7/8,0);
		this.dst.bottom = this.dst.top + this.dst.width()/2;
		
		Resources r = context.getResources();
        bmp = BitmapFactory.decodeResource(r, R.drawable.complete);
	}
	
	public void draw(Canvas canvas){
		canvas.drawBitmap(bmp, src, dst, null);
	}
	
	/**
	 * @param event
	 * @param n　盤面の大きさ
	 */
	public void touch(MotionEvent event,int n){
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			this.mainView.status = Status.playing;
			
			//パズルを初期化
			mainView.playPuzzle.puzzle.init(n+2,1);
			//スタート，ゴールオブジェクト更新
			mainView.startObject.setPoint(mainView.playPuzzle.puzzle.start);
			mainView.goalObject.setPoint(mainView.playPuzzle.puzzle.goal);
		}
	}
}
