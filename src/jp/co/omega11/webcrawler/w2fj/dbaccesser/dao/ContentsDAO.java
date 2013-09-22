/**
 * CONTENTS テーブルのDAOです
 */
package jp.co.omega11.webcrawler.w2fj.dbaccesser.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jp.co.omega11.universal.util.log.Log;
import jp.co.omega11.webcrawler.w2fj.entity.ContentsEntity;

import org.apache.commons.lang.StringUtils;


/**
 * @author Wizard1 2009
 * 
 */


public class ContentsDAO extends AbstractBaseDAO {

	public ContentsDAO(Connection con, String itaName) {
		super(con, itaName, DbConstant.CONTENTSTABLENAME);
	}

	/**
	 * インサートを行います、インサートする契機はDATDownlorderのみ
	 * 
	 * @param entitys
	 * @throws SQLException
	 */
	public void insert(List<ContentsEntity> entitys) throws SQLException {

		String query = "INSERT INTO " + tableName;
		query += " (DAT_ID,URL,FIRST_HIT_THREAD_NUM,CONTENT_SIZE,EXE_FLAG,HTTP_STATE,REGEX_HIT_COUNT,URL_REGEX_FILTER,FILTER_SETNAME,SAVE_DIRECTORY,CREATEDATE,CREATE_USER,RENEWDATE,RENEW_USER,FILE_NAME,DOWN_WORKER_ID,RESURL_COMMENT) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

		PreparedStatement stmt = con.prepareStatement(query);

		for (ContentsEntity entity : entitys) {
			stmt.setInt(1, entity.getDatId());
			stmt.setString(2, entity.getUrl());
			stmt.setInt(3, entity.getFirstHitThreadNum());
			stmt.setLong(4, entity.getContentSize());
			stmt.setInt(5, entity.getExeFlag());
			stmt.setString(6, entity.getHttpState());
			stmt.setInt(7, entity.getRegexHitCount());
			stmt.setString(8, entity.getUrlRegexFilter());
			stmt.setString(9, entity.getFilterSetname());
			stmt.setString(10, entity.getSaveDirectory());
			stmt.setTimestamp(11, new Timestamp(System.currentTimeMillis()));
			stmt.setString(12, entity.getCreateUser());
			stmt.setTimestamp(13, new Timestamp(System.currentTimeMillis()));
			stmt.setString(14, entity.getRenewUser());
			stmt.setString(15, entity.getFileName());
			stmt.setString(16, entity.getDownWorkerId());
			stmt.setString(17, entity.getResurlComment());

			stmt.executeUpdate();
		}

		stmt.close();

	}

	// TODO
	/**
	 * 主キーで１行レコードを選択します
	 * 
	 * @param id
	 * @return
	 */
	public ContentsEntity select(int id) {
		ContentsEntity contentsEntity = new ContentsEntity();
		return contentsEntity;

	}

	// TODO
	/**
	 * 主キーで１行レコードをLOCKします
	 * 
	 * @param id
	 * @return
	 */
	public ContentsEntity lock(int id) {
		ContentsEntity contentsEntity = new ContentsEntity();
		return contentsEntity;

	}

	
	//TODO
	/**
	 * updateを行います 契機はContentDown/重複ＨＩＴカウント時はDAtDown
	 * 複数行更新
	 * @param entitys
	 * @throws SQLException
	 */
	public void update(List<ContentsEntity> entitys) throws SQLException {
		
		for (ContentsEntity entity: entitys) {
			update(entity);
		}
	}
	
	/**
	 * 
	 */
	public void update(ContentsEntity entity) throws SQLException {
		String query = "UPDATE " + tableName;
		query += " SET  CONTENT_SIZE = ? ,EXE_FLAG = ? ,HTTP_STATE = ? ,REGEX_HIT_COUNT = ? ,SAVE_DIRECTORY = ? ,RENEWDATE = ? ,RENEW_USER = ? ,FILE_NAME = ? ,DOWN_WORKER_ID = ? ,RESURL_COMMENT = ? ";
		query += " WHERE CONTENT_ID = ? ";

		Log.print(query);
		
		PreparedStatement stmt = con.prepareStatement(query);

		stmt.setLong(1, entity.getContentSize());
		stmt.setInt(2, entity.getExeFlag());
		stmt.setString(3, entity.getHttpState());
		stmt.setInt(4, entity.getRegexHitCount());
		stmt.setString(5, entity.getSaveDirectory());
		stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
		stmt.setString(7, entity.getRenewUser());
		stmt.setString(8, entity.getFileName());
		stmt.setString(9, entity.getDownWorkerId());
		stmt.setString(10, entity.getResurlComment());

		// Where = Pk
		stmt.setInt(11, entity.getContentId());

		stmt.executeUpdate();

		stmt.close();
	}

