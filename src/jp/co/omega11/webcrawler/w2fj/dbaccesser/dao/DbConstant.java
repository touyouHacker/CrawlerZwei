/**
 * 
 */
package jp.co.omega11.webcrawler.w2fj.dbaccesser.dao;

/**
 * @author Wizard1 2009
 *
 */
public final class DbConstant {

	private DbConstant() {
		
	}
	
	/*-------------- DB���i�� ----------------------------*/
	
	public static final String MYSQL = "MySQL";
	
	public static final String POSTGRESQL = "postgresql";
	
	public static final String ORACLE = "oracle";
	
	
	/*-------------- �����V�X�e���ϐ� -------------------------*/
	/**
	 * DDL���i�[����Ă���t�H���_
	 */
	public static final String DDL_DIRECTORY = "DDL";
	
	
	/*------------- Subject -----------------------------------------*/
	public static final String SUJECTTABLENAME = "SUBJECTTEXT";
	
	
	/*-------------- Contents ---------------------------------------*/
	public static final String CONTENTSTABLENAME = "CONTENTS";
	
	public static final int DOWNLOAD_DEFAULT = 0; 
	
	public static final int DOWNLOAD_NOTNEXT = -1;
	
	public static final int DOWNLOAD_SUCCESS = 1; 
}

