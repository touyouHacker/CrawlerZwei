package jp.co.omega11.universal.controller.receivecommand.model;

public class POP3Model extends MailBaseModel {

	/**
	 * ��M�������郁�[���A�h���X
	 */
	private String receiveAdress;
	
	
	/**
	 * ��M�������錏��
	 */
	private String receiveSubject;

	/**
	 * @return the receiveAdress
	 */
	public String getReceiveAdress() {
		return receiveAdress;
	}

	/**
	 * @param receiveAdress the receiveAdress to set
	 */
	public void setReceiveAdress(String receiveAdress) {
		this.receiveAdress = receiveAdress;
	}

	/**
	 * @return the receiveSubject
	 */
	public String getReceiveSubject() {
		return receiveSubject;
	}

	/**
	 * @param receiveSubject the receiveSubject to set
	 */
	public void setReceiveSubject(String receiveSubject) {
		this.receiveSubject = receiveSubject;
	}
}

