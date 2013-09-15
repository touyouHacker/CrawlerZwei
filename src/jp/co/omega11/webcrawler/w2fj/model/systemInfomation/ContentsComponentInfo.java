/**
 * 
 */
package jp.co.omega11.webcrawler.w2fj.model.systemInfomation;

/**
 * @author Wizard1 2009
 * 
 */
public class ContentsComponentInfo extends AbstractComponentInfo {
	
	/** ダウンロードURLの総数*/
	private int downloadTargetCount;

	/** HTTP通信クラスで例外が発生した回数*/
	private int httpExeceptioConunt;
	
	/** all・・起動してからのすべての数*/
	private int allDownloadTargetCount;
	
	private int allHttpExeceptioConunt;
	
	

	@Override
	public final String toString()
	{
		String retMsg = "";
		
		retMsg += "直前間隔のダウンロードURLの総数 " + downloadTargetCount + " \r\n";
		retMsg += "直前間隔のHTTP通信クラスで例外が発生した回数 " + httpExeceptioConunt + " \r\n";
		retMsg += "起動総数のダウンロードURLの総数 " + allDownloadTargetCount + " \r\n";
		retMsg += "起動総数のHTTP通信クラスで例外が発生した回数 " + allHttpExeceptioConunt + " \r\n";
		
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
