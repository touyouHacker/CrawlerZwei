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
	 * ��ƃ��R�[�h���擾��0���ł����false�� �����������True��
	 * 
	 * @return
	 */
	public boolean preExecute() {

		contentsDAO = new ContentsDAO(con, set.getItaname());

		contentsEntityList = contentsDAO.workTargetSelect(nodeId);

		Loger.print("�������� " + contentsEntityList.size());
		
		componentInfo.setDownloadTargetCount(contentsEntityList.size());
		componentInfo.addAllDownloadTargetCount();
		
		if (ListUtil.isNullSizeZero(contentsEntityList)) {
			return false;
		}

		return true;
	}

	public void execute() {

		// �t�H���_���̂��߃n�b�V���}�b�v�쐬
		folderNameMap = createNameHash();

		// �_�E�����[�h
		download();

		// GC�̂��߈ꉞ�N���A
		contentsEntityList = null;
	}

	/**
	 * �R���e���c���_�E�����[�h���Č��ʂ�DB�o�^���܂� 1���R�[�h���o�^���܂�
	 * 
	 */
	public void download() {
		
		int httpExeceptioConunt = 0;

		for (ContentsEntity entity : contentsEntityList) {

			String saveFolderName = null;

			ContentsFolderNameModel model = folderNameMap
					.get(entity.getDatId());

			saveFolderName = model.getTitle();

			// �t�H���_����Windows�֎~�������܂܂�Ă���ꍇ����
			saveFolderName = UniversalUtil
					.deleteFileProhibitionWordForWindows(saveFolderName);

			// �t���p�X�ɏC��
			saveFolderName = set.getContentsFolderFullPath() + "/" + saveFolderName;
			
			Loger.print("�ۑ��t�H���_�� " + saveFolderName );
			
			// �t�H���_�̍쐬(1�X���Â�)
			if (!UniversalUtil.createDirectryExistCheak(saveFolderName)) {
				// �t�H���_�����Ȃ�������X�����łȂ��X���b�h�ԍ��Ńt�H���_�쐬
				Loger.print("�t�H���_�� " + saveFolderName + "���쐬�ł��Ȃ����߃X���ԍ��ō쐬");

				saveFolderName =  set.getDatFolderFullPath() + "/" + model.getThreadNumber();

				if (!UniversalUtil.createDirectryExistCheak(saveFolderName)) {
					Loger.print("�t�H���_�� " + saveFolderName + "���쐬�ł��Ȃ����ߏI��");
					return;
				}
			}

			String saveFileName = UniversalUtil.urlLastElement(entity.getUrl());
			
			saveFileName = UniversalUtil.deleteFileProhibitionWordForWindows(saveFileName);
			
			// URL������Ȃ�R���e���c�_�E�����[�h
			HttpHeaderSave save = HttpCliantKai.download(entity.getUrl(), saveFolderName, saveFileName,
					HttpCliantKai.FILE_WRITE_OPTION_NEWFILE, null , set.getInvContents().getHttp());

			// ���ʂɂ������Ȃ����ʌ��ʂ��G���e�B�e�B�ɐݒ�
			settingEntityParam(entity);

			if (!save.isException()) {
				// HTTP-STATE�̔���
				// TODO �Ƃ肠�������g���C�͂Ȃ�

				if (save.getContentLength() < 0) {
					// Length��-1�̎�������̂ŋ����⊮
					entity.setContentSize(0);
					entity.setResurlComment(entity.getResurlComment() + "," + "Length -1");
				} else {
					entity.setContentSize(save.getContentLength());
				}
				entity.setExeFlag(DbConstant.DOWNLOAD_SUCCESS);
				entity.setHttpState(String.valueOf(save.getResponseCode()));
				// jpg/png�ɂ���Ă킯�����������ł킯�邱�Ƃ����邩������Ȃ��̂Ōʐݒ�
				entity.setSaveDirectory(saveFolderName);
				entity.setFileName(saveFileName);
			} else {
				httpExeceptioConunt++;
				
				// HTTP-STATE���A��Ȃ��ُ�ȃA�h���X�Ƃ��Ĕ��f����x�ƃ_�E�����[�h�����{���Ȃ�
				entity.setExeFlag(DbConstant.DOWNLOAD_NOTNEXT);
				entity.setResurlComment(save.getExceptionMsg());
			}

			// DB �Ɂ@�o�^
			regist(entity);

		}

		componentInfo.setHttpExeceptioConunt(httpExeceptioConunt);
		componentInfo.addAllhttpExeceptioConunt();
	}

	/**
	 * ���ʓI�Ȑݒ�����{
	 * 
	 * @param entity
	 */
	public void settingEntityParam(ContentsEntity entity) {

	}

	/**
	 * �������ʂ�o�^
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
	 * regist�����s�����Ƃ��A�X�V���e���s���������Ɣ��f��
	 * �ŏ����̕ύX�ōēxUpdate�����݂܂�(�t���O�����ł��X�V���Ȃ��Ɩ������[�v�ɂȂ邽��)
	 */
	private void retryRegist(ContentsEntity entity){
		
		Loger.print(" �X�V���s�̂��߃��g���C�������܂�");
		
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

		// �t�H���_��(�X���� or �X���b�h�ԍ�(10��))�@�̂��ߌ����c�`�n�����s
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
