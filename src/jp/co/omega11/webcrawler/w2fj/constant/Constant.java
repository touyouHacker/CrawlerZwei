package jp.co.omega11.webcrawler.w2fj.constant;

/**
 * 定数定義クラス
 * @author Wizard1
 *
 */

public final class Constant {

	
	public static final String SUBJECTTEXT="subject.txt";

	public static final String SUBJECTTEXTFOLDER ="subjectText";
	
	public static final String CONTENTSFOLDER="contents";
	
	public static final String DATFOLDER="dat";
	
	/* SETTING MAIL -----------------------------------------------------------*/
	public static final String POP = "POP";
	
	public static final String IMAP = "IMAP";
	
	/* THREAD INFO ----------------------------------------------------------- */
	
	
	// スレッドの状態

	public static final int THREAD_NOW_STOP = 0;

	public static final int THREAD_NOW_ALIVE = 1;
	
	public static final int THREAD_NOW_WAIT = 2;
	
	public static final int THREAD_NOW_BUSSY = 10;
	
	// スレッドへの命令
	public static final int THREAD_DO_NOTTING = -1;
	
	public static final int THREAD_DO_STOP = 0;
	
	public static final int THREAD_DO_ALIVE = 1;
	
	public static final int THREAD_DO_WAIT = 2;
	
	public static final int THREAD_DO_REBOOT = 10;
	
	
	// スレッドの作業状態	
	/**
	 * 作業なし
	 */
	public static final int THREAD_WORK_FREE = 0;
	
	/**
	 * 作業中
	 */
	public static final int THREAD_WORK_DOIGN = 1;

}
