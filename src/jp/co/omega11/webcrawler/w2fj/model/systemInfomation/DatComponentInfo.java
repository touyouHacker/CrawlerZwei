/**
 * 
 */
package jp.co.omega11.webcrawler.w2fj.model.systemInfomation;

/**
 * @author Wizard1 2009
 *
 */
public class DatComponentInfo extends AbstractComponentInfo{


	/**
	 * Subject.txt��r���Z�o�����擾�^�[�Q�b�g��
	 */
	private int datDonwnloadTargetInSubCount;

	/**
	 * ���ۂɃp�[�X�����c�`�s��
	 */
	private int parseDatCounter;
	
	
	@Override
	public final String toString(){
		String retMsg = "";
		
		retMsg += "DAT�擾�^�[�Q�b�g��(Subject��r���Z�o) " + datDonwnloadTargetInSubCount + " \r\n";
		retMsg += "���ۂɃp�[�X����DAT�� " + parseDatCounter + " \r\n";
		
		return retMsg;
		
	}
	
	/**
	 * @return the datDonwnloadTargetInSubCount
	 */
	public int getDatDonwnloadTargetInSubCount() {
		return datDonwnloadTargetInSubCount;
	}

	/**
	 * @param datDonwnloadTargetInSubCount the datDonwnloadTargetInSubCount to set
	 */
	public void setDatDonwnloadTargetInSubCount(int datDonwnloadTargetInSubCount) {
		this.datDonwnloadTargetInSubCount = datDonwnloadTargetInSubCount;
	}

	/**
	 * @return the parseDatCounter
	 */
	public int getParseDatCounter() {
		return parseDatCounter;
	}

	/**
	 * @param parseDatCounter the parseDatCounter to set
	 */
	public void setParseDatCounter(int parseDatCounter) {
		this.parseDatCounter = parseDatCounter;
	}

}

