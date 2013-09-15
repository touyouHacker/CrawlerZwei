/**
 *
 */
package jp.co.omega11.universal.util.file;

import java.io.BufferedReader;
import java.io.FileReader;

import jp.co.omega11.universal.util.log.Loger;

/**
 * @author Wizard1
 *
 */
public final class TextUtil {

	private TextUtil() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * �t�@�C���̍s����Ԃ��܂��B�Ȃ�炩�̗�O�����������ꍇ-1��ԋp���܂�
	 *
	 * @param fileName
	 * @return
	 */
	public static int wc(String fileName) {
		int count = 0;

		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));

			while (br.readLine() != null) {
				count++;
			}


		} catch (Exception e) {
			// TODO Auto-generated catch block
			Loger.print(e);
			return -1;
		}
		return count;
	}

	/**
	 * �t�@�C���e�L�X�g��String�ɂ��܂��B�Ȃ�炩�̗�O����������null��ԋp���܂�
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
			Loger.print(e);
			return null;
		}


	}
}
