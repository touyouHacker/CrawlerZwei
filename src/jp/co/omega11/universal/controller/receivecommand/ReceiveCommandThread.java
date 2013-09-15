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
	 * �R���X�g���N�^
	 * @param receiveCommand�@�R�}���h��t�R���|�[�l���g(�R���\�[��/���[��/�l�b�g�T�[�o�[�E�E�Ȃ�)
	 * 							���̃��C�u�����ŗp�ӂ���Ă���̂�I�����Ă��̂܂܎g�p
	 * 
	 * @param executeCommand  �R�}���h���s�R���|�[�l���g
	 * 							1�t���[�����[�N��1��
	 * 							���R�}���h���s�R���|�[�l���g�͊e�t���[�����[�N�����҂�����
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

