package jp.co.omega11.universal.util;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import jp.co.omega11.universal.util.log.Loger;

import org.apache.commons.lang.StringUtils;


// W2FJ以外にも使用できる汎用的なメソッド群

public final class UniversalUtil {
	public UniversalUtil() {
		// 空のコンストラクタ
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
	 * フルパスのフォルダ名を指定し存在しなければ作成します
	 * 存在しているかまたは作成成功ならTrue,作成できなかったらfalse
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
	 * URLの最後の要素を返します。
	 * http://www.gooo.co/uploder/01.jpg の場合 01.jpg
	 * 空の場合(例えば http://google.co.jp/)、index.htmlを返します。
	 * ファイルダウンロードルーチンで使用
	 * @param url URLアドレス
	 * @return URLアドレスの最後の要素
	 */
	public static String urlLastElement(String url) {

		// index.html対策
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
		dateString += String.valueOf( cal.get(Calendar.YEAR)) + "年" ;
		dateString += String.valueOf( cal.get(Calendar.MONTH) + 1) + "月";
		dateString += String.valueOf( cal.get(Calendar.DATE)) + "日";
		dateString += String.valueOf( cal.get(Calendar.HOUR_OF_DAY)) + "時";
		dateString += String.valueOf( cal.get(Calendar.MINUTE)) + "分";
		dateString += String.valueOf( cal.get(Calendar.SECOND)) + "秒";
		return dateString;
	}


	/**
	 * カレンダーの日付表記を返却します
	 * @param cal nullなら""を返却
	 * @return
	 */
	public static String nowDate(Calendar cal){

		if(cal == null){
			return "";
		}


		String dateString = "";
		dateString += String.valueOf( cal.get(Calendar.YEAR)) + "年" ;
		dateString += String.valueOf( cal.get(Calendar.MONTH) + 1) + "月";
		dateString += String.valueOf( cal.get(Calendar.DATE)) + "日";
		dateString += String.valueOf( cal.get(Calendar.HOUR_OF_DAY)) + "時";
		dateString += String.valueOf( cal.get(Calendar.MINUTE)) + "分";
		dateString += String.valueOf( cal.get(Calendar.SECOND)) + "秒";
		return dateString;
	}

	/**
	 * Windowsでファイル名,フォルダ名に使用できない語を除去して返します
	 * @param fileName ファイル名、フォルダ名
	 * @return 削除後の文字列(最終的に空文字になる可能性があるので呼び出し側で考慮必要)
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

		// Windowsでは最後が空白のファイル名が作れない
		if(result.endsWith(" ")){
			result = StringUtils.chomp(result, " ");
		}

		return result;
	}


}