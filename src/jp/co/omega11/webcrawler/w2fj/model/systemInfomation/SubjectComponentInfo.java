/**
 * 
 */
package jp.co.omega11.webcrawler.w2fj.model.systemInfomation;

/**
 * @author Wizard1 2009
 * SubjectTxtGet�X���b�h�̏��������i�[����N���X
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
	 * ���N���X�����Ȃ炸�������郁�\�b�h
	 */
	@Override
	public final String toString() {
		String retMsg = "";
		
		//TODO�@���s�R�[�h�͓��I�ɂ�����K�v������
		retMsg += "�X���b�h�X�V " + String.valueOf(threadUpdateTarget) + "\r\n";
		retMsg += "�X���b�h���� " + String.valueOf(threadDeleteFlagTarget) + "\r\n";
		retMsg += "�X���b�hInsert " + String.valueOf(threadInsertTarget) + "\r\n";
		
		return retMsg;
	}

}

