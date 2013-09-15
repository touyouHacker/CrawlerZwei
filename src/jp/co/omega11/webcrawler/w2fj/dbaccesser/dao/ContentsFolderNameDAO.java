/**
 * 
 */
package jp.co.omega11.webcrawler.w2fj.dbaccesser.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jp.co.omega11.universal.util.log.Loger;
import jp.co.omega11.webcrawler.w2fj.model.ContentsFolderNameModel;


/**
 * @author Wizard1 2009
 * 複合結合テーブルなのでAbstractBaseDAOを継承しません
 */
public class ContentsFolderNameDAO {

	private Connection con;
	private String subjectTable = DbConstant.SUJECTTABLENAME;
	private String contentsTable = DbConstant.CONTENTSTABLENAME;
	
	
	public ContentsFolderNameDAO(Connection _con , String ItaName ) {
		con = _con;
		
		subjectTable +="_" + ItaName.toUpperCase();
		contentsTable +="_" + ItaName.toUpperCase();
	}
	
	public List<ContentsFolderNameModel> find() {
		List <ContentsFolderNameModel> modelList = new ArrayList<ContentsFolderNameModel>();
		Statement stmt;
		ResultSet rs;

		try {
			stmt = con.createStatement();
			
			String query = "SELECT DISTINCT S.ID ,S.THREAD_NUMBER, S.TITLE "
				+ "FROM " + contentsTable + " C, " + subjectTable + " S "
				+ "WHERE C.EXE_FLAG = "+  DbConstant.DOWNLOAD_DEFAULT + " AND C.DAT_ID = S.ID ";
			Loger.print(query);
			
			rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				ContentsFolderNameModel contentsFolderNameModel = new ContentsFolderNameModel();
				
				// 結果をモデルに設定
				setAllColumn(contentsFolderNameModel, rs);
				
				modelList.add(contentsFolderNameModel);
			}
			
			rs.close();
			stmt.close();
			
		} catch (SQLException e) {
			Loger.printSQLException(e);
		}
		

		
		return modelList;
	}
	
	private void setAllColumn(ContentsFolderNameModel model, ResultSet rs) throws SQLException {
		model.setDatId(rs.getInt("ID"));
		model.setThreadNumber(rs.getString("THREAD_NUMBER"));
		model.setTitle(rs.getString("TITLE"));
	}
}

