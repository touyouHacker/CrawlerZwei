/**
 *
 */
package jp.co.omega11.universal.util.log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import jp.co.omega11.universal.util.UniversalUtil;
import jp.co.omega11.universal.util.system.JavaEnviroment;

/**
 * @author Wizard1 2009
 *
 *         デバックロガー ファイルプリント コンソールプリント GUIプリント Mail送信 を想定
 *
 *         log4jをラップすることも想定
 */
public class Log {

	static BufferedWriter debugBffuerFile = null;
	static BufferedWriter exceptionBffuerFile = null;

	public static void setFile(String debugFilename, String exceptionFileName)
			throws IOException {

		FileWriter debugWriter = new FileWriter(debugFilename + ".debug");
		FileWriter exceptionWriter = new FileWriter(exceptionFileName
				+ ".exception");

		debugBffuerFile = new BufferedWriter(debugWriter);
		exceptionBffuerFile = new BufferedWriter(exceptionWriter);
	}

	public static void print(String string) {

		if (debugBffuerFile != null) {
			try {
				debugBffuerFile.write(p(string));
			} catch (IOException e) {
				print(e);
			}
		} else {
			System.out.println(p(string));
		}
	}

	public static void print(Throwable e) {

		if (exceptionBffuerFile != null) {
			try {
				exceptionBffuerFile.write(pd(e.toString()));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
			System.out.print(pd(e.toString()));
		}
	}

	public static void printSQLException(SQLException e) {

		if (exceptionBffuerFile != null) {
			try {
				exceptionBffuerFile.write(pd(e.getSQLState()));
				exceptionBffuerFile.write(pd(e.toString()));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
			System.out.print(pd("SQLState " + e.getSQLState()));
			System.out.print(pd(e.toString()));
		}
	}

	// このクラスで共通的なプリントテンプレート
	private static String p(String s) {
		return s  + JavaEnviroment.line;
	}

	// このクラスで共通的なプリントテンプレート(日付付き)
	private static String pd(String s) {
		return UniversalUtil.nowDate() + " : " + s + JavaEnviroment.line;
	}

	// ログファイルをすべてフラッシュします
	public static void flushAll() {

		try {
			if (debugBffuerFile != null) {
				debugBffuerFile.flush();

			}
			if (exceptionBffuerFile != null) {
				exceptionBffuerFile.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
