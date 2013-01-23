package net.bobuhiro11.puzzleroad;

import java.util.Random;

import net.bobuhiro11.puzzleroadconsole.*;
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
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

public class  PlayPuzzle{

	private Paint paint;
	private Context context;
	
	//スライドの感度,低いほうがよい．
	private int sensitivity=50;	
	private int oldX=-1,oldY=-1;
	
	private Puzzle puzzle;
	
	private Bitmap[] a;
	private Bitmap[] b;
	
	//描画領域
	private Rect rect;
	//n×nマス
	private int n;
	
	//**移動アニメーション
	//移動する列，行
	private int ani_rawColumn;
	//移動する方向
	private Direction ani_direction;
	//移動量 -1のときは動いていない
	private int ani_moving=-1;
	//一ミリ秒間で移動量
	private int ani_moving_per_time;
	
	/**
	 * パズルをするためのViewの一部
	 * @param context コンテキスト
	 * @param rect 描画サイズ
	 * @param n　nマス×nマス
	 */
	public PlayPuzzle(Context context,Rect rect,int n){
		this.context = context;
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
		
		puzzle = new Puzzle(n+2,1);
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
        a[6] = BitmapFactory.decodeResource(r, R.drawable.a6);
		b = new Bitmap[7];
        b[0] = BitmapFactory.decodeResource(r, R.drawable.b0);
        b[1] = BitmapFactory.decodeResource(r, R.drawable.b1);
        b[2] = BitmapFactory.decodeResource(r, R.drawable.b2);
        b[3] = BitmapFactory.decodeResource(r, R.drawable.b3);
        b[4] = BitmapFactory.decodeResource(r, R.drawable.b4);
        b[5] = BitmapFactory.decodeResource(r, R.drawable.b5);
        b[6] = BitmapFactory.decodeResource(r, R.drawable.b6);
        
        this.ani_moving_per_time = rect.width() / 100;
	}
	
	public void update(){
		
	}
	
	public void draw(Canvas canvas){
		canvas.drawRect(rect, paint);
		
		int w = rect.width()/n;
		int h = rect.height()/n;
		Rect src = new Rect(0,0,140,140);
		Cell[][] cells = puzzle.cells;
		boolean[][] ans = puzzle.checkAnswerStart();
		
		//アニメーション(実際はないマスだけどアニメーションには必要)
		if(ani_moving!=-1){
			if(ani_direction==Direction.down){
				Rect dst = new Rect(
						rect.left +ani_rawColumn*w,
						rect.top  -h+ani_moving,
						rect.left +(ani_rawColumn+1)*w,
						rect.top +ani_moving );
				if(ans[ani_rawColumn+1][n])
					canvas.drawBitmap(a[cells[ani_rawColumn+1][n].toInt()],src, dst, paint);
				else
					canvas.drawBitmap(b[cells[ani_rawColumn+1][n].toInt()],src, dst, paint);
			}else if(ani_direction==Direction.up){
				Rect dst = new Rect(
						rect.left + ani_rawColumn*w,
						rect.bottom -ani_moving,
						rect.left + (ani_rawColumn+1)*w,
						rect.bottom +h -ani_moving );
				if(ans[ani_rawColumn+1][1])
					canvas.drawBitmap(a[cells[ani_rawColumn+1][1].toInt()],src, dst, paint);
				else
					canvas.drawBitmap(b[cells[ani_rawColumn+1][1].toInt()],src, dst, paint);
			}else if(ani_direction==Direction.right){
				Rect dst = new Rect(
						rect.left-w+ani_moving,
						rect.top + h*ani_rawColumn,
						rect.left +ani_moving,
						rect.top + h*(ani_rawColumn+1));
				if(ans[n][ani_rawColumn+1])
					canvas.drawBitmap(a[cells[n][ani_rawColumn+1].toInt()],src, dst, paint);
				else
					canvas.drawBitmap(b[cells[n][ani_rawColumn+1].toInt()],src, dst, paint);
			}else if(ani_direction==Direction.left){
				Rect dst = new Rect(
						rect.right -ani_moving,
						rect.top + ani_rawColumn*h,
						rect.right+w-ani_moving,
						rect.top + (ani_rawColumn+1)*h);
				if(ans[1][ani_rawColumn+1])
					canvas.drawBitmap(a[cells[1][ani_rawColumn+1].toInt()],src, dst, paint);
				else
					canvas.drawBitmap(b[cells[1][ani_rawColumn+1].toInt()],src, dst, paint);
			}
			}
		
		for(int x=0;x<n;x++){
			for(int y=0;y<n;y++){
				
				Rect dst = new Rect(
						rect.left + w*x,
						rect.top  + h*y,
						rect.left + w*(x+1),
						rect.top+ h*(y+1));
				//アニメーション
				if(ani_moving!=-1){
					if(ani_direction==Direction.down && x==ani_rawColumn){
						dst.bottom += ani_moving;
						dst.top+=ani_moving;
					}
					else if(ani_direction==Direction.up && x==ani_rawColumn){
						dst.bottom -= ani_moving;
						dst.top-=ani_moving;
					}
					else if(ani_direction==Direction.right && y==ani_rawColumn){
						dst.right += ani_moving;
						dst.left +=ani_moving;
					}
					else if(ani_direction==Direction.left && y==ani_rawColumn){
						dst.right -= ani_moving;
						dst.left -=ani_moving;
					}
				}
				if(ans[x+1][y+1])
					canvas.drawBitmap(a[cells[x+1][y+1].toInt()],src, dst, paint);
				else
					canvas.drawBitmap(b[cells[x+1][y+1].toInt()],src, dst, paint);
					
			}
		}
		//あとで消しとく．
		canvas.drawRect(0, rect.bottom, canvas.getWidth(), canvas.getHeight(), paint);
		canvas.drawRect(rect.right, 0, canvas.getWidth(), canvas.getHeight(), paint);
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
					
					ani_rawColumn = raw;
					ani_direction = Direction.right;
					ani_moving = 0;
					//puzzle.move(raw+1, Direction.right);
				}else if(dx < -sensitivity && raw!=-1){
					//左
					Log.d("TouchEvent", "left"+raw);
					oldX=-1;
					oldY=-1;
					
					ani_rawColumn = raw;
					ani_direction = Direction.left;
					ani_moving = 0;
					//puzzle.move(raw+1, Direction.left);
				}else if(dy > sensitivity && column!=-1){
					//下
					Log.d("TouchEvent", "down"+column);
					oldX=-1;
					oldY=-1;
					
					ani_rawColumn = column;
					ani_direction = Direction.down;
					ani_moving = 0;
					//puzzle.move(column+1, Direction.down);
				}else if(dy < -sensitivity && column!=-1){
					//上
					Log.d("TouchEvent", "up"+column);
					oldX=-1;
					oldY=-1;
					
					ani_rawColumn = column;
					ani_direction = Direction.up;
					ani_moving = 0;
					//puzzle.move(column+1, Direction.up);
				}
			}
			break;
		}

	}

	//タイマー処理
	public void timer() {
		if(ani_moving != -1){
			ani_moving += ani_moving_per_time;
			if(ani_moving >= rect.width()/n){
				//アニメーション終わり
				ani_moving = -1;
				//実際の移動
				puzzle.move(ani_rawColumn+1, ani_direction);
				//パズル完成
				if(puzzle.isComplete()){
					new AlertDialog.Builder(context)
					.setTitle("Complete!!")
					.setMessage("")
					.setPositiveButton("OK", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//パズルを初期化
							puzzle = new Puzzle(n+2,1);
							}
					})
					.show();
				}
			}
		}
	}
}
