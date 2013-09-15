package jp.co.omega11.webcrawler.w2fj.entity;


import java.sql.Timestamp;


public class SubjectTextEntity implements Cloneable {

	/*
	 * id threadNumber title threadResCount downloadStartpoint contentLength
	 * httpState downloadCount deleateFlag createdate renewdate
	 */
	private int id;

	private String threadNumber;

	private String title;

	private int threadResCount;

	private int downloadStartpoint;

	private long contentLength;

	private String httpState;

	private int downloadCount;

	private int deleateFlag;

	private Timestamp createdate;

	private Timestamp renewdate;
	
	private int downloaded;

	public Object clone() {
		try {
			return (super.clone());
		} catch (CloneNotSupportedException e) {
			throw (new InternalError(e.getMessage()));
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getThreadNumber() {
		return threadNumber;
	}

	public void setThreadNumber(String threadNumber) {
		this.threadNumber = threadNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getThreadResCount() {
		return threadResCount;
	}

	public void setThreadResCount(int threadResCount) {
		this.threadResCount = threadResCount;
	}

	public int getDownloadStartpoint() {
		return downloadStartpoint;
	}

	public void setDownloadStartpoint(int downloadStartpoint) {
		this.downloadStartpoint = downloadStartpoint;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public String getHttpState() {
		return httpState;
	}

	public void setHttpState(String httpState) {
		this.httpState = httpState;
	}

	public int getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(int downloadCount) {
		this.downloadCount = downloadCount;
	}

	public int getDeleateFlag() {
		return deleateFlag;
	}

	public void setDeleateFlag(int deleateFlag) {
		this.deleateFlag = deleateFlag;
	}

	public Timestamp getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Timestamp createdate) {
		this.createdate = createdate;
	}

	public Timestamp getRenewdate() {
		return renewdate;
	}

	public void setRenewdate(Timestamp renewdate) {
		this.renewdate = renewdate;
	}

	public int getDownloaded() {
		return downloaded;
	}

	public void setDownloaded(int downloaded) {
		this.downloaded = downloaded;
	}

}
