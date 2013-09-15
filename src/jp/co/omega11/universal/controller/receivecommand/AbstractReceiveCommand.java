package jp.co.omega11.universal.controller.receivecommand;

import jp.co.omega11.universal.controller.receivecommand.model.CommandModel;



public abstract class AbstractReceiveCommand implements IReceiveCommand {


	/**
	 * 受信を実行します
	 * @param commandModel コマンドを格納するモデル
	 */
	public void starting(CommandModel commandModel, IExecuteCommand executeCommand){
		
		if(init()) {
			receive(commandModel, executeCommand);
		} else {
			commandModel.setInitFailFlag(true);
		}
	}
	


}

