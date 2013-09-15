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
	 * �w�肵���t�@�C������URL�𒊏o���܂�
	 * @param datFilePath
	 * @return
	 */
	public 	abstract List<ContentsEntity> extraction(String datFilePath);
	
	/**
	 * �w�肵���t�@�C������URL�𒊏o���܂�(�s���w��)
	 * @param datFilePath
	 * @param line�@�ǂݍ��݊J�n�s
	 * @return
	 */
	public 	abstract List<ContentsEntity> extraction(String datFilePath, int line);
	
	/**
	 * �t�B���^�[����Ԃ��܂�
	 * @return
	 */
	public abstract String getFilterName();
}

