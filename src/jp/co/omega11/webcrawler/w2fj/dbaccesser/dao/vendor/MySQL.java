package jp.co.omega11.webcrawler.w2fj.dbaccesser.dao.vendor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jp.co.omega11.universal.util.log.Log;

public class MySQL implements IindividualSQL {

	/**
	 * 対象テーブルが存在しているかをチェックします 存在してなければfalse してるなら true
	 * 
	 * @param con
	 *            コネクション
	 * @param table
	 *            対象テーブル
	 * @return テーブルが存在してるならtrue,してないならfalse
	 */
	public boolean checkExsistTable(Connection con, String table) {
		// TODO Auto-generated method stub
		// SHOW TABLE STATUS; するとスキーマのテーブル一覧がかえる

		ResultSet rs;
		Statement stmt;

		String query = "SHOW TABLE STATUS";
		try {
			stmt = con.createStatement();

			rs = stmt.executeQuery(query);

			while (rs.next()) {
					String rss = rs.getString("Name");
				if (0 == table.compareToIgnoreCase(rss)) {
					Log.print(table +  "テーブルが存在してるのでDDL投入未実施");
					rs.close();
					stmt.close();
					return true;
				}
			}

		} catch (SQLException e) {
			Log.printSQLException(e);

			return true; // 例外がおきても何もできないのでtrueを返しておく
		}

		return false;

	}

}
