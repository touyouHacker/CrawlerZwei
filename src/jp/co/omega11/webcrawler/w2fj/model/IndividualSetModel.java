/**
 * 
 */
package jp.co.omega11.webcrawler.w2fj.model;

/**
 * @author Wizard1 2009
 *  1コンポーネントで個々にもつ設定値
 */
public class IndividualSetModel {
	private String saveDownload;
	private long sleepTime;
	private HttpOptionSetModel http;

	public IndividualSetModel() {
		this.http = new HttpOptionSetModel();
	}

	public String getSaveDownload() {
		return saveDownload;
	}

	public void setSaveDownload(String saveDownload) {
		this.saveDownload = saveDownload;
	}

	public HttpOptionSetModel getHttp() {
		return http;
	}

	public void setHttp(HttpOptionSetModel http) {
		this.http = http;
	}

	public long getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}

}
