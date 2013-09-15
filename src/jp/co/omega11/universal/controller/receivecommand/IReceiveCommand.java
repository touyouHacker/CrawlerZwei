/**
 * 
 */
package jp.co.omega11.universal.controller.receivecommand;

import jp.co.omega11.universal.controller.receivecommand.model.CommandModel;

/**
 * @author Wizard1 2009
 *
 * ���s���Ă��郁�C���v���O�����ȊO�̏ꏊ����̖��߂���������ėp�I�ȃC���^�[�t�F�[�X�ł�
 * 
 *
 */
public interface IReceiveCommand {

	/**
	 * �R�}���h��M�����s���R�}���h�������Ԃ�
	 * @param CommandModel �R�}���h���i�[���郂�f��
	 * @return
	 */
	abstract void receive(CommandModel commandModel, IExecuteCommand executeCommand);
	
	/**
	 * ��M�@�\�̏�����
	 * @return �����Ȃ�True , ���s�Ȃ�false
	 */
	abstract boolean init();
	
	
	/**
	 * �R�}���h��t�@�\�����s���܂��A��{�I�ɓ����Ŏ�M���[�v���܂�
	 * �R�}���h��M���R�}���h���s���R�}���h��M�@���J��Ԃ��܂�
	 * @param �R�}���h���f��
	 */
	 void starting(CommandModel commandModel, IExecuteCommand executeCommand);
}

