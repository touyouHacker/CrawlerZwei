/**
 * 
 */
package jp.co.omega11.webcrawler.w2fj.model.systemInfomation;


import jp.co.omega11.webcrawler.w2fj.constant.Constant;

/**
 * @author Wizard1 2009
 * 
 */
public class ThreadComponentInfo extends AbstractInformation {

	/**
	 * ���N���X�̏�����
	 * @param name
	 * @param _thread
	 * @param abstractComponentInfo
	 */
	public void init(String name, Thread _thread, AbstractComponentInfo abstractComponentInfo) {
		setClassName(name);
		thread = _thread;
		this.componentInfo = abstractComponentInfo;
	}

	/**
	 * �X���b�h���s��
	 */
	private long threadLoopCount;

	/**
	 * ��O��
	 */
	private long exceptionCount;

	/**
	 * �X���b�h�@static Thread = Thread.currentThread()
	 */
	private Thread thread = null;

	/**
	 * �X���b�h�̏��
	 */
	private int condition = Constant.THREAD_NOW_STOP;

	/**
	 * �X���b�h�ւ̖���
	 */
	private int command = Constant.THREAD_DO_NOTTING;

	/**
	 * �X���b�h�̍�Ə��
	 */
	private int work = Constant.THREAD_WORK_FREE;

	/**
	 * �R���|�[�l���g�P�ʂ̏��
	 */
	private AbstractComponentInfo componentInfo = null;

	/**
	 * @return the threadLoopCount
	 */
	public long getThreadLoopCount() {
		return threadLoopCount;
	}

	/**
	 * @param threadLoopCount
	 *            the threadLoopCount to set
	 */
	public void setThreadLoopCount(long threadLoopCount) {
		this.threadLoopCount = threadLoopCount;
	}

	/**
	 * @return the exceptionCount
	 */
	public long getExceptionCount() {
		return exceptionCount;
	}

	/**
	 * @param exceptionCount
	 *            the exceptionCount to set
	 */
	public void setExceptionCount(long exceptionCount) {
		this.exceptionCount = exceptionCount;
	}

	/**
	 * @return the thead
	 */
	public Thread getThread() {
		return thread;
	}

	/**
	 * @param thead
	 *            the thead to set
	 */
	public void setThread(Thread thread) {
		this.thread = thread;
	}

	/**
	 * @return the condition
	 */
	public int getCondition() {
		return condition;
	}

	/**
	 * @param condition
	 *            the condition to set
	 */
	public void setCondition(int condition) {
		this.condition = condition;
	}

	/**
	 * @return the command
	 */
	public int getCommand() {
		return command;
	}

	/**
	 * @param command
	 *            the command to set
	 */
	public void setCommand(int command) {
		this.command = command;
	}

	/**
	 * @return the work
	 */
	public int getWork() {
		return work;
	}

	/**
	 * @param work
	 *            the work to set
	 */
	public void setWork(int work) {
		this.work = work;
	}

	/**
	 * @return the componentInfo
	 */
	public AbstractComponentInfo getComponentInfo() {
		return componentInfo;
	}

	/**
	 * @param componentInfo
	 *            the componentInfo to set
	 */
	public void setComponentInfo(AbstractComponentInfo componentInfo) {
		this.componentInfo = componentInfo;
	}

}
