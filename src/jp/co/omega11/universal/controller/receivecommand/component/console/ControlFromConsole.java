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
 *         �R���\�[������̃R�}���h��t�R���|�[�l���g
 * 
 */
public class ControlFromConsole extends AbstractReceiveCommand {

	private Console console;

	/**
	 * �R���\�[�����͂��������܂�
	 */
	@Override
	public void receive(CommandModel commandModel,
			IExecuteCommand executeCommand) {

		Log.print("Console ��M�J�n");

		while (true) {
			String command = console.readLine();

			System.out.println("Console �R�}���h��M" + command);

			commandModel.setReceiveCount(commandModel.getReceiveCount() + 1);
			commandModel.setCommand(command);

			// �R�}���h���s
			executeCommand.cmommandExe(commandModel);

			// ���ʂ�\��
			System.out.println(commandModel.getResultMsg());

		}
	}

	/**
	 * ��t�@�\�̏����������܂��B
	 * 
	 * @return true ���� / false ���s
	 * 
	 */
	@Override
	public boolean init() {

		console = System.console();

		if (console != null) {
			return true;
		}

		Log.print("Console �R���\�[�����������ł��܂���ł����B");
		return false;
	}

}
