/**
 * 
 */
package jp.co.omega11.webcrawler.w2fj.model;

/**
 * @author Wizard1 2009
 * HttpURLConnectionとHTTPのオプションを設定する値
 */
public class HttpOptionSetModel {
	
	private String userAgent;
	private int ReTryDownload;
	private int ConnectTimeout;
	private int ReadTimeout;
	
	
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public int getReTryDownload() {
		return ReTryDownload;
	}
	public void setReTryDownload(int reTryDownload) {
		ReTryDownload = reTryDownload;
	}
	/**
	 * @return the connectTimeout
	 */
	public int getConnectTimeout() {
		return ConnectTimeout;
	}
	/**
	 * @param connectTimeout the connectTimeout to set
	 */
	public void setConnectTimeout(int connectTimeout) {
		ConnectTimeout = connectTimeout;
	}
	/**
	 * @return the readTimeout
	 */
	public int getReadTimeout() {
		return ReadTimeout;
	}
	/**
	 * @param readTimeout the readTimeout to set
	 */
	public void setReadTimeout(int readTimeout) {
		ReadTimeout = readTimeout;
	}

}

