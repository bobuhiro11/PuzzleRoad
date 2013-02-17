package net.bobuhiro11.puzzleroadconsole;

import java.util.Random;

/**
 * @author bobuhiro11
 *
 * 一つ一つのセルです．
 */
public class Cell {
	public Boolean up,down,left,right;
	
	/**
	 * @param u 上方向が有効か
	 * @param r 右方向が有効か
	 * @param d 下方向が有効か
	 * @param l 左方向が有効か
	 */
	public Cell(boolean u,boolean r,boolean d,boolean l){
		this.up = u;
		this.down = d;
		this.left = l;
		this.right = r;
	}
	
	/**
	 * すべてfalse
	 */
	public Cell(){
		this.up = false;
		this.down = false;
		this.left = false;
		this.right = false;
	}
	
	/**
	 * このセルを落とし穴にする．
	 * 落とし穴はすべてtrueのマス
	 */
	public void setHole(){
		this.up = true;
		this.down = true;
		this.left = true;
		this.right = true;
	}
	
	/**
	 * このセルが落とし穴かどうか．
	 * （このセルがすべてtrueかどうか）
	 * @return
	 */
	public boolean isHole(){
		return (up&&down&&right&&left);
	}
	
	/**
	 * セルにランダムで方向を与える．（ただし全てfalseもありえる．）
	 * @param gameNunber 何ゲーム目か
	 */
	public void setRandom(int gameNunber){
		
		// 20ゲーム目以降は難易度の変更はなし
		if(gameNunber >= 20)
			gameNunber = 20;
		
		Random rand = new Random();
		int n = 4 + gameNunber/2;
		int x = rand.nextInt(n);
		switch(x){
		case 0:	right=true;left=true;break;
		case 1:	up=true;down=true;break;
		case 2:	up=true;right=true;break;
		case 3:	up=true;left=true;break;
		case 4:	right=true;down=true;break;
		case 5:	down=true;left=true;break;
		default: right=false;left=false;up=false;down=false;break;
		}
	}
	
	/**
	 * @return int型に変換する．できないものは-1
	 */
	public int toInt(){
		if(down&&up&&left&&right)	return 7;
		else if(up==true&&right==true)	return 0;
		else if(up==true&&down==true)	return 1;
		else if(up==true&&left==true)	return 2;
		else if(right==true&&down==true)return 3;
		else if(right==true&&left==true)return 4;
		else if(down==true&&left==true)	return 5;
		else if(!down&&!up&&!left&&!right) return 6;
		else							return -1;
	}
	
	/**
	 * @param d 方向
	 */
	public void set(Direction d){
		if(d==Direction.up)
			up=true;
		else if(d==Direction.down)
			down=true;
		else if(d==Direction.right)
			right=true;
		else if(d==Direction.left)
			left=true;
	}
	
	/**
	 * @return すべてfalseかどうか
	 */
	public boolean isAllFalse(){
		return (!up&&!down&&!right&&!left);
	}
	
	/**
	 * それが直線かどうか
	 * @return
	 */
	public boolean isStraight(){
		return (up&&down || left&&right);
	}
}