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
	 * コマンド
	 */
	private String command = null;

	
	/**
	 * 結果メッセージ
	 */
	private String resultMsg = null;
	
	/**
	 * コマンド受付機構に失敗したときtrue
	 */
	private boolean initFailFlag;
	
	/**
	 * コマンド受信回数をカウントします
	 */
	private long receiveCount;
	
	
	/**
	 * コマンド実行に失敗したときtrue
	 */
	private boolean exeFail;
	
	/**
	 * コマンド受信コンポーネント
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
	 * コマンドを設定し
	 * コマンドを実行します
	 * 
	 * @param command コマンド
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

