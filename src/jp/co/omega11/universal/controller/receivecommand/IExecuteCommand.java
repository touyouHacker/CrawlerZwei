/**
 * 
 */
package jp.co.omega11.universal.controller.receivecommand;

import jp.co.omega11.universal.controller.receivecommand.model.CommandModel;

/**
 * @author Wizard1 2009
 *
 */
public interface IExecuteCommand {

	/**
	 * �R�}���h�����s���܂�
	 * �p�����������N���X�ł��̃��\�b�h�ɋƖ����W�b�N���������܂�
	 */
	abstract boolean cmommandExe(CommandModel commandModel);
}

