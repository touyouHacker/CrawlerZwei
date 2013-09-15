/**
 * 
 */
package jp.co.omega11.universal.util.net.mail.pop;

import java.io.IOException;

import jp.co.omega11.universal.util.net.mail.AbstractMail;

/**
 * @author Wizard1 2009
 *
 */
public abstract class AbstractPop3 extends AbstractMail {

	public AbstractPop3(String _host, int _port, String _username,
			String _password) {
		super(_host, _port, _username, _password);

	}

	/**
	 * メッセージを受信します
	 * @return
	 * @throws IOException
	 */
	public abstract String execute() throws Exception;

	
	
	/**
	 * 指定したメールアドレスからのみ受信
	 * @param mailAdress
	 * @return 受信本文
	 * @throws Exception
	 */
	abstract public String execute(String mailAdress) throws Exception; 
	
	
	/**
	 * 指定したメールアドレス+指定した件名からのみ本文受信
	 * @param mailAdress　指定したメールアドレスしか受信しない
	 * @param subject　指定した件名しか受信しない
	 * @return 受信本文
	 * @throws Exception
	 */
	abstract public String execute(String mailAdress, String subject) throws Exception;
}

