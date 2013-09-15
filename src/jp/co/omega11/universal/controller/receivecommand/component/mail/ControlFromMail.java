package jp.co.omega11.universal.controller.receivecommand.component.mail;

import jp.co.omega11.universal.controller.receivecommand.AbstractReceiveCommand;
import jp.co.omega11.universal.controller.receivecommand.IExecuteCommand;
import jp.co.omega11.universal.controller.receivecommand.model.CommandModel;
import jp.co.omega11.universal.controller.receivecommand.model.POP3Model;
import jp.co.omega11.universal.controller.receivecommand.model.SmtpModel;
import jp.co.omega11.universal.util.log.Loger;
import jp.co.omega11.universal.util.net.mail.pop.AbstractPop3;
import jp.co.omega11.universal.util.net.mail.pop.Pop3;
import jp.co.omega11.universal.util.net.mail.pop.SslPop3;
import jp.co.omega11.universal.util.net.mail.smtp.AbstractSmtp;
import jp.co.omega11.universal.util.net.mail.smtp.Smtp;
import jp.co.omega11.universal.util.net.mail.smtp.SslSmtp;

/**
 * ���[���̑���M���g���A�V�X�e�����R���g���[�����܂�
 * 
 * @author Wizard1 2009
 * @see http://www.sk-jp.com/book/javamail/contents/javamail_protocols.html
 */

/**
 * 
 * �g�у��[������ 5000����
 * http://www.au.kddi.com/service/email/tsukaikata/kino/index.html
 * 
 */
public class ControlFromMail extends AbstractReceiveCommand {

	private POP3Model pop3Model;
	private SmtpModel smtpModel;
	private long sleepTime;

	/**
	 * �R���X�g���N�^
	 * 
	 * @param pop3Model
	 * @param smtpModel
	 * @param sleepTime
	 */
	public ControlFromMail(POP3Model pop3Model, SmtpModel smtpModel,
			long sleepTime) {
		this.pop3Model = pop3Model;
		this.smtpModel = smtpModel;
		this.sleepTime = sleepTime;
	}

	@Override
	public boolean init() {
		// �������f���̒��̃t�B�[���hNULL�`�F�b�N���炢�����ł��Ȃ�
		// �������͈�[���O�I���ł��邩�����e�X�g����
		if (pop3Model == null || smtpModel == null) {
			return false;
		}

		return true;
	}

	@Override
	public void receive(CommandModel commandModel,
			IExecuteCommand executeCommand) {
		try {
			int counter = 0;
			
			

			while (true) {
				
				counter++;
				
				Loger.print("Mail ��M�J�n " + counter);
				
				AbstractPop3 pop3;
				AbstractSmtp smtp;

				if (pop3Model.isSsl()) {
					pop3 = new SslPop3(pop3Model.getHost(),
							pop3Model.getPort(), pop3Model.getUsername(),
							pop3Model.getPassword());
				} else {
					pop3 = new Pop3(pop3Model.getHost(), pop3Model.getPort(),
							pop3Model.getUsername(), pop3Model.getPassword());
				}

				pop3.init();
				//�w�肵�����[���A�h���X+�����̂Ƃ���M
				String command = pop3.execute(pop3Model.getReceiveAdress(), pop3Model.getReceiveSubject());

				Loger.print("Mail �R�}���h��M " + command);

				// �R�}���h��NULL�łȂ��Ƃ��̂݌㑱�������s
				if (command != null) {

					commandModel.setReceiveCount(commandModel.getReceiveCount() + 1);
					commandModel.setCommand(command);

					// �R�}���h���s
					executeCommand.cmommandExe(commandModel);

					// ���ʂ𑗐M commandModel.getResultMsg()
					if(smtpModel.isSsl()) {
						smtp = new SslSmtp(smtpModel.getHost(), smtpModel.getPort() ,smtpModel.getUsername(), smtpModel.getPassword());
					} else {
						smtp = new Smtp(smtpModel.getHost(), smtpModel.getPort() ,smtpModel.getUsername(), smtpModel.getPassword());
					}
					
					smtp.init();
					smtp.execute(smtpModel.getMailToAdress(), smtpModel.getSubject(),  commandModel.getResultMsg());					
				}

				// �w�肵�����Ԃ����X���[�v���܂�
				Loger.print("Mail �X���[�v�J�n " +  String.valueOf(sleepTime) + "ms");
				
				Thread.sleep(sleepTime);

			}

		} catch (Exception e) {
			Loger.print(e);
			return;
		}
	}

}