	/**
	 * ダウンロードすべきコンテンツを探す
	 * 
	 * @param nodeId
	 *            ノードＩＤ　デフォルトＮＵＬＬ
	 * @return
	 * @throws Exception
	 */
	public List<ContentsEntity> workTargetSelect(String nodeId) {

		try {
			List<ContentsEntity> entitys = new ArrayList<ContentsEntity>();

			Statement stmt;

			stmt = con.createStatement();

			String query = "SELECT * FROM " + tableName
					+ " WHERE EXE_FLAG = 0 AND ";

			if (StringUtils.isBlank(nodeId)) {
				query += "DOWN_WORKER_ID IS NULL";
			} else {
				query += "DOWN_WORKER_ID = '" + nodeId + "'";
			}

			ResultSet rs;

			rs = stmt.executeQuery(query);

			while (rs.next()) {
				ContentsEntity contentsEntity = new ContentsEntity();

				// 結果をテーブルエンティティに設定
				setAllColumn(contentsEntity, rs);

				entitys.add(contentsEntity);
			}

			rs.close();
			stmt.close();

			return entitys;
		} catch (SQLException e) {
			Log.printSQLException(e);
		}

		return null;
	}

	/**
	 * インサートするべきレコードかどうかを重複レコードを探す(DatDownlorderで使用)
	 * 
	 * Oracleならmerge命令が使える
	 * @param nodeId
	 * @return
	 * @throws Exception
	 */
	public List<ContentsEntity> searchInsert(String fileName, String url) throws Exception {
		List<ContentsEntity> entitys = new ArrayList<ContentsEntity>();
		
		String query = "SELECT CONTENT_ID FROM " + tableName
		+ " WHERE　FILE_NAME = ? AND URL = ?";
		
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, fileName);
		stmt.setString(2, url);
		
		ResultSet rs = stmt.executeQuery(query);
		
		while (rs.next()) {
			ContentsEntity contentsEntity = new ContentsEntity();

			// 結果をテーブルエンティティに設定
			setAllColumn(contentsEntity, rs);

			entitys.add(contentsEntity);
		}

		
		rs.close();
		stmt.close();
		
		return entitys;
	}
	
	/**
	 * リザルトセットからエンティティに全カラムをコピーします
	 * 
	 * @param entity
	 * @param rs
	 * @throws Exception
	 */
	private void setAllColumn(ContentsEntity entity, ResultSet rs)
			throws SQLException {
		// entity.setId(rs.getInt("ID"));

		entity.setContentId(rs.getInt("CONTENT_ID"));

		entity.setDatId(rs.getInt("DAT_ID"));
		entity.setUrl(rs.getString("URL"));
		entity.setFirstHitThreadNum(rs.getInt("FIRST_HIT_THREAD_NUM"));
		entity.setContentSize(rs.getLong("CONTENT_SIZE"));
		entity.setExeFlag(rs.getInt("EXE_FLAG"));
		entity.setHttpState(rs.getString("HTTP_STATE"));
		entity.setRegexHitCount(rs.getInt("REGEX_HIT_COUNT"));
		entity.setUrlRegexFilter(rs.getString("URL_REGEX_FILTER"));
		entity.setFileName(rs.getString("FILTER_SETNAME"));
		entity.setSaveDirectory(rs.getString("SAVE_DIRECTORY"));
		entity.setCreatedate(rs.getTimestamp("CREATEDATE"));
		entity.setCreateUser(rs.getString("CREATE_USER"));
		entity.setRenewdate(rs.getTimestamp("RENEWDATE"));
		entity.setRenewUser(rs.getString("RENEW_USER"));
		entity.setFileName(rs.getString("FILE_NAME"));
		entity.setDownWorkerId(rs.getString("DOWN_WORKER_ID"));
		entity.setResurlComment(rs.getString("RESURL_COMMENT"));

	}
}
