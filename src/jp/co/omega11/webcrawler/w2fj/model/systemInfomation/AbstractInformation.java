/**
 * 
 */
package jp.co.omega11.webcrawler.w2fj.model.systemInfomation;

import java.util.Calendar;

/**
 * @author Wizard1 2009
 * 
 *         ���N���X�ŋ��ʓI�Ȓ�`���s��
 */
public abstract class AbstractInformation {

	public AbstractInformation() {
		// TODO Auto-generated constructor stub
	}
	public AbstractInformation(String _className) {
		className = _className;
	}
	/**
	 * �N���X��(this.getClass().getSimpleName();)
	 */
	private String className = null;

	/**
	 * �N������
	 */
	private Calendar startTime = null;

	/**
	 * �Ō�Ɏ��s��������
	 */
	private Calendar lastWorkTime = null;

	/**
	 * ���N���X�P�ʂœ��ꂽ�����b�Z�[�W�͘A�����Ă����ɖ��ߍ���
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
