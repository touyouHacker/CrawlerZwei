/**
 * POP3ユーティリティです。commons.netをさらにカプセル化してます
 */
package jp.co.omega11.universal.util.net.mail.pop;


import java.io.IOException;
import java.io.Reader;

import jp.co.omega11.universal.util.log.Loger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.pop3.POP3Client;
import org.apache.commons.net.pop3.POP3MessageInfo;



/**
 * @author Wizard1 2009
 *
 */
public class Pop3 extends AbstractPop3 {


	public Pop3(String _host, int _port, String _username, String _password) {
		super(_host, _port, _username, _password);

	}

	POP3Client pop3Client;


	/* (non-Javadoc)
	 * @see jp.co.omega11.universal.util.net.mail.pop.IPOP3#init()
	 */
	public void init() throws Exception {
		pop3Client = new POP3Client();

		pop3Client.connect(host, port);
		pop3Client.login(username, password);
	}

	/* (non-Javadoc)
	 * @see jp.co.omega11.universal.util.net.mail.pop.IPOP3#receive()
	 */
	public String execute() throws IOException{

		String returnMsg = null;

		POP3MessageInfo[] messages = pop3Client.listMessages();

		for (int i = 0; i < messages.length ; i++) {
			int messageId = messages[i].number;
			Loger.print("ID: " + messageId);
			Reader reder = pop3Client.retrieveMessage(messageId);
			Loger.print(IOUtils.toString(reder));
			IOUtils.closeQuietly(reder);
		}



		pop3Client.logout();
		pop3Client.disconnect();

		return returnMsg;
	}

	@Override
	public String execute(String mailAdress) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String execute(String mailAdress, String subject) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
