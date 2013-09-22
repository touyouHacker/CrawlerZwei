package jp.co.omega11.universal.util.archive;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import jp.co.omega11.universal.util.log.Log;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

public final class GzipUtil {

	public static int READ_BYTE_LIMIT = 4096;

	private GzipUtil() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Gzip�t�@�C�����𓀂��܂�
	 *
	 * @param compressFilename
	 *            GZIP�t�@�C����
	 * @param unCompressFileName
	 *            �@�𓀃t�@�C����
	 * @see http://commons.apache.org/compress/examples.html
	 */
	public static void decompress(String compressFilename,
			String unCompressFileName) {

		try {
			FileInputStream in = new FileInputStream(compressFilename);
			FileOutputStream out = new FileOutputStream(unCompressFileName);
			GzipCompressorInputStream bzIn = new GzipCompressorInputStream(in);
			final byte[] buffer = new byte[READ_BYTE_LIMIT];
			int n = 0;
			while (-1 != (n = bzIn.read(buffer))) {
				out.write(buffer, 0, n);
			}
			out.close();
			bzIn.close();
		} catch (Exception e) {
			Log.print(e);
		}
	}

	/**
	 * .gzip �t�@�C��������.gzip�������ĕԋp�������Ԃ��܂�
	 *
	 * @param gzipDatFullPathName
	 * @return
	 */
	public static String toUnCompressFileName(String gzipDatFullPathName) {
		// TODO ������gzip���w�肷��
		return gzipDatFullPathName.replace(".gzip", "");
	}
}
