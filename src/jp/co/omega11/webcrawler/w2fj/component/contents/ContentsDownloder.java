package jp.co.omega11.webcrawler.w2fj.component.contents;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import jp.co.omega11.universal.util.UniversalUtil;
import jp.co.omega11.universal.util.collections.ListUtil;
import jp.co.omega11.universal.util.log.Loger;
import jp.co.omega11.universal.util.net.http.HttpCliantKai;
import jp.co.omega11.universal.util.net.http.HttpHeaderSave;
import jp.co.omega11.webcrawler.w2fj.component.AbstrarctComponent;
import jp.co.omega11.webcrawler.w2fj.dbaccesser.dao.ContentsDAO;
import jp.co.omega11.webcrawler.w2fj.dbaccesser.dao.ContentsFolderNameDAO;
import jp.co.omega11.webcrawler.w2fj.dbaccesser.dao.DbConstant;
import jp.co.omega11.webcrawler.w2fj.entity.ContentsEntity;
import jp.co.omega11.webcrawler.w2fj.model.ContentsFolderNameModel;
import jp.co.omega11.webcrawler.w2fj.model.systemInfomation.ContentsComponentInfo;
import jp.co.omega11.webcrawler.w2fj.set.Setting;


public class ContentsDownloder extends AbstrarctComponent {

	private List<ContentsEntity> contentsEntityList;
	private HashMap<Integer, ContentsFolderNameModel> folderNameMap;

	private String nodeId = null;
	private ContentsDAO contentsDAO = null;

	private ContentsComponentInfo componentInfo;
	
	
	public ContentsDownloder(Setting set, Connection con, ContentsComponentInfo componentInfo) {
		this.set = set;
		this.con = con;
		this.componentInfo = componentInfo;
		componentInfo.init(this.getClass().getSimpleName());
	}

	/**
	 * 作業レコードを取得し0件であればfalseに 件数があればTrueに
	 * 
	 * @return
	 */
	public boolean preExecute() {

		contentsDAO = new ContentsDAO(con, set.getItaname());

		contentsEntityList = contentsDAO.workTargetSelect(nodeId);

		Loger.print("処理件数 " + contentsEntityList.size());
		
		componentInfo.setDownloadTargetCount(contentsEntityList.size());
		componentInfo.addAllDownloadTargetCount();
		
		if (ListUtil.isNullSizeZero(contentsEntityList)) {
			return false;
		}

		return true;
	}

	public void execute() {

		// フォルダ名のためハッシュマップ作成
		folderNameMap = createNameHash();

		// ダウンロード
		download();

		// GCのため一応クリア
		contentsEntityList = null;
	}

