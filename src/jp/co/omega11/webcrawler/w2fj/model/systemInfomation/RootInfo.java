/**
 * 
 */
package jp.co.omega11.webcrawler.w2fj.model.systemInfomation;

/**
 * @author Wizard1 2009
 * 
 */
public class RootInfo extends AbstractInformation {

	public RootInfo(String name , String itaName) {
		super(name);
		this.itaName = itaName;
		
		subjectInfo = new ThreadComponentInfo();
		datInfo = new ThreadComponentInfo();
		contentsInfo = new ThreadComponentInfo();
	}

	private String itaName;
	
	private ThreadComponentInfo subjectInfo;

	private ThreadComponentInfo datInfo;

	private ThreadComponentInfo contentsInfo;


	/**
	 * @return the itaName
	 */
	public String getItaName() {
		return itaName;
	}

	/**
	 * @param itaName the itaName to set
	 */
	public void setItaName(String itaName) {
		this.itaName = itaName;
	}

	/**
	 * @return the subjectInfo
	 */
	public ThreadComponentInfo getSubjectInfo() {
		return subjectInfo;
	}

	/**
	 * @param subjectInfo
	 *            the subjectInfo to set
	 */
	public void setSubjectInfo(ThreadComponentInfo subjectInfo) {
		this.subjectInfo = subjectInfo;
	}

	/**
	 * @return the datInfo
	 */
	public ThreadComponentInfo getDatInfo() {
		return datInfo;
	}

	/**
	 * @param datInfo
	 *            the datInfo to set
	 */
	public void setDatInfo(ThreadComponentInfo datInfo) {
		this.datInfo = datInfo;
	}

	/**
	 * @return the contentsInfo
	 */
	public ThreadComponentInfo getContentsInfo() {
		return contentsInfo;
	}

	/**
	 * @param contentsInfo
	 *            the contentsInfo to set
	 */
	public void setContentsInfo(ThreadComponentInfo contentsInfo) {
		this.contentsInfo = contentsInfo;
	}

}
