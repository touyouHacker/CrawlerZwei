/**
 * 
 */
package jp.co.omega11.webcrawler.w2fj.model.systemInfomation;

/**
 * @author Wizard1 2009
 * 
 */
public class ContentsComponentInfo extends AbstractComponentInfo {
	
	/** �_�E�����[�hURL�̑���*/
	private int downloadTargetCount;

	/** HTTP�ʐM�N���X�ŗ�O������������*/
	private int httpExeceptioConunt;
	
	/** all�E�E�N�����Ă���̂��ׂĂ̐�*/
	private int allDownloadTargetCount;
	
	private int allHttpExeceptioConunt;
	
	

	@Override
	public final String toString()
	{
		String retMsg = "";
		
		retMsg += "���O�Ԋu�̃_�E�����[�hURL�̑��� " + downloadTargetCount + " \r\n";
		retMsg += "���O�Ԋu��HTTP�ʐM�N���X�ŗ�O������������ " + httpExeceptioConunt + " \r\n";
		retMsg += "�N�������̃_�E�����[�hURL�̑��� " + allDownloadTargetCount + " \r\n";
		retMsg += "�N��������HTTP�ʐM�N���X�ŗ�O������������ " + allHttpExeceptioConunt + " \r\n";
		
		return retMsg;
	}
	
	/**
	 * @return the downloadTargetCount
	 */
	public int getDownloadTargetCount() {
		return downloadTargetCount;
	}

	/**
	 * @param downloadTargetCount the downloadTargetCount to set
	 */
	public void setDownloadTargetCount(int downloadTargetCount) {
		this.downloadTargetCount = downloadTargetCount;
	}

	/**
	 * @return the httpExeceptioConunt
	 */
	public int getHttpExeceptioConunt() {
		return httpExeceptioConunt;
	}

	/**
	 * @param httpExeceptioConunt the httpExeceptioConunt to set
	 */
	public void setHttpExeceptioConunt(int httpExeceptioConunt) {
		this.httpExeceptioConunt = httpExeceptioConunt;
	}



	public void addAllDownloadTargetCount() {
		this.allDownloadTargetCount += downloadTargetCount;
	}


	public void addAllhttpExeceptioConunt() {
		this.allHttpExeceptioConunt += httpExeceptioConunt;
	}

}
