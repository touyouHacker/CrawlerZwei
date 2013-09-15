/**
 *
 */
package jp.co.omega11.universal.util.net.mail.pop;

import java.security.Security;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

import jp.co.omega11.universal.util.log.Loger;

import org.apache.commons.lang.StringUtils;


/**
 * SSL�𗘗p����POP3�ł� JavaMail���J�v�Z�������Ă��܂�
 *
 * @author Wizard1 2009
 */
public class SslPop3 extends AbstractPop3 {

	private Store store;

	public SslPop3(String _host, int _port, String _username, String _password) {
		super(_host, _port, _username, _password);

	}

	@Override
	public void init() throws Exception {
		Properties props = System.getProperties();
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

		props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);

		// POP3 provider
		props.setProperty("mail.pop3.socketFactory.fallback", "false");

		// POP3 provider
		props.setProperty("mail.pop3.port", String.valueOf(port));
		props.setProperty("mail.pop3.socketFactory.port", String.valueOf(port));

		Session session = Session.getDefaultInstance(props, null);

		store = session.getStore("pop3");
		// �ڑ�
		store = session.getStore("pop3");
		store.connect(host, port, username, password);

	}

	@Override
	/**
	 * ���[������M���܂�
	 * ���������ԋp���܂�
	 */
	public String execute() throws Exception {

		return execute(null);
	}

	/**
	 * �w�肵���A�h���X����̃��[������1����M���܂�
	 * @param mailAdress ��M���郁�[���A�h���X
	 * @return ������ԋp���܂�
	 * @throws Exception
	 */
	@Override
	public String execute(String mailAdress) throws Exception {

		return execute(mailAdress, null);
	}


	@Override
	public String execute(String mailAdress, String subject) throws Exception {
		String returnMsg = null;

		Folder folder = store.getFolder("INBOX");


		if (folder.isOpen()) {
			Loger.print("folder is already open");
			return null;
		}
		folder.open(Folder.READ_ONLY);

		// �S���[����ǂݍ���
		Message[] msgs = null;
		msgs = folder.getMessages();
		if (msgs.length > 0)
			Loger.print("mail num=" + msgs.length);
		for (int i = 0; i < msgs.length; i++) {
			// ���[���̉��

			//�w�胁�[���A�h���X��NULL�̎��A�܂��͎w�胁�[���A�h���X�̂Ƃ���M
			//���� �w��T�u�W�F�N�g���m�t�k�k�̓��A�܂��͎w��T�u�W�F�N�g���m�t�k�k�̎���M
			if( (mailAdress == null || mailAdress.matches(".*"+msgs[i].getFrom()[0]+".*"))
					&& (subject == null || msgs[i].getSubject().equals(subject)) ) {
				returnMsg = msgs[i].getContent().toString();

				//���s�͍폜
				returnMsg = StringUtils.chomp(returnMsg);


				 Loger.print( (msgs[i].getFrom()[0]).toString() );
				 Loger.print(msgs[i].getSubject());
				 Loger.print(returnMsg);

				//msgs[i].writeTo(System.out);

				//�폜�}�[�N
				msgs[i].setFlag(Flags.Flag.DELETED, true);

				//�폜
				folder.close(true);
				store.close();
				return returnMsg;

			}

		}

		//�^�[�Q�b�g���[���łȂ��Ȃ�폜���Ȃ�
		folder.close(false);
		store.close();

		return returnMsg;
	}

}
