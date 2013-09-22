/**
 * 正規表現を使用しdatからURLを抽出します
 */
package jp.co.omega11.webcrawler.w2fj.component.dat.extraction;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.omega11.universal.util.log.Log;
import jp.co.omega11.webcrawler.w2fj.entity.ContentsEntity;

/**
 * @author Wizard1 2009
 * 
 */
public class ExtractionNormal implements IDatToContentsUrl {

	/*
	 * ファイルの中身すべてを抽出対象にします
	 */
	@Override
	public List<ContentsEntity> extraction(String datFilePath) {
		// 正規表現でＨＴＴＰを抜きます
		// TODO Auto-generated method stub
		extraction(datFilePath, 1);
		return null;
	}

	/**
	 * ファイルの指定された行数からURLを抽出します
	 * 
	 * @param datFilePat
	 *            抽出対象のファイル（フルパス）
	 * @param line
	 *            抽出対象開始行数
	 */
	public List<ContentsEntity> extraction(String datFilePat, int line) {

		Log.print("extraction " + line + " " + datFilePat);

		// 正規表現でＨＴＴＰを抜きます
		List<ContentsEntity> contentsEntitys = new ArrayList<ContentsEntity>();

		String oneLineString;

		int lineCounter = 0;

		Pattern pattern = loadRegexPattern();
		Matcher matcher = null;

		try {
			// BufferedReader br = new BufferedReader(new
			// FileReader(datFilePat));

			InputStreamReader isr = new InputStreamReader(new FileInputStream(
					datFilePat), "SJIS");

			BufferedReader br = new BufferedReader(isr);

			while ((oneLineString = br.readLine()) != null) {
				lineCounter++;

				if (line <= lineCounter) {
					// 処理開始

					/**
					 * 処理の流れ lineの中にttp://が見つかったら contentsEntity をnewして生成
					 * http:// 形式に成形してURLなのか簡単な検証 検証をとおればリストにadd ラインをEntityに設定
					 * 
					 */

					String[] oneLineStringSplits = oneLineString.split("<br>");
					for (String brSplit : oneLineStringSplits) {
						matcher = pattern.matcher(brSplit);
						while (matcher.find()) {
							ContentsEntity contentsEntity = new ContentsEntity();

							contentsEntity.setUrl("h" + matcher.group());
							contentsEntity.setFirstHitThreadNum(lineCounter);

							contentsEntitys.add(contentsEntity);

						}
					}

				}

			}

			br.close();
			isr.close();

		} catch (FileNotFoundException e) {
			Log.print(e);
			return null;
		} catch (IOException e) {
			Log.print(e);
			return null;
		}

		commonContentsEntitysSetter(contentsEntitys);

		return contentsEntitys;
	}

	public Pattern loadRegexPattern() {
		// 設定ＸＭＬファイルから正規表現のロード
		// いくつかのステージにわかるときはその処理が必要
		// TODO

		// TODO youtube/ニコニコも対応
		/**
		 * 今期最高OPトップ3　 CANAAN　ttp://www.youtube.com/watch?v=xbdN3213U9k
		 * 化物語2話　ttp://www.youtube.com/watch?v=euDnt9D8xkk
		 * 化物語4話　ttp://www.youtube.com/watch?v=V-bXn859BK8
		 * 
		 * @see http://www.nilab.info/zurazure2/000324.html
		 */

		Pattern pattern = Pattern.compile("ttp://.*\\.(jpg|png|gif|jpeg|PNG)");
		return pattern;
	}

	@Override
	/*
	 * * フィルターエンジン名を返します クラス名がわかりやすいのでクラス名を返却。
	 */
	public String getFilterName() {
		return this.getClass().getSimpleName();
	}

	/**
	 * このクラスで共通的なcontentsEntityへの設定項目を設定
	 * 
	 * @param contentsEntity
	 */
	private void commonContentsEntitysSetter(List<ContentsEntity> contentsEntity) {
		for (ContentsEntity entity : contentsEntity) {
			entity.setUrlRegexFilter(getFilterName());
		}

	}

}
