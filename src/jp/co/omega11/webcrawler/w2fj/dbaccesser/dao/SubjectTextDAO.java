package jp.co.omega11.webcrawler.w2fj.dbaccesser.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jp.co.omega11.universal.util.log.Loger;
import jp.co.omega11.webcrawler.w2fj.entity.SubjectTextEntity;


public class SubjectTextDAO extends AbstractBaseDAO  {




	// TODO MultiConnectionを実装 MySQL
	// Oracleなど二つのＤＢに同時に書き込むときconnectionをリストでもちコンストラクタを呼び出す

	public SubjectTextDAO(Connection con, String itaName) {
		super(con, itaName,DbConstant.SUJECTTABLENAME);
	}

	/* DDL

		dmlSql = "DROP TABLE IF EXISTS `test`.`subjecttext_news`; CREATE TABLE " +
		 " `test`.`subjecttext_news` ( `id` int(10) unsigned NOT NULL "+
		 " AUTO_INCREMENT, `threadNumber` varchar(10) NOT NULL, `title`"+
		"  varchar(100) NOT NULL, `threadResCount` int(10) unsigned NOT NULL,"+
		 " `downloadCount` int(10) unsigned NOT NULL DEFAULT '0',"+
		"  `downloadStartPoint` int(10) unsigned NOT NULL DEFAULT '1',"+
		"  `deleateFlag` int(10) unsigned NOT NULL DEFAULT '0', `renewDate`"+
		"  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE"+
		"  CURRENT_TIMESTAMP, PRIMARY KEY (`id`) ) ENGINE=InnoDB"+
		"  AUTO_INCREMENT=2093 DEFAULT CHARSET=sjis";
		*/

	/* (non-Javadoc)
	 * @see omega11.webcrawler.w2fj.dao.IDAO#insert(java.util.List)
	 */
	/* (non-Javadoc)
	 * @see omega11.webcrawler.w2fj.dao.IDAO#insert(java.util.List)
	 */
	public void insert(List<SubjectTextEntity> entitys) {
		try {
			String query = "INSERT INTO " + tableName;
			query += " (THREAD_NUMBER, TITLE, THREAD_RES_COUNT, CREATEDATE, RENEWDATE) VALUES (?,?,?,?,?) ";

			PreparedStatement stmt = con.prepareStatement(query);

			for (SubjectTextEntity entity : entitys) {
				stmt.setString(1, entity.getThreadNumber());
				stmt.setString(2, entity.getTitle());
				stmt.setInt(3, entity.getThreadResCount());

				stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
				stmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
				stmt.executeUpdate();
			}

			stmt.close();

		} catch (Exception e) {
			Loger.print(e);
			return;
		}

	}

	/* (non-Javadoc)
	 * @see omega11.webcrawler.w2fj.dao.IDAO#seclect()
	 */
	/* (non-Javadoc)
	 * @see omega11.webcrawler.w2fj.dao.IDAO#seclect()
	 */

	public List<SubjectTextEntity> seclect() {
		// TODO Auto-generated method stub
		List<SubjectTextEntity> entitys = new ArrayList<SubjectTextEntity>();
		return entitys;

	}

	/* (non-Javadoc)
	 * @see omega11.webcrawler.w2fj.dao.IDAO#lock(java.util.List)
	 */
	/* (non-Javadoc)
	 * @see omega11.webcrawler.w2fj.dao.IDAO#lock(java.util.List)
	 */
	public void lock(List<SubjectTextEntity> entitys) {
	}

	/* (non-Javadoc)
	 * @see omega11.webcrawler.w2fj.dao.IDAO#update(java.util.List)
	 */
	/* (non-Javadoc)
	 * @see omega11.webcrawler.w2fj.dao.IDAO#update(java.util.List)
	 */
	public void update(List<SubjectTextEntity> entitys) {
		try {
			String query = "UPDATE " + tableName;
			query += " SET THREAD_NUMBER = ? , TITLE = ? , THREAD_RES_COUNT = ?, DOWNLOAD_COUNT = ?, DOWNLOAD_STARTPOINT = ?, DELEATE_FLAG = ?, RENEWDATE = ? ";
			query += " WHERE ID = ? ";

			PreparedStatement stmt = con.prepareStatement(query);

			for (SubjectTextEntity entity : entitys) {
				stmt.setString(1, entity.getThreadNumber());
				stmt.setString(2, entity.getTitle());
				stmt.setInt(3, entity.getThreadResCount());
				stmt.setInt(4, entity.getDownloadCount());
				stmt.setInt(5, entity.getDownloadStartpoint());
				stmt.setInt(6, entity.getDeleateFlag());
				stmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
				stmt.setInt(8, entity.getId());
				stmt.executeUpdate();
			}

			stmt.close();

		} catch (Exception e) {
			Loger.print(e);
			return;
		}
	}


