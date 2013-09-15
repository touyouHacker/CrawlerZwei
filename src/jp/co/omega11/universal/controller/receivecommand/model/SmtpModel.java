package jp.co.omega11.universal.controller.receivecommand.model;

public class SmtpModel extends MailBaseModel {

	
	private String mailToAdress;
	private String subject;
	private String messageBody;
	/**
	 * @return the mailToAdress
	 */
	public String getMailToAdress() {
		return mailToAdress;
	}
	/**
	 * @param mailToAdress the mailToAdress to set
	 */
	public void setMailToAdress(String mailToAdress) {
		this.mailToAdress = mailToAdress;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the messageBody
	 */
	public String getMessageBody() {
		return messageBody;
	}
	/**
	 * @param messageBody the messageBody to set
	 */
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	
	
}

