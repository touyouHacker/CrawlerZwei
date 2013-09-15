package jp.co.omega11.universal.util.net.http;

import java.util.List;
import java.util.Map;

import jp.co.omega11.universal.util.log.Loger;


/**
 * HTTP���X�|���X�Ə������ʂ��i�[����\���̃N���X
 * 
 * @author Wizard1
 * 
 */
public class HttpHeaderSave {
	private int responseCode;
	private long lastModified;
	private long contentLength;
	private Map<String, List<String>> responshash;
	private String exceptionMsg = "";
	
	/**
	 * ��O���N�������̃t���O
	 */
	private boolean exception;
	
	public HttpHeaderSave() {
		// TODO Auto-generated constructor stub
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}
	
	/**
	 * int���R���o�[�g����
	 * @param contentLength
	 */
	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public Map<String, List<String>> getResponshash() {
		return responshash;
	}

	public void setResponshash(Map<String, List<String>> responshash) {
		this.responshash = responshash;
	}

	/**
	 * ���X�|���X���v�����g����
	 * @param responshash
	 */
	public void printRespoons() {
		for(Map.Entry<String,List<String>> map :responshash.entrySet()){
			 Loger.print(map.getKey()+" : "+map.getValue());
		}
	}

	/**
	 * @return the exceptionMsg
	 */
	public String getExceptionMsg() {
		return exceptionMsg;
	}

	/**
	 * @param exceptionMsg the exceptionMsg to set
	 */
	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}

	/**
	 * @return the exception
	 */
	public boolean isException() {
		return exception;
	}

	/**
	 * @param exception the exception to set
	 */
	public void setException(boolean exception) {
		this.exception = exception;
	}
	
	
}