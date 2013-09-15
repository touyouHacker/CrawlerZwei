/**
 * 
 */
package jp.co.omega11.universal.controller.receivecommand.model;


/**
 * @author Wizard1 2009
 *
 */
public class CommandModel {


	/**
	 * �R�}���h
	 */
	private String command = null;

	
	/**
	 * ���ʃ��b�Z�[�W
	 */
	private String resultMsg = null;
	
	/**
	 * �R�}���h��t�@�\�Ɏ��s�����Ƃ�true
	 */
	private boolean initFailFlag;
	
	/**
	 * �R�}���h��M�񐔂��J�E���g���܂�
	 */
	private long receiveCount;
	
	
	/**
	 * �R�}���h���s�Ɏ��s�����Ƃ�true
	 */
	private boolean exeFail;
	
	/**
	 * �R�}���h��M�R���|�[�l���g
	 * @param receiveCommand
	 */
	public CommandModel() {
	}
	
	
	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * 
	 * �R�}���h��ݒ肵
	 * �R�}���h�����s���܂�
	 * 
	 * @param command �R�}���h
	 */
	public void setCommand(String command) {
		this.command = command;
	}


	/**
	 * @return the initFailFlag
	 */
	public boolean isInitFailFlag() {
		return initFailFlag;
	}


	/**
	 * @param initFailFlag the initFailFlag to set
	 */
	public void setInitFailFlag(boolean initFailFlag) {
		this.initFailFlag = initFailFlag;
	}


	/**
	 * @return the receiveCount
	 */
	public long getReceiveCount() {
		return receiveCount;
	}


	/**
	 * @param receiveCount the receiveCount to set
	 */
	public void setReceiveCount(long receiveCount) {
		this.receiveCount = receiveCount;
	}


	/**
	 * @return the exeFail
	 */
	public boolean isExeFail() {
		return exeFail;
	}


	/**
	 * @param exeFail the exeFail to set
	 */
	public void setExeFail(boolean exeFail) {
		this.exeFail = exeFail;
	}


	/**
	 * @return the resultMsg
	 */
	public String getResultMsg() {
		return resultMsg;
	}


	/**
	 * @param resultMsg the resultMsg to set
	 */
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	
	
}

