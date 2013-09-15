package jp.co.omega11.webcrawler.w2fj.component.subject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import jp.co.omega11.universal.util.UniversalUtil;
import jp.co.omega11.universal.util.log.Loger;
import jp.co.omega11.webcrawler.w2fj.component.AbstrarctComponent;
import jp.co.omega11.webcrawler.w2fj.constant.Constant;
import jp.co.omega11.webcrawler.w2fj.dbaccesser.dao.SubjectTextDAO;
import jp.co.omega11.webcrawler.w2fj.entity.SubjectTextEntity;
import jp.co.omega11.webcrawler.w2fj.model.systemInfomation.SubjectComponentInfo;
import jp.co.omega11.webcrawler.w2fj.set.Setting;



public class SubjectTextGet extends AbstrarctComponent{


	private String subjectUrl;

	private String subjectFileName;

	private String subjectFileNamePath;

	private SubjectComponentInfo componentInfo;


	//  	�ڑ����URL�֏��𑗐M���A���ʂ�ۑ�����
	// REF		http://www.atmarkit.co.jp/fjava/javatips/069java006.html

	// �R���X�g���N�^�iDB�Ȃ��j
	public SubjectTextGet(Setting setting) {
		set = setting;
	}

	// �R���X�g���N�^(DB����)
	public SubjectTextGet(Setting setting, Connection con, SubjectComponentInfo componentInfo) {
		set = setting;
		this.con = con;
		this.componentInfo = componentInfo;

		componentInfo.init(this.getClass().getSimpleName());
	}

	/**
	 * subject.txt�@���_�E�����[�h
	 */
	public void download() {
		// subjectText_tempoFile�t�H���_
		/*
		 * $content = get("http://tsushima.2ch.net/news/subject.txt"); die
		 * "Couldn't get it!" unless defined $content;
		 *
		 * #http://tsushima.2ch.net/news/dat/1236577332.dat
		 */

		subjectUrl = set.getURL() + Constant.SUBJECTTEXT;

		try {
			int num;

			byte buf[] = new byte[4069];

			URL url = new URL(subjectUrl);

			Object obj = url.getContent();

			subjectFileName = "subject" + UniversalUtil.getTimeString()
					+ ".txt";

			subjectFileNamePath = set.getSubjectFolderFullPath() + "/"
					+ subjectFileName;

			UniversalUtil.createFile(subjectFileNamePath);

			FileOutputStream fo = new FileOutputStream(subjectFileNamePath);

			if (obj instanceof InputStream) {
				InputStream di = (InputStream) obj;
				while ((num = di.read(buf)) != -1) {

					fo.write(buf, 0, num);

				}
			}

			fo.close();

		} catch (Exception e) {
			Loger.print(e);
		}
	}

	/**
	 * subject.txt��JAVA DB���f���Ƀp�[�X
	 */
	private List<SubjectTextEntity> parse() {
		List<SubjectTextEntity> enlist = new ArrayList<SubjectTextEntity>();

		try {
			/*
			BufferedReader br = new BufferedReader(new FileReader(
					subjectFileNamePath));
*/

			//�����R�[�h���w�肵�Ȃ��ƃf�t�H���g�̕����R�[�h��SJIS�łȂ�JavaVM(Linux-OpenJDK[UTF8]�Ȃ�)
			//�ŕ����������Ăc�a�ɑ}�������
			InputStreamReader isr = new InputStreamReader(new FileInputStream(
					subjectFileNamePath),"SJIS");

			BufferedReader br = new BufferedReader(isr);

			String line;

			String threadNumber;

			String title;

			String threadResCountString;

			int threadResCount;

			while ((line = br.readLine()) != null) {
				SubjectTextEntity model = new SubjectTextEntity();

				// 1237284546.dat<>PS3�Ɂw�����ѐ�L�x�wNINJA
				// GAIDEN��2�x�w��ł̱�ش�x�ƐV�삪���X���\�I���S��PS3����̓������H��2 (913)
				// 1237271749.dat<>�y�ǂ��ł��傤�H�z�F�l���E�Q���ĐH�ׂ�����ߕ� (143)

				// �^�C�g���ɂǂ�ȕ������͂���̂��킩��Ȃ��̂�split�͊댯
				// ���n�I�ɕ������ŃJ�b�g������

				threadNumber = line.substring(0, 10);

				String Lkakko = "(";

				String Rkakko = ")";

				title = line.substring(16, line.lastIndexOf(Lkakko
						.codePointAt(0)));

				threadResCountString = line.substring(line.lastIndexOf(Lkakko
						.codePointAt(0)) + 1, line.lastIndexOf(Rkakko
						.codePointAt(0)));

				threadResCount = new Integer(threadResCountString);

				model.setThreadNumber(threadNumber);
				model.setTitle(title);
				model.setThreadResCount(threadResCount);

				enlist.add(model);
			}

			br.close();
			isr.close();
		} catch (Exception e) {
			Loger.print("��O������ " + e.toString());
		}

		return enlist;
	}

