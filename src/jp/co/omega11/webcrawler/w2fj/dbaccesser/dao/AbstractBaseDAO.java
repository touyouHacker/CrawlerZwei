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
 * 基本的なDAOの抽象クラス
 * 1テーブル 1DAO
 *
 * JDBC API 入門
 * http://sdc.sun.co.jp/java/docs/j2se/1.4/ja/docs/ja/guide/jdbc/getstart/GettingStartedTOC.fm.html
 */

/**
 * @author Wizard1
 *
 */
public abstract class AbstractBaseDAO {

	/**
	 * ヒストリーテーブルヘッダ
	 *
	 * ex contents_news_history
	 */
	protected static final String HISTORY = "history";

	/**
	 * バックアップテーブルヘッダ
	 */
	protected static final String BAKUP = "backup";

	// テーブル名
	protected String tableName = null;

	/**
	 * ヒストリーテーブルなどでは変更する必要があるので変数にもちgetter/setterで変更
	 */
	protected String tableNameHead = null;

	// コネクション
	protected Connection con = null;

	// DML SQL
	protected String dmlSqlFileName = null;

	// DBベンダー
	protected String databaseProductName = null;

	/**
	 * コンストラクター
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
	 * 該当テーブルがあるかをチェックします。なければfalse
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
	 * テーブル作成のDDLを発行します
	 * @param stringDDL
	 * @throws SQLException
	 */
	public void createTable(Connection con) throws SQLException{
		if(databaseProductName.equals(DbConstant.MYSQL)) {
		 // ファイル名に_MysqlみたいにつけてDDLをわける
			String sql = TextUtil.file2String(DbConstant.DDL_DIRECTORY + "/" + DbConstant.MYSQL + "/" + tableNameHead + ".ddl");
			Log.print(sql);

			String sql2 = sql.replaceAll("\\?", tableName);

			Log.print(sql2);

			Statement stmt = con.createStatement();

			stmt.executeUpdate(sql2);
			stmt.close();

		}
		//dmlSQLfileを読み込んで実行
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
