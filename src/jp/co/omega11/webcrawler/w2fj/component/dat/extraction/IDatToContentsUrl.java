/**
 * 
 */
package jp.co.omega11.webcrawler.w2fj.component.dat.extraction;

import java.util.List;

import jp.co.omega11.webcrawler.w2fj.entity.ContentsEntity;


/**
 * @author Wizard1 2009
 *
 */
public interface IDatToContentsUrl {

	/**
	 * 指定したファイルからURLを抽出します
	 * @param datFilePath
	 * @return
	 */
	public 	abstract List<ContentsEntity> extraction(String datFilePath);
	
	/**
	 * 指定したファイルからURLを抽出します(行数指定)
	 * @param datFilePath
	 * @param line　読み込み開始行
	 * @return
	 */
	public 	abstract List<ContentsEntity> extraction(String datFilePath, int line);
	
	/**
	 * フィルター名を返します
	 * @return
	 */
	public abstract String getFilterName();
}

