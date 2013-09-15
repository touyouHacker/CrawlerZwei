/**
 * 
 */
package jp.co.omega11.webcrawler.w2fj.model;

/**
 * @author Wizard1 2009
 * 
 *         SubjectTextEntityとContentsEntityの複合モデル Contentsの保存Folderを作成するために必要
 * 
 */
public class ContentsFolderNameModel implements Cloneable {



	/**
	 * SubjectTextEntity id ContentsEntity datId 結合キー
	 */
	private int datId;

	/**
	 * SubjectTextEntity
	 */
	private String threadNumber;

	/**
	 * SubjectTextEntity
	 */
	private String title;

	public Object clone() {
		try {
			return (super.clone());
		} catch (CloneNotSupportedException e) {
			throw (new InternalError(e.getMessage()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + datId;
		result = prime * result
				+ ((threadNumber == null) ? 0 : threadNumber.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
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
		ContentsFolderNameModel other = (ContentsFolderNameModel) obj;
		if (datId != other.datId)
			return false;
		if (threadNumber == null) {
			if (other.threadNumber != null)
				return false;
		} else if (!threadNumber.equals(other.threadNumber))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
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
	 * @return the threadNumber
	 */
	public String getThreadNumber() {
		return threadNumber;
	}

	/**
	 * @param threadNumber
	 *            the threadNumber to set
	 */
	public void setThreadNumber(String threadNumber) {
		this.threadNumber = threadNumber;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

}
