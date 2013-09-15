package jp.co.omega11.universal.util.net.mail.smtp;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SslSmtp extends AbstractSmtp{

	//éQçl http://www.utilz.jp/wiki/JavaMail1
	

	public SslSmtp(String _host, int _port, String _username, String _password) {
		super(_host, _port, _username, _password);

	}

	private Transport transport = null;
	private Properties PROP = null;
	private Session sess= null;
	

	public void init () throws Exception{
		PROP = new Properties();
		PROP.put("mail.smtp.host", host);
		PROP.put("mail.smtp.port", String.valueOf(port));
		PROP.put("mail.smtp.auth", "true");
		PROP.put("mail.smtp.starttls.enable", "true");
		
		sess = Session.getInstance(PROP);
		
		transport = sess.getTransport("smtp");
		transport.connect(username, password);
	}
	
	@Override
	public void execute(String mailTo, String subject, String messageBody) throws Exception {

		try {
		
			MimeMessage mm = new MimeMessage(sess);
			mm.setFrom(new InternetAddress(username));
			mm.setSubject(subject);
			mm.setRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
			mm.setContent(messageBody, "text/plain; charset=iso-2022-jp");
			mm.setHeader("Content-Transfer-Encoding", "7bit");
			transport.sendMessage(mm, mm.getAllRecipients());
			
		} finally {
			if (transport != null) {
				transport.close();
			}
		}
	}




}
