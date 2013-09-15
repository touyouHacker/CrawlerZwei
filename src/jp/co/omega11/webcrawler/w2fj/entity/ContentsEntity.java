package jp.co.omega11.webcrawler.w2fj.entity;

import java.sql.Timestamp;

public class ContentsEntity implements Cloneable {
	/**
	 * CONTENT_ID DAT_ID URL FIRST_HIT_THREAD_NUM CONTENT_SIZE EXE_FLAG
	 * HTTP_STATE REGEX_HIT_COUNT URL_REGEX_FILTER FILTER_SETNAME SAVE_DIRECTORY
	 * CREATEDATE CREATE_USER RENEWDATE RENEW_USER FILE_NAME DOWN_WORKER_ID
	 * RESURL_COMMENT
	 */

	/**
	 * 最大バイト数決定参考資料
	 * 
	 * URL 最大長
	 * http://www.connect-i.co.jp/tipsblog/?p=6
	 * 
	 * ファイル名、パス名
	 * http://ja.wikipedia.org/wiki/%E3%83%95%E3%82%A1%E3%82%A4%E3%83%AB%E3%82%B7%E3%82%B9%E3%83%86%E3%83%A0
	 * ファイル名
	 * 　　　　(NTFS,EXT3) 255 
	 * 			HFS+は255*2
	 * パス 
	 * 　　 EXT3,HFS+ は無制限
	 *      NTFSはUnicodeで32,767
	 * 
	 */
	
	
	private int contentId;
	private int datId;
	private String url;
	private int firstHitThreadNum;
	private long contentSize;
	private int exeFlag;
	private String httpState;
	private int regexHitCount;
	private String urlRegexFilter;
	private String filterSetname;
	private String saveDirectory;
	private Timestamp createdate;
	private String createUser;
	private Timestamp renewdate;
	private String renewUser;
	private String fileName;
	private String downWorkerId;
	private String resurlComment;

	@Override
	public Object clone() {
		try {
			return (super.clone());
		} catch (CloneNotSupportedException e) {
			throw (new InternalError(e.getMessage()));
		}
	}

	/**
	 * @return the contentId
	 */
	public int getContentId() {
		return contentId;
	}

	/**
	 * @param contentId
	 *            the contentId to set
	 */
	public void setContentId(int contentId) {
		this.contentId = contentId;
	}

	/**
	 * @return the datId
	 */
	public int getDatId() {
		return datId;
	}

