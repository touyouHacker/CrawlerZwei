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
	 * ���b�Z�[�W����M���܂�
	 * @return
	 * @throws IOException
	 */
	public abstract String execute() throws Exception;

	
	
	/**
	 * �w�肵�����[���A�h���X����̂ݎ�M
	 * @param mailAdress
	 * @return ��M�{��
	 * @throws Exception
	 */
	abstract public String execute(String mailAdress) throws Exception; 
	
	
	/**
	 * �w�肵�����[���A�h���X+�w�肵����������̂ݖ{����M
	 * @param mailAdress�@�w�肵�����[���A�h���X������M���Ȃ�
	 * @param subject�@�w�肵������������M���Ȃ�
	 * @return ��M�{��
	 * @throws Exception
	 */
	abstract public String execute(String mailAdress, String subject) throws Exception;
}

