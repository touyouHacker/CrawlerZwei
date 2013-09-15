package jp.co.omega11.universal.controller.receivecommand;

import jp.co.omega11.universal.controller.receivecommand.model.CommandModel;



public abstract class AbstractReceiveCommand implements IReceiveCommand {


	/**
	 * ��M�����s���܂�
	 * @param commandModel �R�}���h���i�[���郂�f��
	 */
	public void starting(CommandModel commandModel, IExecuteCommand executeCommand){
		
		if(init()) {
			receive(commandModel, executeCommand);
		} else {
			commandModel.setInitFailFlag(true);
		}
	}
	


}

