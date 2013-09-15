/**
 * 
 */
package jp.co.omega11.universal.controller.receivecommand;

import jp.co.omega11.universal.controller.receivecommand.model.CommandModel;

/**
 * @author Wizard1 2009
 *
 */
public class ReceiveCommandThread extends Thread {
	
	private IExecuteCommand executeCommand;
	private IReceiveCommand receiveCommand;
	
	/**
	 * コンストラクタ
	 * @param receiveCommand　コマンド受付コンポーネント(コンソール/メール/ネットサーバー・・など)
	 * 							このライブラリで用意されてるものを選択してそのまま使用
	 * 
	 * @param executeCommand  コマンド実行コンポーネント
	 * 							1フレームワークで1つ
	 * 							★コマンド実行コンポーネントは各フレームワーク製造者が実装
	 */
	public ReceiveCommandThread(IReceiveCommand receiveCommand, IExecuteCommand executeCommand) {
		this.receiveCommand = receiveCommand;
		this.executeCommand = executeCommand;
	}

	@Override
	public void run(){

		CommandModel commandModel = new CommandModel();
		receiveCommand.starting(commandModel , executeCommand);
	}
}

