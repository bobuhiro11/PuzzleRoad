package net.bobuhiro11.puzzleroad;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Person {
	Paint paint;
	Bitmap bmp;
	Context context;
	Rect dst,src;
	
	public Person(Context context,Rect dst,int id){
		this.context = context;
		this.dst = dst;
		this.src = new Rect(0,0,140,140);
		this.paint = new Paint(Color.WHITE);
		
		Resources r = context.getResources();
        bmp = BitmapFactory.decodeResource(r, id);
	}
	
	public void update(){
		
	}
	public void timer(){
		
	}
	public void draw(Canvas canvas){
		canvas.drawBitmap(bmp, src,dst, paint);
	}

}
