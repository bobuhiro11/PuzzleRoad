package net.bobuhiro11.puzzleroad;

/**
 * 現在のゲームの状態
 */
public enum Status {
	/**
	 * タイトル画面
	 */
	title,
	/**
	 * ゲームのプレイ中
	 */
	playing,
	/**
	 * ゴール後の画像の移動状態
	 */
	personMovin,
	/**
	 * ゴール後のダイアログ
	 */
	dialog,
	/**
	 * 落とし穴後の画像の移動状態
	 */
	personMovingHole,
	/**
	 * 落とし穴後のダイアログ
	 */
	dialogHole
}
