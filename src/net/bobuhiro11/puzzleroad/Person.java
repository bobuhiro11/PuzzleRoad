package net.bobuhiro11.puzzleroad;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Person {
	Paint paint;
	Bitmap bmp;
	Context context;
	Rect dst,src;
	
	//パズルの描画領域（この画像ではない）
	Rect rect;
	//nマス*nマス
	int n;
	
	/**
	 * 動きのある物体を生成する．
	 * @param context コンテキスト
	 * @param point この物体の位置
	 * @param rect　パズルの描画領域（この画像ではない）
	 * @param n　nマス*nマス
	 * @param id　画像のid
	 */
	public Person(Context context,Point point,Rect rect,int n,int id){
		this.context = context;
		this.src = new Rect(0,0,140,140);
		this.paint = new Paint(Color.WHITE);
		this.rect = rect;
		this.n  = n;
		
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
        dst = new Rect(
        		rect.left + cellWidth * (point.x-1),
        		rect.top + cellHeight * (point.y-1),
        		rect.left + cellWidth * point.x,
        		rect.top + cellHeight * point.y);
	}
	/**
	 * 更新処理
	 */
	public void update(){
		
	}
	
	/**
	 * タイマー処理
	 */
	public void timer(){
		
	}
	
	/**
	 * 描画処理
	 * @param canvas キャンパス
	 */
	public void draw(Canvas canvas){
		canvas.drawBitmap(bmp, src,dst, paint);
	}

}
