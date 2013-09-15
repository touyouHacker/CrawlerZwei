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


	//  	接続先のURLへ情報を送信し、結果を保存する
	// REF		http://www.atmarkit.co.jp/fjava/javatips/069java006.html

	// コンストラクタ（DBなし）
	public SubjectTextGet(Setting setting) {
		set = setting;
	}

	// コンストラクタ(DBあり)
	public SubjectTextGet(Setting setting, Connection con, SubjectComponentInfo componentInfo) {
		set = setting;
		this.con = con;
		this.componentInfo = componentInfo;

		componentInfo.init(this.getClass().getSimpleName());
	}

	/**
	 * subject.txt　をダウンロード
	 */
	public void download() {
		// subjectText_tempoFileフォルダ
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
	 * subject.txtをJAVA DBモデルにパース
	 */
	private List<SubjectTextEntity> parse() {
		List<SubjectTextEntity> enlist = new ArrayList<SubjectTextEntity>();

		try {
			/*
			BufferedReader br = new BufferedReader(new FileReader(
					subjectFileNamePath));
*/

			//文字コードを指定しないとデフォルトの文字コードがSJISでないJavaVM(Linux-OpenJDK[UTF8]など)
			//で文字が化けてＤＢに挿入される
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

				// 1237284546.dat<>PS3に『ｶﾞﾝﾀﾞﾑ戦記』『NINJA
				// GAIDENΣ2』『ﾛﾛﾅのｱﾄﾘｴ』と新作が続々発表！完全にPS3時代の到来か？★2 (913)
				// 1237271749.dat<>【どこでしょう？】友人を殺害して食べた女を逮捕 (143)

				// タイトルにどんな文字がはいるのかわからないのでsplitは危険
				// 原始的に文字数でカットが無難

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
			Loger.print("例外発生→ " + e.toString());
		}

		return enlist;
	}

	/**
	 * スレッドモデルをDBに登録する
	 */
	public void regist() {
		List<SubjectTextEntity> entitys = parse();

		// TODO Subject取得失敗防止
		if(entitys.size() < 100){
			Loger.print("SubjectText取得失敗");
			return;
		}


		SubjectTextDAO subjectTextDAO = new SubjectTextDAO(con, set.getItaname());

		// すでに存在しているスレをDBから検索します
		// TODO 後でオンメモリー比較追加
		List<SubjectTextEntity> exsistsEntitys = firstBootDecideNewThread(
				subjectTextDAO);

		// 現在のサブジェクトtxtのレコードとすでに存在してるレコードを比較してインサート対象を抽出します
		// すでに存在してるスレに対しては仮想的な処理flag(-1)をたてます
		// すでに存在してるスレに対しては新しいデータをマージしてダウンロードポイントも計算して設定します
		List<SubjectTextEntity> newEntitys = mergeForInsertRecorde(entitys,
				exsistsEntitys);

		//処理フラグが-1でないものを死スレと判断しフラグ1にします,-1は0に戻します
		mergeForDeathRecorde(exsistsEntitys);

		// 更新対象スレと死スレをUpdateします
		exsistThreadUpdate(subjectTextDAO, exsistsEntitys);


		// 新スレをインサートします
		newThreadInsert(subjectTextDAO, newEntitys);

	}

	private List<SubjectTextEntity> firstBootDecideNewThread(
			SubjectTextDAO dao) {
		// すでに存在しているレコード(Update対象)
		List<SubjectTextEntity> exsistList = dao.seclectExsistThread();

		return exsistList;
	}

	// 現在のサブジェクトtxtのレコードとすでに存在してるレコードを比較してインサート対象を抽出します
	// すでに存在してるスレに対しては仮想的な処理flagをたてます
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

					//ダウンロードポイント計算
					// TODO +1の必要あり？
					exsisEntity.setDownloadStartpoint(exsisEntity.getThreadResCount());

					// すでに存在してるスレに新しいデータをマージします
					exsisEntity.setThreadResCount(nowEntityClone.getThreadResCount());

					//処理マークをつける
					exsisEntity.setDeleateFlag(-1);
					findflg = true;

				}

			}

			if (findflg == false) {

				Loger.print("INSERT対象　" + nowEntity.getTitle());
				mergeList.add(nowEntityClone);

			} else {
				Loger.print("更新対象　" + nowEntity.getTitle());
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

		Loger.print("スレッド更新→ " + String.valueOf(alive));
		Loger.print("スレッド落ち→ " + String.valueOf(death));

		//情報登録
		componentInfo.setThreadUpdateTarget(alive);
		componentInfo.setThreadDeleteFlagTarget(death);
	}

	/**
	 * 新スレをインサートします
	 *
	 * @param subjectTextDAO
	 * @param entity
	 */
	private void newThreadInsert(SubjectTextDAO subjectTextDAO,
			List<SubjectTextEntity> entitys) {


		Loger.print("INSERT数→ " + String.valueOf(entitys.size()));
		componentInfo.setThreadInsertTarget(entitys.size());


		subjectTextDAO.insert(entitys);
	}


	/**
	 * すでに存在しているスレを更新します
	 * @param subjectTextDAO
	 * @param entitys
	 */
	private void exsistThreadUpdate(SubjectTextDAO subjectTextDAO,
			List<SubjectTextEntity> entitys) {


		subjectTextDAO.update(entitys);

	}


}
