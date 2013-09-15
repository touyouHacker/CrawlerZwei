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
	 * Subject.txt比較より算出した取得ターゲット数
	 */
	private int datDonwnloadTargetInSubCount;

	/**
	 * 実際にパースしたＤＡＴ数
	 */
	private int parseDatCounter;
	
	
	@Override
	public final String toString(){
		String retMsg = "";
		
		retMsg += "DAT取得ターゲット数(Subject比較より算出) " + datDonwnloadTargetInSubCount + " \r\n";
		retMsg += "実際にパースしたDAT数 " + parseDatCounter + " \r\n";
		
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

