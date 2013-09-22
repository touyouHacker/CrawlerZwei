package jp.co.omega11.webcrawler.w2fj.dbaccesser.dao.vendor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jp.co.omega11.universal.util.log.Log;

public class MySQL implements IindividualSQL {

	/**
	 * �Ώۃe�[�u�������݂��Ă��邩���`�F�b�N���܂� ���݂��ĂȂ����false ���Ă�Ȃ� true
	 * 
	 * @param con
	 *            �R�l�N�V����
	 * @param table
	 *            �Ώۃe�[�u��
	 * @return �e�[�u�������݂��Ă�Ȃ�true,���ĂȂ��Ȃ�false
	 */
	public boolean checkExsistTable(Connection con, String table) {
		// TODO Auto-generated method stub
		// SHOW TABLE STATUS; ����ƃX�L�[�}�̃e�[�u���ꗗ��������

		ResultSet rs;
		Statement stmt;

		String query = "SHOW TABLE STATUS";
		try {
			stmt = con.createStatement();

			rs = stmt.executeQuery(query);

			while (rs.next()) {
					String rss = rs.getString("Name");
				if (0 == table.compareToIgnoreCase(rss)) {
					Log.print(table +  "�e�[�u�������݂��Ă�̂�DDL���������{");
					rs.close();
					stmt.close();
					return true;
				}
			}

		} catch (SQLException e) {
			Log.printSQLException(e);

			return true; // ��O�������Ă������ł��Ȃ��̂�true��Ԃ��Ă���
		}

		return false;

	}

}