	/**
	 * �X���b�h���f����DB�ɓo�^����
	 */
	public void regist() {
		List<SubjectTextEntity> entitys = parse();

		// TODO Subject�擾���s�h�~
		if(entitys.size() < 100){
			Loger.print("SubjectText�擾���s");
			return;
		}


		SubjectTextDAO subjectTextDAO = new SubjectTextDAO(con, set.getItaname());

		// ���łɑ��݂��Ă���X����DB���猟�����܂�
		// TODO ��ŃI���������[��r�ǉ�
		List<SubjectTextEntity> exsistsEntitys = firstBootDecideNewThread(
				subjectTextDAO);

		// ���݂̃T�u�W�F�N�gtxt�̃��R�[�h�Ƃ��łɑ��݂��Ă郌�R�[�h���r���ăC���T�[�g�Ώۂ𒊏o���܂�
		// ���łɑ��݂��Ă�X���ɑ΂��Ă͉��z�I�ȏ���flag(-1)�����Ă܂�
		// ���łɑ��݂��Ă�X���ɑ΂��Ă͐V�����f�[�^���}�[�W���ă_�E�����[�h�|�C���g���v�Z���Đݒ肵�܂�
		List<SubjectTextEntity> newEntitys = mergeForInsertRecorde(entitys,
				exsistsEntitys);

		//�����t���O��-1�łȂ����̂����X���Ɣ��f���t���O1�ɂ��܂�,-1��0�ɖ߂��܂�
		mergeForDeathRecorde(exsistsEntitys);

		// �X�V�ΏۃX���Ǝ��X����Update���܂�
		exsistThreadUpdate(subjectTextDAO, exsistsEntitys);


		// �V�X�����C���T�[�g���܂�
		newThreadInsert(subjectTextDAO, newEntitys);

	}

	private List<SubjectTextEntity> firstBootDecideNewThread(
			SubjectTextDAO dao) {
		// ���łɑ��݂��Ă��郌�R�[�h(Update�Ώ�)
		List<SubjectTextEntity> exsistList = dao.seclectExsistThread();

		return exsistList;
	}

	// ���݂̃T�u�W�F�N�gtxt�̃��R�[�h�Ƃ��łɑ��݂��Ă郌�R�[�h���r���ăC���T�[�g�Ώۂ𒊏o���܂�
	// ���łɑ��݂��Ă�X���ɑ΂��Ă͉��z�I�ȏ���flag�����Ă܂�
	private List<SubjectTextEntity> mergeForInsertRecorde(
			List<SubjectTextEntity> nowEntitys,
			List<SubjectTextEntity> exsistsEntitys) {
		List<SubjectTextEntity> mergeList = new ArrayList<SubjectTextEntity>();

		boolean findflg = false;
		Loger.print("subjectTEXT=" + nowEntitys.size() + "<>" + " exsist = " +exsistsEntitys.size());


		for (SubjectTextEntity nowEntity : nowEntitys) {

			SubjectTextEntity nowEntityClone = (SubjectTextEntity) nowEntity.clone();

			for (SubjectTextEntity exsisEntity : exsistsEntitys) {

				if (nowEntityClone.getThreadNumber().equals(
						exsisEntity.getThreadNumber())) {

					//�_�E�����[�h�|�C���g�v�Z
					// TODO +1�̕K�v����H
					exsisEntity.setDownloadStartpoint(exsisEntity.getThreadResCount());

					// ���łɑ��݂��Ă�X���ɐV�����f�[�^���}�[�W���܂�
					exsisEntity.setThreadResCount(nowEntityClone.getThreadResCount());

					//�����}�[�N������
					exsisEntity.setDeleateFlag(-1);
					findflg = true;

				}

			}

			if (findflg == false) {

				Loger.print("INSERT�Ώہ@" + nowEntity.getTitle());
				mergeList.add(nowEntityClone);

			} else {
				Loger.print("�X�V�Ώہ@" + nowEntity.getTitle());
				findflg = false;
			}
		}

		return mergeList;

	}

	/**
	 *
	 * @param entitys
	 * @return
	 */
	private void mergeForDeathRecorde(
			List<SubjectTextEntity> entitys){

		int alive=0;
		int death=0;

		for (SubjectTextEntity entity : entitys) {

			if(-1 == entity.getDeleateFlag()){
				entity.setDeleateFlag(0);
				alive++;
			}else {
				entity.setDeleateFlag(1);
				death++;
			}
		}

		Loger.print("�X���b�h�X�V�� " + String.valueOf(alive));
		Loger.print("�X���b�h������ " + String.valueOf(death));

		//���o�^
		componentInfo.setThreadUpdateTarget(alive);
		componentInfo.setThreadDeleteFlagTarget(death);
	}

	/**
	 * �V�X�����C���T�[�g���܂�
	 *
	 * @param subjectTextDAO
	 * @param entity
	 */
	private void newThreadInsert(SubjectTextDAO subjectTextDAO,
			List<SubjectTextEntity> entitys) {


		Loger.print("INSERT���� " + String.valueOf(entitys.size()));
		componentInfo.setThreadInsertTarget(entitys.size());


		subjectTextDAO.insert(entitys);
	}


	/**
	 * ���łɑ��݂��Ă���X�����X�V���܂�
	 * @param subjectTextDAO
	 * @param entitys
	 */
	private void exsistThreadUpdate(SubjectTextDAO subjectTextDAO,
			List<SubjectTextEntity> entitys) {


		subjectTextDAO.update(entitys);

	}


}
