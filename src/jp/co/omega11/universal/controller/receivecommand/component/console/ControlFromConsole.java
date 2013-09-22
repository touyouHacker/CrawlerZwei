/**
 * 
 */
package jp.co.omega11.universal.controller.receivecommand.component.console;

import java.io.Console;

import jp.co.omega11.universal.controller.receivecommand.AbstractReceiveCommand;
import jp.co.omega11.universal.controller.receivecommand.IExecuteCommand;
import jp.co.omega11.universal.controller.receivecommand.model.CommandModel;
import jp.co.omega11.universal.util.log.Log;

/**
 * @author Wizard1 2009
 * 
 *         コンソールからのコマンド受付コンポーネント
 * 
 */
public class ControlFromConsole extends AbstractReceiveCommand {

	private Console console;

	/**
	 * コンソール入力をうけつけます
	 */
	@Override
	public void receive(CommandModel commandModel,
			IExecuteCommand executeCommand) {

		Log.print("Console 受信開始");

		while (true) {
			String command = console.readLine();

			System.out.println("Console コマンド受信" + command);

			commandModel.setReceiveCount(commandModel.getReceiveCount() + 1);
			commandModel.setCommand(command);

			// コマンド実行
			executeCommand.cmommandExe(commandModel);

			// 結果を表示
			System.out.println(commandModel.getResultMsg());

		}
	}

	/**
	 * 受付機構の初期化をします。
	 * 
	 * @return true 成功 / false 失敗
	 * 
	 */
	@Override
	public boolean init() {

		console = System.console();

		if (console != null) {
			return true;
		}

		Log.print("Console コンソールを初期化できませんでした。");
		return false;
	}

}
