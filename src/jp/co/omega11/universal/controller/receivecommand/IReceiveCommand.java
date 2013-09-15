/**
 * 
 */
package jp.co.omega11.universal.controller.receivecommand;

import jp.co.omega11.universal.controller.receivecommand.model.CommandModel;

/**
 * @author Wizard1 2009
 *
 * 実行しているメインプログラム以外の場所からの命令をうけつける汎用的なインターフェースです
 * 
 *
 */
public interface IReceiveCommand {

	/**
	 * コマンド受信を実行しコマンド文字列を返す
	 * @param CommandModel コマンドを格納するモデル
	 * @return
	 */
	abstract void receive(CommandModel commandModel, IExecuteCommand executeCommand);
	
	/**
	 * 受信機構の初期化
	 * @return 成功ならTrue , 失敗ならfalse
	 */
	abstract boolean init();
	
	
	/**
	 * コマンド受付機構を実行します、基本的に内部で受信ループします
	 * コマンド受信→コマンド実行→コマンド受信　を繰り返します
	 * @param コマンドモデル
	 */
	 void starting(CommandModel commandModel, IExecuteCommand executeCommand);
}

