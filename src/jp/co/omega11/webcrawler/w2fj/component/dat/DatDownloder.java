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
	 * MAIN処理
	 *
	 * @throws Exception
	 */
	public void execute() throws Exception {

		// フォルダパスを設定
		datFolderPath = set.getDatFolderFullPath();

		wcHash = new HashMap<Integer, Integer>();

		List<SubjectTextEntity> subjectTextEntitys = getWorkDate();

		componentInfo.setDatDonwnloadTargetInSubCount(subjectTextEntitys.size());

		// 処理するべきスレがあるときのみ後続処理に入ります
		if (subjectTextEntitys.size() > 0) {

			// URLをダウンロードしエンティティを編集（取得サイズなど）
			download(subjectTextEntitys);

			// 結果を基にDB更新をまとめて行う
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
	 * ダウンロードすべきDATのスレをみつけて結果を返します
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
		// HTTP DAT ダウンロード

		for (SubjectTextEntity subjectTextEntity : subjectTextEntitys) {
			
			// TODO 解析のため
			Log.flushAll();
			
			// wcを書き換える前に前のwcを保持
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

			// HTTP通信結果を格納
			// TODO -1が返されるのでＤＢで例外になる
			// 例外発生→ com.mysql.jdbc.MysqlDataTruncation: Data truncation: Out of
			// range value for column 'CONTENT_LENGTH' at row 1
			if (save.getContentLength() > 0) {
				subjectTextEntity.setContentLength(save.getContentLength());
			} else {
				subjectTextEntity.setContentLength(0);
			}
			subjectTextEntity.setHttpState(String.valueOf(save
					.getResponseCode()));

			String gzipDatFullPathName = datFolderPath + "/" + datFileName;
			// .gzip の５文字削除
			String unzipDatFullPathName = GzipUtil
					.toUnCompressFileName(gzipDatFullPathName);

			// gzipを解凍
			GzipUtil.decompress(gzipDatFullPathName, unzipDatFullPathName);

			// 解凍ストリームをよみ行数を返す
			int wc = TextUtil.wc(unzipDatFullPathName);
			
			Log.print("wc=" + wc);
			
			// IF文に入らないときはＤＡＴ取得失敗？ＷＣを書き換えなければパースには入らない
			if (0 <= wc) {
				subjectTextEntity.setDownloaded(wc);
			}

			// 1H 3600秒だから1スレ10秒ぐらいだと1時間360スレを回れる
			// とかげの尻尾切り/バーボンハウス
			// http://info.2ch.net/wiki/index.php?%A4%C8%A4%AB%A4%B2%A4%CE%BF%AC%C8%F8%C0%DA%A4%EA%2F%A5%D0%A1%BC%A5%DC%A5%F3%A5%CF%A5%A6%A5%B9
			/*
			 * bg20サーバからデータを取得れば、バーボンハウス行きにはなりません。
			 * 例えば、http://society6.2ch.net/test/read.cgi/gline/1169964276/ の場合、
			 * http://bg20.2ch.net/test/r.so/society6.2ch.net/gline/1169964276/
			 * からdatを取得できます。 ただし、差分取得はできません。
			 */

		}

	}

	private void regist(List<SubjectTextEntity> subjectTextEntitys)
			throws Exception {
		// ダウンロード結果をＵＰＤＡＴＥ
		updateSubjectTable(subjectTextEntitys);

		if (set.isNoParseExe()) {

			Log.print("■ NoParseExeフラグをfalseに設定");
			set.setNoParseExe(false);

		} else {
			// datをregexで抽出→コンテンツURLをInsert
			parseDatToUrls(subjectTextEntitys);
		}
	}

	// // 結果をUPDATE　競合発生するのでロック処理必要? DBのオートロックに任せる？　必要最低限カラムでよい、更新日は更新しない
	private void updateSubjectTable(List<SubjectTextEntity> subjectTextEntitys) {
		SubjectTextDAO subjectTextDAO = new SubjectTextDAO(con, set
				.getItaname());
		subjectTextDAO.updateForDatDownloadAfter(subjectTextEntitys);
	}

	private void parseDatToUrls(List<SubjectTextEntity> subjectTextEntitys)
			throws Exception {
		// 更新前のdownloaedと現在のdownloadedから差分をとり必要な分だけseekして取得
		// 正規表現でURLを抽出　抽出エンジンは変更できるようプラグインっぽく作る
		// URLを階層ふかくみたり・・とかいろいろバージョンがあるので↑
		List<ContentsEntity> contentsEntitys = null;

		/** パースしたDatの総数*/
		int parseDatCounter=0;


		// TODO
		// 動的にエンジンをかえる
		IDatToContentsUrl idaContentsUrl = new ExtractionNormal();

		for (SubjectTextEntity subjectTextEntity : subjectTextEntitys) {

			contentsEntitys = null;

			// 以前読み込んだ行数 < 今の行数　のとき
			// そもそもDAOでsubject.txtの差分があるやつだけ取得してるがsubjectとDATのコンポーネントの時間差で
			// DATが最新版をとりすぎてる可能性がある (DATが取得したスレ数 > subjectの認識スレ数) ので二重チェック

			Log.print("行数比較" + wcHash.get(subjectTextEntity.getId()) + " < "
					+ subjectTextEntity.getDownloaded());

			if (wcHash.get(subjectTextEntity.getId()) < subjectTextEntity
					.getDownloaded()) {
				// DATからURL抽出
				contentsEntitys = idaContentsUrl.extraction(datFolderPath + "/"
						+ subjectTextEntity.getThreadNumber() + ".dat", wcHash
						.get(subjectTextEntity.getId()) + 1);

				parseDatCounter++;
			}

			if (contentsEntitys == null) {
				continue;
			}

			// TODO USERやNodeIDの調整
			for (ContentsEntity content : contentsEntitys) {
				content.setDatId(subjectTextEntity.getId());
			}

			// TODO
			// 1datにつき1回insert(JDBCのバッチモード使ったほうがよいDAOにモードを持たせる)
			insertContentsTable(contentsEntitys);
		}

		componentInfo.setParseDatCounter(parseDatCounter);
	}

	private void insertContentsTable(List<ContentsEntity> contentsEntitys)
			throws Exception {
		ContentsDAO contentsDAO = new ContentsDAO(con, set.getItaname());

		// TODO 重複をはぶくルーチン
		// 同じＵＲＬでも中身がかわってることがあるのでそれはIFModeifiedでしか対応できない
		// カラムに追加するしかない、ついてにサーバー名とかもカラムふやす Apache～PHP/vとか

		/*
		 * List<ContentsEntity> insertEntitys = new ArrayList<ContentsEntity>();
		 *
		 * //重複を精査してinsertすべきものを探す for(ContentsEntity contentsEntity
		 * :contentsEntitys){
		 * contentsDAO.searchInsert(contentsEntity.getFileName(),
		 * contentsEntity.getUrl()); }
		 *
		 * contentsDAO.insert(insertEntitys);
		 */
		contentsDAO.insert(contentsEntitys);
	}

}
