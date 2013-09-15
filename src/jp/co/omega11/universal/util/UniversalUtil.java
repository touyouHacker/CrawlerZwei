package jp.co.omega11.universal.util;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import jp.co.omega11.universal.util.log.Loger;

import org.apache.commons.lang.StringUtils;


// W2FJ�ȊO�ɂ��g�p�ł���ėp�I�ȃ��\�b�h�Q

public final class UniversalUtil {
	public UniversalUtil() {
		// ��̃R���X�g���N�^
	}

	public static String getTimeString() {
		Date d = new Date();
		return String.valueOf(d.getTime());
	}

	public static void createFile(String fileName) {

		File file1 = new File(fileName);
		try {
			if (file1.createNewFile()) {

			} else {

			}
		} catch (IOException e) {
			Loger.print(e);
		}
	}

	/**
	 * �t���p�X�̃t�H���_�����w�肵���݂��Ȃ���΍쐬���܂�
	 * ���݂��Ă��邩�܂��͍쐬�����Ȃ�True,�쐬�ł��Ȃ�������false
	 * @param folderName
	 */
	public static boolean createDirectryExistCheak(String folderName) {

		if (StringUtils.isBlank(folderName)){
			return false;
		}


		File file = new File(folderName);

		if (file.exists()) {
			return true;
		} else {
			return file.mkdirs();
		}

	}

	/**
	 * URL�̍Ō�̗v�f��Ԃ��܂��B
	 * http://www.gooo.co/uploder/01.jpg �̏ꍇ 01.jpg
	 * ��̏ꍇ(�Ⴆ�� http://google.co.jp/)�Aindex.html��Ԃ��܂��B
	 * �t�@�C���_�E�����[�h���[�`���Ŏg�p
	 * @param url URL�A�h���X
	 * @return URL�A�h���X�̍Ō�̗v�f
	 */
	public static String urlLastElement(String url) {

		// index.html�΍�
		int lashSlashIndex = url.lastIndexOf("/");
		if (lashSlashIndex == url.length() - 1) {
			return "index.html";
		}

		String[] strings = url.split("/");
		return strings[strings.length - 1];
	}


	public static String nowDate(){
		String dateString = "";
		Calendar cal = Calendar.getInstance();
		dateString += String.valueOf( cal.get(Calendar.YEAR)) + "�N" ;
		dateString += String.valueOf( cal.get(Calendar.MONTH) + 1) + "��";
		dateString += String.valueOf( cal.get(Calendar.DATE)) + "��";
		dateString += String.valueOf( cal.get(Calendar.HOUR_OF_DAY)) + "��";
		dateString += String.valueOf( cal.get(Calendar.MINUTE)) + "��";
		dateString += String.valueOf( cal.get(Calendar.SECOND)) + "�b";
		return dateString;
	}


	/**
	 * �J�����_�[�̓��t�\�L��ԋp���܂�
	 * @param cal null�Ȃ�""��ԋp
	 * @return
	 */
	public static String nowDate(Calendar cal){

		if(cal == null){
			return "";
		}


		String dateString = "";
		dateString += String.valueOf( cal.get(Calendar.YEAR)) + "�N" ;
		dateString += String.valueOf( cal.get(Calendar.MONTH) + 1) + "��";
		dateString += String.valueOf( cal.get(Calendar.DATE)) + "��";
		dateString += String.valueOf( cal.get(Calendar.HOUR_OF_DAY)) + "��";
		dateString += String.valueOf( cal.get(Calendar.MINUTE)) + "��";
		dateString += String.valueOf( cal.get(Calendar.SECOND)) + "�b";
		return dateString;
	}

	/**
	 * Windows�Ńt�@�C����,�t�H���_���Ɏg�p�ł��Ȃ�����������ĕԂ��܂�
	 * @param fileName �t�@�C�����A�t�H���_��
	 * @return �폜��̕�����(�ŏI�I�ɋ󕶎��ɂȂ�\��������̂ŌĂяo�����ōl���K�v)
	 */
	public static String deleteFileProhibitionWordForWindows(String fileName) {

		String result = fileName;
		// \ / : * ? " < > |
		result =  StringUtils.remove(result, '\\');
		result =  StringUtils.remove(result, '/');
		result =  StringUtils.remove(result, ':');
		result =  StringUtils.remove(result, '*');
		result =  StringUtils.remove(result, '?');
		result =  StringUtils.remove(result, '"');
		result =  StringUtils.remove(result, '<');
		result =  StringUtils.remove(result, '>');
		result =  StringUtils.remove(result, '|');

		// Windows�ł͍Ōオ�󔒂̃t�@�C���������Ȃ�
		if(result.endsWith(" ")){
			result = StringUtils.chomp(result, " ");
		}

		return result;
	}


}