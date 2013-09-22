package jp.co.omega11.webcrawler.w2fj.dbaccesser.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;



import jp.co.omega11.universal.util.file.TextUtil;
import jp.co.omega11.universal.util.log.Log;
import jp.co.omega11.webcrawler.w2fj.dbaccesser.dao.vendor.IindividualSQL;
import jp.co.omega11.webcrawler.w2fj.dbaccesser.dao.vendor.MySQL;



/**
 * ��{�I��DAO�̒��ۃN���X
 * 1�e�[�u�� 1DAO
 *
 * JDBC API ����
 * http://sdc.sun.co.jp/java/docs/j2se/1.4/ja/docs/ja/guide/jdbc/getstart/GettingStartedTOC.fm.html
 */

/**
 * @author Wizard1
 *
 */
public abstract class AbstractBaseDAO {

	/**
	 * �q�X�g���[�e�[�u���w�b�_
	 *
	 * ex contents_news_history
	 */
	protected static final String HISTORY = "history";

	/**
	 * �o�b�N�A�b�v�e�[�u���w�b�_
	 */
	protected static final String BAKUP = "backup";

	// �e�[�u����
	protected String tableName = null;

	/**
	 * �q�X�g���[�e�[�u���Ȃǂł͕ύX����K�v������̂ŕϐ��ɂ���getter/setter�ŕύX
	 */
	protected String tableNameHead = null;

	// �R�l�N�V����
	protected Connection con = null;

	// DML SQL
	protected String dmlSqlFileName = null;

	// DB�x���_�[
	protected String databaseProductName = null;

	/**
	 * �R���X�g���N�^�[
	 * @param con
	 * @param ItaName
	 */
	public AbstractBaseDAO(Connection con, String ItaName , String _tableName) {
		tableNameHead = _tableName;
		this.con = con;
		tableName = tableNameHead + "_" + ItaName.toUpperCase();

		DatabaseMetaData meta;

		try {
			meta = con.getMetaData();

			databaseProductName = meta.getDatabaseProductName();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.print(e);
			return;
		}
	}


	/**
	 * �Y���e�[�u�������邩���`�F�b�N���܂��B�Ȃ����false
	 */
	public boolean checkExsistTable() {

		IindividualSQL inIindividualSQL;

		if(databaseProductName.equals(DbConstant.MYSQL)) {
			inIindividualSQL = new MySQL();
			return inIindividualSQL.checkExsistTable(con, tableName);
		}

		return false;
	}

	/**
	 * �e�[�u���쐬��DDL�𔭍s���܂�
	 * @param stringDDL
	 * @throws SQLException
	 */
	public void createTable(Connection con) throws SQLException{
		if(databaseProductName.equals(DbConstant.MYSQL)) {
		 // �t�@�C������_Mysql�݂����ɂ���DDL���킯��
			String sql = TextUtil.file2String(DbConstant.DDL_DIRECTORY + "/" + DbConstant.MYSQL + "/" + tableNameHead + ".ddl");
			Log.print(sql);

			String sql2 = sql.replaceAll("\\?", tableName);

			Log.print(sql2);

			Statement stmt = con.createStatement();

			stmt.executeUpdate(sql2);
			stmt.close();

		}
		//dmlSQLfile��ǂݍ���Ŏ��s
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}



	/**
	 * @return the tableNameHead
	 */
	public String getTableNameHead() {
		return tableNameHead;
	}

	/**
	 * @param tableNameHead the tableNameHead to set
	 */
	public void setTableNameHead(String tableNameHead) {
		this.tableNameHead = tableNameHead;
	}

	/**
	 * @return the dmlSqlFileName
	 */
	public String getDmlSqlFileName() {
		return dmlSqlFileName;
	}

	/**
	 * @param dmlSqlFileName the dmlSqlFileName to set
	 */
	public void setDmlSqlFileName(String dmlSqlFileName) {
		this.dmlSqlFileName = dmlSqlFileName;
	}


}
