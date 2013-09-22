package jp.co.omega11.universal.util.net.http;

import java.util.Map;

/**
 * HTTPオプションを保持する構造体
 * @author Wizard1
 *
 */
public class HttpOptionAndUrl {

	private String url;	
	
	private Map<String,String> httpOptionMap;
	
	public HttpOptionAndUrl(String _url, Map<String,String> _httpOptionMap) {
		
		url = _url;
		httpOptionMap = _httpOptionMap;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getHttpOptionMap() {
		return httpOptionMap;
	}

	public void setHttpOptionMap(Map<String, String> httpOptionMap) {
		this.httpOptionMap = httpOptionMap;
	}
}
