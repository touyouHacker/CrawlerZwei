package jp.co.omega11.webcrawler.w2fj.component.dat;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import jp.co.omega11.universal.util.archive.GzipUtil;
import jp.co.omega11.universal.util.file.TextUtil;
import jp.co.omega11.universal.util.log.Log;
import jp.co.omega11.universal.util.net.http.HttpCliantKai;
import jp.co.omega11.universal.util.net.http.HttpHeaderSave;
import jp.co.omega11.webcrawler.w2fj.component.AbstrarctComponent;
import jp.co.omega11.webcrawler.w2fj.component.dat.extraction.ExtractionNormal;
import jp.co.omega11.webcrawler.w2fj.component.dat.extraction.IDatToContentsUrl;
import jp.co.omega11.webcrawler.w2fj.dbaccesser.dao.ContentsDAO;
import jp.co.omega11.webcrawler.w2fj.dbaccesser.dao.SubjectTextDAO;
import jp.co.omega11.webcrawler.w2fj.entity.ContentsEntity;
import jp.co.omega11.webcrawler.w2fj.entity.SubjectTextEntity;
import jp.co.omega11.webcrawler.w2fj.model.systemInfomation.DatComponentInfo;
import jp.co.omega11.webcrawler.w2fj.set.Setting;

public class DatDownloder extends AbstrarctComponent {

	private String datFolderPath;

	private DatComponentInfo componentInfo;

	/**
	 * <SubjectTextEntity-PK,dat-wc>
	 */
	private HashMap<Integer, Integer> wcHash;

	// TODO
	// List<ContentstEntity> contentsEntitys;

	/**
	 * MAIN����
	 *
	 * @throws Exception
	 */
	public void execute() throws Exception {

		// �t�H���_�p�X��ݒ�
		datFolderPath = set.getDatFolderFullPath();

		wcHash = new HashMap<Integer, Integer>();

		List<SubjectTextEntity> subjectTextEntitys = getWorkDate();

		componentInfo.setDatDonwnloadTargetInSubCount(subjectTextEntitys.size());

		// ��������ׂ��X��������Ƃ��̂݌㑱�����ɓ���܂�
		if (subjectTextEntitys.size() > 0) {

			// URL���_�E�����[�h���G���e�B�e�B��ҏW�i�擾�T�C�Y�Ȃǁj
			download(subjectTextEntitys);

			// ���ʂ����DB�X�V���܂Ƃ߂čs��
			regist(subjectTextEntitys);
		}
	}

	public DatDownloder(Setting set, Connection con,
			DatComponentInfo componentInfo) {
		// TODO Auto-generated constructor stub
		this.set = set;
		this.con = con;
		this.componentInfo = componentInfo;
		componentInfo.init(this.getClass().getSimpleName());
	}

	/**
	 * �_�E�����[�h���ׂ�DAT�̃X�����݂��Č��ʂ�Ԃ��܂�
	 *
	 * @return
	 */
	public List<SubjectTextEntity> getWorkDate() {

		SubjectTextDAO subjectTextDAO = new SubjectTextDAO(con, set
				.getItaname());
		List<SubjectTextEntity> subjectTextEntitys = subjectTextDAO
				.selectForDatDownloadRecord();

		return subjectTextEntitys;

	}

	private void download(List<SubjectTextEntity> subjectTextEntitys) {
		// HTTP DAT �_�E�����[�h

		for (SubjectTextEntity subjectTextEntity : subjectTextEntitys) {
			
			// TODO ��͂̂���
			Log.flushAll();
			
			// wc������������O�ɑO��wc��ێ�
			wcHash.put(Integer.valueOf(subjectTextEntity.getId()), Integer
					.valueOf(subjectTextEntity.getDownloaded()));

			String datURL = null;

			if (set.isPinkFlag()) {
				datURL = set.getDatURL() + subjectTextEntity.getThreadNumber()
						+ ".dat";

			} else {
				datURL = set.getDatURL() + subjectTextEntity.getThreadNumber()
						+ "/";
			}

			String datFileName = subjectTextEntity.getThreadNumber()
					+ ".dat.gzip";

			HttpHeaderSave save = HttpCliantKai.download(datURL, datFolderPath,
					datFileName, HttpCliantKai.FILE_REWRITE_NOSEEK,
					HttpCliantKai.createHeader(set.getInvDat().getHttp()
							.getUserAgent(), 0, 0));

			if (save.isException()) {
				continue;
			}

			// HTTP�ʐM���ʂ��i�[
			// TODO -1���Ԃ����̂łc�a�ŗ�O�ɂȂ�
			// ��O������ com.mysql.jdbc.MysqlDataTruncation: Data truncation: Out of
			// range value for column 'CONTENT_LENGTH' at row 1
			if (save.getContentLength() > 0) {
				subjectTextEntity.setContentLength(save.getContentLength());
			} else {
				subjectTextEntity.setContentLength(0);
			}
			subjectTextEntity.setHttpState(String.valueOf(save
					.getResponseCode()));

			String gzipDatFullPathName = datFolderPath + "/" + datFileName;
			// .gzip �̂T�����폜
			String unzipDatFullPathName = GzipUtil
					.toUnCompressFileName(gzipDatFullPathName);

			// gzip����
			GzipUtil.decompress(gzipDatFullPathName, unzipDatFullPathName);

			// �𓀃X�g���[������ݍs����Ԃ�
			int wc = TextUtil.wc(unzipDatFullPathName);
			
			Log.print("wc=" + wc);
			
			// IF���ɓ���Ȃ��Ƃ��͂c�`�s�擾���s�H�v�b�����������Ȃ���΃p�[�X�ɂ͓���Ȃ�
			if (0 <= wc) {
				subjectTextEntity.setDownloaded(wc);
			}

			// 1H 3600�b������1�X��10�b���炢����1����360�X��������
			// �Ƃ����̐K���؂�/�o�[�{���n�E�X
			// http://info.2ch.net/wiki/index.php?%A4%C8%A4%AB%A4%B2%A4%CE%BF%AC%C8%F8%C0%DA%A4%EA%2F%A5%D0%A1%BC%A5%DC%A5%F3%A5%CF%A5%A6%A5%B9
			/*
			 * bg20�T�[�o����f�[�^���擾��΁A�o�[�{���n�E�X�s���ɂ͂Ȃ�܂���B
			 * �Ⴆ�΁Ahttp://society6.2ch.net/test/read.cgi/gline/1169964276/ �̏ꍇ�A
			 * http://bg20.2ch.net/test/r.so/society6.2ch.net/gline/1169964276/
			 * ����dat���擾�ł��܂��B �������A�����擾�͂ł��܂���B
			 */

		}

	}

