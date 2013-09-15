package jp.co.omega11.universal.util.net.mail.smtp;

import jp.co.omega11.universal.util.net.mail.AbstractMail;

public abstract class AbstractSmtp extends AbstractMail {


	
	public AbstractSmtp(String _host, int _port, String _username,
			String _password) {
		super(_host, _port, _username, _password);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param mailTo
	 * @param subject
	 * @param messageBody
	 * @throws Exception
	 */
	public abstract  void execute(String mailTo, String subject, String messageBody) throws Exception;

}