	/**
	 * @param datId
	 *            the datId to set
	 */
	public void setDatId(int datId) {
		this.datId = datId;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the firstHitThreadNum
	 */
	public int getFirstHitThreadNum() {
		return firstHitThreadNum;
	}

	/**
	 * @param firstHitThreadNum
	 *            the firstHitThreadNum to set
	 */
	public void setFirstHitThreadNum(int firstHitThreadNum) {
		this.firstHitThreadNum = firstHitThreadNum;
	}

	/**
	 * @return the contentSize
	 */
	public long getContentSize() {
		return contentSize;
	}

	/**
	 * @param contentSize
	 *            the contentSize to set
	 */
	public void setContentSize(long contentSize) {
		this.contentSize = contentSize;
	}

	/**
	 * @return the exeFlag
	 */
	public int getExeFlag() {
		return exeFlag;
	}

	/**
	 * @param exeFlag
	 *            the exeFlag to set
	 */
	public void setExeFlag(int exeFlag) {
		this.exeFlag = exeFlag;
	}

	/**
	 * @return the httpState
	 */
	public String getHttpState() {
		return httpState;
	}

	/**
	 * @param httpState
	 *            the httpState to set
	 */
	public void setHttpState(String httpState) {
		this.httpState = httpState;
	}

	/**
	 * @return the regexHitCount
	 */
	public int getRegexHitCount() {
		return regexHitCount;
	}

	/**
	 * @param regexHitCount
	 *            the regexHitCount to set
	 */
	public void setRegexHitCount(int regexHitCount) {
		this.regexHitCount = regexHitCount;
	}

	/**
	 * @return the urlRegexFilter
	 */
	public String getUrlRegexFilter() {
		return urlRegexFilter;
	}

	/**
	 * @param urlRegexFilter
	 *            the urlRegexFilter to set
	 */
	public void setUrlRegexFilter(String urlRegexFilter) {
		this.urlRegexFilter = urlRegexFilter;
	}

	/**
	 * @return the filterSetname
	 */
	public String getFilterSetname() {
		return filterSetname;
	}

	/**
	 * @param filterSetname
	 *            the filterSetname to set
	 */
	public void setFilterSetname(String filterSetname) {
		this.filterSetname = filterSetname;
	}

	/**
	 * @return the saveDirectory
	 */
	public String getSaveDirectory() {
		return saveDirectory;
	}

	/**
	 * @param saveDirectory
	 *            the saveDirectory to set
	 */
	public void setSaveDirectory(String saveDirectory) {
		this.saveDirectory = saveDirectory;
	}

	/**
	 * @return the createdate
	 */
	public Timestamp getCreatedate() {
		return createdate;
	}

	/**
	 * @param createdate
	 *            the createdate to set
	 */
	public void setCreatedate(Timestamp createdate) {
		this.createdate = createdate;
	}

	/**
	 * @return the createUser
	 */
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * @param createUser
	 *            the createUser to set
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	/**
	 * @return the renewdate
	 */
	public Timestamp getRenewdate() {
		return renewdate;
	}

	/**
	 * @param renewdate
	 *            the renewdate to set
	 */
	public void setRenewdate(Timestamp renewdate) {
		this.renewdate = renewdate;
	}

	/**
	 * @return the renewUser
	 */
	public String getRenewUser() {
		return renewUser;
	}

	/**
	 * @param renewUser
	 *            the renewUser to set
	 */
	public void setRenewUser(String renewUser) {
		this.renewUser = renewUser;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the downWorkerId
	 */
	public String getDownWorkerId() {
		return downWorkerId;
	}

	/**
	 * @param downWorkerId
	 *            the downWorkerId to set
	 */
	public void setDownWorkerId(String downWorkerId) {
		this.downWorkerId = downWorkerId;
	}

	/**
	 * @return the resurlComment
	 */
	public String getResurlComment() {
		return resurlComment;
	}

	/**
	 * @param resurlComment
	 *            the resurlComment to set
	 */
	public void setResurlComment(String resurlComment) {
		this.resurlComment = resurlComment;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + contentId;
		result = prime * result + (int) (contentSize ^ (contentSize >>> 32));
		result = prime * result
				+ ((createUser == null) ? 0 : createUser.hashCode());
		result = prime * result
				+ ((createdate == null) ? 0 : createdate.hashCode());
		result = prime * result + datId;
		result = prime * result
				+ ((downWorkerId == null) ? 0 : downWorkerId.hashCode());
		result = prime * result + exeFlag;
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result
				+ ((filterSetname == null) ? 0 : filterSetname.hashCode());
		result = prime * result + firstHitThreadNum;
		result = prime * result
				+ ((httpState == null) ? 0 : httpState.hashCode());
		result = prime * result + regexHitCount;
		result = prime * result
				+ ((renewUser == null) ? 0 : renewUser.hashCode());
		result = prime * result
				+ ((renewdate == null) ? 0 : renewdate.hashCode());
		result = prime * result
				+ ((resurlComment == null) ? 0 : resurlComment.hashCode());
		result = prime * result
				+ ((saveDirectory == null) ? 0 : saveDirectory.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result
				+ ((urlRegexFilter == null) ? 0 : urlRegexFilter.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContentsEntity other = (ContentsEntity) obj;
		if (contentId != other.contentId)
			return false;
		if (contentSize != other.contentSize)
			return false;
		if (createUser == null) {
			if (other.createUser != null)
				return false;
		} else if (!createUser.equals(other.createUser))
			return false;
		if (createdate == null) {
			if (other.createdate != null)
				return false;
		} else if (!createdate.equals(other.createdate))
			return false;
		if (datId != other.datId)
			return false;
		if (downWorkerId == null) {
			if (other.downWorkerId != null)
				return false;
		} else if (!downWorkerId.equals(other.downWorkerId))
			return false;
		if (exeFlag != other.exeFlag)
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (filterSetname == null) {
			if (other.filterSetname != null)
				return false;
		} else if (!filterSetname.equals(other.filterSetname))
			return false;
		if (firstHitThreadNum != other.firstHitThreadNum)
			return false;
		if (httpState == null) {
			if (other.httpState != null)
				return false;
		} else if (!httpState.equals(other.httpState))
			return false;
		if (regexHitCount != other.regexHitCount)
			return false;
		if (renewUser == null) {
			if (other.renewUser != null)
				return false;
		} else if (!renewUser.equals(other.renewUser))
			return false;
		if (renewdate == null) {
			if (other.renewdate != null)
				return false;
		} else if (!renewdate.equals(other.renewdate))
			return false;
		if (resurlComment == null) {
			if (other.resurlComment != null)
				return false;
		} else if (!resurlComment.equals(other.resurlComment))
			return false;
		if (saveDirectory == null) {
			if (other.saveDirectory != null)
				return false;
		} else if (!saveDirectory.equals(other.saveDirectory))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (urlRegexFilter == null) {
			if (other.urlRegexFilter != null)
				return false;
		} else if (!urlRegexFilter.equals(other.urlRegexFilter))
			return false;
		return true;
	}
}