	/**
	 * コンテンツをダウンロードして結果をDB登録します 1レコードずつ登録します
	 * 
	 */
	public void download() {
		
		int httpExeceptioConunt = 0;

		for (ContentsEntity entity : contentsEntityList) {

			String saveFolderName = null;

			ContentsFolderNameModel model = folderNameMap
					.get(entity.getDatId());

			saveFolderName = model.getTitle();

			// フォルダ名にWindows禁止文字が含まれている場合除去
			saveFolderName = UniversalUtil
					.deleteFileProhibitionWordForWindows(saveFolderName);

			// フルパスに修正
			saveFolderName = set.getContentsFolderFullPath() + "/" + saveFolderName;
			
			Loger.print("保存フォルダ名 " + saveFolderName );
			
			// フォルダの作成(1スレづつ)
			if (!UniversalUtil.createDirectryExistCheak(saveFolderName)) {
				// フォルダが作れなかったらスレ名でなくスレッド番号でフォルダ作成
				Loger.print("フォルダ名 " + saveFolderName + "が作成できないためスレ番号で作成");

				saveFolderName =  set.getDatFolderFullPath() + "/" + model.getThreadNumber();

				if (!UniversalUtil.createDirectryExistCheak(saveFolderName)) {
					Loger.print("フォルダ名 " + saveFolderName + "が作成できないため終了");
					return;
				}
			}

			String saveFileName = UniversalUtil.urlLastElement(entity.getUrl());
			
			saveFileName = UniversalUtil.deleteFileProhibitionWordForWindows(saveFileName);
			
			// URLが正常ならコンテンツダウンロード
			HttpHeaderSave save = HttpCliantKai.download(entity.getUrl(), saveFolderName, saveFileName,
					HttpCliantKai.FILE_WRITE_OPTION_NEWFILE, null , set.getInvContents().getHttp());

			// 結果にかかわらない共通結果をエンティティに設定
			settingEntityParam(entity);

			if (!save.isException()) {
				// HTTP-STATEの判定
				// TODO とりあえずリトライはなし

				if (save.getContentLength() < 0) {
					// Lengthが-1の時があるので強制補完
					entity.setContentSize(0);
					entity.setResurlComment(entity.getResurlComment() + "," + "Length -1");
				} else {
					entity.setContentSize(save.getContentLength());
				}
				entity.setExeFlag(DbConstant.DOWNLOAD_SUCCESS);
				entity.setHttpState(String.valueOf(save.getResponseCode()));
				// jpg/pngによってわけたり条件判定でわけることがあるかもしれないので個別設定
				entity.setSaveDirectory(saveFolderName);
				entity.setFileName(saveFileName);
			} else {
				httpExeceptioConunt++;
				
				// HTTP-STATEも帰らない異常なアドレスとして判断し二度とダウンロードを実施しない
				entity.setExeFlag(DbConstant.DOWNLOAD_NOTNEXT);
				entity.setResurlComment(save.getExceptionMsg());
			}

			// DB に　登録
			regist(entity);

		}

		componentInfo.setHttpExeceptioConunt(httpExeceptioConunt);
		componentInfo.addAllhttpExeceptioConunt();
	}

	/**
	 * 共通的な設定を実施
	 * 
	 * @param entity
	 */
	public void settingEntityParam(ContentsEntity entity) {

	}

	/**
	 * 処理結果を登録
	 */
	private void regist(ContentsEntity entity) {
		try {
			contentsDAO.update(entity);
		} catch (SQLException e) {
			Loger.printSQLException(e);
			retryRegist(entity);
		}
	}
	
	/**
	 * registが失敗したとき、更新内容が不正だったと判断し
	 * 最小限の変更で再度Updateを試みます(フラグだけでも更新しないと無限ループになるため)
	 */
	private void retryRegist(ContentsEntity entity){
		
		Loger.print(" 更新失敗のためリトライをかけます");
		
		entity.setContentSize(0);
		entity.setResurlComment("Retry");
		entity.setHttpState("");
		entity.setSaveDirectory("");
		entity.setFileName("");
		
		try {
			contentsDAO.update(entity);
		} catch (SQLException e) {
			Loger.printSQLException(e);
		}
	}

	private HashMap<Integer, ContentsFolderNameModel> createNameHash() {

		// フォルダ名(スレ名 or スレッド番号(10桁))　のため結合ＤＡＯを実行
		ContentsFolderNameDAO contentsFolderNameDAO = new ContentsFolderNameDAO(
				con, set.getItaname());
		List<ContentsFolderNameModel> modelList = contentsFolderNameDAO.find();
		HashMap<Integer, ContentsFolderNameModel> map = new HashMap<Integer, ContentsFolderNameModel>();

		for (ContentsFolderNameModel model : modelList) {
			map.put(model.getDatId(), model);

		}

		return map;
	}

	/**
	 * @return the contentsEntityList
	 */
	public List<ContentsEntity> getContentsEntityList() {
		return contentsEntityList;
	}

	/**
	 * @param contentsEntityList
	 *            the contentsEntityList to set
	 */
	public void setContentsEntityList(List<ContentsEntity> contentsEntityList) {
		this.contentsEntityList = contentsEntityList;
	}

	/**
	 * @return the nodeId
	 */
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * @param nodeId
	 *            the nodeId to set
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

}
