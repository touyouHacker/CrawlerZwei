/**
 * 
 */
package jp.co.omega11.universal.util.net.mail;


/**
 * @author Wizard1 2009
 * 
 */
public abstract class AbstractMail implements IMail {

	protected String host = null;
	protected int port;
	protected String username = null;
	protected String password = null;

	public AbstractMail(String _host, int _port, String _username,
			String _password) {
		host = _host;
		port = _port;
		username = _username;
		password = _password;
	}

}