	/* (non-Javadoc)
	 * @see omega11.webcrawler.w2fj.dao.IDAO#updateForDatDownloadAfter(java.util.List)
	 */
	/* (non-Javadoc)
	 * @see omega11.webcrawler.w2fj.dao.IDAO#updateForDatDownloadAfter(java.util.List)
	 */
	public void updateForDatDownloadAfter(List<SubjectTextEntity> entitys) {
		try {
			String query = "UPDATE " + tableName;
			query += " SET DOWNLOADED = ? , HTTP_STATE = ? ";
			query += " WHERE ID = ? ";

			PreparedStatement stmt = con.prepareStatement(query);

			for (SubjectTextEntity entity : entitys) {
				stmt.setInt(1, entity.getDownloaded());
				stmt.setString(2, entity.getHttpState());
				stmt.setInt(3, entity.getId());
				stmt.executeUpdate();
			}

			stmt.close();

		} catch (Exception e) {
			Loger.print(e);
			return;
		}
	}

	/* (non-Javadoc)
	 * @see omega11.webcrawler.w2fj.dao.IDAO#seclectExsistThread()
	 */
	/* (non-Javadoc)
	 * @see omega11.webcrawler.w2fj.dao.IDAO#seclectExsistThread()
	 */
	public List<SubjectTextEntity> seclectExsistThread() {
		List<SubjectTextEntity> exsistEntitys = new ArrayList<SubjectTextEntity>();

		try {
			Statement stmt = con.createStatement();
			String query = "SELECT * FROM " + tableName + " WHERE DELEATE_FLAG = 0";
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				SubjectTextEntity subjectTextEntity = new SubjectTextEntity();

				// 結果をテーブルエンティティに設定
				setAllColumn(subjectTextEntity, rs);

				exsistEntitys.add(subjectTextEntity);
			}

			rs.close();
			stmt.close();

		} catch (Exception e) {
			Loger.print(e);
			return exsistEntitys;
		}

		return exsistEntitys;
	}

	/* (non-Javadoc)
	 * @see omega11.webcrawler.w2fj.dao.IDAO#selectForDatDownloadRecord()
	 */
	/* (non-Javadoc)
	 * @see omega11.webcrawler.w2fj.dao.IDAO#selectForDatDownloadRecord()
	 */
	public List<SubjectTextEntity> selectForDatDownloadRecord() {
		List<SubjectTextEntity> entitys = new ArrayList<SubjectTextEntity>();
		try {
			Statement stmt = con.createStatement();
			String query = "SELECT * FROM "
					+ tableName
					+ " WHERE DELEATE_FLAG = 0 AND THREAD_RES_COUNT > DOWNLOADED";
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				SubjectTextEntity subjectTextEntity = new SubjectTextEntity();

				// 結果をテーブルエンティティに設定
				setAllColumn(subjectTextEntity, rs);

				entitys.add(subjectTextEntity);
			}

			rs.close();
			stmt.close();

		} catch (Exception e) {
			Loger.print(e);
			return entitys;

		}
		return entitys;
	}

	/**
	 * リザルトセットからエンティティに全カラムをコピーします
	 *
	 * @param entity
	 * @param rs
	 */
	private void setAllColumn(SubjectTextEntity subjectTextEntity, ResultSet rs)
			throws Exception {
		/**
		 * DROP TABLE IF EXISTS `test`.`subjecttext_news`;
CREATE TABLE  `test`.`subjecttext_news` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `THREAD_NUMBER` varchar(10) NOT NULL,
  `TITLE` varchar(124) NOT NULL,
  `THREAD_RES_COUNT` int(10) NOT NULL,
  `DOWNLOAD_STARTPOINT` int(10) NOT NULL DEFAULT '1',
  `CONTENT_LENGTH` bigint(20) unsigned DEFAULT '0',
  `HTTP_STATE` varchar(3) DEFAULT NULL,
  `DOWNLOAD_COUNT` int(10) DEFAULT '0',
  `DELEATE_FLAG` int(10) NOT NULL DEFAULT '0',
  `CREATEDATE` datetime NOT NULL,
  `RENEWDATE` datetime NOT NULL,
  `IF_MODIFIED_SINCE` bigint(20) unsigned DEFAULT NULL,
  `IF_MODIFIED_SINCE_DATE` datetime DEFAULT NULL,
  `DOWNLOADED` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `IDX_subjecttext_news_1` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=14624 DEFAULT CHARSET=sjis;
		 */
		subjectTextEntity.setId(rs.getInt("ID"));
		subjectTextEntity.setThreadNumber(rs.getString("THREAD_NUMBER"));
		subjectTextEntity.setTitle(rs.getString("TITLE"));
		subjectTextEntity.setThreadResCount(rs.getInt("THREAD_RES_COUNT"));
		subjectTextEntity.setDownloadStartpoint(rs
				.getInt("DOWNLOAD_STARTPOINT"));
		subjectTextEntity.setContentLength(rs.getLong("CONTENT_LENGTH"));
		subjectTextEntity.setHttpState(rs.getString("HTTP_STATE"));
		subjectTextEntity.setDownloadCount(rs.getInt("DOWNLOAD_COUNT"));

		subjectTextEntity.setDeleateFlag(rs.getInt("DELEATE_FLAG"));
		subjectTextEntity.setRenewdate(rs.getTimestamp("RENEWDATE"));
		subjectTextEntity.setDownloaded(rs.getInt("DOWNLOADED"));
	}
}