	private void regist(List<SubjectTextEntity> subjectTextEntitys)
			throws Exception {
		// �_�E�����[�h���ʂ��t�o�c�`�s�d
		updateSubjectTable(subjectTextEntitys);

		if (set.isNoParseExe()) {

			Log.print("�� NoParseExe�t���O��false�ɐݒ�");
			set.setNoParseExe(false);

		} else {
			// dat��regex�Œ��o���R���e���cURL��Insert
			parseDatToUrls(subjectTextEntitys);
		}
	}

	// // ���ʂ�UPDATE�@������������̂Ń��b�N�����K�v? DB�̃I�[�g���b�N�ɔC����H�@�K�v�Œ���J�����ł悢�A�X�V���͍X�V���Ȃ�
	private void updateSubjectTable(List<SubjectTextEntity> subjectTextEntitys) {
		SubjectTextDAO subjectTextDAO = new SubjectTextDAO(con, set
				.getItaname());
		subjectTextDAO.updateForDatDownloadAfter(subjectTextEntitys);
	}

	private void parseDatToUrls(List<SubjectTextEntity> subjectTextEntitys)
			throws Exception {
		// �X�V�O��downloaed�ƌ��݂�downloaded���獷�����Ƃ�K�v�ȕ�����seek���Ď擾
		// ���K�\����URL�𒊏o�@���o�G���W���͕ύX�ł���悤�v���O�C�����ۂ����
		// URL���K�w�ӂ����݂���E�E�Ƃ����낢��o�[�W����������̂Ł�
		List<ContentsEntity> contentsEntitys = null;

		/** �p�[�X����Dat�̑���*/
		int parseDatCounter=0;


		// TODO
		// ���I�ɃG���W����������
		IDatToContentsUrl idaContentsUrl = new ExtractionNormal();

		for (SubjectTextEntity subjectTextEntity : subjectTextEntitys) {

			contentsEntitys = null;

			// �ȑO�ǂݍ��񂾍s�� < ���̍s���@�̂Ƃ�
			// ��������DAO��subject.txt�̍��������������擾���Ă邪subject��DAT�̃R���|�[�l���g�̎��ԍ���
			// DAT���ŐV�ł��Ƃ肷���Ă�\�������� (DAT���擾�����X���� > subject�̔F���X����) �̂œ�d�`�F�b�N

			Log.print("�s����r" + wcHash.get(subjectTextEntity.getId()) + " < "
					+ subjectTextEntity.getDownloaded());

			if (wcHash.get(subjectTextEntity.getId()) < subjectTextEntity
					.getDownloaded()) {
				// DAT����URL���o
				contentsEntitys = idaContentsUrl.extraction(datFolderPath + "/"
						+ subjectTextEntity.getThreadNumber() + ".dat", wcHash
						.get(subjectTextEntity.getId()) + 1);

				parseDatCounter++;
			}

			if (contentsEntitys == null) {
				continue;
			}

			// TODO USER��NodeID�̒���
			for (ContentsEntity content : contentsEntitys) {
				content.setDatId(subjectTextEntity.getId());
			}

			// TODO
			// 1dat�ɂ�1��insert(JDBC�̃o�b�`���[�h�g�����ق����悢DAO�Ƀ��[�h����������)
			insertContentsTable(contentsEntitys);
		}

		componentInfo.setParseDatCounter(parseDatCounter);
	}

	private void insertContentsTable(List<ContentsEntity> contentsEntitys)
			throws Exception {
		ContentsDAO contentsDAO = new ContentsDAO(con, set.getItaname());

		// TODO �d�����͂Ԃ����[�`��
		// �����t�q�k�ł����g��������Ă邱�Ƃ�����̂ł����IFModeified�ł����Ή��ł��Ȃ�
		// �J�����ɒǉ����邵���Ȃ��A���ĂɃT�[�o�[���Ƃ����J�����ӂ₷ Apache�`PHP/v�Ƃ�

		/*
		 * List<ContentsEntity> insertEntitys = new ArrayList<ContentsEntity>();
		 *
		 * //�d���𐸍�����insert���ׂ����̂�T�� for(ContentsEntity contentsEntity
		 * :contentsEntitys){
		 * contentsDAO.searchInsert(contentsEntity.getFileName(),
		 * contentsEntity.getUrl()); }
		 *
		 * contentsDAO.insert(insertEntitys);
		 */
		contentsDAO.insert(contentsEntitys);
	}

}
