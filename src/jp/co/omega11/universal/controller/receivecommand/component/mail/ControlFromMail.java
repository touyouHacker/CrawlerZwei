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
 * メールの送受信を使い、システムをコントロールします
 * 
 * @author Wizard1 2009
 * @see http://www.sk-jp.com/book/javamail/contents/javamail_protocols.html
 */

/**
 * 
 * 携帯メール制限 5000文字
 * http://www.au.kddi.com/service/email/tsukaikata/kino/index.html
 * 
 */
public class ControlFromMail extends AbstractReceiveCommand {

	private POP3Model pop3Model;
	private SmtpModel smtpModel;
	private long sleepTime;

	/**
	 * コンストラクタ
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
		// 引数モデルの中のフィールドNULLチェックぐらいしかできない
		// もしくは一端ログオンできるかだけテストする
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
				
				Loger.print("Mail 受信開始 " + counter);
				
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
				//指定したメールアドレス+件名のとき受信
				String command = pop3.execute(pop3Model.getReceiveAdress(), pop3Model.getReceiveSubject());

				Loger.print("Mail コマンド受信 " + command);

				// コマンドがNULLでないときのみ後続処理実行
				if (command != null) {

					commandModel.setReceiveCount(commandModel.getReceiveCount() + 1);
					commandModel.setCommand(command);

					// コマンド実行
					executeCommand.cmommandExe(commandModel);

					// 結果を送信 commandModel.getResultMsg()
					if(smtpModel.isSsl()) {
						smtp = new SslSmtp(smtpModel.getHost(), smtpModel.getPort() ,smtpModel.getUsername(), smtpModel.getPassword());
					} else {
						smtp = new Smtp(smtpModel.getHost(), smtpModel.getPort() ,smtpModel.getUsername(), smtpModel.getPassword());
					}
					
					smtp.init();
					smtp.execute(smtpModel.getMailToAdress(), smtpModel.getSubject(),  commandModel.getResultMsg());					
				}

				// 指定した時間だけスリープします
				Loger.print("Mail スリープ開始 " +  String.valueOf(sleepTime) + "ms");
				
				Thread.sleep(sleepTime);

			}

		} catch (Exception e) {
			Loger.print(e);
			return;
		}
	}

}
