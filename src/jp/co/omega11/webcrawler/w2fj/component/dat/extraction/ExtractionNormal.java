/**
 * ���K�\�����g�p��dat����URL�𒊏o���܂�
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
	 * �t�@�C���̒��g���ׂĂ𒊏o�Ώۂɂ��܂�
	 */
	@Override
	public List<ContentsEntity> extraction(String datFilePath) {
		// ���K�\���łg�s�s�o�𔲂��܂�
		// TODO Auto-generated method stub
		extraction(datFilePath, 1);
		return null;
	}

	/**
	 * �t�@�C���̎w�肳�ꂽ�s������URL�𒊏o���܂�
	 * 
	 * @param datFilePat
	 *            ���o�Ώۂ̃t�@�C���i�t���p�X�j
	 * @param line
	 *            ���o�ΏۊJ�n�s��
	 */
	public List<ContentsEntity> extraction(String datFilePat, int line) {

		Log.print("extraction " + line + " " + datFilePat);

		// ���K�\���łg�s�s�o�𔲂��܂�
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
					// �����J�n

					/**
					 * �����̗��� line�̒���ttp://������������ contentsEntity ��new���Đ���
					 * http:// �`���ɐ��`����URL�Ȃ̂��ȒP�Ȍ��� ���؂��Ƃ���΃��X�g��add ���C����Entity�ɐݒ�
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
		// �ݒ�w�l�k�t�@�C�����琳�K�\���̃��[�h
		// �������̃X�e�[�W�ɂ킩��Ƃ��͂��̏������K�v
		// TODO

		// TODO youtube/�j�R�j�R���Ή�
		/**
		 * �����ō�OP�g�b�v3�@ CANAAN�@ttp://www.youtube.com/watch?v=xbdN3213U9k
		 * ������2�b�@ttp://www.youtube.com/watch?v=euDnt9D8xkk
		 * ������4�b�@ttp://www.youtube.com/watch?v=V-bXn859BK8
		 * 
		 * @see http://www.nilab.info/zurazure2/000324.html
		 */

		Pattern pattern = Pattern.compile("ttp://.*\\.(jpg|png|gif|jpeg|PNG)");
		return pattern;
	}

	@Override
	/*
	 * * �t�B���^�[�G���W������Ԃ��܂� �N���X�����킩��₷���̂ŃN���X����ԋp�B
	 */
	public String getFilterName() {
		return this.getClass().getSimpleName();
	}

	/**
	 * ���̃N���X�ŋ��ʓI��contentsEntity�ւ̐ݒ荀�ڂ�ݒ�
	 * 
	 * @param contentsEntity
	 */
	private void commonContentsEntitysSetter(List<ContentsEntity> contentsEntity) {
		for (ContentsEntity entity : contentsEntity) {
			entity.setUrlRegexFilter(getFilterName());
		}

	}

}
