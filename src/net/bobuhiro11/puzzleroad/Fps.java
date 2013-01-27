package net.bobuhiro11.puzzleroad;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Fps {

    private long _startTime = 0;            //測定開始時刻
    private int  _cnt = 0;                  //カウンタ
    private Paint _paint = new Paint();     //paint情報
    private float _fps;                     //fps
    private final static int N = 60;        //平均を取るサンプル数
    private final static int FONT_SIZE = 20;//フォントサイズ
    
    public Fps(){
            _paint.setColor(Color.BLUE);    //フォントの色を青に設定
            _paint.setTextSize(FONT_SIZE);  //フォントサイズを設定
    }
    
    public boolean update(){
            if( _cnt == 0 ){ //1フレーム目なら時刻を記憶
                    _startTime = System.currentTimeMillis();
            }
            if( _cnt == N ){ //60フレーム目なら平均を計算する
                    long t = System.currentTimeMillis();
                    _fps = 1000.f/((t-_startTime)/(float)N);
                    _cnt = 0;
                    _startTime = t;
            }
            _cnt++;
            return true;
    }

    public void onDraw(Canvas c){
            c.drawText(String.format("%.1f", _fps), 0, FONT_SIZE-2, _paint);
    }

}