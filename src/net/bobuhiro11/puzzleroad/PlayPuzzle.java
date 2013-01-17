package net.bobuhiro11.puzzleroad;

import net.bobuhiro11.puzzleroadconsole.*;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

public class PlayPuzzle {

	private Paint paint;
	private Context context;
	
	//スライドの感度,低いほうがよい．
	private int sensitivity=50;	
	private int oldX=-1,oldY=-1;
	
	private Puzzle puzzle;
	
	private Bitmap[] a;
	private Bitmap[] b;
	
	private Rect rect;
	private int n;
	
	/**
	 * パズルをするためのViewの一部
	 * @param context コンテキスト
	 * @param rect 描画サイズ
	 * @param n　nマス×n増す
	 */
	public PlayPuzzle(Context context,Rect rect,int n){
		paint = new Paint();
		paint.setColor(Color.BLACK);
		
		puzzle = new Puzzle(new Point(n+2,n+2),new Point(0,1),new Point(n+1,n));
		this.rect = rect;
		this.n = n;
		
		Resources r = context.getResources();
		a = new Bitmap[7];
        a[0] = BitmapFactory.decodeResource(r, R.drawable.a0);
        a[1] = BitmapFactory.decodeResource(r, R.drawable.a1);
        a[2] = BitmapFactory.decodeResource(r, R.drawable.a2);
        a[3] = BitmapFactory.decodeResource(r, R.drawable.a3);
        a[4] = BitmapFactory.decodeResource(r, R.drawable.a4);
        a[5] = BitmapFactory.decodeResource(r, R.drawable.a5);
        a[6] = BitmapFactory.decodeResource(r, R.drawable.aorigin);
		b = new Bitmap[7];
        b[0] = BitmapFactory.decodeResource(r, R.drawable.b0);
        b[1] = BitmapFactory.decodeResource(r, R.drawable.b1);
        b[2] = BitmapFactory.decodeResource(r, R.drawable.b2);
        b[3] = BitmapFactory.decodeResource(r, R.drawable.b3);
        b[4] = BitmapFactory.decodeResource(r, R.drawable.b4);
        b[5] = BitmapFactory.decodeResource(r, R.drawable.b5);
        b[6] = BitmapFactory.decodeResource(r, R.drawable.borigin);
        
	}
	
	public void update(){
		
	}
	
	public void draw(Canvas canvas){
		canvas.drawRect(rect, paint);
		
		int w = rect.width()/n;
		int h = rect.height()/n;
		Rect src = new Rect(0,0,140,140);
		Cell[][] cells = puzzle.cells;
		boolean[][] ans = puzzle.checkAnswer();
		
		for(int x=0;x<n;x++){
			for(int y=0;y<n;y++){
				Rect dst = new Rect(
						rect.left + w*x,
						rect.top  + h*y,
						rect.left + w*(x+1),
						rect.top+ h*(y+1));
				if(ans[x+1][y+1])
					canvas.drawBitmap(a[cells[x+1][y+1].toInt()],src, dst, paint);
				else
					canvas.drawBitmap(b[cells[x+1][y+1].toInt()],src, dst, paint);
					
			}
		}
	}
	
	/**
	 * 何番目の行かを調べる．
	 * @param oldy
	 * @param newy
	 * @return 何行か０以上，ただし，どの行でもないときは-1を返す．
	 */
	private int checkRaw(int oldy,int newy){
		int h = rect.height()/n;
		//横
		for(int y=0;y<n;y++)
			if( rect.top + h*y<oldy && oldy<rect.top + h*(y+1) &&
				rect.top + h*y<newy && newy<rect.top + h*(y+1)	)
				return y;
		return -1;
	}
	
	/**
	 * 何番目の列かを調べる．
	 * @param oldx
	 * @param newx
	 * @return 何列か０以上，ただし，どの列でもないときは-1を返す．
	 */
	private int checkColumn(int oldx,int newx){
		int w = rect.width()/n;
		//縦
		for(int x=0;x<n;x++)
			if( rect.left + w*x<oldx && oldx<rect.left + w*(x+1) &&
				rect.left + w*x<newx && newx<rect.left + w*(x+1)	)
				return x;
		return -1;
	}

	public void touch(MotionEvent event){
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			oldX = (int) event.getX();
			oldY = (int) event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if(oldX!=-1 && oldY!=-1){
				int raw = this.checkRaw(oldY, (int)event.getY());
				int column = this.checkColumn(oldX, (int)event.getX());
				int dx = (int)event.getX() - oldX;
				int dy = (int)event.getY() - oldY;
				if(dx > sensitivity && raw!=-1){
					//右
					Log.d("TouchEvent", "right"+raw);
					oldX=-1;
					oldY=-1;
					//debug
					puzzle.move(raw+1, Direction.right);
				}else if(dx < -sensitivity && raw!=-1){
					//左
					Log.d("TouchEvent", "left"+raw);
					oldX=-1;
					oldY=-1;
					//debug
					puzzle.move(raw+1, Direction.left);
				}else if(dy > sensitivity && column!=-1){
					//下
					Log.d("TouchEvent", "down"+column);
					oldX=-1;
					oldY=-1;
					//debug
					puzzle.move(column+1, Direction.down);
				}else if(dy < -sensitivity && column!=-1){
					//上
					Log.d("TouchEvent", "up"+column);
					oldX=-1;
					oldY=-1;
					//debug
					puzzle.move(column+1, Direction.up);
				}
			}
			break;
		}

	}
}
