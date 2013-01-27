package net.bobuhiro11.puzzleroad;

import java.util.ArrayList;

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

public class Person {
	Paint paint;
	Bitmap bmp;
	Context context;
	Rect dst,src;
	
	//パズルの描画領域（この画像ではない）
	Rect rect;
	//nマス*nマス
	int n;
	
	//MainView
	MainView mainView;
	
	/**
	 * これから辿っていく座標一覧
	 */
	ArrayList<Point> positions;
	
	/**
	 * 次に行く座標
	 */
	int nextIndex;
	
	// １フレームあたりの移動量
	int moving_per_frame = 6;
	
	//汎用変数
	Point now,next,d;
	
	/**
	 * 動きのある物体を生成する．
	 * @param context コンテキスト
	 * @param point この物体の位置
	 * @param rect　パズルの描画領域（この画像ではない）
	 * @param n　nマス*nマス
	 * @param id　画像のid
	 */
	public Person(Context context,Point point,Rect rect,int n,int id,MainView mainView){
		this.mainView = mainView;
		this.context = context;
		this.src = new Rect(0,0,140,140);
		this.dst = new Rect();
		this.paint = new Paint(Color.WHITE);
		this.rect = rect;
		this.n  = n;
		
		this.now = new Point();
		this.next = new Point();
		this.d = new Point();
		
		Resources r = context.getResources();
        bmp = BitmapFactory.decodeResource(r, id);
  
        this.setPoint(point);
	}
	
	/**
	 * 画像の場所を設定する．
	 * @param point 座標
	 */
	public void setPoint(Point point){
        int cellWidth  = (rect.right - rect.left) /n;
        int cellHeight = (rect.bottom - rect.top) /n;
        dst.set(
        		rect.left + cellWidth * (point.x-1),
        		rect.top + cellHeight * (point.y-1),
        		rect.left + cellWidth * point.x,
        		rect.top + cellHeight * point.y);
	}
	
	/**
	 * 画像の場所を設定する．
	 * @param point 画面上の左上の実座標
	 */
	public void setPositon(Point point){
        int cellWidth  = (rect.right - rect.left) /n;
        int cellHeight = (rect.bottom - rect.top) /n;
        dst.set(
        		point.x,
        		point.y,
        		point.x + cellWidth,
        		point.y + cellHeight);
	}
	
	/**
	 * これから辿っていく座標一覧を設定
	 * @param positions　画面上で左上が通るべき実座標
	 */
	public void setPositions(ArrayList<Point> positions){
		this.positions = positions;
		this.nextIndex = 1;
	}
	
	/**
	 * 更新処理
	 */
	public void update(){
		
		if(mainView.status==Status.personMovin){
			Log.d("size", positions.toString());
			Log.d("nextindex", String.valueOf(nextIndex));
			
			//現在の座標
			now.set(dst.left,dst.top);
			//次の座標
			next = this.positions.get(nextIndex);
			//移動量
			d.set(next.x - now.x , next.y-now.y);
			if(d.x!=0){
				d.x /= Math.abs(d.x);
				d.x *= this.moving_per_frame;
			}
			if(d.y!=0){
				d.y /= Math.abs(d.y);
				d.y *= this.moving_per_frame;
			}
			//実際の移動
			now.set(now.x+d.x, now.y+d.y);
			//this.setPositon(new Point(now.x+d.x,now.y+d.y));
			this.setPositon(now);
			
			//目標を変更
			//now.set(dst.left,dst.top);
			if(Math.abs(now.x - next.x) < moving_per_frame &&
			   Math.abs(now.y - next.y) < moving_per_frame){
				
				setPositon(next);
				nextIndex++;
			}
			
			//終了
			if(nextIndex == positions.size()){
				mainView.status = Status.dialog;
			}
		}
	}
	
	/**
	 * 描画処理
	 * @param canvas キャンパス
	 */
	public void draw(Canvas canvas){
		canvas.drawBitmap(bmp, src,dst, paint);
	}

}
