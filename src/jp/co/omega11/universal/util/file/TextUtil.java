/**
 *
 */
package jp.co.omega11.universal.util.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

import jp.co.omega11.universal.util.log.Log;

/**
 * @author Wizard1
 *
 */
public final class TextUtil {

	private TextUtil() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * ファイルの行数を返します。なんらかの例外が発生した場合-1を返却します
	 *
	 * @param fileName
	 * @return
	 */
	public static int wc(String fileName) {
		int count = 0;

		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(
					fileName),"SJIS");
			BufferedReader br = new BufferedReader(isr);
			while (br.readLine() != null) {
				count++;
			}

			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.print(e);
			return -1;
		}
		
		
		return count;
	}

	/**
	 * ファイルテキストをStringにします。なんらかの例外が発生したnullを返却します
	 *
	 * @param fileName
	 * @return
	 */
	public static String file2String(String fileName) {
		String fileText = "";
		String oneLine = null;

		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));

			while (true) {
				oneLine = null;
				oneLine = br.readLine();

				if(oneLine == null){return fileText;}

				fileText += oneLine;
			}


		} catch (Exception e) {
			Log.print(e);
			return null;
		}


	}
}
