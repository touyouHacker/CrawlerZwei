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
	 * Gzipファイルを解凍します
	 *
	 * @param compressFilename
	 *            GZIPファイル名
	 * @param unCompressFileName
	 *            　解凍ファイル名
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
	 * .gzip ファイル名から.gzipを消して返却文字列を返します
	 *
	 * @param gzipDatFullPathName
	 * @return
	 */
	public static String toUnCompressFileName(String gzipDatFullPathName) {
		// TODO 厳密にgzipを指定する
		return gzipDatFullPathName.replace(".gzip", "");
	}
}
