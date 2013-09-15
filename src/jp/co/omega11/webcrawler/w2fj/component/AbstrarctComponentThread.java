/**
 * 
 */
package jp.co.omega11.webcrawler.w2fj.component;

import java.sql.Connection;

import jp.co.omega11.webcrawler.w2fj.model.systemInfomation.ThreadComponentInfo;
import jp.co.omega11.webcrawler.w2fj.set.Setting;


/**
 * @author Wizard1
 * 
 */
public abstract class AbstrarctComponentThread extends Thread implements
		IComponentThread {

	protected Setting setting;
	protected Connection con;
	protected ThreadComponentInfo threadComponentInfo;
	
	/**
	 * コンストラクター
	 * @param set
	 */
	public AbstrarctComponentThread(Setting setting, ThreadComponentInfo threadComponentInfo) {
		this.setting = setting;
		this.threadComponentInfo = threadComponentInfo;
		
	}
}
