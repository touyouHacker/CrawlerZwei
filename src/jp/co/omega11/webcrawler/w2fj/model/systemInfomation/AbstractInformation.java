/**
 * 
 */
package jp.co.omega11.webcrawler.w2fj.model.systemInfomation;

import java.util.Calendar;

/**
 * @author Wizard1 2009
 * 
 *         情報クラスで共通的な定義を行う
 */
public abstract class AbstractInformation {

	public AbstractInformation() {
		// TODO Auto-generated constructor stub
	}
	public AbstractInformation(String _className) {
		className = _className;
	}
	/**
	 * クラス名(this.getClass().getSimpleName();)
	 */
	private String className = null;

	/**
	 * 起動時間
	 */
	private Calendar startTime = null;

	/**
	 * 最後に実行した時間
	 */
	private Calendar lastWorkTime = null;

	/**
	 * 情報クラス単位で入れたいメッセージは連結してここに埋め込む
	 */
	private String msg = null;

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the startTime
	 */
	public Calendar getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the lastWorkTime
	 */
	public Calendar getLastWorkTime() {
		return lastWorkTime;
	}

	/**
	 * @param lastWorkTime the lastWorkTime to set
	 */
	public void setLastWorkTime(Calendar lastWorkTime) {
		this.lastWorkTime = lastWorkTime;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
