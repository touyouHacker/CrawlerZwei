/**
 * 
 */
package jp.co.omega11.webcrawler.w2fj.model.systemInfomation;

/**
 * @author Wizard1 2009
 * SubjectTxtGetスレッドの処理情報を格納するクラス
 *
 */
public class SubjectComponentInfo extends AbstractComponentInfo{


	private int threadUpdateTarget;
	private int threadDeleteFlagTarget;
	private int threadInsertTarget;
	
	
	/**
	 * @return the threadUpdateTarget
	 */
	public int getThreadUpdateTarget() {
		return threadUpdateTarget;
	}


	/**
	 * @param threadUpdateTarget the threadUpdateTarget to set
	 */
	public void setThreadUpdateTarget(int threadUpdateTarget) {
		this.threadUpdateTarget = threadUpdateTarget;
	}


	/**
	 * @return the threadDeleteFlagTarget
	 */
	public int getThreadDeleteFlagTarget() {
		return threadDeleteFlagTarget;
	}


	/**
	 * @param threadDeleteFlagTarget the threadDeleteFlagTarget to set
	 */
	public void setThreadDeleteFlagTarget(int threadDeleteFlagTarget) {
		this.threadDeleteFlagTarget = threadDeleteFlagTarget;
	}


	/**
	 * @return the threadInsertTarget
	 */
	public int getThreadInsertTarget() {
		return threadInsertTarget;
	}


	/**
	 * @param threadInsertTarget the threadInsertTarget to set
	 */
	public void setThreadInsertTarget(int threadInsertTarget) {
		this.threadInsertTarget = threadInsertTarget;
	}


	/**
	 * 情報クラスがかならず実装するメソッド
	 */
	@Override
	public final String toString() {
		String retMsg = "";
		
		//TODO　改行コードは動的にかえる必要がある
		retMsg += "スレッド更新 " + String.valueOf(threadUpdateTarget) + "\r\n";
		retMsg += "スレッド落ち " + String.valueOf(threadDeleteFlagTarget) + "\r\n";
		retMsg += "スレッドInsert " + String.valueOf(threadInsertTarget) + "\r\n";
		
		return retMsg;
	}

}

