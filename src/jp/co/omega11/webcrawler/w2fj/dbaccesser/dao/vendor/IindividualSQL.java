/**
 * 
 */
package jp.co.omega11.webcrawler.w2fj.dbaccesser.dao.vendor;

import java.sql.Connection;

/**
 * 
 * ベンダーで個別実装するあるコードはこれを継承して実装
 * 
 * @author Wizard1 2009
 * 
 *
 */
public interface IindividualSQL {

	/**
	 * テーブルが存在しているかをチェックします
	 * @return
	 */
	public boolean checkExsistTable(Connection con, String table);
}

