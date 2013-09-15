/**
 * 
 */
package jp.co.omega11.webcrawler.w2fj.model.systemInfomation;

/**
 * @author Wizard1 2009
 * コンポーネント共通の情報
 */
public class AbstractComponentInfo extends AbstractInformation {

	public void init(String className) {
		this.setClassName(className);
	}
	
}

